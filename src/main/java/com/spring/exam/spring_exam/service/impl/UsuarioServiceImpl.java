package com.spring.exam.spring_exam.service.impl;

import com.spring.exam.spring_exam.aggregates.mapper.UsuarioMapper;
import com.spring.exam.spring_exam.aggregates.request.UsuarioRequest;
import com.spring.exam.spring_exam.aggregates.response.ReniecApiServiceResponse;
import com.spring.exam.spring_exam.aggregates.response.UsuarioResponse;
import com.spring.exam.spring_exam.entity.UsuarioEntity;
import com.spring.exam.spring_exam.redis.RedisService;
import com.spring.exam.spring_exam.repository.UsuarioRepository;
import com.spring.exam.spring_exam.service.UsuarioService;
import com.spring.exam.spring_exam.service.retrofit.ReniecApiService;
import com.spring.exam.spring_exam.service.retrofit.impl.ReniecApiServiceImpl;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Objects;

import static com.spring.exam.spring_exam.util.RedisUtil.parseFromString;
import static com.spring.exam.spring_exam.util.RedisUtil.parseToString;
import static com.spring.exam.spring_exam.util.constans.ServiceConstant.*;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RedisService redisService;
    ReniecApiService reniecApiServiceRetroFit = ReniecApiServiceImpl.getClient().create(ReniecApiService.class);

    @Value("${token.api}")
    private String tokenApi;

    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest usuarioRequest) {
        try {
            UsuarioEntity usuario = getUsuarioRetrofit(usuarioRequest);
            if(Objects.nonNull(usuario)) {
                return UsuarioMapper.mapToUsuarioResponse(usuarioRepository.save(usuario));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public UsuarioResponse buscarUsuario(String numeroDocumento) {
        return UsuarioMapper.mapToUsuarioResponse(getUsuario(numeroDocumento));
    }



    private UsuarioEntity getUsuarioRetrofit(UsuarioRequest usuarioRequest) throws IOException {

        Call<ReniecApiServiceResponse> callReniec= reniecApiServiceRetroFit.getPersonaReniec(
                HEADER_TOKEN.concat(tokenApi), usuarioRequest.getNumDoc());

        Response<ReniecApiServiceResponse> execute = callReniec.execute();
        if(execute.isSuccessful() && Objects.nonNull(execute.body())){
            ReniecApiServiceResponse response = execute.body();
            return getUsuarioEntity(usuarioRequest, response);
        }
        return null;
    }


    @NotNull
    private static UsuarioEntity getUsuarioEntity(UsuarioRequest usuarioRequest, ReniecApiServiceResponse response) {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNombres(response.getNombres());
        usuario.setApellidoPaterno(response.getApellidoPaterno());
        usuario.setApellidoMaterno(response.getApellidoMaterno());
        usuario.setTipoDocumento(response.getTipoDocumento());
        usuario.setDigitoVerificador(response.getDigitoVerificador());
        usuario.setNumeroDocumento(response.getNumeroDocumento());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setPassword(usuarioRequest.getPassword());
        return usuario;
    }

    private UsuarioEntity getUsuario(String numeroDocumento) {
        String redisInfo = redisService.getDataFromRedis(REDIS_KEY_API_USER+numeroDocumento);
        if(Objects.nonNull(redisInfo)){
            return parseFromString(redisInfo, UsuarioEntity.class);
        } else {
            UsuarioEntity response= usuarioRepository.findByNumeroDocumento(numeroDocumento)
                    .orElseThrow(() -> new RuntimeException("PERSONA NO ENCONTRADA"));
            String dataForRedis = parseToString(response);
            redisService.saveInRedis(REDIS_KEY_API_USER+numeroDocumento, dataForRedis, REDIS_EXPIRATION_TIME);
            return response;
        }
    }
}

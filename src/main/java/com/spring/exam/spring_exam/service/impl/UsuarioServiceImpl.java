package com.spring.exam.spring_exam.service.impl;

import com.spring.exam.spring_exam.aggregates.mapper.UsuarioMapper;
import com.spring.exam.spring_exam.aggregates.request.UsuarioRequest;
import com.spring.exam.spring_exam.aggregates.response.ReniecApiServiceResponse;
import com.spring.exam.spring_exam.aggregates.response.UsuarioResponse;
import com.spring.exam.spring_exam.entity.UsuarioEntity;
import com.spring.exam.spring_exam.repository.UsuarioRepository;
import com.spring.exam.spring_exam.service.UsuarioService;
import com.spring.exam.spring_exam.service.retrofit.ReniecApiService;
import com.spring.exam.spring_exam.service.retrofit.impl.ReniecApiServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Objects;

import static com.spring.exam.spring_exam.util.constans.ServiceConstant.HEADER_TOKEN;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
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


    private UsuarioEntity getUsuarioRetrofit(UsuarioRequest usuarioRequest) throws IOException {

        Call<ReniecApiServiceResponse> callReniec= reniecApiServiceRetroFit.getPersonaReniec(
                HEADER_TOKEN.concat(tokenApi), usuarioRequest.getNumDoc());

        Response<ReniecApiServiceResponse> execute = callReniec.execute();
        if(execute.isSuccessful() && Objects.nonNull(execute.body())){
            ReniecApiServiceResponse response = execute.body();

            return UsuarioEntity.builder()
                    .nombres(response.getNombres())
                    .apellidoPaterno(response.getApellidoPaterno())
                    .apellidoMaterno(response.getApellidoMaterno())
                    .tipoDocumento(response.getTipoDocumento())
                    .numeroDocumento(response.getNumeroDocumento())
                    .digitoVerificador(response.getDigitoVerificador())
                    .email(usuarioRequest.getEmail())
                    .password(usuarioRequest.getPassword())
                    .build();
        }
        return null;
    }
}

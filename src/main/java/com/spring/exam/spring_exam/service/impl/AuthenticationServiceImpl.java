package com.spring.exam.spring_exam.service.impl;

import com.spring.exam.spring_exam.aggregates.mapper.UsuarioMapper;
import com.spring.exam.spring_exam.aggregates.request.SignInRequest;
import com.spring.exam.spring_exam.aggregates.request.UsuarioRequest;
import com.spring.exam.spring_exam.aggregates.response.ReniecApiServiceResponse;
import com.spring.exam.spring_exam.aggregates.response.SignInResponse;
import com.spring.exam.spring_exam.aggregates.response.UsuarioResponse;
import com.spring.exam.spring_exam.entity.Rol;
import com.spring.exam.spring_exam.entity.UsuarioEntity;
import com.spring.exam.spring_exam.repository.RolRepository;
import com.spring.exam.spring_exam.repository.UsuarioRepository;
import com.spring.exam.spring_exam.service.AuthenticationService;
import com.spring.exam.spring_exam.service.JwtService;
import com.spring.exam.spring_exam.service.retrofit.ReniecApiService;
import com.spring.exam.spring_exam.service.retrofit.impl.ReniecApiServiceImpl;
import com.spring.exam.spring_exam.util.enums.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import static com.spring.exam.spring_exam.util.constans.ServiceConstant.HEADER_TOKEN;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    ReniecApiService reniecApiServiceRetroFit = ReniecApiServiceImpl.getClient().create(ReniecApiService.class);

    @Value("${token.api}")
    private String tokenApi;
    @Override
    @Transactional
    public UsuarioResponse singUpUser(UsuarioRequest usuarioRequest) {
        try {
            UsuarioEntity usuario = getUsuarioRetrofit(usuarioRequest);
            if(Objects.nonNull(usuario)){
                return UsuarioMapper.mapToUsuarioResponse(usuarioRepository.save(usuario));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.getEmail(),signInRequest.getPassword()));
        var user = usuarioRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("ERROR USUARIO NO ENCONTRADO"));
        var token = jwtService.generateToken(user);
        SignInResponse response = new SignInResponse();
        response.setToken(token);
        return response;
    }

    private UsuarioEntity getUsuarioRetrofit(UsuarioRequest usuarioRequest) throws IOException {

        Call<ReniecApiServiceResponse> callReniec = reniecApiServiceRetroFit.getPersonaReniec(
                HEADER_TOKEN.concat(tokenApi), usuarioRequest.getNumDoc());

        Response<ReniecApiServiceResponse> execute = callReniec.execute();
        if(execute.isSuccessful() && Objects.nonNull(execute.body())){
            ReniecApiServiceResponse response = execute.body();
            return createUsuarioEntity(usuarioRequest, response);
        }
        return null;
    }


    @NotNull
    private UsuarioEntity createUsuarioEntity(UsuarioRequest usuarioRequest, ReniecApiServiceResponse response) {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNombres(response.getNombres());
        usuario.setApellidoPaterno(response.getApellidoPaterno());
        usuario.setApellidoMaterno(response.getApellidoMaterno());
        usuario.setTipoDocumento(response.getTipoDocumento());
        usuario.setDigitoVerificador(response.getDigitoVerificador());
        usuario.setNumeroDocumento(response.getNumeroDocumento());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setPassword(new BCryptPasswordEncoder().encode(usuarioRequest.getPassword()));
        usuario.setRoles(Collections.singleton(getRoles(Role.USER)));
        return usuario;
    }

    private Rol getRoles(Role rolBuscado){
        return rolRepository.findByNombreRol(rolBuscado.name())
                .orElseThrow(() -> new RuntimeException("EL ROL BSUCADO NO EXISTE : "
                        + rolBuscado.name()));
    }
}

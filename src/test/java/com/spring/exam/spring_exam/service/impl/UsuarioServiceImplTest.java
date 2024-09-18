package com.spring.exam.spring_exam.service.impl;

import com.spring.exam.spring_exam.aggregates.mapper.UsuarioMapper;
import com.spring.exam.spring_exam.aggregates.request.UsuarioRequest;
import com.spring.exam.spring_exam.aggregates.response.UsuarioResponse;
import com.spring.exam.spring_exam.entity.UsuarioEntity;
import com.spring.exam.spring_exam.redis.RedisService;
import com.spring.exam.spring_exam.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {


    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RedisService redisService;

    @Test
    void buscarUsuarioEnRedis() {

        String numeroDocumento = "44668833";

        String responseFromRedis = "{\"id\":7,\"nombres\":\"CESAR AUGUSTO\",\"apellidos\":\"SCARAMUTTI SILVA\",\"email\":\"cscaramutti@hotmail.com\"}";

        UsuarioResponse expectedResponse = new UsuarioResponse();
        expectedResponse.setId(7L);
        expectedResponse.setApellidos("SCARAMUTTI SILVA");
        expectedResponse.setNombres("CESAR AUGUSTO");
        expectedResponse.setEmail("cscaramutti@hotmail.com");

        when(redisService.getDataFromRedis(anyString()))
                .thenReturn(responseFromRedis);

        assertEquals(expectedResponse, usuarioService.buscarUsuario(numeroDocumento));

    }

    @Test
    void buscarUsuarioEnPg() {

        String numeroDocumento = "44668833";

        String responseFromRedis = null;

        UsuarioResponse expectedResponse = new UsuarioResponse();
        expectedResponse.setId(7L);
        expectedResponse.setApellidos("SCARAMUTTI SILVA");
        expectedResponse.setNombres("CESAR AUGUSTO");
        expectedResponse.setEmail("cscaramutti@hotmail.com");

        UsuarioEntity response = new UsuarioEntity();
        response.setId(7L);
        response.setApellidoMaterno("SILVA");
        response.setApellidoPaterno("SCARAMUTTI");
        response.setEmail("cscaramutti@hotmail.com");
        response.setNombres("CESAR AUGUSTO");

        when(redisService.getDataFromRedis(anyString()))
                .thenReturn(responseFromRedis);

        when(usuarioRepository.findByNumeroDocumento(anyString()))
                .thenReturn(Optional.of(response));

        assertEquals(expectedResponse, usuarioService.buscarUsuario(numeroDocumento));

    }

    @Test
    void listarUsuarios() {
        UsuarioEntity userEntity1 = new UsuarioEntity();
        userEntity1.setId(7L);
        userEntity1.setApellidoMaterno("SILVA");
        userEntity1.setApellidoPaterno("SCARAMUTTI");
        userEntity1.setEmail("cscaramutti@hotmail.com");
        userEntity1.setNombres("CESAR AUGUSTO");
        UsuarioEntity userEntity2 = new UsuarioEntity();
        userEntity2.setId(5L);
        userEntity2.setApellidoMaterno("QUISPE");
        userEntity2.setApellidoPaterno("SCARAMUTTI");
        userEntity2.setEmail("ascara@hotmail.com");
        userEntity2.setNombres("ANGEL AUGUSTO");
        List<UsuarioEntity> userEntityList = Arrays.asList(userEntity1, userEntity2);
        UsuarioResponse userResponse1 = UsuarioMapper.mapToUsuarioResponse(userEntity1);
        UsuarioResponse userResponse2 = UsuarioMapper.mapToUsuarioResponse(userEntity2);
        List<UsuarioResponse> expectedResponse = Arrays.asList(userResponse1, userResponse2);

        when(usuarioRepository.findAll()).thenReturn(userEntityList);

        List<UsuarioResponse> actualResponse = usuarioService.listarUsuarios();

        verify(usuarioRepository, times(1)).findAll();

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void eliminarUsuario() {

        Long id = 7L;

        UsuarioEntity response = new UsuarioEntity();
        response.setId(7L);
        response.setApellidoMaterno("SILVA");
        response.setApellidoPaterno("SCARAMUTTI");
        response.setEmail("cscaramutti@hotmail.com");
        response.setNombres("CESAR AUGUSTO");

        when(usuarioRepository.findById(anyLong()))
                .thenReturn(Optional.of(response));

        usuarioService.eliminarUsuario(id);

        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).deleteById(id);


    }

    @Test
    void actualizarUsuario() {

        Long id = 7L;
        UsuarioRequest usuarioRequest = new UsuarioRequest();
        usuarioRequest.setEmail("agscaramutti@hotmail.com");
        usuarioRequest.setPassword("administrador");

        UsuarioEntity response = new UsuarioEntity();
        response.setId(7L);
        response.setApellidoMaterno("SILVA");
        response.setApellidoPaterno("SCARAMUTTI");
        response.setEmail("cscaramutti@hotmail.com");
        response.setNombres("CESAR AUGUSTO");

        UsuarioEntity expected = new UsuarioEntity();
        expected.setId(7L);
        expected.setApellidoMaterno("SILVA");
        expected.setApellidoPaterno("SCARAMUTTI");
        expected.setEmail("agscaramutti@hotmail.com");
        expected.setNombres("CESAR AUGUSTO");
        expected.setPassword("$2a$10$kJ5imzZAxetXQxThWzsq1eMBQEoSeQPMLJNJfmzDs3");

        when(usuarioRepository.findById(anyLong()))
                .thenReturn(Optional.of(response));

        assertEquals(expected.getEmail(), usuarioService.actualizarUsuario(id, usuarioRequest).getEmail());

    }
}
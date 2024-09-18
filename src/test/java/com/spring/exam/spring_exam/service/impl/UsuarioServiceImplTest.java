package com.spring.exam.spring_exam.service.impl;

import com.spring.exam.spring_exam.aggregates.response.UsuarioResponse;
import com.spring.exam.spring_exam.entity.UsuarioEntity;
import com.spring.exam.spring_exam.redis.RedisService;
import com.spring.exam.spring_exam.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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


    }

    @Test
    void eliminarUsuario() {
    }

    @Test
    void actualizarUsuario() {
    }
}
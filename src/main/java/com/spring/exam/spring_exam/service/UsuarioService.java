package com.spring.exam.spring_exam.service;

import com.spring.exam.spring_exam.aggregates.request.UsuarioRequest;
import com.spring.exam.spring_exam.aggregates.response.UsuarioResponse;

public interface UsuarioService {
    UsuarioResponse crearUsuario(UsuarioRequest usuarioRequest);
}

package com.spring.exam.spring_exam.service;

import com.spring.exam.spring_exam.aggregates.response.UsuarioResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioService {
    UsuarioResponse buscarUsuario(String numeroDocumento);

    UserDetailsService userDetailService();
}

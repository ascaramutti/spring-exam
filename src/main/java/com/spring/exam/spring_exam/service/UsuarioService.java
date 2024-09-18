package com.spring.exam.spring_exam.service;

import com.spring.exam.spring_exam.aggregates.request.UsuarioRequest;
import com.spring.exam.spring_exam.aggregates.response.UsuarioResponse;
import com.spring.exam.spring_exam.entity.UsuarioEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UsuarioService {
    UsuarioResponse buscarUsuario(String numeroDocumento);

    UserDetailsService userDetailService();

    List<UsuarioResponse> listarUsuarios();

    void eliminarUsuario(Long id);

    UsuarioEntity actualizarUsuario(Long id, UsuarioRequest usuarioRequest);
}

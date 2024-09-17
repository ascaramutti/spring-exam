package com.spring.exam.spring_exam.aggregates.mapper;

import com.spring.exam.spring_exam.aggregates.response.UsuarioResponse;
import com.spring.exam.spring_exam.entity.UsuarioEntity;

public class UsuarioMapper {

    public static UsuarioResponse mapToUsuarioResponse(UsuarioEntity usuarioEntity) {
        UsuarioResponse usuarioResponse = new UsuarioResponse();
        usuarioResponse.setId(usuarioEntity.getId());
        usuarioResponse.setApellidos(usuarioEntity.getApellidoPaterno().concat(" ").concat(usuarioEntity.getApellidoMaterno()));
        usuarioResponse.setNombres(usuarioEntity.getNombres());
        return usuarioResponse;
    }
}

package com.spring.exam.spring_exam.aggregates.mapper;

import com.spring.exam.spring_exam.aggregates.response.UsuarioResponse;
import com.spring.exam.spring_exam.entity.UsuarioEntity;

public class UsuarioMapper {

    public static UsuarioResponse mapToUsuarioResponse(UsuarioEntity usuarioEntity) {
        return UsuarioResponse.builder()
                .id(usuarioEntity.getId())
                .nombres(usuarioEntity.getNombres())
                .apellidos(usuarioEntity.getApellidoPaterno().concat(" ")
                        .concat(usuarioEntity.getApellidoMaterno()))
                .build();
    }
}

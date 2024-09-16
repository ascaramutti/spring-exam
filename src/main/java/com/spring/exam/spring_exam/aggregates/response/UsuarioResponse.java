package com.spring.exam.spring_exam.aggregates.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioResponse {

    private Long id;
    private String nombres;
    private String apellidos;
}

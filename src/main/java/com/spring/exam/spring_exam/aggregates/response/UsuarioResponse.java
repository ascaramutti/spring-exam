package com.spring.exam.spring_exam.aggregates.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class UsuarioResponse {

    private Long id;
    private String nombres;
    private String apellidos;
    private String email;
}

package com.spring.exam.spring_exam.aggregates.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequest {

    private String numDoc;
    private String nombres;
    private String apellidos;
    private String email;
    private String password;
}

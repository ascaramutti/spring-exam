package com.spring.exam.spring_exam.aggregates.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReniecApiServiceResponse {

    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tipoDocumento;
    private String numeroDocumento;
    private String digitoVerificador;

}

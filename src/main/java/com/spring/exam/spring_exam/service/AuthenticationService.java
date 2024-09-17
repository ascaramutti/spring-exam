package com.spring.exam.spring_exam.service;

import com.spring.exam.spring_exam.aggregates.request.SignInRequest;
import com.spring.exam.spring_exam.aggregates.request.UsuarioRequest;
import com.spring.exam.spring_exam.aggregates.response.SignInResponse;
import com.spring.exam.spring_exam.aggregates.response.UsuarioResponse;

public interface AuthenticationService {

    UsuarioResponse singUpUser(UsuarioRequest usuarioRequest);

    SignInResponse singIn(SignInRequest signInRequest);

}

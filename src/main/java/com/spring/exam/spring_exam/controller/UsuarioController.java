package com.spring.exam.spring_exam.controller;

import com.spring.exam.spring_exam.aggregates.request.UsuarioRequest;
import com.spring.exam.spring_exam.aggregates.response.UsuarioResponse;
import com.spring.exam.spring_exam.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/v1")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;


    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registrarUsuario(@RequestBody UsuarioRequest usuarioRequest){
        return new ResponseEntity<>(usuarioService.crearUsuario(usuarioRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<UsuarioResponse> buscarUsuario(@PathVariable("dni") String dni) {
        return new ResponseEntity<>(usuarioService.buscarUsuario(dni), HttpStatus.OK);
    }

}

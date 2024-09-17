package com.spring.exam.spring_exam.controller;

import com.spring.exam.spring_exam.aggregates.request.SignInRequest;
import com.spring.exam.spring_exam.aggregates.request.UsuarioRequest;
import com.spring.exam.spring_exam.aggregates.response.SignInResponse;
import com.spring.exam.spring_exam.aggregates.response.UsuarioResponse;
import com.spring.exam.spring_exam.service.AuthenticationService;
import com.spring.exam.spring_exam.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> signUpUser(@RequestBody UsuarioRequest usuarioRequest){
        return new ResponseEntity<>(authenticationService.singUpUser(usuarioRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signInRequest){
        return ResponseEntity.ok(authenticationService.signIn(signInRequest));
    }

    @GetMapping("/{dni}")
    public ResponseEntity<UsuarioResponse> buscarUsuario(@PathVariable("dni") String dni) {
        return new ResponseEntity<>(usuarioService.buscarUsuario(dni), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarUsuario(@PathVariable("id") Long id){
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok("Usuario eliminado.");
    }

}

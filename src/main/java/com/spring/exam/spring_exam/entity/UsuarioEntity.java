package com.spring.exam.spring_exam.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@Builder
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String tipoDocumento;
    private String numeroDocumento;
    private String digitoVerificador;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_rol",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol"))
    private Set<Rol> roles = new HashSet<>();
    
}

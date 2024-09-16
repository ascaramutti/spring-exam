package com.spring.exam.spring_exam.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rol")
@Data
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;
    private String nombreRol;
}

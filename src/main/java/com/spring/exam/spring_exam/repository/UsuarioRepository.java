package com.spring.exam.spring_exam.repository;

import com.spring.exam.spring_exam.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByNumeroDocumento(String numeroDocumento);

}

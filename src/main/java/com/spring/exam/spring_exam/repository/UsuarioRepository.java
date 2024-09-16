package com.spring.exam.spring_exam.repository;

import com.spring.exam.spring_exam.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
}

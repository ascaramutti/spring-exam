package com.spring.exam.spring_exam.repository;

import com.spring.exam.spring_exam.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombreRol(String rol);
}

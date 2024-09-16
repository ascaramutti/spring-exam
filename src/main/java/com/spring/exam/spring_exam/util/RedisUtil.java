package com.spring.exam.spring_exam.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.exam.spring_exam.entity.UsuarioEntity;

public class RedisUtil {

    public static <T> T parseFromString(String json, Class<T> tipoClase) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, tipoClase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String parseToString(UsuarioEntity usuarioEntity) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(usuarioEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

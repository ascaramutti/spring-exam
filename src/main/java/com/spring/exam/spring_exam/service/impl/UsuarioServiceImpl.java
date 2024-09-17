package com.spring.exam.spring_exam.service.impl;

import com.spring.exam.spring_exam.aggregates.mapper.UsuarioMapper;
import com.spring.exam.spring_exam.aggregates.response.UsuarioResponse;
import com.spring.exam.spring_exam.entity.UsuarioEntity;
import com.spring.exam.spring_exam.redis.RedisService;
import com.spring.exam.spring_exam.repository.UsuarioRepository;
import com.spring.exam.spring_exam.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.spring.exam.spring_exam.util.RedisUtil.parseFromString;
import static com.spring.exam.spring_exam.util.RedisUtil.parseToString;
import static com.spring.exam.spring_exam.util.constans.ServiceConstant.REDIS_EXPIRATION_TIME;
import static com.spring.exam.spring_exam.util.constans.ServiceConstant.REDIS_KEY_API_USER;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RedisService redisService;

    @Override
    public UsuarioResponse buscarUsuario(String numeroDocumento) {
        return UsuarioMapper.mapToUsuarioResponse(getUsuario(numeroDocumento));
    }

    @Override
    public UserDetailsService userDetailService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return usuarioRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("USUARIO NO ENCONTRADO"));
            }
        };
    }

    private UsuarioEntity getUsuario(String numeroDocumento) {
        String redisInfo = redisService.getDataFromRedis(REDIS_KEY_API_USER+numeroDocumento);
        if(Objects.nonNull(redisInfo)){
            return parseFromString(redisInfo, UsuarioEntity.class);
        } else {
            UsuarioEntity response= usuarioRepository.findByNumeroDocumento(numeroDocumento)
                    .orElseThrow(() -> new RuntimeException("PERSONA NO ENCONTRADA"));
            String dataForRedis = parseToString(response);
            redisService.saveInRedis(REDIS_KEY_API_USER+numeroDocumento, dataForRedis, REDIS_EXPIRATION_TIME);
            return response;
        }
    }
}

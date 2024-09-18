package com.spring.exam.spring_exam.service.impl;

import com.spring.exam.spring_exam.aggregates.mapper.UsuarioMapper;
import com.spring.exam.spring_exam.aggregates.request.UsuarioRequest;
import com.spring.exam.spring_exam.aggregates.response.UsuarioResponse;
import com.spring.exam.spring_exam.entity.UsuarioEntity;
import com.spring.exam.spring_exam.redis.RedisService;
import com.spring.exam.spring_exam.repository.UsuarioRepository;
import com.spring.exam.spring_exam.service.UsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.spring.exam.spring_exam.aggregates.mapper.UsuarioMapper.mapToUsuarioResponse;
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
        return getUsuario(numeroDocumento);
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

    @Override
    public List<UsuarioResponse> listarUsuarios() {
        List<UsuarioEntity> usuarioEntityList = usuarioRepository.findAll();
        return usuarioEntityList.stream()
                .map(UsuarioMapper::mapToUsuarioResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("USUARIO NO EXISTENTE EN LA BASE DE DATOS"));
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UsuarioEntity actualizarUsuario(Long id, UsuarioRequest usuarioRequest) {
        UsuarioEntity usuarioEntity = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("USUARIO NO EXISTENTE EN LA BASE DE DATOS"));
        usuarioEntity.setEmail(usuarioRequest.getEmail());
        usuarioEntity.setPassword(new BCryptPasswordEncoder().encode(usuarioRequest.getPassword()));
        return usuarioEntity;
    }

    private UsuarioResponse getUsuario(String numeroDocumento) {
        String redisInfo = redisService.getDataFromRedis(REDIS_KEY_API_USER+numeroDocumento);
        if(Objects.nonNull(redisInfo)){
            return parseFromString(redisInfo, UsuarioResponse.class);
        } else {
            UsuarioEntity response= usuarioRepository.findByNumeroDocumento(numeroDocumento)
                    .orElseThrow(() -> new RuntimeException("PERSONA NO ENCONTRADA"));
            String dataForRedis = parseToString(response);
            redisService.saveInRedis(REDIS_KEY_API_USER+numeroDocumento, dataForRedis, REDIS_EXPIRATION_TIME);
            return mapToUsuarioResponse(response);
        }
    }
}

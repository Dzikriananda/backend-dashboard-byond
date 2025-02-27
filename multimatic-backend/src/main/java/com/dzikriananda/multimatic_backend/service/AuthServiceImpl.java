package com.dzikriananda.multimatic_backend.service;

import com.dzikriananda.multimatic_backend.dto.RegisterDto;
import com.dzikriananda.multimatic_backend.interfaces.AuthService;
import com.dzikriananda.multimatic_backend.model.User;
import com.dzikriananda.multimatic_backend.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void saveUser(RegisterDto registerDto) {
        User userData = User
                .builder()
                .email(registerDto.getEmail())
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .build();
        authRepository.saveUser(userData.getEmail(), userData.getUsername(), userData.getPassword());
    }

    @Override
    public User findUserByEmail(String email) {
        List<User> users = authRepository.findUserByEmail(email);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> findAllUser() {
        return authRepository.findAllUser();
    }

    @Override
    public Boolean matchPassword(String plainPassword,String hashedPassword) {
        return passwordEncoder.matches(plainPassword,hashedPassword);
    }
}

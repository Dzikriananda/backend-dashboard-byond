package com.dzikriananda.multimatic_backend.interfaces;

import com.dzikriananda.multimatic_backend.dto.RegisterDto;
import com.dzikriananda.multimatic_backend.model.User;

import java.util.List;

public interface AuthService {
    void saveUser(RegisterDto userData);
//
    User findUserByEmail(String email);
//
    List<User> findAllUser();
    Boolean matchPassword(String plainPassword,String hashedPassword);
}
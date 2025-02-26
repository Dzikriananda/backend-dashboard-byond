package com.dzikriananda.multimatic_backend.repository;

import com.dzikriananda.multimatic_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthRepository extends JpaRepository<User, Integer> {


    @Query(value = "SELECT * FROM users", nativeQuery = true)
    List<User> findAllUser();

    @Transactional
    @Query(value = "INSERT INTO users(email, username, password) " +
            "VALUES (:email, :username, :password) RETURNING id", nativeQuery = true)
    int saveUser(
            @Param("email") String email,
            @Param("username") String username,
            @Param("password") String password
    );

    @Query(value = "SELECT * FROM Users WHERE email = :email", nativeQuery = true)
    List<User> findUserByEmail(@Param("email") String email);



}
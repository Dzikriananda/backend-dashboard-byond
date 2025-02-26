package com.dzikriananda.multimatic_backend.controller;


import com.dzikriananda.multimatic_backend.dto.LoginDto;
import com.dzikriananda.multimatic_backend.dto.RegisterDto;
import com.dzikriananda.multimatic_backend.interfaces.AuthService;
import com.dzikriananda.multimatic_backend.model.User;
import com.dzikriananda.multimatic_backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    JwtService jwtService;

    @GetMapping("/cors-test")
    public String cors() {
        return "cors bypassed";
    }




    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Validated RegisterDto userData) {
        authService.saveUser(userData);
        Map<String, String> response = Map.of("message", "User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity login(@RequestBody @Validated LoginDto userData) {
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> resMessage = new HashMap<>();


        if (userData.getUsername() == null && userData.getEmail() == null) {
            resMessage.put("message", "Email Must be not empty");
            return new ResponseEntity<>(resMessage, headers, HttpStatus.BAD_REQUEST);
        }

        User userFromDb = authService.findUserByEmail(userData.getEmail());

        if (userFromDb == null) {
            resMessage.put("message", "Email or password invalid");
            return new ResponseEntity<>(resMessage, headers, HttpStatus.NOT_FOUND);
        }

        if (!authService.matchPassword(userData.getPassword(), userFromDb.getPassword())) {
            return new ResponseEntity<>("Email or password invalid", headers, HttpStatus.NOT_FOUND);
        }
        String jwtToken = jwtService.generateToken(userFromDb);
        Map<String, String> response = Map.of("token", jwtToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}

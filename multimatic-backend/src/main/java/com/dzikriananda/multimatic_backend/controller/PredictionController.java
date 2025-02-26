package com.dzikriananda.multimatic_backend.controller;

import com.dzikriananda.multimatic_backend.interfaces.AuthService;
import com.dzikriananda.multimatic_backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/predict")
public class PredictionController {
    @Autowired
    private AuthService authService;

    @GetMapping("/dummy")
    public ResponseEntity<?> dummy()
    {
        List<User> data = authService.findAllUser();
        return ResponseEntity.ok().body(data);
    }

}


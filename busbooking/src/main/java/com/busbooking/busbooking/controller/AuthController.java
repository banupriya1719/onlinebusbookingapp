package com.busbooking.busbooking.controller;

import com.busbooking.busbooking.dto.LoginRequest;
import com.busbooking.busbooking.dto.LoginResponse;
import com.busbooking.busbooking.dto.RegisterRequest;
import com.busbooking.busbooking.dto.RegisterResponse;
import com.busbooking.busbooking.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserService userService;

    @Autowired
    public AuthController(AppUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<RegisterResponse>> registerUser(@Valid @RequestBody RegisterRequest request) {
        return userService.register(request)
                .map(ResponseEntity::ok)
                .onErrorResume(error ->
                        Mono.just(ResponseEntity.badRequest()
                                .body(new RegisterResponse(error.getMessage())))
                );
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> loginUser(@Valid @RequestBody LoginRequest request) {
        return userService.login(request)
                .map(ResponseEntity::ok)
                .onErrorResume(error ->
                        Mono.just(ResponseEntity.badRequest()
                                .body(new LoginResponse("Login failed: " + error.getMessage())))
                );
    }
}

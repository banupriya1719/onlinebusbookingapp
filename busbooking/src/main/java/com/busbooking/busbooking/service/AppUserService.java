package com.busbooking.busbooking.service;

import com.busbooking.busbooking.dto.RegisterRequest;
import com.busbooking.busbooking.dto.RegisterResponse;
import com.busbooking.busbooking.dto.LoginRequest;
import com.busbooking.busbooking.dto.LoginResponse;
import reactor.core.publisher.Mono;

public interface AppUserService {
    Mono<RegisterResponse> register(RegisterRequest request);
    Mono<LoginResponse> login(LoginRequest request);
}

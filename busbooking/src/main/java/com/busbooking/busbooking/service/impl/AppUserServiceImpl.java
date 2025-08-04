package com.busbooking.busbooking.service.impl;

import com.busbooking.busbooking.dto.*;
import com.busbooking.busbooking.exception.ResourceNotFoundException;
import com.busbooking.busbooking.exception.UserAlreadyExistsException;
import com.busbooking.busbooking.model.AppUser;
import com.busbooking.busbooking.repository.AppUserRepository;
import com.busbooking.busbooking.security.JwtService;
import com.busbooking.busbooking.service.AppUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AppUserServiceImpl(AppUserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public Mono<RegisterResponse> register(RegisterRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .flatMap(existingUser -> Mono.<RegisterResponse>error(new UserAlreadyExistsException("Username already exists")))
                .switchIfEmpty(Mono.defer(() -> {
                    AppUser user = new AppUser();
                    user.setUsername(request.getUsername());
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));

                    // ✅ Filter allowed roles
                    List<String> allowedRoles = List.of("ROLE_USER", "ROLE_ADMIN");
                    List<String> roles = request.getRoles() == null ? List.of() :
                            request.getRoles().stream()
                                    .filter(allowedRoles::contains)
                                    .collect(Collectors.toList());

                    if (roles.isEmpty()) {
                        roles = List.of("ROLE_USER"); // Default
                    }

                    user.setRoles(roles);

                    return userRepository.save(user)
                            .map(savedUser -> new RegisterResponse("Registration successful"));
                }));
    }

    @Override
    public Mono<LoginResponse> login(LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Invalid username or password")))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        return Mono.error(new ResourceNotFoundException("Invalid username or password"));
                    }

                    String token = jwtService.generateToken(user.getUsername(), user.getRoles());
                    return Mono.just(new LoginResponse(token));
                });
    }
}

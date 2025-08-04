package com.busbooking.busbooking.Service.impl;

import com.busbooking.busbooking.dto.*;
import com.busbooking.busbooking.exception.ResourceNotFoundException;
import com.busbooking.busbooking.exception.UserAlreadyExistsException;
import com.busbooking.busbooking.model.AppUser;
import com.busbooking.busbooking.repository.AppUserRepository;
import com.busbooking.busbooking.security.JwtService;
import com.busbooking.busbooking.service.impl.AppUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

public class AppUserServiceImplTest {

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AppUserServiceImpl userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("newuser@example.com");
        request.setPassword("password");
        request.setRoles(List.of("ROLE_USER"));

        when(userRepository.findByUsername("newuser")).thenReturn(Mono.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(AppUser.class))).thenReturn(Mono.just(new AppUser()));

        StepVerifier.create(userService.register(request))
                .expectNextMatches(response -> response.getMessage().equals("Registration successful"))
                .verifyComplete();
    }

    @Test
    public void testRegisterUser_AlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existinguser");

        when(userRepository.findByUsername("existinguser")).thenReturn(Mono.just(new AppUser()));

        StepVerifier.create(userService.register(request))
                .expectError(UserAlreadyExistsException.class)
                .verify();
    }

    @Test
    public void testLogin_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        AppUser user = new AppUser();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setRoles(List.of("ROLE_USER"));

        when(userRepository.findByUsername("testuser")).thenReturn(Mono.just(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken("testuser", List.of("ROLE_USER"))).thenReturn("jwt-token");

        StepVerifier.create(userService.login(request))
                .expectNextMatches(resp -> resp.getToken().equals("jwt-token"))
                .verifyComplete();
    }

    @Test
    public void testLogin_InvalidPassword() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        AppUser user = new AppUser();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Mono.just(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        StepVerifier.create(userService.login(request))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
}

package com.busbooking.busbooking.controller;

import com.busbooking.busbooking.dto.LoginRequest;
import com.busbooking.busbooking.dto.RegisterRequest;
import com.busbooking.busbooking.service.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

public class AuthControllerTest {

    private AppUserService userService;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(AppUserService.class);
        AuthController controller = new AuthController(userService);

        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void testRegisterUser_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("test123");

        Mockito.when(userService.register(any()))
                .thenReturn(Mono.just(new com.busbooking.busbooking.dto.RegisterResponse("Registration successful")));

        webTestClient.post()
                .uri("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Registration successful");
    }

    @Test
    void testLoginUser_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("test123");

        Mockito.when(userService.login(any()))
                .thenReturn(Mono.just(new com.busbooking.busbooking.dto.LoginResponse("mock-jwt-token")));

        webTestClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").isEqualTo("mock-jwt-token");
    }
}

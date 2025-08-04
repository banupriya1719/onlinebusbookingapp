package com.busbooking.busbooking.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    // ✅ Manual constructor injection (no Lombok)
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter authFilter = new AuthenticationWebFilter(reactiveAuthenticationManager());
        authFilter.setServerAuthenticationConverter(jwtAuthFilter); // convert request to authentication

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/swagger-resources/**",
                                "/swagger-resources/configuration/ui",
                                "/swagger-resources/configuration/security"
                        ).permitAll()
                        .pathMatchers("/api/buses").hasAnyRole("USER", "ADMIN")
                        .pathMatchers("/api/buses/**").hasRole("ADMIN")
                        .pathMatchers("/api/bookings/user/**").hasRole("USER")
                        .pathMatchers("/api/bookings").hasRole("ADMIN")
                        .pathMatchers("/api/passengers/**").hasRole("ADMIN")
                        .pathMatchers("/api/bookings/**").hasAnyRole("USER", "ADMIN")
                        .anyExchange().authenticated()
                )
                .addFilterAt(authFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return authentication -> Mono.just(authentication); // Already authenticated from JWT
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

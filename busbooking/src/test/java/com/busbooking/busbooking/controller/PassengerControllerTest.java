package com.busbooking.busbooking.controller;

import com.busbooking.busbooking.dto.PassengerRequestDTO;
import com.busbooking.busbooking.dto.PassengerResponseDTO;
import com.busbooking.busbooking.service.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PassengerControllerTest {

    private PassengerService passengerService;
    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        passengerService = mock(PassengerService.class);
        PassengerController controller = new PassengerController(passengerService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    public void testCreatePassenger() {
        PassengerRequestDTO request = new PassengerRequestDTO("John", "john@example.com", "1234567890");
        PassengerResponseDTO response = new PassengerResponseDTO("1", "John", "john@example.com", "1234567890");

        when(passengerService.createPassenger(any())).thenReturn(Mono.just(response));

        webTestClient.post().uri("/api/passengers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody(PassengerResponseDTO.class)
                .value(actual -> {
                    assertEquals(response.getId(), actual.getId());
                    assertEquals(response.getName(), actual.getName());
                    assertEquals(response.getEmail(), actual.getEmail());
                    assertEquals(response.getPhoneNumber(), actual.getPhoneNumber());
                });
    }

    @Test
    public void testGetAllPassengers() {
        PassengerResponseDTO passenger1 = new PassengerResponseDTO("1", "John", "john@example.com", "1234567890");
        PassengerResponseDTO passenger2 = new PassengerResponseDTO("2", "Jane", "jane@example.com", "0987654321");

        when(passengerService.getAllPassengers()).thenReturn(Flux.just(passenger1, passenger2));

        webTestClient.get().uri("/api/passengers")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PassengerResponseDTO.class)
                .hasSize(2);
    }

    @Test
    public void testGetPassengerById() {
        PassengerResponseDTO response = new PassengerResponseDTO("1", "John", "john@example.com", "1234567890");

        when(passengerService.getPassengerById("1")).thenReturn(Mono.just(response));

        webTestClient.get().uri("/api/passengers/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PassengerResponseDTO.class)
                .value(actual -> {
                    assertEquals(response.getId(), actual.getId());
                    assertEquals(response.getName(), actual.getName());
                    assertEquals(response.getEmail(), actual.getEmail());
                    assertEquals(response.getPhoneNumber(), actual.getPhoneNumber());
                });
    }

    @Test
    public void testUpdatePassenger() {
        PassengerRequestDTO request = new PassengerRequestDTO("Johnny", "johnny@example.com", "1112223333");
        PassengerResponseDTO updated = new PassengerResponseDTO("1", "Johnny", "johnny@example.com", "1112223333");

        when(passengerService.updatePassenger(eq("1"), any())).thenReturn(Mono.just(updated));

        webTestClient.put().uri("/api/passengers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PassengerResponseDTO.class)
                .value(actual -> {
                    assertEquals(updated.getId(), actual.getId());
                    assertEquals(updated.getName(), actual.getName());
                    assertEquals(updated.getEmail(), actual.getEmail());
                    assertEquals(updated.getPhoneNumber(), actual.getPhoneNumber());
                });
    }

    @Test
    public void testDeletePassenger() {
        when(passengerService.deletePassenger("1")).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/passengers/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}

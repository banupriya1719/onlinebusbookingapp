package com.busbooking.busbooking.Service.impl;

import com.busbooking.busbooking.dto.PassengerRequestDTO;
import com.busbooking.busbooking.dto.PassengerResponseDTO;
import com.busbooking.busbooking.exception.ResourceNotFoundException;
import com.busbooking.busbooking.model.Passenger;
import com.busbooking.busbooking.repository.PassengerRepository;
import com.busbooking.busbooking.service.impl.PassengerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PassengerServiceImplTest {

    private PassengerRepository passengerRepository;
    private PassengerServiceImpl passengerService;

    @BeforeEach
    void setUp() {
        passengerRepository = mock(PassengerRepository.class);
        passengerService = new PassengerServiceImpl(passengerRepository);
    }

    @Test
    void testCreatePassenger() {
        PassengerRequestDTO dto = new PassengerRequestDTO("John", "john@example.com", "1234567890");

        Passenger saved = new Passenger("1", "John", "john@example.com", "1234567890");

        when(passengerRepository.save(any(Passenger.class))).thenReturn(Mono.just(saved));

        Mono<PassengerResponseDTO> result = passengerService.createPassenger(dto);

        StepVerifier.create(result)
                .assertNext(res -> {
                    assertEquals("1", res.getId());
                    assertEquals("John", res.getName());
                    assertEquals("john@example.com", res.getEmail());
                    assertEquals("1234567890", res.getPhoneNumber());
                })
                .verifyComplete();
    }

    @Test
    void testGetAllPassengers() {
        Passenger p1 = new Passenger("1", "John", "john@example.com", "1234567890");
        Passenger p2 = new Passenger("2", "Jane", "jane@example.com", "0987654321");

        when(passengerRepository.findAll()).thenReturn(Flux.just(p1, p2));

        Flux<PassengerResponseDTO> result = passengerService.getAllPassengers();

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void testGetPassengerById_Success() {
        Passenger p = new Passenger("1", "John", "john@example.com", "1234567890");
        when(passengerRepository.findById("1")).thenReturn(Mono.just(p));

        Mono<PassengerResponseDTO> result = passengerService.getPassengerById("1");

        StepVerifier.create(result)
                .assertNext(res -> {
                    assertEquals("1", res.getId());
                    assertEquals("John", res.getName());
                })
                .verifyComplete();
    }

    @Test
    void testGetPassengerById_NotFound() {
        when(passengerRepository.findById("1")).thenReturn(Mono.empty());

        Mono<PassengerResponseDTO> result = passengerService.getPassengerById("1");

        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof ResourceNotFoundException &&
                        ex.getMessage().equals("Passenger not found with id: 1"))
                .verify();
    }

    @Test
    void testUpdatePassenger_Success() {
        Passenger existing = new Passenger("1", "Old", "old@mail.com", "000");
        Passenger updated = new Passenger("1", "New", "new@mail.com", "999");

        PassengerRequestDTO dto = new PassengerRequestDTO("New", "new@mail.com", "999");

        when(passengerRepository.findById("1")).thenReturn(Mono.just(existing));
        when(passengerRepository.save(any(Passenger.class))).thenReturn(Mono.just(updated));

        Mono<PassengerResponseDTO> result = passengerService.updatePassenger("1", dto);

        StepVerifier.create(result)
                .assertNext(res -> {
                    assertEquals("1", res.getId());
                    assertEquals("New", res.getName());
                })
                .verifyComplete();
    }

    @Test
    void testUpdatePassenger_NotFound() {
        PassengerRequestDTO dto = new PassengerRequestDTO("New", "new@mail.com", "999");
        when(passengerRepository.findById("1")).thenReturn(Mono.empty());

        Mono<PassengerResponseDTO> result = passengerService.updatePassenger("1", dto);

        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof ResourceNotFoundException &&
                        ex.getMessage().equals("Passenger not found with id: 1"))
                .verify();
    }

    @Test
    void testDeletePassenger_Success() {
        Passenger existing = new Passenger("1", "John", "john@example.com", "1234567890");

        when(passengerRepository.findById("1")).thenReturn(Mono.just(existing));
        when(passengerRepository.deleteById("1")).thenReturn(Mono.empty());

        Mono<Void> result = passengerService.deletePassenger("1");

        StepVerifier.create(result).verifyComplete();

        verify(passengerRepository).deleteById("1");
    }

    @Test
    void testDeletePassenger_NotFound() {
        when(passengerRepository.findById("1")).thenReturn(Mono.empty());

        Mono<Void> result = passengerService.deletePassenger("1");

        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof ResourceNotFoundException &&
                        ex.getMessage().equals("Passenger not found with id: 1"))
                .verify();

        verify(passengerRepository, never()).deleteById("1");
    }
}

package com.busbooking.busbooking.Service.impl;

import com.busbooking.busbooking.dto.BusRequestDTO;
import com.busbooking.busbooking.dto.BusResponseDTO;
import com.busbooking.busbooking.exception.ResourceNotFoundException;
import com.busbooking.busbooking.model.Bus;
import com.busbooking.busbooking.repository.BusRepository;
import com.busbooking.busbooking.service.impl.BusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.*;

class BusServiceImplTest {

    @Mock
    private BusRepository busRepository;

    @InjectMocks
    private BusServiceImpl busService;

    private BusRequestDTO requestDTO;
    private Bus savedBus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDTO = new BusRequestDTO(
                "TN01AB1234",
                "Chennai",
                "Madurai",
                "2025-08-05",
                45
        );

        savedBus = new Bus();
        savedBus.setId(UUID.randomUUID().toString());
        savedBus.setBusNumber(requestDTO.getBusNumber());
        savedBus.setSource(requestDTO.getSource());
        savedBus.setDestination(requestDTO.getDestination());
        savedBus.setTravelDate(requestDTO.getTravelDate());
        savedBus.setTotalSeats(requestDTO.getTotalSeats());
    }

    @Test
    void testAddBus() {
        when(busRepository.save(any(Bus.class))).thenReturn(Mono.just(savedBus));

        Mono<BusResponseDTO> result = busService.addBus(requestDTO);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getBusNumber().equals(savedBus.getBusNumber()) &&
                                response.getSource().equals(savedBus.getSource()))
                .verifyComplete();

        verify(busRepository, times(1)).save(any(Bus.class));
    }

    @Test
    void testGetAllBuses() {
        when(busRepository.findAll()).thenReturn(Flux.just(savedBus));

        Flux<BusResponseDTO> result = busService.getAllBuses();

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getId().equals(savedBus.getId()) &&
                                response.getTotalSeats() == savedBus.getTotalSeats())
                .verifyComplete();

        verify(busRepository, times(1)).findAll();
    }

    @Test
    void testGetBusById_Success() {
        when(busRepository.findById(savedBus.getId())).thenReturn(Mono.just(savedBus));

        Mono<BusResponseDTO> result = busService.getBusById(savedBus.getId());

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getDestination().equals(savedBus.getDestination()))
                .verifyComplete();

        verify(busRepository, times(1)).findById(savedBus.getId());
    }

    @Test
    void testGetBusById_NotFound() {
        String id = "nonexistent-id";
        when(busRepository.findById(id)).thenReturn(Mono.empty());

        Mono<BusResponseDTO> result = busService.getBusById(id);

        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof ResourceNotFoundException &&
                        ex.getMessage().equals("Bus not found with id: " + id))
                .verify();

        verify(busRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteBus_Success() {
        when(busRepository.findById(savedBus.getId())).thenReturn(Mono.just(savedBus));
        when(busRepository.deleteById(savedBus.getId())).thenReturn(Mono.empty());

        Mono<Void> result = busService.deleteBus(savedBus.getId());

        StepVerifier.create(result)
                .verifyComplete();

        verify(busRepository, times(1)).findById(savedBus.getId());
        verify(busRepository, times(1)).deleteById(savedBus.getId());
    }

    @Test
    void testDeleteBus_NotFound() {
        String id = "invalid-id";
        when(busRepository.findById(id)).thenReturn(Mono.empty());

        Mono<Void> result = busService.deleteBus(id);

        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof ResourceNotFoundException &&
                        ex.getMessage().equals("Bus not found with id: " + id))
                .verify();

        verify(busRepository, times(1)).findById(id);
        verify(busRepository, never()).deleteById(id);
    }
}

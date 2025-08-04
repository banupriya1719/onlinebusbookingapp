package com.busbooking.busbooking.controller;

import com.busbooking.busbooking.dto.PassengerRequestDTO;
import com.busbooking.busbooking.dto.PassengerResponseDTO;
import com.busbooking.busbooking.service.PassengerService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@RestController
@RequestMapping("/api/passengers")

public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PassengerResponseDTO> createPassenger(@RequestBody PassengerRequestDTO dto) {
        return passengerService.createPassenger(dto);
    }

    @GetMapping
    public Flux<PassengerResponseDTO> getAllPassengers() {
        return passengerService.getAllPassengers();
    }

    @GetMapping("/{id}")
    public Mono<PassengerResponseDTO> getPassengerById(@PathVariable String id) {
        return passengerService.getPassengerById(id);
    }

    @PutMapping("/{id}")
    public Mono<PassengerResponseDTO> updatePassenger(@PathVariable String id, @RequestBody PassengerRequestDTO dto) {
        return passengerService.updatePassenger(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deletePassenger(@PathVariable String id) {
        return passengerService.deletePassenger(id);
    }
}

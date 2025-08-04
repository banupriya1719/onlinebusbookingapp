package com.busbooking.busbooking.controller;

import com.busbooking.busbooking.dto.BusRequestDTO;
import com.busbooking.busbooking.dto.BusResponseDTO;
import com.busbooking.busbooking.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@RestController
@RequestMapping("/api/buses")

public class BusController {

    private final BusService busService;

    @Autowired
    public BusController(BusService busService) {
        this.busService = busService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BusResponseDTO> addBus(@RequestBody BusRequestDTO dto) {
        return busService.addBus(dto);
    }

    @GetMapping
    public Flux<BusResponseDTO> getAllBuses() {
        return busService.getAllBuses();
    }

    @GetMapping("/{id}")
    public Mono<BusResponseDTO> getBusById(@PathVariable String id) {
        return busService.getBusById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBus(@PathVariable String id) {
        return busService.deleteBus(id);
    }
}

package com.busbooking.busbooking.service;

import com.busbooking.busbooking.dto.BusRequestDTO;
import com.busbooking.busbooking.dto.BusResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BusService {
    Mono<BusResponseDTO> addBus(BusRequestDTO requestDTO);
    Flux<BusResponseDTO> getAllBuses();
    Mono<BusResponseDTO> getBusById(String id);
    Mono<Void> deleteBus(String id);
}

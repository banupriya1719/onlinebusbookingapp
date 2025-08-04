package com.busbooking.busbooking.service;

import com.busbooking.busbooking.dto.PassengerRequestDTO;
import com.busbooking.busbooking.dto.PassengerResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PassengerService {

    Mono<PassengerResponseDTO> createPassenger(PassengerRequestDTO dto);

    Flux<PassengerResponseDTO> getAllPassengers();

    Mono<PassengerResponseDTO> getPassengerById(String id);

    Mono<PassengerResponseDTO> updatePassenger(String id, PassengerRequestDTO dto);

    Mono<Void> deletePassenger(String id);
}

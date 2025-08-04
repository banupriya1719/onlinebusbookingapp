package com.busbooking.busbooking.service.impl;

import com.busbooking.busbooking.dto.PassengerRequestDTO;
import com.busbooking.busbooking.dto.PassengerResponseDTO;
import com.busbooking.busbooking.exception.ResourceNotFoundException;
import com.busbooking.busbooking.model.Passenger;
import com.busbooking.busbooking.repository.PassengerRepository;
import com.busbooking.busbooking.service.PassengerService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

    public PassengerServiceImpl(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Override
    public Mono<PassengerResponseDTO> createPassenger(PassengerRequestDTO dto) {
        Passenger passenger = new Passenger();
        passenger.setName(dto.getName());
        passenger.setEmail(dto.getEmail());
        passenger.setPhoneNumber(dto.getPhoneNumber());

        return passengerRepository.save(passenger)
                .map(this::mapToResponseDTO);
    }

    @Override
    public Flux<PassengerResponseDTO> getAllPassengers() {
        return passengerRepository.findAll()
                .map(this::mapToResponseDTO);
    }

    @Override
    public Mono<PassengerResponseDTO> getPassengerById(String id) {
        return passengerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Passenger not found with id: " + id)))
                .map(this::mapToResponseDTO);
    }

    @Override
    public Mono<PassengerResponseDTO> updatePassenger(String id, PassengerRequestDTO dto) {
        return passengerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Passenger not found with id: " + id)))
                .flatMap(existing -> {
                    existing.setName(dto.getName());
                    existing.setEmail(dto.getEmail());
                    existing.setPhoneNumber(dto.getPhoneNumber());
                    return passengerRepository.save(existing);
                })
                .map(this::mapToResponseDTO);
    }

    @Override
    public Mono<Void> deletePassenger(String id) {
        return passengerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Passenger not found with id: " + id)))
                .flatMap(existing -> passengerRepository.deleteById(id));
    }

    private PassengerResponseDTO mapToResponseDTO(Passenger passenger) {
        return new PassengerResponseDTO(
                passenger.getId(),
                passenger.getName(),
                passenger.getEmail(),
                passenger.getPhoneNumber()
        );
    }
}

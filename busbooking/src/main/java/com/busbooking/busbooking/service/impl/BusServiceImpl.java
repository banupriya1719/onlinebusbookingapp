package com.busbooking.busbooking.service.impl;

import com.busbooking.busbooking.dto.BusRequestDTO;
import com.busbooking.busbooking.dto.BusResponseDTO;
import com.busbooking.busbooking.exception.ResourceNotFoundException;
import com.busbooking.busbooking.model.Bus;
import com.busbooking.busbooking.repository.BusRepository;
import com.busbooking.busbooking.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;

    @Autowired
    public BusServiceImpl(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    @Override
    public Mono<BusResponseDTO> addBus(BusRequestDTO dto) {
        Bus bus = new Bus();
        bus.setBusNumber(dto.getBusNumber());
        bus.setSource(dto.getSource());
        bus.setDestination(dto.getDestination());
        bus.setTravelDate(dto.getTravelDate());
        bus.setTotalSeats(dto.getTotalSeats());

        return busRepository.save(bus)
                .map(this::mapToResponseDTO);
    }

    @Override
    public Flux<BusResponseDTO> getAllBuses() {
        return busRepository.findAll()
                .map(this::mapToResponseDTO);
    }

    @Override
    public Mono<BusResponseDTO> getBusById(String id) {
        return busRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Bus not found with id: " + id)))
                .map(this::mapToResponseDTO);
    }

    @Override
    public Mono<Void> deleteBus(String id) {
        return busRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Bus not found with id: " + id)))
                .flatMap(existing -> busRepository.deleteById(id));
    }

    private BusResponseDTO mapToResponseDTO(Bus bus) {
        return new BusResponseDTO(
                bus.getId(),
                bus.getBusNumber(),
                bus.getSource(),
                bus.getDestination(),
                bus.getTravelDate(),
                bus.getTotalSeats()
        );
    }
}

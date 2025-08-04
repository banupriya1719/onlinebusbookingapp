package com.busbooking.busbooking.service.impl;

import com.busbooking.busbooking.dto.BookingRequestDTO;
import com.busbooking.busbooking.dto.BookingResponseDTO;
import com.busbooking.busbooking.exception.ResourceNotFoundException;
import com.busbooking.busbooking.model.Booking;
import com.busbooking.busbooking.repository.BookingRepository;
import com.busbooking.busbooking.service.BookingService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Mono<BookingResponseDTO> createBooking(BookingRequestDTO dto) {
        Booking booking = new Booking();
        booking.setPassengerId(dto.getPassengerId());
        booking.setBusId(dto.getBusId());
        booking.setSeatNumber(dto.getSeatNumber());
        booking.setBookingDate(dto.getBookingDate());

        return bookingRepository.save(booking)
                .map(saved -> mapToResponseDTO(saved));
    }

    @Override
    public Flux<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .map(this::mapToResponseDTO);
    }

    @Override
    public Mono<BookingResponseDTO> getBookingById(String id) {
        return bookingRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Booking not found with id: " + id)))
                .map(this::mapToResponseDTO);
    }

    @Override
    public Mono<Void> deleteBooking(String id) {
        return bookingRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Booking not found with id: " + id)))
                .flatMap(existing -> bookingRepository.deleteById(id));
    }

    private BookingResponseDTO mapToResponseDTO(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getPassengerId(),
                booking.getBusId(),
                booking.getSeatNumber(),
                booking.getBookingDate()
        );
    }
}

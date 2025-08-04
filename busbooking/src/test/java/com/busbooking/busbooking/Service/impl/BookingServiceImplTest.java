package com.busbooking.busbooking.Service.impl;

import com.busbooking.busbooking.dto.BookingRequestDTO;
import com.busbooking.busbooking.dto.BookingResponseDTO;
import com.busbooking.busbooking.exception.ResourceNotFoundException;
import com.busbooking.busbooking.model.Booking;
import com.busbooking.busbooking.repository.BookingRepository;
import com.busbooking.busbooking.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookingServiceImplTest {

    private BookingRepository bookingRepository;
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        bookingRepository = Mockito.mock(BookingRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository);
    }

    @Test
    void testCreateBooking() {
        BookingRequestDTO dto = new BookingRequestDTO("passenger1", "bus1", "A1", "2025-08-01");

        Booking savedBooking = new Booking();
        savedBooking.setId("b1");
        savedBooking.setPassengerId(dto.getPassengerId());
        savedBooking.setBusId(dto.getBusId());
        savedBooking.setSeatNumber(dto.getSeatNumber());
        savedBooking.setBookingDate(dto.getBookingDate());

        when(bookingRepository.save(any(Booking.class))).thenReturn(Mono.just(savedBooking));

        Mono<BookingResponseDTO> result = bookingService.createBooking(dto);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getId().equals("b1") &&
                                response.getPassengerId().equals("passenger1") &&
                                response.getBusId().equals("bus1") &&
                                response.getSeatNumber().equals("A1") &&
                                response.getBookingDate().equals("2025-08-01")
                )
                .verifyComplete();

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testGetAllBookings() {
        Booking booking1 = new Booking("b1", "p1", "bus1", "A1", "2025-08-01");
        Booking booking2 = new Booking("b2", "p2", "bus2", "B2", "2025-08-02");

        when(bookingRepository.findAll()).thenReturn(Flux.just(booking1, booking2));

        Flux<BookingResponseDTO> result = bookingService.getAllBookings();

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();

        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void testGetBookingById_Success() {
        Booking booking = new Booking("b1", "p1", "bus1", "A1", "2025-08-01");
        when(bookingRepository.findById("b1")).thenReturn(Mono.just(booking));

        Mono<BookingResponseDTO> result = bookingService.getBookingById("b1");

        StepVerifier.create(result)
                .expectNextMatches(res -> res.getId().equals("b1"))
                .verifyComplete();

        verify(bookingRepository, times(1)).findById("b1");
    }

    @Test
    void testGetBookingById_NotFound() {
        when(bookingRepository.findById("b1")).thenReturn(Mono.empty());

        Mono<BookingResponseDTO> result = bookingService.getBookingById("b1");

        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof ResourceNotFoundException &&
                        ex.getMessage().equals("Booking not found with id: b1"))
                .verify();

        verify(bookingRepository, times(1)).findById("b1");
    }

    @Test
    void testDeleteBooking_Success() {
        Booking booking = new Booking("b1", "p1", "bus1", "A1", "2025-08-01");

        when(bookingRepository.findById("b1")).thenReturn(Mono.just(booking));
        when(bookingRepository.deleteById("b1")).thenReturn(Mono.empty());

        Mono<Void> result = bookingService.deleteBooking("b1");

        StepVerifier.create(result)
                .verifyComplete();

        verify(bookingRepository, times(1)).findById("b1");
        verify(bookingRepository, times(1)).deleteById("b1");
    }

    @Test
    void testDeleteBooking_NotFound() {
        when(bookingRepository.findById("b1")).thenReturn(Mono.empty());

        Mono<Void> result = bookingService.deleteBooking("b1");

        StepVerifier.create(result)
                .expectErrorMatches(ex -> ex instanceof ResourceNotFoundException &&
                        ex.getMessage().equals("Booking not found with id: b1"))
                .verify();

        verify(bookingRepository, times(1)).findById("b1");
        verify(bookingRepository, never()).deleteById("b1");
    }
}

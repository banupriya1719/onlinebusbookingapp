package com.busbooking.busbooking.controller;

import com.busbooking.busbooking.dto.BookingRequestDTO;
import com.busbooking.busbooking.dto.BookingResponseDTO;
import com.busbooking.busbooking.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookingResponseDTO> createBooking(@RequestBody BookingRequestDTO dto) {
        return bookingService.createBooking(dto);
    }

    @GetMapping
    public Flux<BookingResponseDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public Mono<BookingResponseDTO> getBookingById(@PathVariable String id) {
        return bookingService.getBookingById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBooking(@PathVariable String id) {
        return bookingService.deleteBooking(id);
    }
}

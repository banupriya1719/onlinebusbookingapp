package com.busbooking.busbooking.service;

import com.busbooking.busbooking.dto.BookingRequestDTO;
import com.busbooking.busbooking.dto.BookingResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookingService {
    Mono<BookingResponseDTO> createBooking(BookingRequestDTO dto);
    Flux<BookingResponseDTO> getAllBookings();
    Mono<BookingResponseDTO> getBookingById(String id);
    Mono<Void> deleteBooking(String id);
}

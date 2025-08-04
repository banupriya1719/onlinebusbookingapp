package com.busbooking.busbooking.controller;

import com.busbooking.busbooking.dto.BookingRequestDTO;
import com.busbooking.busbooking.dto.BookingResponseDTO;
import com.busbooking.busbooking.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

public class BookingControllerTest {

    private BookingService bookingService;
    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        bookingService = mock(BookingService.class);
        BookingController controller = new BookingController(bookingService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    public void testCreateBooking() {
        BookingRequestDTO requestDTO = new BookingRequestDTO("p1", "b1", "A1", "2025-08-01");
        BookingResponseDTO responseDTO = new BookingResponseDTO("id1", "p1", "b1", "A1", "2025-08-01");

        when(bookingService.createBooking(any())).thenReturn(Mono.just(responseDTO));

        webTestClient.post().uri("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookingResponseDTO.class)
                .value(response -> {
                    assert response.getId().equals("id1");
                    assert response.getPassengerId().equals("p1");
                    assert response.getBusId().equals("b1");
                    assert response.getSeatNumber().equals("A1");
                    assert response.getBookingDate().equals("2025-08-01");
                });
    }

    @Test
    public void testGetAllBookings() {
        BookingResponseDTO booking1 = new BookingResponseDTO("id1", "p1", "b1", "A1", "2025-08-01");
        BookingResponseDTO booking2 = new BookingResponseDTO("id2", "p2", "b2", "B2", "2025-08-02");

        when(bookingService.getAllBookings()).thenReturn(Flux.just(booking1, booking2));

        webTestClient.get().uri("/api/bookings")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookingResponseDTO.class)
                .hasSize(2);
    }

    @Test
    public void testGetBookingById() {
        BookingResponseDTO booking = new BookingResponseDTO("id1", "p1", "b1", "A1", "2025-08-01");

        when(bookingService.getBookingById("id1")).thenReturn(Mono.just(booking));

        webTestClient.get().uri("/api/bookings/id1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookingResponseDTO.class)
                .isEqualTo(booking);
    }

    @Test
    public void testDeleteBooking() {
        when(bookingService.deleteBooking("id1")).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/bookings/id1")
                .exchange()
                .expectStatus().isNoContent();
    }
}

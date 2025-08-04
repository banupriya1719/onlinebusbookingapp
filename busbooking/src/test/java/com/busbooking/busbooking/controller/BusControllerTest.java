package com.busbooking.busbooking.controller;

import com.busbooking.busbooking.dto.BusRequestDTO;
import com.busbooking.busbooking.dto.BusResponseDTO;
import com.busbooking.busbooking.service.BusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

public class BusControllerTest {

    private BusService busService;
    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        busService = mock(BusService.class);
        BusController controller = new BusController(busService);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    public void testAddBus() {
        BusRequestDTO requestDTO = new BusRequestDTO("TN01AB1234", "Chennai", "Madurai", "2025-08-05", 40);
        BusResponseDTO responseDTO = new BusResponseDTO("id1", "TN01AB1234", "Chennai", "Madurai", "2025-08-05", 40);

        when(busService.addBus(any())).thenReturn(Mono.just(responseDTO));

        webTestClient.post().uri("/api/buses")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody(BusResponseDTO.class)
                .value(response -> {
                    assert response.getId().equals("id1");
                    assert response.getBusNumber().equals("TN01AB1234");
                    assert response.getSource().equals("Chennai");
                    assert response.getDestination().equals("Madurai");
                    assert response.getTravelDate().equals("2025-08-05");
                    assert response.getTotalSeats() == 40;
                });
    }

    @Test
    public void testGetAllBuses() {
        BusResponseDTO bus1 = new BusResponseDTO("id1", "TN01AB1234", "Chennai", "Madurai", "2025-08-05", 40);
        BusResponseDTO bus2 = new BusResponseDTO("id2", "TN02CD5678", "Salem", "Trichy", "2025-08-06", 30);

        when(busService.getAllBuses()).thenReturn(Flux.just(bus1, bus2));

        webTestClient.get().uri("/api/buses")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BusResponseDTO.class)
                .hasSize(2);
    }

    @Test
    public void testGetBusById() {
        BusResponseDTO bus = new BusResponseDTO("id1", "TN01AB1234", "Chennai", "Madurai", "2025-08-05", 40);

        when(busService.getBusById("id1")).thenReturn(Mono.just(bus));

        webTestClient.get().uri("/api/buses/id1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BusResponseDTO.class)
                .value(response -> {
                    assert response.getId().equals("id1");
                    assert response.getBusNumber().equals("TN01AB1234");
                });
    }

    @Test
    public void testDeleteBus() {
        when(busService.deleteBus("id1")).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/buses/id1")
                .exchange()
                .expectStatus().isNoContent();
    }
}

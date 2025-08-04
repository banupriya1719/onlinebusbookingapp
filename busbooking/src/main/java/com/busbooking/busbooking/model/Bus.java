package com.busbooking.busbooking.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "buses")
public class Bus {
    @Id
    private String id;
    private String busNumber;
    private String source;
    private String destination;
    private String travelDate;
    private int totalSeats;

    public Bus() {
    }

    public Bus(String id, String busNumber, String source, String destination, String travelDate, int totalSeats) {
        this.id = id;
        this.busNumber = busNumber;
        this.source = source;
        this.destination = destination;
        this.travelDate = travelDate;
        this.totalSeats = totalSeats;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
}

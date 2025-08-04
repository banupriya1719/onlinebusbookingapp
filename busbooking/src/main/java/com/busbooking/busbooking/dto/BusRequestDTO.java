package com.busbooking.busbooking.dto;

public class BusRequestDTO {
    private String busNumber;
    private String source;
    private String destination;
    private String travelDate;
    private int totalSeats;

    public BusRequestDTO() {
    }

    public BusRequestDTO(String busNumber, String source, String destination, String travelDate, int totalSeats) {
        this.busNumber = busNumber;
        this.source = source;
        this.destination = destination;
        this.travelDate = travelDate;
        this.totalSeats = totalSeats;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
}

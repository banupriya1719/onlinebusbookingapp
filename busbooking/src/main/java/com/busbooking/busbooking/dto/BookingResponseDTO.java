package com.busbooking.busbooking.dto;

import java.util.Objects;

public class BookingResponseDTO {
    private String id;
    private String passengerId;
    private String busId;
    private String seatNumber;
    private String bookingDate;

    public BookingResponseDTO() {
    }

    public BookingResponseDTO(String id, String passengerId, String busId, String seatNumber, String bookingDate) {
        this.id = id;
        this.passengerId = passengerId;
        this.busId = busId;
        this.seatNumber = seatNumber;
        this.bookingDate = bookingDate;
    }

    public String getId() {
        return id;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public String getBusId() {
        return busId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingResponseDTO)) return false;
        BookingResponseDTO that = (BookingResponseDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(passengerId, that.passengerId) &&
                Objects.equals(busId, that.busId) &&
                Objects.equals(seatNumber, that.seatNumber) &&
                Objects.equals(bookingDate, that.bookingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passengerId, busId, seatNumber, bookingDate);
    }
}
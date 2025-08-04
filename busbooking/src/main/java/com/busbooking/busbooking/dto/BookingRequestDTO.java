package com.busbooking.busbooking.dto;

public class BookingRequestDTO {
    private String passengerId;
    private String busId;
    private String seatNumber;
    private String bookingDate;

    public BookingRequestDTO() {
    }

    public BookingRequestDTO(String passengerId, String busId, String seatNumber, String bookingDate) {
        this.passengerId = passengerId;
        this.busId = busId;
        this.seatNumber = seatNumber;
        this.bookingDate = bookingDate;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }
}

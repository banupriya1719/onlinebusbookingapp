package com.busbooking.busbooking.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bookings")
public class Booking {

    @Id
    private String id;

    private String passengerId;
    private String busId;
    private String seatNumber;
    private String bookingDate;

    public Booking() {
    }

    public Booking(String id, String passengerId, String busId, String seatNumber, String bookingDate) {
        this.id = id;
        this.passengerId = passengerId;
        this.busId = busId;
        this.seatNumber = seatNumber;
        this.bookingDate = bookingDate;
    }

    // Getters
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

    // Setters
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
}

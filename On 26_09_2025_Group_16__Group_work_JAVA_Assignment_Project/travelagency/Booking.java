package com.travelagency;

import java.util.Date;

public class Booking {
    private int bookingId;
    private Customer customer;
    private Destination destination;
    private Date travelDate;
    private String status;

   
    public Booking(Customer customer, Destination destination, Date travelDate) {
        this.customer = customer;
        this.destination = destination;
        this.travelDate = travelDate;
        this.status = "Pending";
    }

    
    public Booking(int bookingId, Customer customer, Destination destination, Date travelDate, String status) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.destination = destination;
        this.travelDate = travelDate;
        this.status = status;
    }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public Customer getCustomer() { return customer; }
    public Destination getDestination() { return destination; }
    public Date getTravelDate() { return travelDate; }
    public void setTravelDate(Date travelDate) { this.travelDate = travelDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

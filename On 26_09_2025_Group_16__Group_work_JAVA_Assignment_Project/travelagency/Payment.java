package com.travelagency;

import java.util.Date;

public class Payment {
    private int paymentId;
    private Booking booking;
    private double amount;
    private String method;
    private Date paymentDate;
    private String status;

    public Payment(int paymentId, Booking booking, double amount, String method, Date paymentDate, String status) {
        this.paymentId = paymentId;
        this.booking = booking;
        this.amount = amount;
        this.method = method;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    public Payment(Booking booking, double amount, String method, Date paymentDate, String status) {
        this(0, booking, amount, method, paymentDate, status);
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public Booking getBooking() { return booking; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public Date getPaymentDate() { return paymentDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

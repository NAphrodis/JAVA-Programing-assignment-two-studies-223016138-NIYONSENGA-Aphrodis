package com.travelagency;

public class Destination {
    private int destinationId;
    private String country;
    private String city;
    private double price;
    private int availableSeats;

    public Destination(int destinationId, String country, String city, double price, int availableSeats) {
        this.destinationId = destinationId;
        this.country = country;
        this.city = city;
        this.price = price;
        this.availableSeats = availableSeats;
    }

    public int getDestinationId() { return destinationId; }
    public String getCountry() { return country; }
    public String getCity() { return city; }
    public double getPrice() { return price; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
}

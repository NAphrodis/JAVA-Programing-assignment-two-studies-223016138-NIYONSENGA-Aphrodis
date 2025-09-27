package com.travelagency;

import java.util.ArrayList;

public class DataStore {
    public static ArrayList<Customer> customers = new ArrayList<Customer>();
    public static ArrayList<Destination> destinations = new ArrayList<Destination>();
    public static ArrayList<Booking> bookings = new ArrayList<Booking>();
    public static ArrayList<Payment> payments = new ArrayList<Payment>();

    
    public static Customer findCustomerById(int id) {
        for (Customer c : customers) {
            if (c.getCustomerId() == id) {
                return c;
            }
        }
        return null;
    }

    public static Destination findDestinationById(int id) {
        for (Destination d : destinations) {
            if (d.getDestinationId() == id) {
                return d;
            }
        }
        return null;
    }

    public static Booking findBookingById(int id) {
        for (Booking b : bookings) {
            if (b.getBookingId() == id) {
                return b;
            }
        }
        return null;
    }

    public static Payment findPaymentById(int id) {
        for (Payment p : payments) {
            if (p.getPaymentId() == id) {
                return p;
            }
        }
        return null;
    }

    
    public static void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public static void addDestination(Destination destination) {
        destinations.add(destination);
    }

    public static void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public static void addPayment(Payment payment) {
        payments.add(payment);
    }

    
    public static boolean removeCustomer(Customer customer) {
        return customers.remove(customer);
    }

    public static boolean removeDestination(Destination destination) {
        return destinations.remove(destination);
    }

    public static boolean removeBooking(Booking booking) {
        return bookings.remove(booking);
    }

    public static boolean removePayment(Payment payment) {
        return payments.remove(payment);
    }
}

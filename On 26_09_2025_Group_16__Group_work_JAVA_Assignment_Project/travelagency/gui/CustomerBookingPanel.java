package com.travelagency.gui;

import com.travelagency.*;
import com.travelagency.db.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomerBookingPanel extends JPanel {

    private User loggedUser;
    private Customer linkedCustomer; 

    private DefaultTableModel tripsModel;
    private JTable tripsTable;

    private DefaultTableModel bookingsModel;
    private JTable bookingsTable;

    private DestinationDAO destinationDAO = new DestinationDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private CustomerDAO customerDAO = new CustomerDAO();

    public CustomerBookingPanel(User user) {
        this.loggedUser = user;
        setLayout(new BorderLayout());

       
        try {
            linkedCustomer = resolveCustomerForUser(user);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error resolving customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Top: trips
        tripsModel = new DefaultTableModel(new Object[]{"ID","Country","City","Price","Seats"},0);
        tripsTable = new JTable(tripsModel);

        // Bottom: my bookings
        bookingsModel = new DefaultTableModel(new Object[]{"BookingID","Destination","Date","Status"},0);
        bookingsTable = new JTable(bookingsModel);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(tripsTable), new JScrollPane(bookingsTable));
        split.setDividerLocation(250);
        add(split, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        JButton bookBtn = new JButton("Book Selected Trip");
        JButton updateBtn = new JButton("Update Selected Booking");
        JButton cancelBtn = new JButton("Cancel Selected Booking");
        JButton refreshBtn = new JButton("Refresh");

        controls.add(bookBtn);
        controls.add(updateBtn);
        controls.add(cancelBtn);
        controls.add(refreshBtn);

        add(controls, BorderLayout.SOUTH);

        loadTrips();
        loadMyBookings();

        bookBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { doBook(); }
        });
        updateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { doUpdate(); }
        });
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { doCancel(); }
        });
        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { loadTrips(); loadMyBookings(); }
        });
    }

    
    private Customer resolveCustomerForUser(User user) throws Exception {
        if (user.getCustomerId() != null) {
            Customer c = customerDAO.getCustomerById(user.getCustomerId());
            if (c != null) return c;
        }

        // try email match
        Customer byEmail = customerDAO.findByEmail(user.getUsername());
        if (byEmail != null) return byEmail;

        // try name match
        Customer byName = customerDAO.findByName(user.getUsername());
        if (byName != null) return byName;

        
        List<Customer> all = customerDAO.getAllCustomers();
        if (all.size() == 0) {
            throw new Exception("No customer records exist. Ask admin to add a customer record for you.");
        }
        String[] items = new String[all.size()];
        for (int i = 0; i < all.size(); i++) {
            Customer c = all.get(i);
            items[i] = c.getCustomerId() + " - " + c.getName() + " (" + c.getEmail() + ")";
        }
        String selected = (String) JOptionPane.showInputDialog(
                this,
                "Select your customer record (this will be used for bookings):",
                "Choose Customer",
                JOptionPane.QUESTION_MESSAGE,
                null,
                items,
                items[0]
        );
        if (selected == null) {
            throw new Exception("No customer selected.");
        }
        int dash = selected.indexOf(" - ");
        int cid = Integer.parseInt(selected.substring(0, dash));
        return customerDAO.getCustomerById(cid);
    }

    private void loadTrips() {
        tripsModel.setRowCount(0);
        try {
            List<Destination> list = destinationDAO.getAllDestinations();
            for (int i = 0; i < list.size(); i++) {
                Destination d = list.get(i);
                if (d.getAvailableSeats() > 0) {
                    tripsModel.addRow(new Object[]{d.getDestinationId(), d.getCountry(), d.getCity(), d.getPrice(), d.getAvailableSeats()});
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading trips: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMyBookings() {
        bookingsModel.setRowCount(0);
        if (linkedCustomer == null) return;
        try {
            List<Booking> list = bookingDAO.getBookingsByCustomerId(linkedCustomer.getCustomerId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < list.size(); i++) {
                Booking b = list.get(i);
                String dest = b.getDestination() != null ? (b.getDestination().getCountry() + "-" + b.getDestination().getCity()) : "Unknown";
                bookingsModel.addRow(new Object[]{b.getBookingId(), dest, sdf.format(b.getTravelDate()), b.getStatus()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doBook() {
        int r = tripsTable.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Select a trip to book.");
            return;
        }
        try {
            int destId = (int) tripsModel.getValueAt(r, 0);
            Destination dest = destinationDAO.getDestinationById(destId);
            if (dest == null) { JOptionPane.showMessageDialog(this, "Destination not found."); return; }
            if (dest.getAvailableSeats() <= 0) { JOptionPane.showMessageDialog(this, "No seats available."); return; }

            String dateStr = JOptionPane.showInputDialog(this, "Enter travel date (yyyy-MM-dd):", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            if (dateStr == null) return;
            Date travelDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);

            Booking b = new Booking(linkedCustomer, dest, travelDate);
            b.setStatus("Confirmed");
            bookingDAO.addBooking(b);

            JOptionPane.showMessageDialog(this, "Booking successful. ID: " + b.getBookingId());
            loadTrips();
            loadMyBookings();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Booking failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doUpdate() {
        int r = bookingsTable.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Select a booking to update."); return; }
        try {
            int bookingId = (int) bookingsModel.getValueAt(r, 0);
            Booking b = bookingDAO.getBookingById(bookingId);
            if (b == null) { JOptionPane.showMessageDialog(this, "Booking not found."); return; }

            String dateStr = JOptionPane.showInputDialog(this, "Enter new travel date (yyyy-MM-dd):", new SimpleDateFormat("yyyy-MM-dd").format(b.getTravelDate()));
            if (dateStr == null) return;
            Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            b.setTravelDate(newDate);

            String status = JOptionPane.showInputDialog(this, "Set status (Pending/Confirmed/Cancelled):", b.getStatus());
            if (status == null) return;
            b.setStatus(status);

            bookingDAO.updateBooking(b);
            JOptionPane.showMessageDialog(this, "Booking updated.");
            loadMyBookings();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Update failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doCancel() {
        int r = bookingsTable.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Select a booking to cancel."); return; }
        try {
            int bookingId = (int) bookingsModel.getValueAt(r, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Cancel booking?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            bookingDAO.deleteBooking(bookingId); // increments seats inside DAO
            JOptionPane.showMessageDialog(this, "Booking canceled.");
            loadTrips();
            loadMyBookings();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Cancel failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

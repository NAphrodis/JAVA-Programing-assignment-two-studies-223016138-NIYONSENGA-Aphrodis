package com.travelagency.gui;

import com.travelagency.Booking;
import com.travelagency.db.BookingDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class BookingPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public BookingPanel() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new Object[]{"ID","Customer","Destination","Date","Status"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        load();
    }

    private void load() {
        model.setRowCount(0);
        try {
            List<Booking> list = new BookingDAO().getAllBookings();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < list.size(); i++) {
                Booking b = list.get(i);
                String cust = (b.getCustomer() != null ? b.getCustomer().getName() : "Unknown");
                String dest = (b.getDestination() != null ? b.getDestination().getCity() + ", " + b.getDestination().getCountry() : "Unknown");
                model.addRow(new Object[]{b.getBookingId(), cust, dest, sdf.format(b.getTravelDate()), b.getStatus()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load bookings: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

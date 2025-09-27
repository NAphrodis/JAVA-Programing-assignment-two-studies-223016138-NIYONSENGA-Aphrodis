package com.travelagency.gui;

import com.travelagency.Booking;
import com.travelagency.Destination;
import com.travelagency.Payment;
import com.travelagency.db.BookingDAO;
import com.travelagency.db.DestinationDAO;
import com.travelagency.db.PaymentDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ReportPanel extends JPanel {

    private JLabel totalBookingsLabel;
    private JLabel totalRevenueLabel;
    private JLabel totalDestLabel;
    private DefaultTableModel destModel;
    private JTable destTable;

    private BookingDAO bookingDAO = new BookingDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();
    private DestinationDAO destinationDAO = new DestinationDAO();

    public ReportPanel() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new GridLayout(1, 3, 10, 10));
        totalBookingsLabel = new JLabel("Total Bookings: 0");
        totalRevenueLabel = new JLabel("Total Revenue: 0.00");
        totalDestLabel = new JLabel("Total Destinations: 0");
        top.add(totalBookingsLabel);
        top.add(totalRevenueLabel);
        top.add(totalDestLabel);
        add(top, BorderLayout.NORTH);

        destModel = new DefaultTableModel(new Object[]{"Destination", "Bookings"}, 0);
        destTable = new JTable(destModel);
        add(new JScrollPane(destTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton refresh = new JButton("Refresh");
        bottom.add(refresh);
        add(bottom, BorderLayout.SOUTH);

        
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadReports();
            }
        });

        
        loadReports();
    }

    private void loadReports() {
        try {
            // get data from DAOs (they may throw SQLException -> caught here)
            List<Booking> bookings = bookingDAO.getAllBookings();
            List<Payment> payments = paymentDAO.getAllPayments();
            List<Destination> dests = destinationDAO.getAllDestinations();

            // totals
            int totalBookings = (bookings != null ? bookings.size() : 0);
            double totalRevenue = 0.0;
            if (payments != null) {
                for (Payment p : payments) {
                    totalRevenue += p.getAmount();
                }
            }

            totalBookingsLabel.setText("Total Bookings: " + totalBookings);
            totalRevenueLabel.setText(String.format("Total Revenue: %.2f", totalRevenue));
            totalDestLabel.setText("Total Destinations: " + (dests != null ? dests.size() : 0));

           
            Map<String, Integer> map = new LinkedHashMap<String, Integer>();
            if (bookings != null) {
                for (Booking b : bookings) {
                    Destination d = b.getDestination();
                    String key = (d != null ? (d.getCountry() + " - " + d.getCity()) : "Unknown");
                    Integer cur = map.get(key);
                    if (cur == null) cur = Integer.valueOf(0);
                    map.put(key, Integer.valueOf(cur.intValue() + 1));
                }
            }

            destModel.setRowCount(0);
            for (Map.Entry<String, Integer> e : map.entrySet()) {
                destModel.addRow(new Object[]{e.getKey(), e.getValue()});
            }

        } catch (Exception ex) {
            // show friendly error and print stack for debug
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating reports: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

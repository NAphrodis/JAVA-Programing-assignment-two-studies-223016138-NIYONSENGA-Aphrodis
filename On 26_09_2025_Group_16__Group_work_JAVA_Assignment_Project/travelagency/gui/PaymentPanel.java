package com.travelagency.gui;

import com.travelagency.*;
import com.travelagency.db.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class PaymentPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private PaymentDAO paymentDAO = new PaymentDAO();
    private Customer filterCustomer; // if non-null, show only payments for this customer

    // ✅ Customer view (requires User to map to Customer)
    public PaymentPanel(User user) {
        Customer linked = null;
        try {
            if (user != null) {
                CustomerDAO cdao = new CustomerDAO();
                if (user.getCustomerId() != null) {
                    linked = cdao.getCustomerById(user.getCustomerId());
                }
                if (linked == null) linked = cdao.findByEmail(user.getUsername());
                if (linked == null) linked = cdao.findByName(user.getUsername());
            }
        } catch (Exception ignore) {}

        this.filterCustomer = linked;
        init();
    }

    // ✅ Admin view (no filter)
    public PaymentPanel() {
        this.filterCustomer = null;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(
                new Object[]{"PaymentID", "BookingID", "Customer", "Amount", "Method", "Date", "Status"}, 0
        );
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        load();
    }

    private void load() {
        model.setRowCount(0);
        try {
            List<Payment> list = paymentDAO.getAllPayments();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Payment p : list) {
                Booking b = p.getBooking();
                Customer c = (b != null ? b.getCustomer() : null);

                if (filterCustomer != null &&
                        (c == null || c.getCustomerId() != filterCustomer.getCustomerId())) {
                    continue; // skip others if filtering
                }

                model.addRow(new Object[]{
                        p.getPaymentId(),
                        (b != null ? b.getBookingId() : 0),
                        (c != null ? c.getName() : "Unknown"),
                        p.getAmount(),
                        p.getMethod(),
                        (p.getPaymentDate() != null ? sdf.format(p.getPaymentDate()) : ""),
                        p.getStatus()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load payments: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

package com.travelagency.gui;

import com.travelagency.Customer;
import com.travelagency.db.CustomerDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CustomerPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public CustomerPanel() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new Object[]{"ID","Name","Email","Phone","Passport"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        load();
    }

    private void load() {
        model.setRowCount(0);
        try {
            List<Customer> list = new CustomerDAO().getAllCustomers();
            for (int i = 0; i < list.size(); i++) {
                Customer c = list.get(i);
                model.addRow(new Object[]{c.getCustomerId(), c.getName(), c.getEmail(), c.getPhone(), c.getPassportNo()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load customers: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

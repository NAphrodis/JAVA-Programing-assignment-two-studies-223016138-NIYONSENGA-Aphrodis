package com.travelagency.gui;

import com.travelagency.Destination;
import com.travelagency.db.DestinationDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DestinationPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public DestinationPanel() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new Object[]{"ID","Country","City","Price","Seats"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        load();
    }

    private void load() {
        model.setRowCount(0);
        try {
            List<Destination> list = new DestinationDAO().getAllDestinations();
            for (int i = 0; i < list.size(); i++) {
                Destination d = list.get(i);
                model.addRow(new Object[]{d.getDestinationId(), d.getCountry(), d.getCity(), d.getPrice(), d.getAvailableSeats()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to load destinations: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

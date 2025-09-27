package com.travelagency;

import com.travelagency.gui.*;
import com.travelagency.db.CustomerDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class TravelAgencyApp extends JFrame {
    private User loggedUser;
    private JTabbedPane tabbedPane;

    public TravelAgencyApp(User user) {
        this.loggedUser = user;
        setTitle("Travel Agency - Logged in as: " + user.getUsername() + " (" + user.getRole() + ")");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        buildUI();
    }

    private void buildUI() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logout = new JMenuItem("Logout");
        JMenuItem exit = new JMenuItem("Exit");
        fileMenu.add(logout);
        fileMenu.add(exit);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginForm lf = new LoginForm();
                lf.setVisible(true);
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        if ("admin".equalsIgnoreCase(loggedUser.getRole())) {
            
            tabbedPane.addTab("Customers", new CustomerPanel());
            tabbedPane.addTab("Destinations", new DestinationPanel());
            tabbedPane.addTab("Bookings", new BookingPanel());
            tabbedPane.addTab("Payments", new PaymentPanel()); // admin sees all payments
            tabbedPane.addTab("User Mgmt", new UserManagementPanel());
            tabbedPane.addTab("Reports", new ReportPanel());
            tabbedPane.addTab("Settings", new SystemSettingsPanel());

        } else {
            
            CustomerBookingPanel cbp = new CustomerBookingPanel(loggedUser);
            tabbedPane.addTab("My Bookings", cbp);
            
            PaymentPanel pp = new PaymentPanel(loggedUser);
            tabbedPane.addTab("My Payments", pp);
        }
    }
}

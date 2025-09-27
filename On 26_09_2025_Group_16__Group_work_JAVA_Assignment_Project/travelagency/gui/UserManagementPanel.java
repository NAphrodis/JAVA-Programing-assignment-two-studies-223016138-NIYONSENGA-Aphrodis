package com.travelagency.gui;

import com.travelagency.User;
import com.travelagency.db.UserDAO;
import com.travelagency.db.CustomerDAO;
import com.travelagency.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class UserManagementPanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private UserDAO userDAO = new UserDAO();
    private CustomerDAO customerDAO = new CustomerDAO();

    public UserManagementPanel() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new Object[]{"UserID", "Username", "Role", "CustomerID"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton addBtn = new JButton("Add User");
        JButton editBtn = new JButton("Edit User");
        JButton deleteBtn = new JButton("Delete User");
        JButton refreshBtn = new JButton("Refresh");
        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(deleteBtn);
        buttons.add(refreshBtn);
        add(buttons, BorderLayout.SOUTH);

        loadUsers();

        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showUserDialog(null);
            }
        });

        editBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) {
                    JOptionPane.showMessageDialog(UserManagementPanel.this, "Select a user to edit.");
                    return;
                }
                int userId = (int) model.getValueAt(r, 0);
                try {
                    User u = userDAO.getUserById(userId);
                    showUserDialog(u);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(UserManagementPanel.this, "Error loading user: " + ex.getMessage());
                }
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int r = table.getSelectedRow();
                if (r < 0) {
                    JOptionPane.showMessageDialog(UserManagementPanel.this, "Select a user to delete.");
                    return;
                }
                final int userId = (int) model.getValueAt(r, 0);
                int conf = JOptionPane.showConfirmDialog(UserManagementPanel.this, "Delete user ID " + userId + " ?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    try {
                        userDAO.deleteUser(userId);
                        loadUsers();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(UserManagementPanel.this, "Delete failed: " + ex.getMessage());
                    }
                }
            }
        });

        refreshBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });
    }

    private void loadUsers() {
        model.setRowCount(0);
        try {
            List<User> users = userDAO.getAllUsers();
            for (User u : users) {
                model.addRow(new Object[]{u.getUserId(), u.getUsername(), u.getRole(), u.getCustomerId()});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load users: " + ex.getMessage());
        }
    }

    // If 'user' is null -> create new; else edit existing
    private void showUserDialog(final User user) {
        final JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this),
                (user == null ? "Add User" : "Edit User"), Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(420, 260);
        dialog.setLocationRelativeTo(this);

        JPanel p = new JPanel(new GridLayout(5, 2, 8, 8));
        p.add(new JLabel("Username:"));
        final JTextField usernameField = new JTextField(user != null ? user.getUsername() : "");
        p.add(usernameField);

        p.add(new JLabel("Password:"));
        final JPasswordField passwordField = new JPasswordField(user != null ? user.getPassword() : "");
        p.add(passwordField);

        p.add(new JLabel("Role:"));
        final JComboBox<String> roleBox = new JComboBox<String>(new String[]{"admin", "customer"});
        if (user != null) roleBox.setSelectedItem(user.getRole());
        p.add(roleBox);

        // Build customer choices (fetch before creating customerBox)
        String[] items;
        int sel = 0;
        try {
            List<Customer> customers = customerDAO.getAllCustomers();
            items = new String[customers.size() + 1];
            items[0] = "None";
            for (int i = 0; i < customers.size(); i++) {
                Customer c = customers.get(i);
                items[i + 1] = c.getCustomerId() + " - " + c.getName();
                if (user != null && user.getCustomerId() != null && user.getCustomerId().intValue() == c.getCustomerId()) {
                    sel = i + 1;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load customers for linking: " + ex.getMessage());
            items = new String[]{"None"};
        }

        p.add(new JLabel("Link Customer:"));
        final JComboBox<String> customerBox = new JComboBox<String>(items);
        customerBox.setSelectedIndex(sel);
        p.add(customerBox);

        // Buttons
        final JButton saveBtn = new JButton("Save");
        final JButton cancelBtn = new JButton("Cancel");
        JPanel btns = new JPanel();
        btns.add(saveBtn);
        btns.add(cancelBtn);

        dialog.getContentPane().add(p, BorderLayout.CENTER);
        dialog.getContentPane().add(btns, BorderLayout.SOUTH);

        // Save action
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String uname = usernameField.getText().trim();
                String pwd = new String(passwordField.getPassword());
                String role = (String) roleBox.getSelectedItem();
                Integer custId = null;
                int idx = customerBox.getSelectedIndex();
                if (idx > 0) {
                    String sel = (String) customerBox.getSelectedItem();
                    int dash = sel.indexOf(" - ");
                    try {
                        custId = Integer.valueOf(Integer.parseInt(sel.substring(0, dash)));
                    } catch (Exception ignored) {
                    }
                }
                if (uname.length() == 0 || pwd.length() == 0) {
                    JOptionPane.showMessageDialog(dialog, "Username and password required.");
                    return;
                }
                try {
                    if (user == null) {
                        User newUser = new User(0, uname, pwd, role, custId);
                        int newId = userDAO.addUser(newUser);
                        if (newId > 0) {
                            JOptionPane.showMessageDialog(dialog, "User created (id=" + newId + ")");
                            dialog.dispose();
                            loadUsers();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Failed to create user.");
                        }
                    } else {
                        User up = new User(user.getUserId(), uname, pwd, role, custId);
                        userDAO.updateUser(up);
                        JOptionPane.showMessageDialog(dialog, "User updated.");
                        dialog.dispose();
                        loadUsers();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, "DB error: " + ex.getMessage());
                }
            }
        });

        // Cancel
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }
}

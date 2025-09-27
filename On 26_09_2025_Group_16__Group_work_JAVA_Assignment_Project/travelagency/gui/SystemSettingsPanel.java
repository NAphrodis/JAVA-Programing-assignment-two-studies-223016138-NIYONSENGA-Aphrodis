package com.travelagency.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

public class SystemSettingsPanel extends JPanel {

    private JCheckBox emailNotifications;
    private JTextField currencyField;
    private JTextField taxField;
    private File configFile = new File("config.properties");

    public SystemSettingsPanel() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(3,2,8,8));
        form.add(new JLabel("Email Notifications:"));
        emailNotifications = new JCheckBox("Enable");
        form.add(emailNotifications);

        form.add(new JLabel("Default Currency:"));
        currencyField = new JTextField();
        form.add(currencyField);

        form.add(new JLabel("Default Tax Rate (%):"));
        taxField = new JTextField();
        form.add(taxField);

        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton saveBtn = new JButton("Save");
        JButton reloadBtn = new JButton("Reload");
        JButton resetBtn = new JButton("Reset Defaults");
        buttons.add(saveBtn);
        buttons.add(reloadBtn);
        buttons.add(resetBtn);
        add(buttons, BorderLayout.SOUTH);

        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { save(); }
        });
        reloadBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { load(); }
        });
        resetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                emailNotifications.setSelected(true);
                currencyField.setText("USD");
                taxField.setText("18");
            }
        });

        load();
    }

    private void load() {
        Properties p = new Properties();
        if (configFile.exists()) {
            try (FileInputStream in = new FileInputStream(configFile)) {
                p.load(in);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to load config: " + ex.getMessage());
            }
        }
        emailNotifications.setSelected(Boolean.parseBoolean(p.getProperty("emailNotifications", "true")));
        currencyField.setText(p.getProperty("currency", "USD"));
        taxField.setText(p.getProperty("taxRate", "18"));
    }

    private void save() {
        Properties p = new Properties();
        p.setProperty("emailNotifications", Boolean.toString(emailNotifications.isSelected()));
        p.setProperty("currency", currencyField.getText().trim());
        p.setProperty("taxRate", taxField.getText().trim());
        try (FileOutputStream out = new FileOutputStream(configFile)) {
            p.store(out, "Travel Agency Settings");
            JOptionPane.showMessageDialog(this, "Settings saved.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save settings: " + ex.getMessage());
        }
    }
}

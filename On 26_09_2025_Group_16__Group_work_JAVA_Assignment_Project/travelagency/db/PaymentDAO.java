package com.travelagency.db;

import com.travelagency.Payment;
import com.travelagency.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public int addPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (bookingId, amount, method, paymentDate, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, payment.getBooking().getBookingId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setString(3, payment.getMethod());
            if (payment.getPaymentDate() != null) {
                stmt.setDate(4, new java.sql.Date(payment.getPaymentDate().getTime()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setString(5, payment.getStatus());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    payment.setPaymentId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public Payment getPaymentById(int id) throws SQLException {
        String sql = "SELECT * FROM payments WHERE paymentId = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Booking b = new BookingDAO().getBookingById(rs.getInt("bookingId"));
                    java.util.Date pd = null;
                    java.sql.Date sqlDate = rs.getDate("paymentDate");
                    if (sqlDate != null) pd = new java.util.Date(sqlDate.getTime());
                    return new Payment(
                            rs.getInt("paymentId"),
                            b,
                            rs.getDouble("amount"),
                            rs.getString("method"),
                            pd,
                            rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    public List<Payment> getAllPayments() throws SQLException {
        List<Payment> list = new ArrayList<Payment>();
        String sql = "SELECT * FROM payments";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Booking b = new BookingDAO().getBookingById(rs.getInt("bookingId"));
                java.util.Date pd = null;
                java.sql.Date sqlDate = rs.getDate("paymentDate");
                if (sqlDate != null) pd = new java.util.Date(sqlDate.getTime());
                list.add(new Payment(
                        rs.getInt("paymentId"),
                        b,
                        rs.getDouble("amount"),
                        rs.getString("method"),
                        pd,
                        rs.getString("status")
                ));
            }
        }
        return list;
    }

    public void updatePayment(Payment p) throws SQLException {
        String sql = "UPDATE payments SET bookingId=?, amount=?, method=?, paymentDate=?, status=? WHERE paymentId=?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getBooking().getBookingId());
            stmt.setDouble(2, p.getAmount());
            stmt.setString(3, p.getMethod());
            if (p.getPaymentDate() != null) {
                stmt.setDate(4, new java.sql.Date(p.getPaymentDate().getTime()));
            } else {
                stmt.setNull(4, Types.DATE);
            }
            stmt.setString(5, p.getStatus());
            stmt.setInt(6, p.getPaymentId());
            stmt.executeUpdate();
        }
    }

    public void deletePayment(int id) throws SQLException {
        String sql = "DELETE FROM payments WHERE paymentId = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}

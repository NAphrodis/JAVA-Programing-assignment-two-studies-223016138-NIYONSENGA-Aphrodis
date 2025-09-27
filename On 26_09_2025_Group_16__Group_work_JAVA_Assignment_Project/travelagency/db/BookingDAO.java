package com.travelagency.db;

import com.travelagency.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class BookingDAO {

    
    public int addBooking(Booking booking) throws SQLException {
        String updateSeatsSql = "UPDATE destinations SET availableSeats = availableSeats - 1 WHERE destinationId = ? AND availableSeats > 0";
        String insertSql = "INSERT INTO bookings (customerId, destinationId, travelDate, status) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement updStmt = null;
        PreparedStatement insStmt = null;
        ResultSet keys = null;
        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false);


            updStmt = conn.prepareStatement(updateSeatsSql);
            updStmt.setInt(1, booking.getDestination().getDestinationId());
            int changed = updStmt.executeUpdate();
            if (changed == 0) {
                conn.rollback();
                throw new SQLException("No available seats for destination " + booking.getDestination().getDestinationId());
            }

            
            insStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            insStmt.setInt(1, booking.getCustomer().getCustomerId());
            insStmt.setInt(2, booking.getDestination().getDestinationId());
            insStmt.setDate(3, new java.sql.Date(booking.getTravelDate().getTime()));
            insStmt.setString(4, booking.getStatus());
            insStmt.executeUpdate();

            keys = insStmt.getGeneratedKeys();
            int bookingId = -1;
            if (keys.next()) bookingId = keys.getInt(1);
            else {
                conn.rollback();
                throw new SQLException("Failed to create booking (no ID returned).");
            }

            conn.commit();
            booking.setBookingId(bookingId);
            return bookingId;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignore) {}
            }
            throw e;
        } finally {
            try { if (keys != null) keys.close(); } catch (Exception ignore) {}
            try { if (insStmt != null) insStmt.close(); } catch (Exception ignore) {}
            try { if (updStmt != null) updStmt.close(); } catch (Exception ignore) {}
            try { if (conn != null) conn.setAutoCommit(true); conn.close(); } catch (Exception ignore) {}
        }
    }

    public Booking getBookingById(int bookingId) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE bookingId = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int custId = rs.getInt("customerId");
                    int destId = rs.getInt("destinationId");
                    Date travelDate = rs.getDate("travelDate");
                    String status = rs.getString("status");

                    Customer customer = new CustomerDAO().getCustomerById(custId);
                    Destination dest = new DestinationDAO().getDestinationById(destId);

                    return new Booking(bookingId, customer, dest, travelDate, status);
                }
            }
        }
        return null;
    }

    public List<Booking> getBookingsByCustomerId(int customerId) throws SQLException {
        List<Booking> list = new ArrayList<Booking>();
        String sql = "SELECT * FROM bookings WHERE customerId = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int bookingId = rs.getInt("bookingId");
                    int destId = rs.getInt("destinationId");
                    Date travelDate = rs.getDate("travelDate");
                    String status = rs.getString("status");

                    Customer customer = new CustomerDAO().getCustomerById(customerId);
                    Destination dest = new DestinationDAO().getDestinationById(destId);

                    list.add(new Booking(bookingId, customer, dest, travelDate, status));
                }
            }
        }
        return list;
    }

    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> list = new ArrayList<Booking>();
        String sql = "SELECT * FROM bookings";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int bookingId = rs.getInt("bookingId");
                int custId = rs.getInt("customerId");
                int destId = rs.getInt("destinationId");
                Date travelDate = rs.getDate("travelDate");
                String status = rs.getString("status");

                Customer customer = new CustomerDAO().getCustomerById(custId);
                Destination dest = new DestinationDAO().getDestinationById(destId);

                list.add(new Booking(bookingId, customer, dest, travelDate, status));
            }
        }
        return list;
    }

    public void updateBooking(Booking booking) throws SQLException {
        String sql = "UPDATE bookings SET customerId = ?, destinationId = ?, travelDate = ?, status = ? WHERE bookingId = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, booking.getCustomer().getCustomerId());
            stmt.setInt(2, booking.getDestination().getDestinationId());
            stmt.setDate(3, new java.sql.Date(booking.getTravelDate().getTime()));
            stmt.setString(4, booking.getStatus());
            stmt.setInt(5, booking.getBookingId());
            stmt.executeUpdate();
        }
    }

    
    public void deleteBooking(int bookingId) throws SQLException {
        Connection conn = null;
        PreparedStatement updStmt = null;
        PreparedStatement delStmt = null;
        try {
            conn = DB.getConnection();
            conn.setAutoCommit(false);

            Booking b = getBookingById(bookingId);
            if (b == null) {
                conn.rollback();
                throw new SQLException("Booking not found: " + bookingId);
            }

            String incSeatsSql = "UPDATE destinations SET availableSeats = availableSeats + 1 WHERE destinationId = ?";
            updStmt = conn.prepareStatement(incSeatsSql);
            updStmt.setInt(1, b.getDestination().getDestinationId());
            updStmt.executeUpdate();

            String deleteSql = "DELETE FROM bookings WHERE bookingId = ?";
            delStmt = conn.prepareStatement(deleteSql);
            delStmt.setInt(1, bookingId);
            delStmt.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ignore) {}
            throw e;
        } finally {
            try { if (updStmt != null) updStmt.close(); } catch (Exception ignore) {}
            try { if (delStmt != null) delStmt.close(); } catch (Exception ignore) {}
            try { if (conn != null) conn.setAutoCommit(true); conn.close(); } catch (Exception ignore) {}
        }
    }
}

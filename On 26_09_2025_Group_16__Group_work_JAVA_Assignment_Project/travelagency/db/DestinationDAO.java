package com.travelagency.db;

import com.travelagency.Destination;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DestinationDAO {

    public Destination getDestinationById(int id) throws SQLException {
        String sql = "SELECT * FROM destinations WHERE destinationId = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Destination(
                            rs.getInt("destinationId"),
                            rs.getString("country"),
                            rs.getString("city"),
                            rs.getDouble("price"),
                            rs.getInt("availableSeats")
                    );
                }
            }
        }
        return null;
    }

    public List<Destination> getAllDestinations() throws SQLException {
        List<Destination> list = new ArrayList<Destination>();
        String sql = "SELECT * FROM destinations";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Destination(
                        rs.getInt("destinationId"),
                        rs.getString("country"),
                        rs.getString("city"),
                        rs.getDouble("price"),
                        rs.getInt("availableSeats")
                ));
            }
        }
        return list;
    }

    public void updateDestination(Destination dest) throws SQLException {
        String sql = "UPDATE destinations SET country=?, city=?, price=?, availableSeats=? WHERE destinationId=?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dest.getCountry());
            stmt.setString(2, dest.getCity());
            stmt.setDouble(3, dest.getPrice());
            stmt.setInt(4, dest.getAvailableSeats());
            stmt.setInt(5, dest.getDestinationId());
            stmt.executeUpdate();
        }
    }
}

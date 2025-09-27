package com.travelagency.db;

import com.travelagency.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public User authenticate(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer custId = null;
                    try {
                        int c = rs.getInt("customerId");
                        if (!rs.wasNull()) custId = Integer.valueOf(c);
                    } catch (SQLException ignore) {}
                    return new User(
                            rs.getInt("userId"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            custId
                    );
                }
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        String sql = "SELECT * FROM users ORDER BY userId";
        List<User> list = new ArrayList<User>();
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Integer custId = null;
                try {
                    int c = rs.getInt("customerId");
                    if (!rs.wasNull()) custId = Integer.valueOf(c);
                } catch (SQLException ignore) {}
                list.add(new User(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        custId
                ));
            }
        }
        return list;
    }

    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE userId = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Integer custId = null;
                    try {
                        int c = rs.getInt("customerId");
                        if (!rs.wasNull()) custId = Integer.valueOf(c);
                    } catch (SQLException ignore) {}
                    return new User(
                            rs.getInt("userId"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            custId
                    );
                }
            }
        }
        return null;
    }

    
    public int addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, role, customerId) VALUES (?, ?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            if (user.getCustomerId() != null) stmt.setInt(4, user.getCustomerId()); else stmt.setNull(4, Types.INTEGER);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, role = ?, customerId = ? WHERE userId = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            if (user.getCustomerId() != null) stmt.setInt(4, user.getCustomerId()); else stmt.setNull(4, Types.INTEGER);
            stmt.setInt(5, user.getUserId());
            stmt.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE userId = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
}

package com.travelagency;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    private Integer customerId; 

    public User(int userId, String username, String password, String role, Integer customerId) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.customerId = customerId;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public Integer getCustomerId() { return customerId; }
}

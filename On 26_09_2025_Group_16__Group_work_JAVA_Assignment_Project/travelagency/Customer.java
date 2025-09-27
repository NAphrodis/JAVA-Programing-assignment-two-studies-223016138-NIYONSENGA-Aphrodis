package com.travelagency;

public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String phone;
    private String passportNo;

    public Customer(int customerId, String name, String email, String phone, String passportNo) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.passportNo = passportNo;
    }

    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassportNo() { return passportNo; }
}

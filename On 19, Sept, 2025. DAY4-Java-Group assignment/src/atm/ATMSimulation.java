package atm;

import java.util.Scanner;

class ATM {
    private double balance;
    private final int pin = 1234; 

    public ATM(double initialBalance) {
        this.balance = initialBalance;
    }

    public boolean authenticate(int enteredPin) {
        return enteredPin == pin;
    }

    public void checkBalance() {
        System.out.println("Your current balance is: " + balance);
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount!");
        }
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        } else {
            System.out.println(" Insufficient Funds!");
        }
    }

    public double getBalance() {
        return balance;
    }
}

public class ATMSimulation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ATM atm = new ATM(0); 

        System.out.print("Enter PIN: ");
        int enteredPin = sc.nextInt();

        if (atm.authenticate(enteredPin)) {
            System.out.println(" PIN Verified. Welcome!");

         
            atm.deposit(10000);   
            atm.withdraw(3000);  

            atm.checkBalance();  
        } else {
            System.out.println(" Incorrect PIN. Access Denied.");
        }

        sc.close();
    }
}

package BankAcount;

import java.util.Scanner;

class BankAccount {
    private String accountNumber;
    private String holderName;
    private double balance;


    public BankAccount(String accountNumber, String holderName, double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println(amount + " deposited successfully.");
        } else {
            System.out.println("Invalid deposit amount.");
        }
        checkLowBalance();
    }

    public void withdraw(double amount) {
        if (amount > 0) {
            if (amount <= balance) {
                balance -= amount;
                System.out.println(amount + " withdrawn successfully.");
            } else {
                System.out.println("Insufficient Funds!");
            }
        } else {
            System.out.println("Invalid withdrawal amount.");
        }
        checkLowBalance();
    }


    public void displayBalance() {
        System.out.println("\n--- Account Details ---");
        System.out.println("Account Holder: " + holderName);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Balance: " + balance);
        checkLowBalance();
        System.out.println("---------------------------");
    }


    private void checkLowBalance() {
        if (balance < 1000) {
            System.out.println("Warning: Low Balance!");
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}

public class BankAccountMenu {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Account Number for Account 1: ");
        String accNo1 = sc.nextLine();
        System.out.print("Enter Holder Name for Account 1: ");
        String holder1 = sc.nextLine();
        System.out.print("Enter Initial Balance for Account 1: ");
        double bal1 = sc.nextDouble();
        sc.nextLine();
        BankAccount acc1 = new BankAccount(accNo1, holder1, bal1);

        System.out.print("\nEnter Account Number for Account 2: ");
        String accNo2 = sc.nextLine();
        System.out.print("Enter Holder Name for Account 2: ");
        String holder2 = sc.nextLine();
        System.out.print("Enter Initial Balance for Account 2: ");
        double bal2 = sc.nextDouble();
        sc.nextLine();
        BankAccount acc2 = new BankAccount(accNo2, holder2, bal2);

        int choice;
        do {
            System.out.println("\n=== Bank Menu ===");
            System.out.println("1. Deposit Money");
            System.out.println("2. Withdraw Money");
            System.out.println("3. Check Balance");
            System.out.println("4. Display All Accounts");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1: 
                    System.out.print("Enter Account Number: ");
                    sc.nextLine(); 
                    String depAcc = sc.nextLine();
                    System.out.print("Enter Amount to Deposit: ");
                    double depAmt = sc.nextDouble();
                    if (depAcc.equals(acc1.getAccountNumber())) {
                        acc1.deposit(depAmt);
                    } else if (depAcc.equals(acc2.getAccountNumber())) {
                        acc2.deposit(depAmt);
                    } else {
                        System.out.println("Account not found!");
                    }
                    break;

                case 2: 
                    System.out.print("Enter Account Number: ");
                    sc.nextLine();
                    String wAcc = sc.nextLine();
                    System.out.print("Enter Amount to Withdraw: ");
                    double wAmt = sc.nextDouble();
                    if (wAcc.equals(acc1.getAccountNumber())) {
                        acc1.withdraw(wAmt);
                    } else if (wAcc.equals(acc2.getAccountNumber())) {
                        acc2.withdraw(wAmt);
                    } else {
                        System.out.println("Account not found!");
                    }
                    break;

                case 3: 
                    System.out.print("Enter Account Number: ");
                    sc.nextLine();
                    String bAcc = sc.nextLine();
                    if (bAcc.equals(acc1.getAccountNumber())) {
                        acc1.displayBalance();
                    } else if (bAcc.equals(acc2.getAccountNumber())) {
                        acc2.displayBalance();
                    } else {
                        System.out.println("Account not found!");
                    }
                    break;

                case 4: 
                    acc1.displayBalance();
                    acc2.displayBalance();
                    break;

                case 5:
                    System.out.println("Exiting... Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 5);

        sc.close();
    }
}

package EmployeeSalarySystem;

import java.util.Scanner;

abstract class Employee {
 protected String name;

 public Employee(String name) {
     this.name = name;
 }

 public abstract double calculateSalary();

 public String getName() {
     return name;
 }
}

class FullTimeEmployee extends Employee {
 private double baseSalary;

 public FullTimeEmployee(String name, double baseSalary) {
     super(name);
     this.baseSalary = baseSalary;
 }

 @Override
 public double calculateSalary() {
     double allowances = baseSalary * 0.2; // 20% of base
     return baseSalary + allowances;
 }
}


class PartTimeEmployee extends Employee {
 private double hoursWorked;
 private double hourlyRate;

 public PartTimeEmployee(String name, double hoursWorked, double hourlyRate) {
     super(name);
     this.hoursWorked = hoursWorked;
     this.hourlyRate = hourlyRate;
 }

 @Override
 public double calculateSalary() {
     return hoursWorked * hourlyRate;
 }
}


public class EmployeeSalarySystem {
 public static void main(String[] args) {
     Scanner sc = new Scanner(System.in);

    
     System.out.print("Enter number of full-time employees: ");
     int ftCount = sc.nextInt();
     sc.nextLine(); 

     FullTimeEmployee[] fullTime = new FullTimeEmployee[ftCount];
     for (int i = 0; i < ftCount; i++) {
         System.out.print("\nEnter name of full-time employee " + (i+1) + ": ");
         String name = sc.nextLine();
         System.out.print("Enter base salary: ");
         double base = sc.nextDouble();
         sc.nextLine();
         fullTime[i] = new FullTimeEmployee(name, base);
     }

     System.out.print("\nEnter number of part-time employees: ");
     int ptCount = sc.nextInt();
     sc.nextLine();

     PartTimeEmployee[] partTime = new PartTimeEmployee[ptCount];
     for (int i = 0; i < ptCount; i++) {
         System.out.print("\nEnter name of part-time employee " + (i+1) + ": ");
         String name = sc.nextLine();
         System.out.print("Enter hours worked: ");
         double hours = sc.nextDouble();
         System.out.print("Enter hourly rate: ");
         double rate = sc.nextDouble();
         sc.nextLine();
         partTime[i] = new PartTimeEmployee(name, hours, rate);
     }

     double totalExpenditure = 0;

     System.out.println("\n--- Salary Details ---");

     for (FullTimeEmployee e : fullTime) {
         double salary = e.calculateSalary();
         System.out.println(e.getName() + " - Salary: " + salary);
         if (salary > 60000) {
             System.out.println("High Earner!");
         }
         totalExpenditure += salary;
     }


     for (PartTimeEmployee e : partTime) {
         double salary = e.calculateSalary();
         System.out.println(e.getName() + " - Salary: " + salary);
         if (salary > 60000) {
             System.out.println("High Earner!");
         }
         totalExpenditure += salary;
     }

     System.out.println("\nTotal Salary Expenditure: " + totalExpenditure);

     sc.close();
 }
}

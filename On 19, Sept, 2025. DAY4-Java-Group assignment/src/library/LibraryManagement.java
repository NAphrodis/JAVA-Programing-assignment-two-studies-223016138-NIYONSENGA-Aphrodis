package library;

import java.util.Scanner;

class Book {
    String title;
    String author;
    boolean available;

    Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.available = true;
    }

  
    public void borrowBook() {
        if (available) {
            available = false;
            System.out.println(" succesful, You borrowed: " + title +"book");
        } else {
            System.out.println(" Sorry! '" + title + "' is already borrowed.");
        }
    }

 
    public void returnBook() {
        if (!available) {
            available = true;
            System.out.println("  You returned: " + title);
        } else {
            System.out.println(" This book was not borrowed.");
        }
    }

 
    public void displayBook(int index) {
        if (available) {
            System.out.println(index + ". " + title + " | Author: " + author + " | Status: Available");
        }
    }
}

public class LibraryManagement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

   
        Book[] books = {
            new Book("Algebla Mathematics", "Niyonsenga Aphrodis"),
            new Book("Database system", "Niyikiza Emmanuel"),
            new Book("English story", "Ishimwe Valensi"),
            new Book("Java Programming", "Tuyubahe Nicodemu")
        };

        boolean running = true;

        while (running) {
            System.out.println("\n=====  LIBRARY MENU =====");
            System.out.println("1. View Available Books");
            System.out.println("2. Borrow a Book");
            System.out.println("3. Return a Book");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n--- Available Books ---");
                    for (int i = 0; i < books.length; i++) {
                        books[i].displayBook(i + 1);
                    }
                    break;

                case 2:
                    System.out.println("\nEnter the number of the book to borrow:");
                    for (int i = 0; i < books.length; i++) {
                        books[i].displayBook(i + 1);
                    }
                    int borrowChoice = sc.nextInt();
                    sc.nextLine();
                    if (borrowChoice >= 1 && borrowChoice <= books.length) {
                        books[borrowChoice - 1].borrowBook();
                    } else {
                        System.out.println(" Invalid selection!");
                    }
                    break;

                case 3:
                    System.out.println("\nEnter the number of the book to return:");
                    for (int i = 0; i < books.length; i++) {
                        System.out.println((i + 1) + ". " + books[i].title);
                    }
                    int returnChoice = sc.nextInt();
                    if (returnChoice >= 1 && returnChoice <= books.length) {
                        books[returnChoice - 1].returnBook();
                    } else {
                        System.out.println(" Invalid selection!");
                    }
                    break;

                case 4:
                    System.out.println(" Thank you for using the library system!");
                    running = false;
                    break;

                default:
                    System.out.println(" Invalid option. Please try again.");
            }
        }

        sc.close();
    }
}

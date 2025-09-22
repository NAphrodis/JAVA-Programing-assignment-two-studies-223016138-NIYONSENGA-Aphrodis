package StudentMarksCalculation;

import java.util.Scanner;

class Student {
    private String id;   
    private String name;
    private int[] marks = new int[3];

    public Student(String id, String name, int[] marks) {
        this.id = id;
        this.name = name;
        this.marks = marks;
    }


    public double getAverage() {
        int sum = 0;
        for (int mark : marks) {
            sum += mark;
        }
        return sum / 3.0;
    }

    
    public String getGrade() {
        double avg = getAverage();
        if (avg >= 80) return "A";
        else if (avg >= 60) return "B";
        else if (avg >= 40) return "C";
        else return "Fail";
    }

    public void displayStudent() {
        System.out.println("\n--- Student Details ---");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.print("Marks: ");
        for (int mark : marks) {
            System.out.print(mark + " ");
        }
        System.out.println("\nAverage: " + getAverage());
        System.out.println("Grade: " + getGrade());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

public class StudentMarksTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

     
        Student[] students = new Student[3];

        for (int i = 0; i < 3; i++) {
            System.out.print("\nEnter ID for Student " + (i+1) + ": ");
            String id = sc.nextLine(); 
            System.out.print("Enter Name for Student " + (i+1) + ": ");
            String name = sc.nextLine();

            int[] marks = new int[3];
            for (int j = 0; j < 3; j++) {
                System.out.print("Enter mark for subject " + (j+1) + ": ");
                marks[j] = sc.nextInt();
            }
            sc.nextLine();

            students[i] = new Student(id, name, marks);
        }

        for (Student s : students) {
            s.displayStudent();
        }

        Student topStudent = students[0];
        for (int i = 1; i < students.length; i++) {
            if (students[i].getAverage() > topStudent.getAverage()) {
                topStudent = students[i];
            }
        }

        System.out.println("\nHighest Average Mark: " + topStudent.getAverage() +
                           " (Student: " + topStudent.getName() + ", ID: " + topStudent.getId() + ")");

        sc.close();
    }
}

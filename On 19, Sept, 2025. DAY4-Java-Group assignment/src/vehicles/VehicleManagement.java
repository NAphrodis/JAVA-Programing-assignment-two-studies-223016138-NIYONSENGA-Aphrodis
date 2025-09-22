package vehicles;

class Vehicle {
    String registrationNumber;
    int speed;

    Vehicle(String registrationNumber, int speed) {
        this.registrationNumber = registrationNumber;
        this.speed = speed;
    }

    public void displayDetails() {
        System.out.println("Vehicle Reg No: " + registrationNumber + "  ||   Speed: " + speed + " km/h");
    }
}

class Car extends Vehicle {
    double fuelEfficiency; 
    Car(String registrationNumber, int speed, double fuelEfficiency) {
        super(registrationNumber, speed);
        this.fuelEfficiency = fuelEfficiency;
    }

    @Override
    public void displayDetails() {
        System.out.println(" Car - Reg No: " + registrationNumber + 
                           "   ||  Speed: " + speed + " km/h  ||   Fuel Efficiency: " + fuelEfficiency + " km/l");
    }
}

class Bike extends Vehicle {
    double fuelEfficiency; 

    Bike(String registrationNumber, int speed, double fuelEfficiency) {
        super(registrationNumber, speed);
        this.fuelEfficiency = fuelEfficiency;
    }

    @Override
    public void displayDetails() {
        System.out.println(" Bike - Reg No: " + registrationNumber + 
                           "  ||   Speed: " + speed + " km/h  ||   Fuel Efficiency: " + fuelEfficiency + " km/l");
    }
}

class Truck extends Vehicle {
    double loadCapacity; 
    Truck(String registrationNumber, int speed, double loadCapacity) {
        super(registrationNumber, speed);
        this.loadCapacity = loadCapacity;
    }

    @Override
    public void displayDetails() {
        System.out.println(" Truck - Reg No: " + registrationNumber + 
                           "  ||   Speed: " + speed + " km/h  ||   Load Capacity: " + loadCapacity + " tons");
    }
}

public class VehicleManagement {
    public static void main(String[] args) {
      
        Vehicle[] vehicles = new Vehicle[4];
        vehicles[0] = new Car("CAR123", 120, 18.5);
        vehicles[1] = new Bike("BIKE456", 80, 40.0);
        vehicles[2] = new Truck("TRUCK789", 70, 15);
        vehicles[3] = new Car("CAR999", 140, 16.2);


        System.out.println("=====  VEHICLE DETAILS =====");
        System.out.println("-------------------------------------------------------------------------");
        for (Vehicle v : vehicles) {
            v.displayDetails(); 
        }
    }
}

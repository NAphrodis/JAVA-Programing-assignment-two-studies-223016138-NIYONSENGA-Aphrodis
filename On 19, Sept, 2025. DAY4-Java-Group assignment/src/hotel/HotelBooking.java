package hotel;

class Room {
    private int roomNumber;
    private String type;
    private double pricePerNight;


    public Room(int roomNumber, String type, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerNight = pricePerNight;
    }

   
    public double calculateCost(int nights) {
        double total = pricePerNight * nights;
        if (nights > 5) {  
            total -= total * 0.15;
            System.out.println("Discount applied of 15%");
        }
        return total;
    }

    public String getType() {
        return type;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public int getRoomNumber() {
        return roomNumber;
    }
}

public class HotelBooking {
    public static void main(String[] args) {
     
        Room standard = new Room(101, "Standard", 5000);
        Room deluxe = new Room(102, "Deluxe", 8000);
        Room suite = new Room(103, "Suite", 12000);

        int nightsStayed = 6;

        System.out.println("Room Type: " + deluxe.getType());
        System.out.println("Price per Night: " + deluxe.getPricePerNight());
        System.out.println("Nights Stayed: " + nightsStayed);

        double totalBill = deluxe.calculateCost(nightsStayed);

        System.out.println("Total Bill: " + totalBill);
    }
}

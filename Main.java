
import java.util.ArrayList;
import java.util.Scanner;

// Abstraction
abstract class Passenger {
    private String name;
    private int age;

    public Passenger(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public abstract double getDiscount();
    public abstract String getType();
}

// Inheritance
class RegularPassenger extends Passenger {

    public RegularPassenger(String name, int age) {
        super(name, age);
    }

    @Override
    public double getDiscount() {
        return 0.0;
    }

    @Override
    public String getType() {
        return "Regular";
    }
}

// Inheritance
class StudentPassenger extends Passenger {

    public StudentPassenger(String name, int age) {
        super(name, age);
    }

    @Override
    public double getDiscount() {
        return 0.10;
    }

    @Override
    public String getType() {
        return "Student";
    }
}

// Flight class
class Flight {
    private String code;
    private String airline;
    private String source;
    private String destination;
    private int seats;
    private double fare;

    public Flight(String code, String airline, String source,
                  String destination, int seats, double fare) {
        this.code = code;
        this.airline = airline;
        this.source = source;
        this.destination = destination;
        this.seats = seats;
        this.fare = fare;
    }

    public String getCode() {
        return code;
    }

    public String getAirline() {
        return airline;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getSeats() {
        return seats;
    }

    public double getFare() {
        return fare;
    }

    public boolean hasSeats(int tickets) {
        return tickets <= seats;
    }

    public void bookSeats(int tickets) {
        seats = seats - tickets;
    }

    public void displayFlight() {
        System.out.println("Flight Code: " + code);
        System.out.println("Airline: " + airline);
        System.out.println("Route: " + source + " to " + destination);
        System.out.println("Available Seats: " + seats);
        System.out.println("Base Fare: Rs. " + fare);
        System.out.println("------------------------------");
    }
}

// Booking class
class Booking {
    private String bookingId;
    private Flight flight;
    private Passenger passenger;
    private int tickets;
    private double subtotal;
    private double discount;
    private double total;

    public Booking(String bookingId, Flight flight,
                   Passenger passenger, int tickets) {
        this.bookingId = bookingId;
        this.flight = flight;
        this.passenger = passenger;
        this.tickets = tickets;

        subtotal = flight.getFare() * tickets;
        discount = subtotal * passenger.getDiscount();
        total = subtotal - discount;
    }

    public void displaySummary() {
        System.out.println("\n===== BOOKING SUMMARY =====");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Flight Code: " + flight.getCode());
        System.out.println("Airline: " + flight.getAirline());
        System.out.println("Route: " + flight.getSource()
                           + " to " + flight.getDestination());
        System.out.println("Passenger: " + passenger.getName());
        System.out.println("Age: " + passenger.getAge());
        System.out.println("Passenger Type: " + passenger.getType());
        System.out.println("Tickets: " + tickets);
        System.out.println("Base Fare Per Ticket: Rs. "
                           + flight.getFare());
        System.out.println("Subtotal: Rs. " + subtotal);
        System.out.println("Discount: Rs. " + discount);
        System.out.println("Final Amount: Rs. " + total);
        System.out.println("===========================");
    }
}

// Main booking system
class FlightBookingSystem {
    private ArrayList<Flight> flights = new ArrayList<>();
    private ArrayList<Booking> bookings = new ArrayList<>();
    private int bookingNumber = 1001;

    public FlightBookingSystem() {
        flights.add(new Flight("PN101", "Air India",
                "Pune", "Delhi", 20, 4000));

        flights.add(new Flight("MB102", "IndiGo",
                "Mumbai", "Bengaluru", 25, 3500));

        flights.add(new Flight("PN103", "Akasa Air",
                "Pune", "Hyderabad", 15, 3000));
    }

    public void displayFlights() {
        System.out.println("\n===== AVAILABLE FLIGHTS =====");

        for (Flight flight : flights) {
            flight.displayFlight();
        }
    }

    public Flight findFlight(String code) {
        for (Flight flight : flights) {
            if (flight.getCode().equalsIgnoreCase(code)) {
                return flight;
            }
        }
        return null;
    }

    public boolean validateDetails(String name, int age,
                                   int tickets, Flight flight) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Name cannot be blank.");
            return false;
        }

        if (age <= 0) {
            System.out.println("Error: Age must be greater than 0.");
            return false;
        }

        if (tickets <= 0) {
            System.out.println("Error: Tickets must be greater than 0.");
            return false;
        }

        if (flight == null) {
            System.out.println("Error: Invalid flight code.");
            return false;
        }

        if (!flight.hasSeats(tickets)) {
            System.out.println("Booking failed: Not enough seats.");
            return false;
        }

        return true;
    }

    public void bookFlight(Scanner sc) {
        System.out.print("Enter flight code: ");
        String code = sc.nextLine().trim();

        Flight flight = findFlight(code);

        System.out.print("Enter passenger name: ");
        String name = sc.nextLine().trim();

        int age;
        int tickets;

        try {
            System.out.print("Enter age: ");
            age = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Passenger type (Regular/Student): ");
            String type = sc.nextLine().trim();

            System.out.print("Enter number of tickets: ");
            tickets = Integer.parseInt(sc.nextLine().trim());

            if (!type.equalsIgnoreCase("Regular")
                    && !type.equalsIgnoreCase("Student")) {
                System.out.println("Error: Invalid passenger type.");
                return;
            }

            if (!validateDetails(name, age, tickets, flight)) {
                return;
            }

            // Polymorphism
            Passenger passenger;

            if (type.equalsIgnoreCase("Student")) {
                passenger = new StudentPassenger(name, age);
            } else {
                passenger = new RegularPassenger(name, age);
            }

            // Create booking only after validation
            String id = "BK" + bookingNumber;

            Booking booking = new Booking(
                    id, flight, passenger, tickets);

            flight.bookSeats(tickets);
            bookings.add(booking);
            bookingNumber++;

            System.out.println("\nBooking confirmed!");
            booking.displaySummary();

        } catch (NumberFormatException e) {
            System.out.println("Error: Enter valid numeric values.");
        }
    }

    public void viewBooking() {
        if (bookings.isEmpty()) {
            System.out.println("No booking found.");
        } else {
            bookings.get(bookings.size() - 1).displaySummary();
        }
    }

    public void start() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== KIRAN AIR BOOKING =====");
            System.out.println("1. View Flights");
            System.out.println("2. Book Flight");
            System.out.println("3. View Booking");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    displayFlights();
                    break;

                case "2":
                    bookFlight(sc);
                    break;

                case "3":
                    viewBooking();
                    break;

                case "4":
                    System.out.println(
                        "Thank you for using Kiran Air Booking!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        FlightBookingSystem system = new FlightBookingSystem();
        system.start();
    }
}

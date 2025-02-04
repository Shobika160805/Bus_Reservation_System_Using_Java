package Prjct;

import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.*;
class OverbookingException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public OverbookingException(String message) {
        super(message);
    }
}

class Passenger implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;
    int seatNumber;
    private String reservationNumber;
    private String paymentMode;
    private String contactNumber;
    private double ticketPrice;
    
    public Passenger(String name, int age, int seatNumber, String paymentMode, String contactNumber, double ticketPrice) {
        this.name = name;
        this.age = age;
        this.seatNumber = seatNumber;
        this.reservationNumber = UUID.randomUUID().toString();
        this.paymentMode = paymentMode;
        this.contactNumber = contactNumber;
        this.ticketPrice = ticketPrice;
    }

    public String getName() {
        return name;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    @Override
    public String toString() {
        return "Passenger: " + name + ", Age: " + age + ", Seat Number: " + seatNumber + ", Reservation No: " + reservationNumber + ", Payment Mode: " + paymentMode + ", Contact: " + contactNumber + ", Ticket Price: " + ticketPrice;
    }
}

class Bus implements Serializable {
    private static final long serialVersionUID = 1L;
    private int busNumber;
    private int capacity;
    private List<Passenger> passengers;
    private String destination;
    private LocalDate date;
    private LocalTime departureTime;
    private String busModel;
    private String busType;
    private String driverName;
    private String driverContact;
    private double distance;
    private double estimatedDuration;
    private String pickupLocation;
    private String dropLocation;
    private boolean[] seatLayout;

    public Bus(int busNumber, int capacity, String destination, LocalDate date, LocalTime departureTime, String busModel, String busType, String driverName, String driverContact, double distance, double estimatedDuration, String pickupLocation, String dropLocation) {
        this.busNumber = busNumber;
        this.capacity = capacity;
        this.passengers = new ArrayList<>();
        this.destination = destination;
        this.date = date;
        this.departureTime = departureTime;
        this.busModel = busModel;
        this.busType = busType;
        this.driverName = driverName;
        this.driverContact = driverContact;
        this.distance = distance;
        this.estimatedDuration = estimatedDuration;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.seatLayout = new boolean[capacity];
    }

    public void reserveSeat(Passenger passenger) throws OverbookingException {
        if (passengers.size() >= capacity) {
            throw new OverbookingException("Bus is fully booked! No seats available.");
        }
        seatLayout[passenger.seatNumber - 1] = true;
        passengers.add(passenger);
        System.out.println("Reservation successful for " + passenger.getName() + " | Reservation No: " + passenger.getReservationNumber());
    }

    public void cancelReservation(String reservationNumber) {
        passengers.removeIf(p -> {
            if (p.getReservationNumber().equals(reservationNumber)) {
                seatLayout[p.seatNumber - 1] = false;
                System.out.println("Reservation cancelled for Reservation No: " + reservationNumber);
                return true;
            }
            return false;
        });
    }

    public void displayPassengers() {
        System.out.println("Bus " + busNumber + " (" + busModel + " - " + busType + ")");
        System.out.println("Driver: " + driverName + " | Contact: " + driverContact);
        System.out.println("Route: " + pickupLocation + " -> " + dropLocation + " (" + distance + " km, Estimated: " + estimatedDuration + " hrs)");
        System.out.println("Departure: " + date + " at " + departureTime);
        System.out.println("Passengers:");
        for (Passenger p : passengers) {
            System.out.println(p);
        }
        System.out.println("Seat Layout:");
        for (int i = 0; i < seatLayout.length; i++) {
            System.out.print((seatLayout[i] ? "[X]" : "[ ]") + " ");
        }
        System.out.println();
    }
}

class User {
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

class AuthenticateUser {
    private static Map<String, User> userDatabase = new HashMap<>();

    static {
        userDatabase.put("shobi123@gmail.com", new User("shobi123@gmail.com", "password123"));
        userDatabase.put("javith123@gmail.com", new User("javith123@gmail.com", "password456"));
    }

    public static boolean login(String email, String password) {
        User user = userDatabase.get(email);
        return user != null && user.getPassword().equals(password);
    }

    public static void register(String email, String password) {
        if (userDatabase.containsKey(email)) {
            System.out.println("Email already exists. Please try with a different one.");
        } else {
            userDatabase.put(email, new User(email, password));
            System.out.println("Registration successful!");
        }
    }

    public static String generateOTP() {
        Random rand = new Random();
        int otp = rand.nextInt(999999);
        return String.format("%06d", otp);  
    }

    public static boolean verifyOTP(String enteredOtp, String generatedOtp) {
        return enteredOtp.equals(generatedOtp);
    }
}

public class  BusSystemReservation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Bus Reservation System!");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("Choose an option (1/2): ");
        int choice = Integer.parseInt(scanner.nextLine());

        String email;
        String password;

        if (choice == 1) {
            System.out.print("Enter your email: ");
            email = scanner.nextLine();
            System.out.print("Enter your password: ");
            password = scanner.nextLine();

            if (!AuthenticateUser.login(email, password)) {
                System.out.println("Invalid email or password. Please try again.");
                return;
            }
            System.out.println("Login successful!");

        } else if (choice == 2) {
            System.out.print("Enter your email: ");
            email = scanner.nextLine();
            System.out.print("Enter your password: ");
            password = scanner.nextLine();

            AuthenticateUser.register(email, password);
        } else {
            System.out.println("Invalid choice. Exiting...");
            return;
        }
        String generatedOtp = AuthenticateUser.generateOTP();
        System.out.println("An OTP has been sent to your contact number (Simulated): " + generatedOtp);

        System.out.print("Enter the OTP to verify: ");
        String enteredOtp = scanner.nextLine();

        if (AuthenticateUser.verifyOTP(enteredOtp, generatedOtp)) {
            System.out.println("OTP verification successful!");
        } else {
            System.out.println("Invalid OTP. Exiting...");
            return;
        }

        Bus bus = new Bus(108, 5, "Chennai", LocalDate.of(2024, 3, 15), LocalTime.of(10, 30), "Volvo 9400", "AC Sleeper", "Ramchi", "9876543219", 350.0, 6.5, "Bangalore", "Chennai");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter your seat number (1-5): ");
        int seatNumber = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter your payment mode (Credit Card, Debit Card, UPI): ");
        String paymentMode = scanner.nextLine();
        System.out.print("Enter your contact number: ");
        String contactNumber = scanner.nextLine();
        System.out.print("Enter The Ticket Price : ");
        double ticketPrice = Double.parseDouble(scanner.nextLine());
        Passenger passenger = new Passenger(name, age, seatNumber, paymentMode, contactNumber, ticketPrice);

        try {
            bus.reserveSeat(passenger);
        } catch (OverbookingException e) {
            System.out.println("Error: " + e.getMessage());
        }
        bus.displayPassengers();
    }
}



abstract class Device {
    private String deviceName;  
    private boolean powerStatus;

    public Device(String deviceName) {
        this.deviceName = deviceName;
        this.powerStatus = false;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public boolean isPowerOn() {
        return powerStatus;
    }

    public void turnOn() {
        powerStatus = true;
    }

    public void turnOff() {
        powerStatus = false;
    }


    public abstract void displayStatus();
}

class Light extends Device {

    public Light(String name) {
        super(name);
    }

    @Override
    public void displayStatus() {
        System.out.println(getDeviceName() + " Light is " +
                (isPowerOn() ? "ON" : "OFF"));
    }
}

class Thermostat extends Device {
    private int temperature;

    public Thermostat(String name, int temp) {
        super(name);
        this.temperature = temp;
    }


    @Override
    public void displayStatus() {
        System.out.println(getDeviceName() + " Thermostat is " +
                (isPowerOn() ? "ON" : "OFF") +
                " | Temp: " + temperature + "°C");
    }
}

interface PaymentMethod {
    void processPayment(double amount);
}

class CreditCardPayment implements PaymentMethod {
    public void processPayment(double amount) {
        System.out.println("Payment of ₹" + amount + " done using Credit Card");
    }
}

class PayPalPayment implements PaymentMethod {
    public void processPayment(double amount) {
        System.out.println("Payment of ₹" + amount + " done using PayPal");
    }
}

class UPIPayment implements PaymentMethod {
    public void processPayment(double amount) {
        System.out.println("Payment of ₹" + amount + " done using UPI");
    }
}

class PaymentProcessor {
    public void makePayment(PaymentMethod method, double amount) {
        method.processPayment(amount);
    }
}

interface EmailSender {
    void sendEmail(String message);
}

interface SMSSender {
    void sendSMS(String message);
}

interface PushNotificationSender {
    void sendPushNotification(String message);
}

class EmailNotification implements EmailSender {
    public void sendEmail(String message) {
        System.out.println("Email: " + message);
    }
}

class SMSNotification implements SMSSender {
    public void sendSMS(String message) {
        System.out.println("SMS: " + message);
    }
}

class MobileAppNotification implements PushNotificationSender {
    public void sendPushNotification(String message) {
        System.out.println("Push Notification: " + message);
    }
}



public class week3_solution {

    public static void main(String[] args) {

        //  QUESTION 1 
        System.out.println("---- Smart Home Devices ----");

        Device light = new Light("Hall");
        Device thermostat = new Thermostat("Bedroom", 24);

        light.turnOn();
        thermostat.turnOn();

        light.displayStatus();
        thermostat.displayStatus();

        light.turnOff();
        light.displayStatus();


        // QUESTION 2 
        System.out.println("\n---- Payment Processing ----");

        PaymentProcessor processor = new PaymentProcessor();

        PaymentMethod card = new CreditCardPayment();
        PaymentMethod upi = new UPIPayment();

        processor.makePayment(card, 2000);
        processor.makePayment(upi, 750);
           
        // QUESTION 3

        System.out.println("\n---- Notification System ----");

        EmailSender email = new EmailNotification();
        email.sendEmail("Welcome User!");

        SMSSender sms = new SMSNotification();
        sms.sendSMS("Your OTP is 5678");

        PushNotificationSender push = new MobileAppNotification();
        push.sendPushNotification("New message received");
    }
}
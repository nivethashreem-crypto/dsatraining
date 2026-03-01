// ============================================================
//         COMPLETE OOP CONCEPTS IN JAVA - EXECUTABLE GUIDE
// ============================================================
// Run this file: javac OOPConcepts.java && java OOPConcepts
// ============================================================

import java.util.*;

// ============================================================
// 1. CLASS & OBJECT
// A Class is a blueprint. An Object is an instance of that blueprint.
// ============================================================
class Animal {
    // FIELDS (instance variables)
    String name;
    int age;

    // CONSTRUCTOR - called when object is created
    Animal(String name, int age) {
        this.name = name; // 'this' refers to the current object
        this.age = age;
    }

    // METHOD
    void introduce() {
        System.out.println("I am " + name + ", age " + age);
    }
}


// ============================================================
// 2. ENCAPSULATION
// Wrapping data (fields) and methods together, and restricting
// direct access using private + getters/setters.
// ============================================================
class BankAccount {
    private String owner;
    private double balance; // private = cannot be accessed directly outside

    BankAccount(String owner, double initialBalance) {
        this.owner = owner;
        this.balance = initialBalance;
    }

    // GETTER - controlled read access
    public double getBalance() {
        return balance;
    }

    // SETTER - controlled write access with validation
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println(owner + " deposited ₹" + amount + " | New Balance: ₹" + balance);
        } else {
            System.out.println("Invalid deposit amount!");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println(owner + " withdrew ₹" + amount + " | New Balance: ₹" + balance);
        } else {
            System.out.println("Insufficient funds or invalid amount!");
        }
    }
}


// ============================================================
// 3. INHERITANCE
// A child class inherits fields and methods from a parent class.
// Promotes code reuse. Uses 'extends' keyword.
// ============================================================
class Vehicle {
    String brand;
    int speed;

    Vehicle(String brand, int speed) {
        this.brand = brand;
        this.speed = speed;
    }

    void move() {
        System.out.println(brand + " is moving at " + speed + " km/h");
    }
}

// Car inherits from Vehicle
class Car extends Vehicle {
    int doors;

    Car(String brand, int speed, int doors) {
        super(brand, speed); // calls parent constructor
        this.doors = doors;
    }

    void carInfo() {
        System.out.println(brand + " | Speed: " + speed + " km/h | Doors: " + doors);
    }
}

// ElectricCar inherits from Car (MULTI-LEVEL INHERITANCE)
class ElectricCar extends Car {
    int batteryRange;

    ElectricCar(String brand, int speed, int doors, int batteryRange) {
        super(brand, speed, doors);
        this.batteryRange = batteryRange;
    }

    void electricInfo() {
        System.out.println(brand + " | Battery Range: " + batteryRange + " km");
    }
}


// ============================================================
// 4. POLYMORPHISM
// One interface, many forms.
// a) Compile-time (Method Overloading) - same method name, different params
// b) Runtime (Method Overriding) - child redefines parent method
// ============================================================

// --- Method OVERLOADING (Compile-time Polymorphism) ---
class Calculator {
    int add(int a, int b) {
        return a + b;
    }

    double add(double a, double b) { // same name, different types
        return a + b;
    }

    int add(int a, int b, int c) { // same name, different number of params
        return a + b + c;
    }
}

// --- Method OVERRIDING (Runtime Polymorphism) ---
class Shape {
    void draw() {
        System.out.println("Drawing a generic shape");
    }

    double area() {
        return 0;
    }
}

class Circle extends Shape {
    double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override // annotation tells compiler we're overriding
    void draw() {
        System.out.println("Drawing a Circle with radius " + radius);
    }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    double width, height;

    Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    void draw() {
        System.out.println("Drawing a Rectangle " + width + " x " + height);
    }

    @Override
    double area() {
        return width * height;
    }
}

class Triangle extends Shape {
    double base, height;

    Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    @Override
    void draw() {
        System.out.println("Drawing a Triangle base=" + base + " height=" + height);
    }

    @Override
    double area() {
        return 0.5 * base * height;
    }
}


// ============================================================
// 5. ABSTRACTION
// Hiding internal implementation. Showing only what's necessary.
// a) Abstract Class - partial abstraction (can have concrete methods too)
// b) Interface - full abstraction (all methods are abstract by default)
// ============================================================

// --- Abstract Class ---
abstract class Employee {
    String name;
    double baseSalary;

    Employee(String name, double baseSalary) {
        this.name = name;
        this.baseSalary = baseSalary;
    }

    // Abstract method — MUST be implemented by subclass
    abstract double calculateBonus();

    // Concrete method — shared by all
    void displaySalary() {
        double total = baseSalary + calculateBonus();
        System.out.println(name + " | Base: ₹" + baseSalary +
                " | Bonus: ₹" + calculateBonus() + " | Total: ₹" + total);
    }
}

class Manager extends Employee {
    Manager(String name, double baseSalary) {
        super(name, baseSalary);
    }

    @Override
    double calculateBonus() {
        return baseSalary * 0.30; // 30% bonus
    }
}

class Developer extends Employee {
    Developer(String name, double baseSalary) {
        super(name, baseSalary);
    }

    @Override
    double calculateBonus() {
        return baseSalary * 0.20; // 20% bonus
    }
}

// --- Interface ---
interface Flyable {
    int MAX_ALTITUDE = 40000; // implicitly public static final

    void fly();           // implicitly public abstract
    void land();

    // Default method (Java 8+) — optional to override
    default void status() {
        System.out.println("Aircraft is operational");
    }
}

interface Swimmable {
    void swim();
}

// A class can implement MULTIPLE interfaces (unlike extends)
class Duck extends Animal implements Flyable, Swimmable {
    Duck(String name) {
        super(name, 2);
    }

    @Override
    public void fly() {
        System.out.println(name + " is flying at low altitude");
    }

    @Override
    public void land() {
        System.out.println(name + " has landed on water");
    }

    @Override
    public void swim() {
        System.out.println(name + " is swimming in the pond");
    }
}


// ============================================================
// 6. STATIC MEMBERS
// Belong to the CLASS, not to any specific object.
// Shared across all instances.
// ============================================================
class Counter {
    static int count = 0; // shared across all objects
    int id;

    Counter() {
        count++;
        this.id = count;
    }

    static void showCount() { // static method
        System.out.println("Total Counter objects created: " + count);
    }
}


// ============================================================
// 7. FINAL KEYWORD
// final variable  = constant (cannot be reassigned)
// final method    = cannot be overridden
// final class     = cannot be extended
// ============================================================
final class MathConstants {
    static final double PI = 3.14159265358979;
    static final double E  = 2.71828182845904;

    // This class cannot be extended
    static void display() {
        System.out.println("PI = " + PI + " | E = " + E);
    }
}


// ============================================================
// 8. CONSTRUCTOR TYPES
// ============================================================
class Student {
    String name;
    int grade;
    String school;

    // Default Constructor
    Student() {
        this.name = "Unknown";
        this.grade = 0;
        this.school = "Not Enrolled";
    }

    // Parameterized Constructor
    Student(String name, int grade) {
        this.name = name;
        this.grade = grade;
        this.school = "Default School";
    }

    // Constructor Chaining using this()
    Student(String name, int grade, String school) {
        this(name, grade); // calls the 2-param constructor
        this.school = school;
    }

    void display() {
        System.out.println("Student: " + name + " | Grade: " + grade + " | School: " + school);
    }
}


// ============================================================
// 9. INNER CLASS TYPES
// ============================================================
class OuterClass {
    private String secret = "I am the outer class secret";

    // Regular Inner Class
    class InnerClass {
        void reveal() {
            System.out.println("Inner class accessing: " + secret);
        }
    }

    // Static Nested Class
    static class StaticNested {
        void greet() {
            System.out.println("Hello from static nested class!");
        }
    }

    void demonstrateLocalClass() {
        // Local Class (defined inside a method)
        class LocalClass {
            void say() {
                System.out.println("I am a local class inside a method!");
            }
        }
        new LocalClass().say();
    }
}


// ============================================================
// 10. GENERICS (Type-safe programming)
// ============================================================
class Box<T> {
    private T value;

    Box(T value) {
        this.value = value;
    }

    T getValue() {
        return value;
    }

    void showType() {
        System.out.println("Box contains: " + value + " | Type: " + value.getClass().getSimpleName());
    }
}


// ============================================================
//                         MAIN CLASS
// ============================================================
public class OOPConcepts {

    static void printHeader(String title) {
        System.out.println("\n" + "=".repeat(55));
        System.out.println("  " + title);
        System.out.println("=".repeat(55));
    }

    public static void main(String[] args) {

        // ----- 1. CLASS & OBJECT -----
        printHeader("1. CLASS & OBJECT");
        Animal dog = new Animal("Bruno", 3);
        Animal cat = new Animal("Whiskers", 5);
        dog.introduce();
        cat.introduce();
        System.out.println("Dog's name field: " + dog.name);

        // ----- 2. ENCAPSULATION -----
        printHeader("2. ENCAPSULATION");
        BankAccount acc = new BankAccount("Navaneeth", 10000);
        System.out.println("Initial Balance: ₹" + acc.getBalance());
        acc.deposit(5000);
        acc.withdraw(3000);
        acc.withdraw(50000); // should fail

        // ----- 3. INHERITANCE -----
        printHeader("3. INHERITANCE");
        Car myCar = new Car("Toyota", 120, 4);
        myCar.move();       // inherited from Vehicle
        myCar.carInfo();    // own method

        ElectricCar tesla = new ElectricCar("Tesla", 200, 4, 500);
        tesla.move();       // from Vehicle (grandparent)
        tesla.carInfo();    // from Car (parent)
        tesla.electricInfo(); // own method

        // ----- 4a. POLYMORPHISM - OVERLOADING -----
        printHeader("4a. POLYMORPHISM - Method Overloading");
        Calculator calc = new Calculator();
        System.out.println("add(2, 3)       = " + calc.add(2, 3));
        System.out.println("add(2.5, 3.5)   = " + calc.add(2.5, 3.5));
        System.out.println("add(1, 2, 3)    = " + calc.add(1, 2, 3));

        // ----- 4b. POLYMORPHISM - OVERRIDING -----
        printHeader("4b. POLYMORPHISM - Method Overriding (Runtime)");
        // Parent reference holding child objects — RUNTIME polymorphism
        Shape[] shapes = {
            new Circle(7),
            new Rectangle(4, 5),
            new Triangle(6, 8)
        };

        for (Shape s : shapes) {
            s.draw();  // which draw() to call is decided at RUNTIME
            System.out.printf("   Area = %.2f%n", s.area());
        }

        // ----- 5a. ABSTRACTION - Abstract Class -----
        printHeader("5a. ABSTRACTION - Abstract Class");
        Employee[] employees = {
            new Manager("Alice", 80000),
            new Developer("Navaneeth", 70000)
        };
        for (Employee e : employees) {
            e.displaySalary();
        }

        // ----- 5b. ABSTRACTION - Interface -----
        printHeader("5b. ABSTRACTION - Interface");
        Duck donald = new Duck("Donald");
        donald.fly();
        donald.swim();
        donald.land();
        donald.status(); // default interface method
        System.out.println("Max altitude constant: " + Flyable.MAX_ALTITUDE + " ft");

        // ----- 6. STATIC MEMBERS -----
        printHeader("6. STATIC MEMBERS");
        Counter c1 = new Counter();
        Counter c2 = new Counter();
        Counter c3 = new Counter();
        System.out.println("c1 ID: " + c1.id);
        System.out.println("c2 ID: " + c2.id);
        System.out.println("c3 ID: " + c3.id);
        Counter.showCount(); // called on class, not object

        // ----- 7. FINAL KEYWORD -----
        printHeader("7. FINAL KEYWORD");
        MathConstants.display();
        // MathConstants.PI = 3; // ERROR! Cannot reassign final
        // class FinalExtend extends MathConstants {} // ERROR! Cannot extend final class

        // ----- 8. CONSTRUCTOR TYPES -----
        printHeader("8. CONSTRUCTOR TYPES");
        Student s1 = new Student();                            // default
        Student s2 = new Student("Ravi", 10);                 // parameterized
        Student s3 = new Student("Priya", 12, "IIT School");  // chained
        s1.display();
        s2.display();
        s3.display();

        // ----- 9. INNER CLASSES -----
        printHeader("9. INNER CLASSES");
        OuterClass outer = new OuterClass();
        OuterClass.InnerClass inner = outer.new InnerClass();
        inner.reveal();

        OuterClass.StaticNested nested = new OuterClass.StaticNested();
        nested.greet();

        outer.demonstrateLocalClass();

        // Anonymous Inner Class
        Shape anonymousShape = new Shape() {
            @Override
            void draw() {
                System.out.println("Drawing from anonymous inner class!");
            }
        };
        anonymousShape.draw();

        // ----- 10. GENERICS -----
        printHeader("10. GENERICS");
        Box<Integer> intBox    = new Box<>(42);
        Box<String>  strBox    = new Box<>("Hello OOP");
        Box<Double>  doubleBox = new Box<>(3.14);
        intBox.showType();
        strBox.showType();
        doubleBox.showType();

        // ----- SUMMARY -----
        printHeader("OOP CONCEPTS SUMMARY");
        System.out.println("  1. Class & Object    - Blueprint and instance");
        System.out.println("  2. Encapsulation     - Data hiding with getters/setters");
        System.out.println("  3. Inheritance       - Reuse via extends");
        System.out.println("  4. Polymorphism      - Overloading & Overriding");
        System.out.println("  5. Abstraction       - Abstract class & Interface");
        System.out.println("  6. Static Members    - Class-level shared data");
        System.out.println("  7. Final Keyword     - Constants, no-override, no-extend");
        System.out.println("  8. Constructors      - Default, Parameterized, Chained");
        System.out.println("  9. Inner Classes     - Inner, Static Nested, Local, Anonymous");
        System.out.println(" 10. Generics          - Type-safe reusable code");
        System.out.println("=".repeat(55));
    }
}

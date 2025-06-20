/**
 * Basic Singleton Pattern Implementation
 * 
 * This demonstrates the classic singleton pattern where only one instance
 * of a class can be created throughout the application lifecycle.
 */
public class BasicSingleton {
    
    // Private static instance - the single instance
    private static BasicSingleton instance;
    
    // Private data
    private String data;
    private int counter;
    
    // Private constructor to prevent instantiation
    private BasicSingleton() {
        this.data = "Singleton Instance";
        this.counter = 0;
        System.out.println("BasicSingleton instance created!");
    }
    
    // Public method to get the single instance
    public static BasicSingleton getInstance() {
        if (instance == null) {
            instance = new BasicSingleton();
        }
        return instance;
    }
    
    // Business methods
    public void setData(String data) {
        this.data = data;
    }
    
    public String getData() {
        return this.data;
    }
    
    public void incrementCounter() {
        this.counter++;
    }
    
    public int getCounter() {
        return this.counter;
    }
    
    public void displayInfo() {
        System.out.println("Instance ID: " + this.hashCode());
        System.out.println("Data: " + this.data);
        System.out.println("Counter: " + this.counter);
    }
    
    // Demo method
    public static void main(String[] args) {
        System.out.println("=== Basic Singleton Demo ===");
        
        // Get first instance
        BasicSingleton singleton1 = BasicSingleton.getInstance();
        singleton1.setData("First access");
        singleton1.incrementCounter();
        System.out.println("\nFirst instance:");
        singleton1.displayInfo();
        
        // Get second instance - should be the same object
        BasicSingleton singleton2 = BasicSingleton.getInstance();
        singleton2.setData("Second access");
        singleton2.incrementCounter();
        System.out.println("\nSecond instance:");
        singleton2.displayInfo();
        
        // Verify they are the same instance
        System.out.println("\nVerification:");
        System.out.println("Are they the same instance? " + (singleton1 == singleton2));
        System.out.println("singleton1 hash: " + singleton1.hashCode());
        System.out.println("singleton2 hash: " + singleton2.hashCode());
    }
} 
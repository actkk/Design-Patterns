/**
 * Eager Initialization Singleton Pattern Implementation
 * 
 * This singleton implementation creates the instance at class loading time.
 * It's thread-safe by nature since the class loading mechanism is thread-safe.
 * Best for cases where the singleton will definitely be used.
 */
public class EagerSingleton {
    
    // Instance is created at class loading time
    private static final EagerSingleton INSTANCE = new EagerSingleton();
    
    // Private data
    private final String data;
    private final long creationTime;
    private int counter;
    
    // Private constructor
    private EagerSingleton() {
        this.data = "Eager Singleton - Created at Class Loading";
        this.creationTime = System.currentTimeMillis();
        this.counter = 0;
        System.out.println("EagerSingleton instance created at class loading time!");
        System.out.println("Creation thread: " + Thread.currentThread().getName());
    }
    
    // Public method to get the instance
    public static EagerSingleton getInstance() {
        return INSTANCE;
    }
    
    // Business methods
    public String getData() {
        return this.data;
    }
    
    public synchronized void incrementCounter() {
        this.counter++;
    }
    
    public int getCounter() {
        return this.counter;
    }
    
    public long getCreationTime() {
        return this.creationTime;
    }
    
    public void displayInfo() {
        System.out.println("Current thread: " + Thread.currentThread().getName());
        System.out.println("Instance ID: " + this.hashCode());
        System.out.println("Data: " + this.data);
        System.out.println("Counter: " + this.counter);
        System.out.println("Creation Time: " + this.creationTime);
        System.out.println("Time since creation: " + (System.currentTimeMillis() - creationTime) + " ms");
    }
    
    // Method to demonstrate when the singleton is created
    public static void triggerClassLoading() {
        System.out.println("triggerClassLoading() called - this will load the class if not already loaded");
    }
    
    // Test thread for concurrent access
    static class TestThread extends Thread {
        private String threadName;
        
        public TestThread(String name) {
            this.threadName = name;
            setName(name);
        }
        
        @Override
        public void run() {
            System.out.println("\n" + threadName + " getting singleton instance...");
            
            EagerSingleton singleton = EagerSingleton.getInstance();
            
            // Perform operations
            for (int i = 0; i < 3; i++) {
                singleton.incrementCounter();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            System.out.println(threadName + " results:");
            singleton.displayInfo();
        }
    }
    
    // Demo method
    public static void main(String[] args) {
        System.out.println("=== Eager Singleton Demo ===");
        
        System.out.println("\n1. Before any getInstance() call:");
        System.out.println("Note: The singleton instance is already created when the class is loaded!");
        
        // Add a small delay to show timing
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // First access
        System.out.println("\n2. First getInstance() call:");
        EagerSingleton singleton1 = EagerSingleton.getInstance();
        singleton1.incrementCounter();
        singleton1.displayInfo();
        
        // Second access
        System.out.println("\n3. Second getInstance() call:");
        EagerSingleton singleton2 = EagerSingleton.getInstance();
        singleton2.incrementCounter();
        
        System.out.println("Are they the same instance? " + (singleton1 == singleton2));
        
        // Multi-threaded test
        System.out.println("\n4. Multi-threaded access test:");
        
        TestThread thread1 = new TestThread("Worker-1");
        TestThread thread2 = new TestThread("Worker-2");
        TestThread thread3 = new TestThread("Worker-3");
        
        // Start threads
        thread1.start();
        thread2.start();
        thread3.start();
        
        // Wait for completion
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Final state
        System.out.println("\n5. Final singleton state:");
        EagerSingleton finalSingleton = EagerSingleton.getInstance();
        finalSingleton.displayInfo();
        
        // Demonstrate class loading behavior
        System.out.println("\n6. Class loading demonstration:");
        System.out.println("This shows that the instance was created when the class was first loaded,");
        System.out.println("not when getInstance() was first called.");
    }
} 
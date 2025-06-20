/**
 * Bill Pugh Singleton Pattern Implementation (Initialization-on-demand holder idiom)
 * 
 * This approach uses a static inner class to hold the singleton instance.
 * It provides lazy initialization without synchronization overhead.
 * The inner class is not loaded until it's referenced, ensuring lazy loading.
 */
public class BillPughSingleton {
    
    // Private data
    private String data;
    private int counter;
    private final long creationTime;
    
    // Private constructor
    private BillPughSingleton() {
        this.data = "Bill Pugh Singleton - Lazy Loading with Static Inner Class";
        this.counter = 0;
        this.creationTime = System.currentTimeMillis();
        System.out.println("BillPughSingleton instance created!");
        System.out.println("Creation thread: " + Thread.currentThread().getName());
        
        // Simulate some initialization work
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Static inner class - inner classes are not loaded until they are referenced.
     * This provides lazy initialization without synchronization.
     */
    private static class SingletonHelper {
        // The instance is created when this class is first loaded
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }
    
    // Public method to get the instance
    public static BillPughSingleton getInstance() {
        // This will load the SingletonHelper class and create the instance
        return SingletonHelper.INSTANCE;
    }
    
    // Business methods
    public void setData(String data) {
        this.data = data;
    }
    
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
    
    // Method to demonstrate lazy loading
    public static void demonstrateLazyLoading() {
        System.out.println("demonstrateLazyLoading() called");
        System.out.println("BillPughSingleton class is loaded, but inner class is not yet loaded");
        System.out.println("Instance will be created only when getInstance() is called for the first time");
    }
    
    // Test thread for concurrent access
    static class TestThread extends Thread {
        private String threadName;
        private long startTime;
        
        public TestThread(String name) {
            this.threadName = name;
            setName(name);
        }
        
        @Override
        public void run() {
            startTime = System.nanoTime();
            System.out.println("\n" + threadName + " calling getInstance()...");
            
            BillPughSingleton singleton = BillPughSingleton.getInstance();
            
            long endTime = System.nanoTime();
            System.out.println(threadName + " got instance in " + (endTime - startTime) / 1_000_000 + " ms");
            
            // Perform operations
            for (int i = 0; i < 3; i++) {
                singleton.incrementCounter();
                singleton.setData(threadName + " - Operation " + (i + 1));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            System.out.println(threadName + " final state:");
            singleton.displayInfo();
        }
    }
    
    // Demo method
    public static void main(String[] args) {
        System.out.println("=== Bill Pugh Singleton Demo ===");
        
        // Demonstrate lazy loading
        System.out.println("\n1. Lazy loading demonstration:");
        BillPughSingleton.demonstrateLazyLoading();
        
        System.out.println("\nWaiting 2 seconds before first getInstance() call...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // First access - this is when the instance is actually created
        System.out.println("\n2. First getInstance() call (this creates the instance):");
        long start = System.nanoTime();
        BillPughSingleton singleton1 = BillPughSingleton.getInstance();
        long end = System.nanoTime();
        
        System.out.println("First getInstance() took " + (end - start) / 1_000_000 + " ms");
        singleton1.setData("First access");
        singleton1.incrementCounter();
        singleton1.displayInfo();
        
        // Second access - should be much faster
        System.out.println("\n3. Second getInstance() call (returns existing instance):");
        start = System.nanoTime();
        BillPughSingleton singleton2 = BillPughSingleton.getInstance();
        end = System.nanoTime();
        
        System.out.println("Second getInstance() took " + (end - start) / 1000 + " nanoseconds");
        singleton2.incrementCounter();
        
        System.out.println("Are they the same instance? " + (singleton1 == singleton2));
        
        // Multi-threaded test
        System.out.println("\n4. Multi-threaded access test:");
        
        // Reset for clean test (this is just for demo - normally you wouldn't do this)
        System.out.println("Note: In a real application, you wouldn't reset the singleton");
        
        TestThread thread1 = new TestThread("Worker-1");
        TestThread thread2 = new TestThread("Worker-2");
        TestThread thread3 = new TestThread("Worker-3");
        
        // Start threads simultaneously
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
        BillPughSingleton finalSingleton = BillPughSingleton.getInstance();
        finalSingleton.displayInfo();
        
        // Advantages summary
        System.out.println("\n6. Bill Pugh Singleton Advantages:");
        System.out.println("   - Lazy initialization (created only when needed)");
        System.out.println("   - Thread-safe without synchronization overhead");
        System.out.println("   - No performance penalty after initialization");
        System.out.println("   - Uses the classloader's thread safety guarantees");
        System.out.println("   - Simple and clean implementation");
        System.out.println("   - No locks or synchronization blocks in getInstance()");
    }
} 
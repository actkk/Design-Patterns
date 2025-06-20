/**
 * Enum Singleton Pattern Implementation
 * 
 * This is considered the best way to implement singleton in Java.
 * Enum provides implicit thread safety, serialization safety, and 
 * reflection safety. It's also very concise.
 */
public enum EnumSingleton {
    
    // The single instance
    INSTANCE;
    
    // Private data
    private String data;
    private int counter;
    private final long creationTime;
    
    // Constructor (implicitly private in enum)
    EnumSingleton() {
        this.data = "Enum Singleton - Most Robust Implementation";
        this.counter = 0;
        this.creationTime = System.currentTimeMillis();
        System.out.println("EnumSingleton instance created!");
        System.out.println("Creation thread: " + Thread.currentThread().getName());
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
        System.out.println("Instance: " + this);
        System.out.println("Instance ID: " + this.hashCode());
        System.out.println("Data: " + this.data);
        System.out.println("Counter: " + this.counter);
        System.out.println("Creation Time: " + this.creationTime);
    }
    
    // Method to demonstrate singleton behavior
    public void doSomething() {
        System.out.println("EnumSingleton is doing something useful!");
        incrementCounter();
    }
    
    // Demonstration of serialization safety
    public void demonstrateSerializationSafety() {
        System.out.println("Enum singletons are automatically serialization-safe!");
        System.out.println("No need to implement readResolve() method");
    }
    
    // Demonstration of reflection safety
    public void demonstrateReflectionSafety() {
        System.out.println("Enum singletons are automatically reflection-safe!");
        System.out.println("Cannot create new instances using reflection");
    }
}

/**
 * Demo class to show EnumSingleton usage
 */
class EnumSingletonDemo {
    
    // Test thread for concurrent access
    static class TestThread extends Thread {
        private String threadName;
        
        public TestThread(String name) {
            this.threadName = name;
            setName(name);
        }
        
        @Override
        public void run() {
            System.out.println("\n" + threadName + " accessing EnumSingleton...");
            
            EnumSingleton singleton = EnumSingleton.INSTANCE;
            singleton.setData(threadName + " was here");
            
            // Perform operations
            for (int i = 0; i < 3; i++) {
                singleton.doSomething();
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
    
    public static void main(String[] args) {
        System.out.println("=== Enum Singleton Demo ===");
        
        // Basic usage
        System.out.println("\n1. Basic usage:");
        EnumSingleton singleton1 = EnumSingleton.INSTANCE;
        singleton1.setData("First access");
        singleton1.doSomething();
        singleton1.displayInfo();
        
        // Second access - should be same instance
        System.out.println("\n2. Second access:");
        EnumSingleton singleton2 = EnumSingleton.INSTANCE;
        singleton2.doSomething();
        
        System.out.println("Are they the same instance? " + (singleton1 == singleton2));
        System.out.println("singleton1 == EnumSingleton.INSTANCE? " + (singleton1 == EnumSingleton.INSTANCE));
        System.out.println("singleton2 == EnumSingleton.INSTANCE? " + (singleton2 == EnumSingleton.INSTANCE));
        
        // Demonstrate safety features
        System.out.println("\n3. Safety demonstrations:");
        EnumSingleton.INSTANCE.demonstrateSerializationSafety();
        EnumSingleton.INSTANCE.demonstrateReflectionSafety();
        
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
        EnumSingleton finalSingleton = EnumSingleton.INSTANCE;
        finalSingleton.displayInfo();
        
        // Advantages summary
        System.out.println("\n6. Enum Singleton Advantages:");
        System.out.println("   - Thread-safe by default");
        System.out.println("   - Serialization-safe by default");
        System.out.println("   - Reflection-safe by default");
        System.out.println("   - Very concise code");
        System.out.println("   - No need for getInstance() method");
        System.out.println("   - Lazy initialization (created when first accessed)");
        
        // Reflection test demonstration
        System.out.println("\n7. Reflection safety test:");
        try {
            Class<?> enumClass = EnumSingleton.class;
            System.out.println("Enum class: " + enumClass.getName());
            System.out.println("Is enum? " + enumClass.isEnum());
            System.out.println("Enum constants: " + java.util.Arrays.toString(enumClass.getEnumConstants()));
            
            // This would throw an exception if we tried to create a new instance
            System.out.println("Cannot create new enum instances via reflection!");
        } catch (Exception e) {
            System.out.println("Exception (expected): " + e.getMessage());
        }
    }
} 
/**
 * Double-Checked Locking Singleton Pattern Implementation
 * 
 * This is a more efficient thread-safe singleton implementation that reduces
 * synchronization overhead by using double-checked locking pattern.
 */
public class DoubleCheckedLockingSingleton {
    
    // Private static volatile instance - volatile ensures visibility across threads
    private static volatile DoubleCheckedLockingSingleton instance;
    
    // Private data
    private String data;
    private int counter;
    private long creationTime;
    
    // Private constructor
    private DoubleCheckedLockingSingleton() {
        this.data = "Double-Checked Locking Singleton";
        this.counter = 0;
        this.creationTime = System.currentTimeMillis();
        System.out.println("DoubleCheckedLockingSingleton created by thread: " + Thread.currentThread().getName());
        
        // Simulate some initialization work
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // Double-checked locking getInstance method
    public static DoubleCheckedLockingSingleton getInstance() {
        // First check (no synchronization)
        if (instance == null) {
            synchronized (DoubleCheckedLockingSingleton.class) {
                // Second check (with synchronization)
                if (instance == null) {
                    instance = new DoubleCheckedLockingSingleton();
                }
            }
        }
        return instance;
    }
    
    // Business methods with selective synchronization
    public synchronized void setData(String data) {
        this.data = data;
    }
    
    public String getData() {
        return this.data; // Reading is generally safe without synchronization for immutable operations
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
        System.out.println("Thread: " + Thread.currentThread().getName());
        System.out.println("Instance ID: " + this.hashCode());
        System.out.println("Data: " + this.getData());
        System.out.println("Counter: " + this.getCounter());
        System.out.println("Creation Time: " + this.creationTime);
    }
    
    // Performance test thread
    static class PerformanceTestThread extends Thread {
        private String threadName;
        private int iterations;
        private long startTime;
        private long endTime;
        
        public PerformanceTestThread(String name, int iterations) {
            this.threadName = name;
            this.iterations = iterations;
            setName(name);
        }
        
        @Override
        public void run() {
            startTime = System.nanoTime();
            
            for (int i = 0; i < iterations; i++) {
                DoubleCheckedLockingSingleton singleton = DoubleCheckedLockingSingleton.getInstance();
                singleton.incrementCounter();
            }
            
            endTime = System.nanoTime();
            
            System.out.println(threadName + " completed " + iterations + 
                " getInstance() calls in " + (endTime - startTime) / 1_000_000 + " ms");
        }
    }
    
    // Demo method
    public static void main(String[] args) {
        System.out.println("=== Double-Checked Locking Singleton Demo ===");
        
        // Basic functionality test
        System.out.println("\n1. Basic functionality test:");
        DoubleCheckedLockingSingleton s1 = DoubleCheckedLockingSingleton.getInstance();
        s1.setData("First instance data");
        s1.incrementCounter();
        
        DoubleCheckedLockingSingleton s2 = DoubleCheckedLockingSingleton.getInstance();
        s2.incrementCounter();
        
        System.out.println("Are they the same instance? " + (s1 == s2));
        s1.displayInfo();
        
        // Performance test - multiple threads getting instances
        System.out.println("\n2. Performance test:");
        int numThreads = 5;
        int iterationsPerThread = 100000;
        
        PerformanceTestThread[] threads = new PerformanceTestThread[numThreads];
        
        long overallStart = System.nanoTime();
        
        // Create and start threads
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new PerformanceTestThread("PerfTest-" + (i + 1), iterationsPerThread);
            threads[i].start();
        }
        
        // Wait for all threads to complete
        try {
            for (PerformanceTestThread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        long overallEnd = System.nanoTime();
        
        System.out.println("\nOverall performance test completed in: " + 
            (overallEnd - overallStart) / 1_000_000 + " ms");
        
        // Final state
        DoubleCheckedLockingSingleton finalSingleton = DoubleCheckedLockingSingleton.getInstance();
        System.out.println("\nFinal singleton state:");
        finalSingleton.displayInfo();
        System.out.println("Total getInstance() calls: " + (numThreads * iterationsPerThread + 2));
    }
} 
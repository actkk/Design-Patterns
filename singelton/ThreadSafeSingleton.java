/**
 * Thread-Safe Singleton Pattern Implementation
 * 
 * This implementation ensures thread safety in multi-threaded environments
 * using synchronized methods to prevent race conditions.
 */
public class ThreadSafeSingleton {
    
    // Private static instance
    private static ThreadSafeSingleton instance;
    
    // Private data
    private String data;
    private int counter;
    private long creationTime;
    
    // Private constructor
    private ThreadSafeSingleton() {
        this.data = "Thread-Safe Singleton";
        this.counter = 0;
        this.creationTime = System.currentTimeMillis();
        System.out.println("ThreadSafeSingleton created by thread: " + Thread.currentThread().getName());
    }
    
    // Thread-safe getInstance method using synchronized
    public static synchronized ThreadSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeSingleton();
        }
        return instance;
    }
    
    // Thread-safe business methods
    public synchronized void setData(String data) {
        this.data = data;
    }
    
    public synchronized String getData() {
        return this.data;
    }
    
    public synchronized void incrementCounter() {
        this.counter++;
    }
    
    public synchronized int getCounter() {
        return this.counter;
    }
    
    public long getCreationTime() {
        return this.creationTime;
    }
    
    public synchronized void displayInfo() {
        System.out.println("Thread: " + Thread.currentThread().getName());
        System.out.println("Instance ID: " + this.hashCode());
        System.out.println("Data: " + this.data);
        System.out.println("Counter: " + this.counter);
        System.out.println("Creation Time: " + this.creationTime);
    }
    
    // Worker thread class for testing
    static class WorkerThread extends Thread {
        private String threadName;
        
        public WorkerThread(String name) {
            this.threadName = name;
            setName(name);
        }
        
        @Override
        public void run() {
            System.out.println("\n" + threadName + " started");
            
            // Get singleton instance
            ThreadSafeSingleton singleton = ThreadSafeSingleton.getInstance();
            
            // Perform some operations
            for (int i = 0; i < 3; i++) {
                singleton.incrementCounter();
                singleton.setData(threadName + " - Operation " + (i + 1));
                
                try {
                    Thread.sleep(100); // Simulate some work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            System.out.println(threadName + " finished:");
            singleton.displayInfo();
        }
    }
    
    // Demo method
    public static void main(String[] args) {
        System.out.println("=== Thread-Safe Singleton Demo ===");
        
        // Single-threaded test
        System.out.println("\n1. Single-threaded test:");
        ThreadSafeSingleton s1 = ThreadSafeSingleton.getInstance();
        s1.incrementCounter();
        
        ThreadSafeSingleton s2 = ThreadSafeSingleton.getInstance();
        s2.incrementCounter();
        
        System.out.println("Are they the same instance? " + (s1 == s2));
        System.out.println("Counter value: " + s1.getCounter());
        
        // Multi-threaded test
        System.out.println("\n2. Multi-threaded test:");
        
        // Create multiple threads
        WorkerThread thread1 = new WorkerThread("Worker-1");
        WorkerThread thread2 = new WorkerThread("Worker-2");
        WorkerThread thread3 = new WorkerThread("Worker-3");
        
        // Start all threads
        thread1.start();
        thread2.start();
        thread3.start();
        
        // Wait for all threads to complete
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Final state
        System.out.println("\nFinal singleton state:");
        ThreadSafeSingleton finalSingleton = ThreadSafeSingleton.getInstance();
        finalSingleton.displayInfo();
    }
} 
/**
 * Event-Driven Observer Pattern Implementation
 * 
 * This demonstrates a modern approach to the observer pattern using
 * functional interfaces, lambda expressions, and event handling.
 * It provides a more flexible and functional programming approach.
 */

import java.util.*;
import java.util.function.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Event base class
abstract class Event {
    private final LocalDateTime timestamp;
    private final String source;
    
    public Event(String source) {
        this.timestamp = LocalDateTime.now();
        this.source = source;
    }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getSource() { return source; }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: %s", 
            timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss")), 
            source, getEventDescription());
    }
    
    protected abstract String getEventDescription();
}

// Specific event types
class UserLoginEvent extends Event {
    private final String username;
    private final String ipAddress;
    
    public UserLoginEvent(String source, String username, String ipAddress) {
        super(source);
        this.username = username;
        this.ipAddress = ipAddress;
    }
    
    public String getUsername() { return username; }
    public String getIpAddress() { return ipAddress; }
    
    @Override
    protected String getEventDescription() {
        return String.format("User '%s' logged in from %s", username, ipAddress);
    }
}

class FileUploadEvent extends Event {
    private final String filename;
    private final long fileSize;
    private final String uploadedBy;
    
    public FileUploadEvent(String source, String filename, long fileSize, String uploadedBy) {
        super(source);
        this.filename = filename;
        this.fileSize = fileSize;
        this.uploadedBy = uploadedBy;
    }
    
    public String getFilename() { return filename; }
    public long getFileSize() { return fileSize; }
    public String getUploadedBy() { return uploadedBy; }
    
    @Override
    protected String getEventDescription() {
        return String.format("File '%s' (%.2f KB) uploaded by %s", 
            filename, fileSize / 1024.0, uploadedBy);
    }
}

class SystemErrorEvent extends Event {
    private final String errorMessage;
    private final String errorLevel;
    
    public SystemErrorEvent(String source, String errorMessage, String errorLevel) {
        super(source);
        this.errorMessage = errorMessage;
        this.errorLevel = errorLevel;
    }
    
    public String getErrorMessage() { return errorMessage; }
    public String getErrorLevel() { return errorLevel; }
    
    @Override
    protected String getEventDescription() {
        return String.format("[%s] Error: %s", errorLevel, errorMessage);
    }
}

// Functional event listener interface
@FunctionalInterface
interface EventListener<T extends Event> {
    void onEvent(T event);
}

// Event publisher using functional approach
class EventPublisher {
    private final Map<Class<? extends Event>, List<Consumer<Event>>> listeners;
    private final String publisherName;
    
    public EventPublisher(String name) {
        this.listeners = new HashMap<>();
        this.publisherName = name;
    }
    
    // Method to add listeners using Consumer (for lambdas and method references)
    @SuppressWarnings("unchecked")
    public <T extends Event> void addEventListener(Class<T> eventType, Consumer<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>())
                 .add((Consumer<Event>) listener);
        System.out.println(publisherName + ": Added listener for " + eventType.getSimpleName());
    }
    
    // Publish event to all relevant listeners
    public void publishEvent(Event event) {
        System.out.println("\n--- " + publisherName + ": Publishing Event ---");
        System.out.println("Event: " + event);
        
        List<Consumer<Event>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null && !eventListeners.isEmpty()) {
            System.out.println("Notifying " + eventListeners.size() + " listeners");
            eventListeners.forEach(listener -> listener.accept(event));
        } else {
            System.out.println("No listeners for " + event.getClass().getSimpleName());
        }
    }
    
    // Get listener count for an event type
    public int getListenerCount(Class<? extends Event> eventType) {
        List<Consumer<Event>> eventListeners = listeners.get(eventType);
        return eventListeners != null ? eventListeners.size() : 0;
    }
}

// Concrete event handlers/observers
class SecurityAuditHandler {
    private final String handlerName;
    private final List<UserLoginEvent> loginHistory;
    
    public SecurityAuditHandler(String name) {
        this.handlerName = name;
        this.loginHistory = new ArrayList<>();
    }
    
    public void handleUserLogin(UserLoginEvent event) {
        loginHistory.add(event);
        System.out.println(handlerName + ": Logged security event - " + event.getEventDescription());
        
        // Check for suspicious activity
        if (event.getIpAddress().startsWith("192.168.")) {
            System.out.println(handlerName + ": Local network login detected");
        } else {
            System.out.println(handlerName + ": External login detected - monitoring");
        }
    }
    
    public void showLoginHistory() {
        System.out.println(handlerName + " login history:");
        loginHistory.forEach(event -> 
            System.out.println("  " + event.getUsername() + " from " + event.getIpAddress() + 
                             " at " + event.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
    }
}

class FileManagerHandler {
    private final String handlerName;
    private long totalBytesUploaded;
    private int fileCount;
    
    public FileManagerHandler(String name) {
        this.handlerName = name;
        this.totalBytesUploaded = 0;
        this.fileCount = 0;
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        totalBytesUploaded += event.getFileSize();
        fileCount++;
        System.out.println(handlerName + ": Processing file upload - " + event.getEventDescription());
        System.out.println(handlerName + ": Total storage used: " + (totalBytesUploaded / 1024.0) + " KB");
    }
    
    public void showStatistics() {
        System.out.println(handlerName + " statistics:");
        System.out.println("  Files uploaded: " + fileCount);
        System.out.println("  Total size: " + String.format("%.2f KB", totalBytesUploaded / 1024.0));
    }
}

class LoggingHandler {
    private final String handlerName;
    
    public LoggingHandler(String name) {
        this.handlerName = name;
    }
    
    // Generic event logger using lambda
    public void logEvent(Event event) {
        System.out.println(handlerName + ": [LOG] " + event);
    }
    
    // Specific error handler
    public void handleSystemError(SystemErrorEvent event) {
        System.out.println(handlerName + ": [ERROR] " + event.getEventDescription());
        
        if ("CRITICAL".equals(event.getErrorLevel())) {
            System.out.println(handlerName + ": 🚨 CRITICAL ERROR ALERT sent to admin!");
        }
    }
}

// Demo class
public class EventDrivenObserver {
    public static void main(String[] args) {
        System.out.println("=== Event-Driven Observer Pattern Demo ===");
        
        // Create event publisher
        EventPublisher eventSystem = new EventPublisher("Application Event System");
        
        // Create event handlers
        SecurityAuditHandler securityHandler = new SecurityAuditHandler("Security Audit System");
        FileManagerHandler fileHandler = new FileManagerHandler("File Management System");
        LoggingHandler logger = new LoggingHandler("System Logger");
        
        System.out.println("\n1. Adding event listeners:");
        
        // Add listeners using method references
        eventSystem.addEventListener(UserLoginEvent.class, securityHandler::handleUserLogin);
        eventSystem.addEventListener(FileUploadEvent.class, fileHandler::handleFileUpload);
        eventSystem.addEventListener(SystemErrorEvent.class, logger::handleSystemError);
        
        // Add generic logger for all events using lambda
        eventSystem.addEventListener(UserLoginEvent.class, logger::logEvent);
        eventSystem.addEventListener(FileUploadEvent.class, logger::logEvent);
        eventSystem.addEventListener(SystemErrorEvent.class, logger::logEvent);
        
        // Add additional listeners using lambda expressions
        eventSystem.addEventListener(UserLoginEvent.class, event -> {
            if (event.getUsername().equals("admin")) {
                System.out.println("🔐 Admin Login Alert: Administrator accessed the system!");
            }
        });
        
        eventSystem.addEventListener(FileUploadEvent.class, event -> {
            if (event.getFileSize() > 1000000) { // > 1MB
                System.out.println("📁 Large File Alert: File size exceeds 1MB!");
            }
        });
        
        System.out.println("\n2. Publishing various events:");
        
        // Publish user login events
        eventSystem.publishEvent(new UserLoginEvent("Web App", "john_doe", "192.168.1.100"));
        eventSystem.publishEvent(new UserLoginEvent("Mobile App", "admin", "203.45.67.89"));
        
        // Publish file upload events
        eventSystem.publishEvent(new FileUploadEvent("Cloud Storage", "document.pdf", 512000, "john_doe"));
        eventSystem.publishEvent(new FileUploadEvent("Cloud Storage", "video.mp4", 2048000, "jane_smith"));
        
        // Publish system error events
        eventSystem.publishEvent(new SystemErrorEvent("Database", "Connection timeout", "WARNING"));
        eventSystem.publishEvent(new SystemErrorEvent("Payment Gateway", "Payment processing failed", "CRITICAL"));
        
        System.out.println("\n3. Handler statistics:");
        securityHandler.showLoginHistory();
        System.out.println();
        fileHandler.showStatistics();
        
        System.out.println("\n4. Listener counts:");
        System.out.println("UserLoginEvent listeners: " + eventSystem.getListenerCount(UserLoginEvent.class));
        System.out.println("FileUploadEvent listeners: " + eventSystem.getListenerCount(FileUploadEvent.class));
        System.out.println("SystemErrorEvent listeners: " + eventSystem.getListenerCount(SystemErrorEvent.class));
        
        System.out.println("\n=== Event-Driven Observer Benefits ===");
        System.out.println("✓ Functional Programming: Uses lambdas and method references");
        System.out.println("✓ Type Safety: Compile-time type checking for events");
        System.out.println("✓ Flexibility: Easy to add new event types and handlers");
        System.out.println("✓ Decoupling: Publishers don't know about specific handlers");
        System.out.println("✓ Modern Java: Leverages Java 8+ functional features");
    }
} 
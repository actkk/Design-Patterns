/**
 * Push vs Pull Observer Pattern Implementation
 * 
 * This demonstrates both push and pull models in the observer pattern:
 * - Push Model: Subject sends specific data to observers
 * - Pull Model: Subject notifies observers, who then request specific data
 * 
 * Shows the trade-offs between the two approaches.
 */

import java.util.*;

// Observer interfaces for different models
interface PushObserver {
    void update(String data, Object additionalInfo);
}

interface PullObserver {
    void update(PullSubject subject);
}

// Subject interfaces
interface PushSubject {
    void addObserver(PushObserver observer);
    void removeObserver(PushObserver observer);
    void notifyObservers(String data, Object additionalInfo);
}

interface PullSubject {
    void addObserver(PullObserver observer);
    void removeObserver(PullObserver observer);
    void notifyObservers();
    
    // Getter methods for pull model
    String getCurrentStatus();
    Map<String, Object> getAllData();
    Object getSpecificData(String key);
}

// Data class for complex information
class SensorData {
    private double temperature;
    private double humidity;
    private double pressure;
    private String location;
    private long timestamp;
    
    public SensorData(String location, double temperature, double humidity, double pressure) {
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public double getPressure() { return pressure; }
    public String getLocation() { return location; }
    public long getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("SensorData[%s: %.1f°C, %.1f%% humidity, %.1f hPa]", 
            location, temperature, humidity, pressure);
    }
}

// PUSH MODEL Implementation
class PushWeatherStation implements PushSubject {
    private List<PushObserver> observers;
    private SensorData currentData;
    private String stationName;
    
    public PushWeatherStation(String name) {
        this.observers = new ArrayList<>();
        this.stationName = name;
    }
    
    @Override
    public void addObserver(PushObserver observer) {
        observers.add(observer);
        System.out.println(stationName + " (Push): Observer added. Total: " + observers.size());
    }
    
    @Override
    public void removeObserver(PushObserver observer) {
        observers.remove(observer);
        System.out.println(stationName + " (Push): Observer removed. Total: " + observers.size());
    }
    
    @Override
    public void notifyObservers(String data, Object additionalInfo) {
        System.out.println(stationName + " (Push): Notifying " + observers.size() + " observers with specific data");
        for (PushObserver observer : observers) {
            observer.update(data, additionalInfo);
        }
    }
    
    public void updateWeatherData(SensorData data) {
        this.currentData = data;
        System.out.println("\n--- " + stationName + " (Push): Weather data updated ---");
        System.out.println("New data: " + data);
        
        // Push specific data to different types of observers
        notifyObservers("temperature", data.getTemperature());
        notifyObservers("humidity", data.getHumidity());
        notifyObservers("full_data", data);
    }
    
    public SensorData getCurrentData() {
        return currentData;
    }
}

// PULL MODEL Implementation
class PullWeatherStation implements PullSubject {
    private List<PullObserver> observers;
    private SensorData currentData;
    private String stationName;
    private Map<String, Object> dataMap;
    private String status;
    
    public PullWeatherStation(String name) {
        this.observers = new ArrayList<>();
        this.stationName = name;
        this.dataMap = new HashMap<>();
        this.status = "OFFLINE";
    }
    
    @Override
    public void addObserver(PullObserver observer) {
        observers.add(observer);
        System.out.println(stationName + " (Pull): Observer added. Total: " + observers.size());
    }
    
    @Override
    public void removeObserver(PullObserver observer) {
        observers.remove(observer);
        System.out.println(stationName + " (Pull): Observer removed. Total: " + observers.size());
    }
    
    @Override
    public void notifyObservers() {
        System.out.println(stationName + " (Pull): Notifying " + observers.size() + " observers (minimal notification)");
        for (PullObserver observer : observers) {
            observer.update(this);
        }
    }
    
    @Override
    public String getCurrentStatus() {
        return status;
    }
    
    @Override
    public Map<String, Object> getAllData() {
        return new HashMap<>(dataMap);
    }
    
    @Override
    public Object getSpecificData(String key) {
        return dataMap.get(key);
    }
    
    public void updateWeatherData(SensorData data) {
        this.currentData = data;
        this.status = "ONLINE";
        
        // Update data map
        dataMap.put("temperature", data.getTemperature());
        dataMap.put("humidity", data.getHumidity());
        dataMap.put("pressure", data.getPressure());
        dataMap.put("location", data.getLocation());
        dataMap.put("timestamp", data.getTimestamp());
        dataMap.put("full_sensor_data", data);
        
        System.out.println("\n--- " + stationName + " (Pull): Weather data updated ---");
        System.out.println("New data: " + data);
        
        // Only notify that something changed - observers pull what they need
        notifyObservers();
    }
    
    public SensorData getCurrentData() {
        return currentData;
    }
}

// PUSH MODEL Observers
class PushTemperatureDisplay implements PushObserver {
    private String displayName;
    private Double lastTemperature;
    
    public PushTemperatureDisplay(String name) {
        this.displayName = name;
    }
    
    @Override
    public void update(String data, Object additionalInfo) {
        if ("temperature".equals(data) && additionalInfo instanceof Double) {
            lastTemperature = (Double) additionalInfo;
            System.out.println(displayName + " (Push): Temperature updated to " + lastTemperature + "°C");
            displayTemperature();
        } else if ("full_data".equals(data) && additionalInfo instanceof SensorData) {
            SensorData sensorData = (SensorData) additionalInfo;
            lastTemperature = sensorData.getTemperature();
            System.out.println(displayName + " (Push): Received full data, extracting temperature: " + lastTemperature + "°C");
        }
    }
    
    private void displayTemperature() {
        if (lastTemperature != null) {
            String status = lastTemperature > 25 ? "HOT" : lastTemperature < 10 ? "COLD" : "COMFORTABLE";
            System.out.println(displayName + " (Push): Displaying " + status + " temperature");
        }
    }
}

class PushAllDataLogger implements PushObserver {
    private String loggerName;
    private List<String> logs;
    
    public PushAllDataLogger(String name) {
        this.loggerName = name;
        this.logs = new ArrayList<>();
    }
    
    @Override
    public void update(String data, Object additionalInfo) {
        String logEntry = String.format("[%s] %s: %s", 
            System.currentTimeMillis(), data, additionalInfo);
        logs.add(logEntry);
        System.out.println(loggerName + " (Push): Logged - " + data + " = " + additionalInfo);
    }
    
    public void showLogs() {
        System.out.println(loggerName + " (Push) logs:");
        logs.forEach(log -> System.out.println("  " + log));
    }
}

// PULL MODEL Observers
class PullTemperatureDisplay implements PullObserver {
    private String displayName;
    
    public PullTemperatureDisplay(String name) {
        this.displayName = name;
    }
    
    @Override
    public void update(PullSubject subject) {
        System.out.println(displayName + " (Pull): Received notification, pulling temperature data...");
        
        // Pull only the data we need
        Object tempData = subject.getSpecificData("temperature");
        if (tempData instanceof Double) {
            double temperature = (Double) tempData;
            System.out.println(displayName + " (Pull): Retrieved temperature: " + temperature + "°C");
            displayTemperature(temperature);
        }
    }
    
    private void displayTemperature(double temperature) {
        String status = temperature > 25 ? "HOT" : temperature < 10 ? "COLD" : "COMFORTABLE";
        System.out.println(displayName + " (Pull): Displaying " + status + " temperature");
    }
}

class PullSelectiveMonitor implements PullObserver {
    private String monitorName;
    private Set<String> interestedData;
    
    public PullSelectiveMonitor(String name, String... dataTypes) {
        this.monitorName = name;
        this.interestedData = new HashSet<>(Arrays.asList(dataTypes));
    }
    
    @Override
    public void update(PullSubject subject) {
        System.out.println(monitorName + " (Pull): Received notification, checking status and pulling selective data...");
        
        String status = subject.getCurrentStatus();
        System.out.println(monitorName + " (Pull): Subject status: " + status);
        
        if ("ONLINE".equals(status)) {
            // Pull only the data we're interested in
            for (String dataType : interestedData) {
                Object data = subject.getSpecificData(dataType);
                if (data != null) {
                    System.out.println(monitorName + " (Pull): Retrieved " + dataType + ": " + data);
                    processData(dataType, data);
                }
            }
        }
    }
    
    private void processData(String dataType, Object data) {
        switch (dataType) {
            case "humidity":
                if (data instanceof Double && (Double) data > 80) {
                    System.out.println(monitorName + " (Pull): 💧 HIGH HUMIDITY ALERT!");
                }
                break;
            case "pressure":
                if (data instanceof Double && (Double) data < 1000) {
                    System.out.println(monitorName + " (Pull): 🌀 LOW PRESSURE ALERT!");
                }
                break;
        }
    }
}

// Demo class
public class PushPullObserver {
    public static void main(String[] args) {
        System.out.println("=== Push vs Pull Observer Pattern Demo ===");
        
        // PUSH MODEL Demonstration
        System.out.println("\n1. PUSH MODEL - Subject sends specific data to observers");
        System.out.println("-------------------------------------------------------");
        
        PushWeatherStation pushStation = new PushWeatherStation("Downtown Weather Station");
        
        // Create push observers
        PushTemperatureDisplay pushTempDisplay = new PushTemperatureDisplay("Temperature Display (Push)");
        PushAllDataLogger pushLogger = new PushAllDataLogger("Data Logger (Push)");
        
        // Add observers
        pushStation.addObserver(pushTempDisplay);
        pushStation.addObserver(pushLogger);
        
        // Update weather data (push model)
        pushStation.updateWeatherData(new SensorData("Downtown", 22.5, 65.0, 1013.2));
        pushStation.updateWeatherData(new SensorData("Downtown", 28.8, 70.0, 1010.5));
        
        // PULL MODEL Demonstration
        System.out.println("\n\n2. PULL MODEL - Observers request specific data they need");
        System.out.println("--------------------------------------------------------");
        
        PullWeatherStation pullStation = new PullWeatherStation("Airport Weather Station");
        
        // Create pull observers
        PullTemperatureDisplay pullTempDisplay = new PullTemperatureDisplay("Temperature Display (Pull)");
        PullSelectiveMonitor pullMonitor = new PullSelectiveMonitor("Environmental Monitor (Pull)", 
                                                                   "humidity", "pressure", "location");
        
        // Add observers
        pullStation.addObserver(pullTempDisplay);
        pullStation.addObserver(pullMonitor);
        
        // Update weather data (pull model)
        pullStation.updateWeatherData(new SensorData("Airport", 18.2, 85.0, 995.8));
        pullStation.updateWeatherData(new SensorData("Airport", 15.5, 75.0, 1005.2));
        
        // Show push logger data
        System.out.println("\n3. Push model logger history:");
        pushLogger.showLogs();
        
        // Comparison
        System.out.println("\n=== Push vs Pull Model Comparison ===");
        System.out.println("\nPUSH MODEL:");
        System.out.println("✓ Subject controls what data is sent");
        System.out.println("✓ Efficient if observers need most/all data");
        System.out.println("✗ Less flexible - observers get what subject decides to send");
        System.out.println("✗ Tight coupling between subject and observer data needs");
        System.out.println("✗ More network traffic if observers don't need all data");
        
        System.out.println("\nPULL MODEL:");
        System.out.println("✓ Observers control what data they request");
        System.out.println("✓ Flexible - observers can request only what they need");
        System.out.println("✓ Loose coupling - subject doesn't need to know observer needs");
        System.out.println("✓ More efficient if observers need different subsets of data");
        System.out.println("✗ More method calls - each observer makes separate requests");
        System.out.println("✗ Potential for inconsistent data if state changes between requests");
        
        System.out.println("\n=== Best Practices ===");
        System.out.println("• Use PUSH when observers typically need most of the data");
        System.out.println("• Use PULL when observers need different subsets of data");
        System.out.println("• Consider hybrid approaches for complex scenarios");
        System.out.println("• PULL is generally more flexible and loosely coupled");
    }
} 
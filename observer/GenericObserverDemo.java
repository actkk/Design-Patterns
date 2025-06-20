/**
 * Generic Observer Pattern Implementation
 * 
 * This demonstrates a type-safe observer pattern using Java generics.
 * It allows for different types of data to be passed to observers
 * and provides compile-time type safety.
 */

import java.util.*;

// Generic Observer interface
interface GenericObserver<T> {
    void update(T data);
}

// Generic Subject interface
interface GenericSubject<T> {
    void addObserver(GenericObserver<T> observer);
    void removeObserver(GenericObserver<T> observer);
    void notifyObservers(T data);
}

// Stock data class
class StockData {
    private String symbol;
    private double price;
    private double change;
    private long timestamp;
    
    public StockData(String symbol, double price, double change) {
        this.symbol = symbol;
        this.price = price;
        this.change = change;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public String getSymbol() { return symbol; }
    public double getPrice() { return price; }
    public double getChange() { return change; }
    public long getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("%s: $%.2f (%.2f%s)", 
            symbol, price, Math.abs(change), change >= 0 ? "↑" : "↓");
    }
}

// Weather data class
class WeatherData {
    private double temperature;
    private double humidity;
    private double pressure;
    private String location;
    
    public WeatherData(String location, double temperature, double humidity, double pressure) {
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
    }
    
    // Getters
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public double getPressure() { return pressure; }
    public String getLocation() { return location; }
    
    @Override
    public String toString() {
        return String.format("%s: %.1f°C, %.1f%% humidity, %.1f hPa", 
            location, temperature, humidity, pressure);
    }
}

// Generic concrete subject
class GenericDataPublisher<T> implements GenericSubject<T> {
    private List<GenericObserver<T>> observers;
    private String publisherName;
    
    public GenericDataPublisher(String name) {
        this.observers = new ArrayList<>();
        this.publisherName = name;
    }
    
    @Override
    public void addObserver(GenericObserver<T> observer) {
        observers.add(observer);
        System.out.println(publisherName + ": Observer added. Total: " + observers.size());
    }
    
    @Override
    public void removeObserver(GenericObserver<T> observer) {
        observers.remove(observer);
        System.out.println(publisherName + ": Observer removed. Total: " + observers.size());
    }
    
    @Override
    public void notifyObservers(T data) {
        System.out.println(publisherName + ": Notifying " + observers.size() + " observers");
        for (GenericObserver<T> observer : observers) {
            observer.update(data);
        }
    }
    
    public void publishData(T data) {
        System.out.println("\n--- " + publisherName + ": Publishing data ---");
        System.out.println("Data: " + data);
        notifyObservers(data);
    }
}

// Concrete observers for stock data
class StockDisplay implements GenericObserver<StockData> {
    private String displayName;
    private List<StockData> stockHistory;
    
    public StockDisplay(String name) {
        this.displayName = name;
        this.stockHistory = new ArrayList<>();
    }
    
    @Override
    public void update(StockData stockData) {
        stockHistory.add(stockData);
        System.out.println(displayName + " received stock update: " + stockData);
        displayCurrentPrice(stockData);
    }
    
    private void displayCurrentPrice(StockData data) {
        String trend = data.getChange() >= 0 ? "UP" : "DOWN";
        System.out.println(displayName + " displaying: " + data.getSymbol() + 
                          " trend is " + trend);
    }
    
    public void showHistory() {
        System.out.println(displayName + " stock history:");
        for (int i = 0; i < stockHistory.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + stockHistory.get(i));
        }
    }
}

class StockAlert implements GenericObserver<StockData> {
    private String alertName;
    private double alertThreshold;
    
    public StockAlert(String name, double threshold) {
        this.alertName = name;
        this.alertThreshold = threshold;
    }
    
    @Override
    public void update(StockData stockData) {
        System.out.println(alertName + " received stock update: " + stockData);
        checkAlert(stockData);
    }
    
    private void checkAlert(StockData data) {
        if (Math.abs(data.getChange()) >= alertThreshold) {
            System.out.println("🚨 " + alertName + " ALERT: " + data.getSymbol() + 
                              " changed by " + data.getChange() + "%!");
        }
    }
}

// Concrete observers for weather data
class WeatherDisplay implements GenericObserver<WeatherData> {
    private String displayName;
    
    public WeatherDisplay(String name) {
        this.displayName = name;
    }
    
    @Override
    public void update(WeatherData weatherData) {
        System.out.println(displayName + " received weather update: " + weatherData);
        displayWeather(weatherData);
    }
    
    private void displayWeather(WeatherData data) {
        String condition = data.getTemperature() > 25 ? "HOT" : 
                          data.getTemperature() < 10 ? "COLD" : "MODERATE";
        System.out.println(displayName + " showing: " + condition + " weather in " + data.getLocation());
    }
}

class WeatherAlert implements GenericObserver<WeatherData> {
    private String alertName;
    private double tempThreshold;
    
    public WeatherAlert(String name, double tempThreshold) {
        this.alertName = name;
        this.tempThreshold = tempThreshold;
    }
    
    @Override
    public void update(WeatherData weatherData) {
        System.out.println(alertName + " received weather update: " + weatherData);
        checkWeatherAlert(weatherData);
    }
    
    private void checkWeatherAlert(WeatherData data) {
        if (data.getTemperature() > tempThreshold) {
            System.out.println("🌡️ " + alertName + " HIGH TEMP ALERT: " + 
                              data.getTemperature() + "°C in " + data.getLocation());
        }
        if (data.getTemperature() < 0) {
            System.out.println("❄️ " + alertName + " FREEZE ALERT: " + 
                              data.getTemperature() + "°C in " + data.getLocation());
        }
    }
}

// Demo class
public class GenericObserverDemo {
    public static void main(String[] args) {
        System.out.println("=== Generic Observer Pattern Demo ===");
        
        // Stock data demonstration
        System.out.println("\n1. Stock Data Observer Pattern:");
        GenericDataPublisher<StockData> stockPublisher = new GenericDataPublisher<>("Stock Exchange");
        
        // Create stock observers
        StockDisplay tradingScreen = new StockDisplay("Trading Screen");
        StockDisplay mobileApp = new StockDisplay("Mobile Trading App");
        StockAlert volatilityAlert = new StockAlert("Volatility Alert System", 5.0);
        
        // Add observers
        stockPublisher.addObserver(tradingScreen);
        stockPublisher.addObserver(mobileApp);
        stockPublisher.addObserver(volatilityAlert);
        
        // Publish stock data
        stockPublisher.publishData(new StockData("AAPL", 150.25, 2.5));
        stockPublisher.publishData(new StockData("GOOGL", 2750.80, -1.2));
        stockPublisher.publishData(new StockData("TSLA", 850.00, 8.7)); // Should trigger alert
        
        // Weather data demonstration
        System.out.println("\n\n2. Weather Data Observer Pattern:");
        GenericDataPublisher<WeatherData> weatherPublisher = new GenericDataPublisher<>("Weather Station");
        
        // Create weather observers
        WeatherDisplay weatherApp = new WeatherDisplay("Weather App");
        WeatherDisplay tvStation = new WeatherDisplay("TV Weather Station");
        WeatherAlert tempAlert = new WeatherAlert("Temperature Alert System", 30.0);
        
        // Add observers
        weatherPublisher.addObserver(weatherApp);
        weatherPublisher.addObserver(tvStation);
        weatherPublisher.addObserver(tempAlert);
        
        // Publish weather data
        weatherPublisher.publishData(new WeatherData("New York", 25.5, 65.0, 1013.2));
        weatherPublisher.publishData(new WeatherData("Phoenix", 35.2, 25.0, 1010.5)); // Should trigger alert
        weatherPublisher.publishData(new WeatherData("Alaska", -5.0, 80.0, 1020.1)); // Should trigger freeze alert
        
        // Show trading screen history
        System.out.println("\n3. Trading screen history:");
        tradingScreen.showHistory();
        
        // Remove an observer and publish more data
        System.out.println("\n4. Removing mobile app observer:");
        stockPublisher.removeObserver(mobileApp);
        stockPublisher.publishData(new StockData("MSFT", 300.15, 1.8));
        
        // Demonstrate type safety
        System.out.println("\n=== Generic Observer Benefits ===");
        System.out.println("✓ Type Safety: Compile-time checking of data types");
        System.out.println("✓ Reusability: Same pattern for different data types");
        System.out.println("✓ Flexibility: Easy to add new data types");
        System.out.println("✓ Maintainability: Clear separation of concerns");
        
        // Note: The following would cause compile-time errors:
        // stockPublisher.addObserver(weatherApp); // Type mismatch!
        // weatherPublisher.addObserver(tradingScreen); // Type mismatch!
        System.out.println("✓ Prevented type mismatches at compile time!");
    }
} 
/**
 * Basic Observer Pattern Implementation
 * 
 * This demonstrates the classic observer pattern where observers are notified
 * when the subject's state changes. The pattern defines a one-to-many dependency
 * between objects.
 */

import java.util.*;

// Observer interface
interface Observer {
    void update(String message);
}

// Subject interface
interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}

// Concrete Subject
class NewsAgency implements Subject {
    private List<Observer> observers;
    private String news;
    
    public NewsAgency() {
        this.observers = new ArrayList<>();
    }
    
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
        System.out.println("Observer added. Total observers: " + observers.size());
    }
    
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
        System.out.println("Observer removed. Total observers: " + observers.size());
    }
    
    @Override
    public void notifyObservers() {
        System.out.println("Notifying " + observers.size() + " observers...");
        for (Observer observer : observers) {
            observer.update(news);
        }
    }
    
    public void setNews(String news) {
        System.out.println("\n--- NewsAgency: Setting news ---");
        this.news = news;
        notifyObservers();
    }
    
    public String getNews() {
        return news;
    }
}

// Concrete Observers
class NewsChannel implements Observer {
    private String name;
    private String latestNews;
    
    public NewsChannel(String name) {
        this.name = name;
    }
    
    @Override
    public void update(String message) {
        this.latestNews = message;
        System.out.println(name + " received news update: \"" + message + "\"");
        broadcast();
    }
    
    private void broadcast() {
        System.out.println(name + " is broadcasting: " + latestNews);
    }
    
    public String getName() {
        return name;
    }
}

class OnlinePortal implements Observer {
    private String name;
    private String latestNews;
    
    public OnlinePortal(String name) {
        this.name = name;
    }
    
    @Override
    public void update(String message) {
        this.latestNews = message;
        System.out.println(name + " received news update: \"" + message + "\"");
        publishOnline();
    }
    
    private void publishOnline() {
        System.out.println(name + " published online: " + latestNews);
    }
    
    public String getName() {
        return name;
    }
}

class MobileApp implements Observer {
    private String name;
    private String latestNews;
    private List<String> newsHistory;
    
    public MobileApp(String name) {
        this.name = name;
        this.newsHistory = new ArrayList<>();
    }
    
    @Override
    public void update(String message) {
        this.latestNews = message;
        this.newsHistory.add(message);
        System.out.println(name + " received news update: \"" + message + "\"");
        sendPushNotification();
    }
    
    private void sendPushNotification() {
        System.out.println(name + " sent push notification: " + latestNews);
    }
    
    public void showHistory() {
        System.out.println(name + " news history:");
        for (int i = 0; i < newsHistory.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + newsHistory.get(i));
        }
    }
    
    public String getName() {
        return name;
    }
}

// Demo class
public class BasicObserver {
    public static void main(String[] args) {
        System.out.println("=== Basic Observer Pattern Demo ===");
        
        // Create the subject (news agency)
        NewsAgency newsAgency = new NewsAgency();
        
        // Create observers
        NewsChannel cnn = new NewsChannel("CNN");
        NewsChannel bbc = new NewsChannel("BBC");
        OnlinePortal yahoo = new OnlinePortal("Yahoo News");
        MobileApp newsApp = new MobileApp("News Mobile App");
        
        System.out.println("\n1. Adding observers to news agency:");
        newsAgency.addObserver(cnn);
        newsAgency.addObserver(bbc);
        newsAgency.addObserver(yahoo);
        newsAgency.addObserver(newsApp);
        
        // Publish some news
        System.out.println("\n2. Publishing first news:");
        newsAgency.setNews("Breaking: New technology breakthrough announced!");
        
        System.out.println("\n3. Publishing second news:");
        newsAgency.setNews("Sports: Championship finals this weekend!");
        
        // Remove an observer
        System.out.println("\n4. Removing BBC observer:");
        newsAgency.removeObserver(bbc);
        
        System.out.println("\n5. Publishing third news (BBC won't receive this):");
        newsAgency.setNews("Weather: Storm warning issued for coastal areas!");
        
        // Show mobile app history
        System.out.println("\n6. Mobile app news history:");
        newsApp.showHistory();
        
        // Add new observer
        System.out.println("\n7. Adding new observer:");
        OnlinePortal reddit = new OnlinePortal("Reddit News");
        newsAgency.addObserver(reddit);
        
        System.out.println("\n8. Publishing final news:");
        newsAgency.setNews("Economy: Stock market reaches new high!");
        
        // Demonstrate observer pattern benefits
        System.out.println("\n=== Observer Pattern Benefits Demo ===");
        System.out.println("✓ Loose coupling: Subject doesn't know concrete observer types");
        System.out.println("✓ Dynamic relationships: Can add/remove observers at runtime");
        System.out.println("✓ Broadcast communication: One subject can notify many observers");
        System.out.println("✓ Open/Closed principle: Can add new observer types without modifying subject");
    }
} 
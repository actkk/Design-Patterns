# Singleton Design Pattern Implementation in Java

This directory contains various implementations of the Singleton design pattern in Java, demonstrating different approaches, their advantages, disadvantages, and use cases.

## What is the Singleton Pattern?

The Singleton pattern ensures that a class has only one instance throughout the application lifecycle and provides a global point of access to that instance. It's useful for:
- Configuration objects
- Logging services
- Database connections
- Thread pools
- Cache managers

## Implementations Included

### 1. BasicSingleton.java
**Classic Singleton Implementation**

```java
public static BasicSingleton getInstance() {
    if (instance == null) {
        instance = new BasicSingleton();
    }
    return instance;
}
```

**Pros:**
- Simple and straightforward
- Lazy initialization (created only when needed)

**Cons:**
- Not thread-safe
- Can create multiple instances in multi-threaded environment

**Use Case:** Single-threaded applications or when thread safety is not a concern.

### 2. ThreadSafeSingleton.java
**Thread-Safe Singleton using Synchronized Method**

```java
public static synchronized ThreadSafeSingleton getInstance() {
    if (instance == null) {
        instance = new ThreadSafeSingleton();
    }
    return instance;
}
```

**Pros:**
- Thread-safe
- Lazy initialization
- Simple implementation

**Cons:**
- Performance overhead due to synchronization
- Every call to getInstance() is synchronized

**Use Case:** Multi-threaded applications where performance is not critical.

### 3. DoubleCheckedLockingSingleton.java
**Double-Checked Locking Pattern**

```java
public static DoubleCheckedLockingSingleton getInstance() {
    if (instance == null) {
        synchronized (DoubleCheckedLockingSingleton.class) {
            if (instance == null) {
                instance = new DoubleCheckedLockingSingleton();
            }
        }
    }
    return instance;
}
```

**Pros:**
- Thread-safe
- Better performance than synchronized method
- Lazy initialization
- Minimal synchronization overhead

**Cons:**
- Complex implementation
- Requires volatile keyword for instance variable

**Use Case:** High-performance multi-threaded applications where getInstance() is called frequently.

### 4. EagerSingleton.java
**Eager Initialization Singleton**

```java
private static final EagerSingleton INSTANCE = new EagerSingleton();

public static EagerSingleton getInstance() {
    return INSTANCE;
}
```

**Pros:**
- Thread-safe (JVM handles thread safety during class loading)
- Simple implementation
- No synchronization overhead

**Cons:**
- No lazy initialization (instance created at class loading)
- May waste memory if instance is never used

**Use Case:** When you're sure the singleton will be used and memory is not a concern.

### 5. BillPughSingleton.java
**Bill Pugh Solution (Initialization-on-demand holder idiom)**

```java
private static class SingletonHelper {
    private static final BillPughSingleton INSTANCE = new BillPughSingleton();
}

public static BillPughSingleton getInstance() {
    return SingletonHelper.INSTANCE;
}
```

**Pros:**
- Thread-safe without synchronization
- Lazy initialization
- High performance
- Simple and clean

**Cons:**
- Slightly more complex than basic implementations

**Use Case:** Recommended approach for most applications requiring lazy initialization.

### 6. EnumSingleton.java
**Enum-based Singleton (Recommended)**

```java
public enum EnumSingleton {
    INSTANCE;
    
    // methods here
}
```

**Pros:**
- Thread-safe by default
- Serialization-safe by default
- Reflection-safe by default
- Very concise
- Lazy initialization
- JVM guarantees

**Cons:**
- Less flexible than class-based approach
- Cannot extend other classes

**Use Case:** **Recommended approach** for most singleton implementations. Best overall solution.

## Running the Examples

Each implementation includes a `main` method with comprehensive demonstrations. To run any example:

```bash
# Compile
javac singelton/BasicSingleton.java
javac singelton/ThreadSafeSingleton.java
javac singelton/DoubleCheckedLockingSingleton.java
javac singelton/EagerSingleton.java
javac singelton/BillPughSingleton.java
javac singelton/EnumSingleton.java

# Run
java singelton.BasicSingleton
java singelton.ThreadSafeSingleton
java singelton.DoubleCheckedLockingSingleton
java singelton.EagerSingleton
java singelton.BillPughSingleton
java singelton.EnumSingletonDemo
```

## Comparison Table

| Implementation | Thread Safe | Lazy Loading | Performance | Complexity | Recommended |
|----------------|-------------|--------------|-------------|------------|-------------|
| Basic | ❌ | ✅ | High | Low | ❌ |
| Synchronized | ✅ | ✅ | Poor | Low | ❌ |
| Double-Checked | ✅ | ✅ | Good | High | ⚠️ |
| Eager | ✅ | ❌ | High | Low | ⚠️ |
| Bill Pugh | ✅ | ✅ | High | Medium | ✅ |
| Enum | ✅ | ✅ | High | Low | ✅ |

## Best Practices

1. **Use Enum Singleton for most cases** - It's the most robust implementation
2. **Use Bill Pugh for class-based singletons** - When you need more flexibility than enums
3. **Avoid Basic Singleton** - Unless you're certain about single-threaded usage
4. **Consider Eager Initialization** - When the singleton will definitely be used
5. **Be careful with Serialization** - Implement `readResolve()` method for class-based singletons
6. **Watch out for Reflection attacks** - Enum singletons are naturally protected

## Common Pitfalls

1. **Forgetting thread safety** in multi-threaded applications
2. **Synchronization overhead** affecting performance
3. **Serialization issues** creating multiple instances
4. **Reflection attacks** bypassing singleton constraints
5. **Testing difficulties** due to global state

## Testing Singleton Classes

```java
// Reset singleton for testing (only for testing purposes)
// Note: This breaks the singleton pattern and should only be used in tests
Field instanceField = SingletonClass.class.getDeclaredField("instance");
instanceField.setAccessible(true);
instanceField.set(null, null);
```

## Alternatives to Singleton

Consider these alternatives before implementing singleton:
- **Dependency Injection** frameworks (Spring, Guice)
- **Factory Pattern** with instance management
- **Service Locator Pattern**
- **Static classes** for stateless utilities

## Conclusion

The **Enum Singleton** is generally the best choice for implementing the Singleton pattern in Java due to its built-in thread safety, serialization safety, and reflection safety. Use **Bill Pugh Singleton** when you need class-based inheritance or more complex initialization logic.

Remember that singletons can make testing difficult and create tight coupling. Consider whether you really need a singleton or if dependency injection might be a better solution for your use case. 
# Requirements 
- Java 8+
- Gradle 6+

# Build

```
RSA_Project $ ./gradlew build
```

# Run 

Run with gradle 

```
# for rsa generation CLI
RSA_Project $ ./gradlew :rsa:run --args=""
# for client and server
RSA_Project $ ./gradlew :client:run --args=""
RSA_Project $ ./gradlew :server:run --args=""
```

Or go to build\distributions folder for each subproject there will be each 
subproject compiled and ready

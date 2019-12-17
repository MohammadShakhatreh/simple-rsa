# Requirements 
- Java 8+
- Gradle 6+

# Build

```bash
$ ./gradlew build
```

# Run 

Run with gradle 

```
# for rsa generation CLI
$ ./gradlew :rsa:run --args="filename"
# for client and server
$ ./gradlew :client:run --args="host port username"
$ ./gradlew :server:run --args="port"
```

Or go to build\distributions folder for each subproject there will be each 
subproject compiled and ready

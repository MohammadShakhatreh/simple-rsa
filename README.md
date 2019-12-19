# Requirements 
- Java 8+
- Gradle 6+

# Build

To build all 3 sub projects (rsa, client, server)
```bash
$ ./gradlew build
```

# Demo

This will create a demo directory with the three projects in it
```bash
$ ./demo.sh

# To run rsa project to generate keys
$ cd rsa/bin
$ ./rsa filename

# the keys should be in /bin directory of each project

# To run the client
$ cd ./client/bin
$ ./client host port username

# To run the server
$ cd ./server/bin
$ ./server port
```

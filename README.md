to compile

```
$ javac -d . --source-path src src/com/rsa/*
$ jar -cv -m META-INF/MANIFEST.MF -f rsa.jar com/rsa/*
```

to run 

```
$ java -jar rsa.jar
```

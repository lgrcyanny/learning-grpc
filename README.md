# Introduction

A simple grpc demo in scala
I came across some issues when build this simple demo and solved it, so I want to share this demo.

+ grpc version: 1.16.1 [grpc-java](https://github.com/grpc/grpc-java)
+ protobuf version: 3.5.1
+ jdk 1.8
+ guava 20.0

# Usage

1. compile

```
mvn clean package -DskipTests
```

2. starting server

```
java -cp target/learning-grpc-1.0-SNAPSHOT.jar com.cyanny.learning.grpc.HelloServer
```
you will get output:
Server started, listening on 8088

3. starting client

```
java -cp target/learning-grpc-1.0-SNAPSHOT.jar com.cyanny.learning.grpc.HelloClient
```

you will get output like this:
Got response: message: "Got message: ping: 1"
, time taken: 498ms
Got response: message: "Got message: ping: 2"
, time taken: 3ms
...
Got response: message: "Got message: ping: 9999"
, time taken: 0ms
Got response: message: "Got message: ping: 10000"
, time taken: 1ms
done time taken: 5719ms

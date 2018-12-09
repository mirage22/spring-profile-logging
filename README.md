## Spring-Profile-Logging example

[![Build Status](https://travis-ci.org/mirage22/spring-profile-logging.svg?branch=master)](https://travis-ci.org/mirage22/spring-profile-logging)


**spring-profile-logging** is the collection of the simple spring-boot based 
applications (consumer, producer). Both apps send data to the jfrTracer (Jaeger, Zipkin) and 
can be connected with Java Mission Control and Flight Recorder.
Each part of project can be run locally, Docker containers or docker-compose file can be used. 

#### Available Tracers
Both tracers are configured on standard default ports
- Jaeger 
- Zipkin 

## Building
The project uses the Gradle build system. Application is running on OpenJDK 11 and above.
```bash
$ ./gradlew build
```
##### Building a docker images
The both spring-boot applications can be run inside the separate Docker containers. You can build 
them by following commands

```bash
$ docker build -f ./docker/Dockerfile_gradle_boot_producer -t tracing-producer .
```

```bash
$ docker build -f ./docker/Dockerfile_gradle_boot_consumer -t tracing-consumer .
```

## Running from the IDE
Import the project as the Gradle one to your favorite IDE. From the IDE is possible to execute:
- ConsumerApp
- ProducerApp

## Running the Docker-Compose
The docker-compose can be run by the following command. 
```bash
$ docker-compose -f ./docker/docker-compose.yml up
```
To run this command it's required to have prepared the Docker images: `tracing-consumer` and `tracing-producer`.

## Running local Jaeger and Zipkin Tracer
Both tracers can be run inside the separate docker containers 

##### Jeager in docker
```bash
$ docker run -d -p 6831:6831/udp -p 16686:16686 jaegertracing/all-in-one:latest
```

##### Zipkin in docker
```bash
$ docker run -d -p 9411:9411 openzipkin/zipkin:latest
```

## Monitoring and Alerting

####  Prometheus in docker 
```bash
$ docker run --name prometheus -d -p 127.0.0.1:9090:9090 prom/prometheus
```

default port is configured to 9000

custom configuration: it's necessary to edit /etc/prometheus/prometheus.yml

#### Grafana in docker 
```bash
$ docker run -d -p 3000:3000 grafana/grafana
```

link: http://localhost:3000 pass: admin/admin



#### Valuable Resources
- JDK Mission Control Tutorial : [Here](https://github.com/thegreystone/jmc-tutorial/tree/master/projects) 

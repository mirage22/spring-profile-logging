## Spring-Profile-Logging example

**spring-profile-logging** is the collection of the simple spring-boot based 
applications (consumer, producer). Those apps help with utilizing open-tracing 
standard or profiling by using Java Mission Control together with Flight Recorder. 
The Collection can be run inside the docker container. 
The collection does contains two tracers: Jaeger and Zipkin in default setup.
<UNDER CONTRUCTION>

## Building
The application uses the gradle build system. Application is running on OpenJDK 11.
<UNDER CONTRUCTION>

## Running the collection
The collection can be imported to your favorite IDE. From the IDE is possible to execute:
- ConsumerApp
- ProducerApp
<UNDER CONTRUCTION>

## Jaeger and Zipkin Tracer
Both tracers can be run inside the separate docker containers 

#### Jeager in docker
```bash
$docker run -d -p 6831:6831/udp -p 16686:16686 jaegertracing/all-in-one:latest
```

#### Zipkin in docker
```bash
$docker run -d -p 9411:9411 openzipkin/zipkin
```
<UNDER CONTRUCTION>

#### Valuable Resources
- JDK Mission Control Tutorial : [Here](https://github.com/thegreystone/jmc-tutorial/tree/master/projects) 

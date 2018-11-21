## Spring-Profile-Logging example

**spring-profile-logging** is the collection of the simple spring-boot based applications.
They help with utilizing open-tracing standard or profiling by using Java Mission 
Control together with Flight Recorder. Collection can be run inside the docker container.
<UNDER CONTRUCTION>

## Building
The application uses the gradle build system.
<UNDER CONTRUCTION>


## Jaeger and Zipkin Tracer
Both tracers are running inside the docker container 
```bash
$docker run -d -p 6831:6831/udp -p 16686:16686 jaegertracing/all-in-one:latest
```
<UNDER CONTRUCTION>

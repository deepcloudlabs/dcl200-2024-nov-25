# JVM Monitoring

1. `JDK_HOME\bin`
2. CLI vs GUI  
   - **CLI**: `jcmd`, `jps`, `jstack`, `jmap`,...  
   - **GUI**: `jconsole` -> JMX/MBean Console  
3. Client-Server -> Remote Monitoring  
4. JMX (Java Management eXtension) and MBeans  
    - i. Metric  
    - ii. Command  
    - iii. Observer/Event-Driven  
    - MBean Development  
5. JFR (JDK/JVM Flight Recorder)  
    - Custom Event  
6. VisualVM (NetBeans RCP)  
    - Monitoring, Profiling  
        - i. Sampling  
        - ii. Instrumentation  
    - MBean Console  
7. JMC (Oracle, Mission Control)  
    - i. JFR  
    - ii. Monitoring, MBean Console
    
# G1GC Configuration and Tuning: Throughput <--> Latency

```bash
-XX:+UseG1GC
-Xms4g -Xmx8g
-XX:MaxGCPauseMillis=50ms
-XX:G1HeapRegionSize=4m
```
increasing regions size -> GC Pause Time
                                     decreaseses overhead due to RSet, CSet          
```bash
-XX:InitiatingHeapOccupancyPercent=45
```
Marking Phase -> Decreasing the value -> decreaseses FullGC
                 increases # of Minor GC
```bash
-XX:+StringDeduplication
-XX:+PrintStringDeduplicationStatistics
```
use it if you %10 gain

```bash
-Xlog:gc+heap+stats,age*=debug:file=c:/tmp/myapp.log:time,uptime,level,tags			 
```
Humoungous: size(Object) >= RegionSize/2

# Automatic G1GC Configuration
```bash
java -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -XX:+UseAdaptiveSizePolicy \
     -XX:+G1UseAdaptiveIHOP \
     -Xlog:gc*:file=gc.log:time,uptime,level,tags \
     -jar your-application.jar
```
# Software Architecture

## 1. Process-Thread
   - **I. Single Process Multiple Thread**
   - **II. Multiple Process**
   - **III. Multiple Process Multiple Thread**
       - RMI
       - SOAP WS, RESTful WS, gRPC, ...

## 2. Modularity
   - OSGi
   - JBoss 6 -> MSC
   - Since Java SE 9+
       1. Platform
       2. Application

## 3. Reactive Systems -> Reactive Programming
   - Since Java SE 9

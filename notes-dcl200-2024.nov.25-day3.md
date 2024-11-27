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
     -XX:+ParallelRefProcEnabled \
     -Xlog:gc*:file=gc.log:time,uptime,level,tags \
     -jar your-application.jar
```
**-XX:+ParallelRefProcEnabled**: Enables parallel processing for reference objects, reducing latency during garbage collection
# G1GC Improvements for Large Heaps
In **Java 23**, **G1GC** handles large heaps (*up to* **16 TB**) more efficiently with less fragmentation and better pause time consistency. 

When the JVM allocates heap memory, it reserves the memory but doesn't immediately map it to physical memory. By default, physical memory pages are only allocated when the JVM writes to them for the first time during runtime (this is called "demand paging").

With **-XX:+AlwaysPreTouch**, the JVM proactively touches (writes to) every page of the heap during startup. This forces the operating system to allocate physical memory for the heap in advance.

For applications with very large heaps:
```bash
-XX:+AlwaysPreTouch
```

### Benefits of **-XX:+AlwaysPreTouch**
- **Reduced Runtime Latency**:
Pre-touching ensures that memory pages are already backed by physical memory, avoiding the overhead of demand paging during application runtime. This eliminates potential page faults, which can cause unpredictable pauses.
- **Heap Initialization Verification**:
By pre-touching the heap, the JVM verifies at startup that the operating system can actually allocate the requested amount of memory. This can prevent situations where an application starts successfully but fails later due to insufficient memory.
- **Improved Performance for Large Heaps**:
For applications with large heaps (e.g., >10GB), demand paging can cause significant latency during the first GC cycles. Pre-touching ensures that this latency is handled upfront, making GC behavior more predictable.

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

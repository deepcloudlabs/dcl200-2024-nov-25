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

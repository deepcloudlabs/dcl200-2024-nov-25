
# Java Performance and Monitoring

## Scheduler Overhead Example

```java
public class SchedulerOverhead {
    public static void main(String[] args) throws InterruptedException {
        var begin = System.currentTimeMillis();
        for (int i = 0; i < 2_000; ++i) {
            Thread.sleep(2);
        }
        var end = System.currentTimeMillis();
        System.out.println("Millis elapsed: " + (end - begin));
        System.out.println("Overhead      : " + (end - begin) / 4_000.0);
    }
}
```

### Results
#### For Windows:
- **Millis elapsed:** 5502 ms  
- **Overhead:** 1.3755  

#### For Ubuntu:
- **Millis elapsed:** 4226 ms  
- **Overhead:** 1.0565  

---

## Exercise01: Lock-Based Counter Increment

```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Exercise01 {
    private static final int THREAD_COUNT = 10;
    private static final int INCREMENT_COUNT = 1_000_000_000;
    private static int counter = 0;
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < INCREMENT_COUNT; j++) {
                    incrementCounter();
                }
            });
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            // Wait for all tasks to complete
        }

        System.out.println("Final counter value: " + counter);
    }

    private static void incrementCounter() {
        lock.lock();
        try {
            counter++;
        } finally {
            lock.unlock();
        }
    }
}
```

### Lock-Based Performance Results
```shell
/usr/bin/time -v java Exercise01
Final counter value: 1410065408
        Command being timed: "java Exercise01"
        User time (seconds): 719.14
        System time (seconds): 182.91
        Percent of CPU this job got: 311%
        Elapsed (wall clock) time (h:mm:ss or m:ss): 4:49.98
        Maximum resident set size (kbytes): 347660
        Voluntary context switches: 90309083
        Involuntary context switches: 1265
        Exit status: 0
```

---

## Exercise02: Lock-Free Atomic Counter Increment

```java
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Exercise02 {
    private static final int THREAD_COUNT = 10;
    private static final int INCREMENT_COUNT = 1_000_000_000;
    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < INCREMENT_COUNT; j++) {
                    incrementCounter();
                }
            });
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
            // Wait for all tasks to complete
        }

        System.out.println("Final counter value: " + counter.get());
    }

    private static void incrementCounter() {
         counter.incrementAndGet();
    }
}
```

### Lock-Free Performance Results
```shell
/usr/bin/time -v java Exercise02
Final counter value: 1410065408
        Command being timed: "java Exercise02"
        User time (seconds): 1748.05
        Percent of CPU this job got: 977%
        Elapsed (wall clock) time (h:mm:ss or m:ss): 2:58.82
        Maximum resident set size (kbytes): 40460
        Voluntary context switches: 5033
        Involuntary context switches: 2023
        Exit status: 0
```

---

## OS Monitoring

### Key Concepts
- **Dev, Test, Prod Environments**  
- **Periodic Sampling**  
- **System Resource Utilization**: CPU, GPU, Memory, IO (Disk, Network)  
- **System Capacity**  
  - `lscpu`  
  - `lsmem`  
  - `/proc/meminfo`  
  - `/proc/cpuinfo`  

### OS Monitoring Tools
1. `vmstat`  
2. `top`  

#### `vmstat` Metrics
- **CPU Usage**  
  - `us`: User time (process/thread)  
  - `sy`: System time (kernel)  
  - `id`: Idle time  
  - `wa`: Waiting for IO  
  - `st`: Stolen time (ideally 0)  

---

## Serialization Solutions
- [Protocol Buffers](https://protobuf.dev/)  
- [Apache Parquet](https://parquet.apache.org/)  

---

## JVM Monitoring Tools
 *Non-interactive**: `jps`, `jcmd`, `jstack`, etc.  
 **GUI Tools**: VisualVM, `jconsole`, JMC  
 **Remote Monitoring**: `jps`  
---
### Example: Running `jstatd`
```bash
jstatd -p 4000 -J-Djava.security.policy=jstatd.all.policy -J-Djava.rmi.server.hostname=192.168.1.104
```

```bash
jcmd 53328 help
53328:
The following commands are available:
Compiler.CodeHeap_Analytics
Compiler.codecache
Compiler.codelist
Compiler.directives_add
Compiler.directives_clear
Compiler.directives_print
Compiler.directives_remove
Compiler.queue
GC.class_histogram
GC.class_stats
GC.finalizer_info
GC.heap_dump
GC.heap_info
GC.run
GC.run_finalization
JFR.check
JFR.configure
JFR.dump
JFR.start
JFR.stop
JVMTI.agent_load
JVMTI.data_dump
ManagementAgent.start
ManagementAgent.start_local
ManagementAgent.status
ManagementAgent.stop
Thread.print
VM.check_commercial_features
VM.class_hierarchy
VM.classloader_stats
VM.classloaders
VM.command_line
VM.dynlibs
VM.flags
VM.info
VM.log
VM.metaspace
VM.native_memory
VM.print_touched_methods
VM.set_flag
VM.stringtable
VM.symboltable
VM.system_properties
VM.systemdictionary
VM.unlock_commercial_features
VM.uptime
VM.version
help

This lists the commands available for a given process ID (PID).
```

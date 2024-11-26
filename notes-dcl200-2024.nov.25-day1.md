# JVM Modes and Compilers

## Mixed Mode
- **Default Mode**: `-Xmixed`

## Compiled Mode
- **Flag**: `-Xcomp`

## Interpreted Mode
- **Flag**: `-Xinterp`

---

## JIT Compilers
1. **Client VM (C1)**: Default for 32-bit JVMs  
   - Flag: `-client`

2. **Server VM (C2)**: Default for 64-bit JVMs  
   - Flag: `-server`  
   - **Note**: 64-bit JVMs always use Server VM; `-client` has no effect.

---

## Tiered Compiler (Since Java SE 7)
- **Flag**: `-XX:+TieredCompiler`  
- **Tiers**:
  - **T0**: Interpreted  
  - **T1**: C1 Simple  
  - **T2**: C1 Intermediate  
  - **T3**: C1 Full  
  - **T4**: C2  

```java
public class A {
  public void fun() {} // T0 -> T3 -> T4
}
```

# JVM Options

```bash
java -XX:+UnlockDiagnosticVMOptions -XX:+PrintFlagsFinal -version > c:\tmp\jdk-23-flags.txt
java -XX:+UnlockDiagnosticVMOptions -XX:+PrintCompilation -jar c:\tmp\Java2D.jar
java -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -jar c:\tmp\Java2D.jar
java -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:+PrintFlagsFinal -version > c:\tmp\jdk-23-experimental-flags.txt
```

# Compilation Techniques
1. AOT
2. AOT + PGO (Profile-Guided Optimizations)
3. JIT: Server VM (C2), Client VM (C1)
4. Native Compilation: GraalVM
   
5. Since Java 23: GraalVM JIT Compiler (Experimental)
```bash
java -XX:+UnlockExperimentalVMOptions -XX:+UseGraalJIT
```

# Native Image Compilation with GraalVM

## Example code for GraalVM and native-image

```java
import java.util.concurrent.ThreadLocalRandom;

public class App {
  public static void main(String[] args) {
    ThreadLocalRandom.current()
                     .ints(1, 60)
                     .distinct()
                     .limit(6)
                     .sorted()
                     .boxed()
                     .forEach(System.out::println);
  }
}
```

## Build Process:

1. Basic Command
```bash
  native-image --gc=epsilon -cp App.jar App
========================================================================================================================
GraalVM Native Image: Generating 'app' (executable)...
========================================================================================================================
[1/8] Initializing...                                                                                    (3.5s @ 0.12GB)
 Java version: 23.0.1+11, vendor version: Oracle GraalVM 23.0.1+11.1
 Graal compiler: optimization level: 2, target machine: x86-64-v3, PGO: ML-inferred
 C compiler: gcc (linux, x86_64, 11.4.0)
 Garbage collector: Epsilon GC (max heap size: 80% of RAM)
 1 user-specific feature(s):
 - com.oracle.svm.thirdparty.gson.GsonFeature
------------------------------------------------------------------------------------------------------------------------
Build resources:
 - 12.44GB of memory (81.6% of 15.25GB system memory, determined at start)
 - 16 thread(s) (100.0% of 16 available processor(s), determined at start)
[2/8] Performing analysis...  [*****]                                                                    (3.2s @ 0.20GB)
    2,189 reachable types   (60.1% of    3,644 total)
    1,917 reachable fields  (38.1% of    5,034 total)
    9,314 reachable methods (35.7% of   26,068 total)
      783 types,     9 fields, and    96 methods registered for reflection
       49 types,    34 fields, and    48 methods registered for JNI access
        4 native libraries: dl, pthread, rt, z
[3/8] Building universe...                                                                               (0.7s @ 0.24GB)
[4/8] Parsing methods...      [*]                                                                        (0.9s @ 0.26GB)
[5/8] Inlining methods...     [***]                                                                      (0.2s @ 0.26GB)
[6/8] Compiling methods...    [***]                                                                      (6.0s @ 0.36GB)
[7/8] Laying out methods...   [*]                                                                        (0.9s @ 0.41GB)
[8/8] Creating image...       [*]                                                                        (0.8s @ 0.43GB)
   2.65MB (38.37%) for code area:     4,368 compilation units
   3.50MB (50.71%) for image heap:   54,366 objects and 52 resources
 771.91kB (10.92%) for other data
   6.90MB in total
------------------------------------------------------------------------------------------------------------------------
Top 10 origins of code area:                                Top 10 object types in image heap:
   1.45MB java.base                                          711.32kB byte[] for code metadata
 965.11kB svm.jar (Native Image)                             704.12kB byte[] for java.lang.String
  81.16kB com.oracle.svm.svm_enterprise                      380.46kB java.lang.Class
  32.31kB jdk.proxy2                                         375.17kB heap alignment
  30.94kB org.graalvm.nativeimage.base                       365.67kB java.lang.String
  25.00kB jdk.graal.compiler                                 147.25kB java.util.HashMap$Node
  23.92kB jdk.proxy1                                         114.52kB char[]
  18.19kB org.graalvm.collections                            102.61kB com.oracle.svm.core.hub.DynamicHubCompanion
  13.82kB jdk.internal.vm.ci                                  87.47kB java.lang.Object[]
   7.62kB jdk.proxy3                                          86.32kB byte[] for reflection metadata
   1.98kB for 4 more packages                                509.09kB for 521 more object types
                            Use '--emit build-report' to create a report with more details.
------------------------------------------------------------------------------------------------------------------------
Security report:
 - Binary includes Java deserialization.
 - Use '--enable-sbom' to assemble a Software Bill of Materials (SBOM).
------------------------------------------------------------------------------------------------------------------------
Recommendations:
 PGO:  Use Profile-Guided Optimizations ('--pgo') for improved throughput.
 HEAP: Set max heap for improved and more predictable memory usage.
 CPU:  Enable more CPU features with '-march=native' for improved performance.
 QBM:  Use the quick build mode ('-Ob') to speed up builds during development.
------------------------------------------------------------------------------------------------------------------------
                        1.0s (5.9% of total time) in 372 GCs | Peak RSS: 1.02GB | CPU load: 9.28
------------------------------------------------------------------------------------------------------------------------
Build artifacts:
 /home/binkurt/app (executable)
========================================================================================================================
Finished generating 'app' in 17.0s.
```

2. Profile-Guided Optimizations (PGO):
   
```bash
native-image --pgo-instrument -cp App.jar App 
./app -> profile.iprof
native-image --pgo=profile.iprof -cp App.jar App
```

## Optimization:
Use -O3 for aggressive optimizations
```bash
native-image -O3 -cp App.jar App
```

## Performance Metrics

# JVM Result

```bash
/usr/bin/time -v java -cp App.jar App		
7
17
27
36
43
53
        Command being timed: "java -cp App.jar App"
        User time (seconds): 0.09
        System time (seconds): 0.06
        Percent of CPU this job got: 205%
        Elapsed (wall clock) time (h:mm:ss or m:ss): 0:00.07
        Average shared text size (kbytes): 0
        Average unshared data size (kbytes): 0
        Average stack size (kbytes): 0
        Average total size (kbytes): 0
        Maximum resident set size (kbytes): 109888
        Average resident set size (kbytes): 0
        Major (requiring I/O) page faults: 8
        Minor (reclaiming a frame) page faults: 11926
        Voluntary context switches: 1141
        Involuntary context switches: 3
        Swaps: 0
        File system inputs: 0
        File system outputs: 64
        Socket messages sent: 0
        Socket messages received: 0
        Signals delivered: 0
        Page size (bytes): 4096
        Exit status: 0	
```

# Native image Result

```bash
/usr/bin/time -v ./app
10
14
24
26
41
58
        Command being timed: "./app"
        User time (seconds): 0.00
        System time (seconds): 0.00
        Percent of CPU this job got: 50%
        Elapsed (wall clock) time (h:mm:ss or m:ss): 0:00.00
        Average shared text size (kbytes): 0
        Average unshared data size (kbytes): 0
        Average stack size (kbytes): 0
        Average total size (kbytes): 0
        Maximum resident set size (kbytes): 8100
        Average resident set size (kbytes): 0
        Major (requiring I/O) page faults: 0
        Minor (reclaiming a frame) page faults: 393
        Voluntary context switches: 6
        Involuntary context switches: 0
        Swaps: 0
        File system inputs: 0
        File system outputs: 0
        Socket messages sent: 0
        Socket messages received: 0
        Signals delivered: 0
        Page size (bytes): 4096
        Exit status: 0		
```
        
# POG Result

```bash
/usr/bin/time -v ./app
3
30
36
37
54
58
        Command being timed: "./app"
        User time (seconds): 0.00
        System time (seconds): 0.00
        Percent of CPU this job got: 50%
        Elapsed (wall clock) time (h:mm:ss or m:ss): 0:00.00
        Average shared text size (kbytes): 0
        Average unshared data size (kbytes): 0
        Average stack size (kbytes): 0
        Average total size (kbytes): 0
        Maximum resident set size (kbytes): 9484
        Average resident set size (kbytes): 0
        Major (requiring I/O) page faults: 0
        Minor (reclaiming a frame) page faults: 374
        Voluntary context switches: 5
        Involuntary context switches: 0
        Swaps: 0
        File system inputs: 0
        File system outputs: 0
        Socket messages sent: 0
        Socket messages received: 0
        Signals delivered: 0
        Page size (bytes): 4096
        Exit status: 0		
```

## CRaC (Coordinated Restore at Checkpoint)

### Project Page
  [CRaC OpenJDK](https://openjdk.org/projects/crac/)

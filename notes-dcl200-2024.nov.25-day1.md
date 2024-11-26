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

# Build Process:

1. Basic Command
 ```bash
  native-image --gc=epsilon -cp App.jar App
 ```
2. Profile-Guided Optimizations (PGO):
   ```bash
native-image --pgo-instrument -cp App.jar App 
./app -> profile.iprof
native-image --pgo=profile.iprof -cp App.jar App
 ```

# Optimization:
Use -O3 for aggressive optimizations
  ```bash
native-image -O3 -cp App.jar App
  ```

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

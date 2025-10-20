# Java

## JDK Evolution: Key Features from 8 → 25

### ☕ JDK 8 (LTS, 2014)

- Lambda Expressions & Functional Interfaces
  <br>
  _Enable passing behavior (functions) as parameters._

  ```java
  List<String> names = List.of("Ann", "Bob", "Chris");
  names.forEach(n -> System.out.println(n.toUpperCase()));
  ```

- Streams API (java.util.stream)
  <br>
  _Declarative processing of collections._

  ```java
  long count = names.stream()
  .filter(n -> n.startsWith("C"))
  .count(); // 1
  ```

- New Date & Time API (java.time)
  _Immutable and timezone-safe date/time classes._
  ```java
  LocalDate today = LocalDate.now();
  LocalDate nextWeek = today.plusWeeks(1);
  ```
- Default & Static Methods in Interfaces
  _Allow methods with bodies in interfaces._
  ```java
  interface Logger {
  default void log(String msg) { System.out.println(msg); }
  }
  ```
- Repeating & Type Annotations
  Annotate types or repeat annotations.
  ```java
  @Schedule(day="Mon") @Schedule(day="Tue")
  void meeting() {}
  ```
- Removal of PermGen → Metaspace
  Improves memory management for class metadata. (No code example; JVM change)
- Stronger Defaults in Cryptography / TLS
  TLS 1.2 enabled by default; stricter key handling.

### ☕ JDK 9 (2017)

- Java Platform Module System (JPMS)
  _Supports modular applications._
  ```java
  // module-info.java
  module com.example.app {
  requires java.sql;
  exports com.example.app.core;
  }
  ```
- jlink – Create custom runtime images
  ```bash
  jlink --module-path mods --add-modules com.example.app --output runtime
  ```
- JShell (REPL)
  _Interactive evaluation for quick experimentation._
  ```bash
  $ jshell
  jshell> System.out.println("Hello, JShell!");
  ```
- Improved Process API
  _Simpler process handling._
  ```java
  ProcessHandle.current().info().command().ifPresent(System.out::println);
  ```
- VarHandles & Unified Logging
  _Low-level atomic access; configurable JVM logging system._
  (Mostly JVM-level; rarely used in app code)

### ☕ JDK 11 (LTS, 2018)

- Standardized HTTP Client (HTTP/2)
  _Replaces HttpURLConnection._
  ```java
  var client = HttpClient.newHttpClient();
  var req = HttpRequest.newBuilder(URI.create("https://example.com")).build();
  var res = client.send(req, HttpResponse.BodyHandlers.ofString());
  System.out.println(res.body());
  ```
- New String Methods
  ```java
  " Java ".strip(); // "Java"
  "".isBlank(); // true
  "one\ntwo".lines().count(); // 2
  ```
- Nest-Based Access Control
  _Improves access among nested classes — automatic, no syntax change._
- ZGC (Experimental)
  _Low-latency garbage collector. (Runtime feature)_
- Removed: Applet, JavaFX, WebStart
  _Decoupled from JDK — now separate modules._

### ☕ JDK 17 (LTS, 2021)

- Sealed Classes & Interfaces
  _Restrict which classes can extend a type._
  ```java
  sealed class Shape permits Circle, Square {}
  final class Circle extends Shape {}
  final class Square extends Shape {}
  ```
- Pattern Matching for instanceof
  _Simplifies type checks._
  ```java
  if (obj instanceof String s) {
  System.out.println(s.toUpperCase());
  }
  ```
- Enhanced PRNG API
  _New random number generators._
  ```java
  var rng = RandomGenerator.of("L64X128MixRandom");
  System.out.println(rng.nextInt(10));
  ```
- Foreign Function & Memory API (Preview)
  _Access native code safely without JNI._
  ```java
  // Pseudo-example (API evolving)
  try (Arena arena = Arena.ofConfined()) {
  MemorySegment seg = arena.allocate(100);
  }
  ```
- Stronger Encapsulation
  _Internal JDK APIs hidden unless explicitly opened via command line._

### ☕ JDK 21 (LTS, 2023)

- Record Patterns
  _Deconstruct records directly in code._
  ```java
  record Point(int x, int y) {}
  static void print(Point p) {
  if (p instanceof Point(int x, int y))
  System.out.println(x + ", " + y);
  }
  ```
- Pattern Matching for switch
  _More expressive control flow._
  ```java
  static String format(Object o) {
  return switch (o) {
  case Integer i -> "int " + i;
  case String s -> "str " + s;
  default -> "unknown";
  };
  }
  ```
- Sequenced Collections
  _Order-aware collections with first() / last()._
  ```java
  SequencedCollection<String> sc = new ArrayList<>(List.of("A", "B", "C"));
  System.out.println(sc.first()); // A
  System.out.println(sc.last()); // C
  ```
- Virtual Threads (Project Loom)
  _Lightweight threads for massive concurrency._
  ```java
  try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
  executor.submit(() -> System.out.println(Thread.currentThread()));
  }
  ```
- String Templates (Preview)
  _Inline expressions inside strings._
  ```java
  String name = "Alice";
  String msg = STR."Hello, \{name.toUpperCase()}!";
  ```
- Unnamed Classes & Instance Main (Preview)
  _Less boilerplate for small programs._
  ```java
  void main() {
  System.out.println("Hello, world!");
  }
  ```
- Scoped Values (Preview)
  _Safe alternative to ThreadLocal._
  ```java
  static final ScopedValue<String> USER = ScopedValue.newInstance();
  ScopedValue.where(USER, "admin").run(() -> System.out.println(USER.get()));
  ```
- Generational ZGC
  _Improved GC efficiency for generational heaps._
- Key Encapsulation Mechanism (KEM) API
  _New cryptographic standard API._
  ```java
  KEM kem = KEM.getInstance("DHKEM");
  ```

### ☕ JDK 25 (LTS, 2025)

- Structured Concurrency (Preview)
  _Treats a group of related tasks as one logical operation — simplifies cancellation and error handling._
  ```java
  try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
  Future<String> user = scope.fork(() -> fetchUser());
  Future<String> posts = scope.fork(() -> fetchPosts());
  scope.join(); // wait for both
  scope.throwIfFailed(); // rethrow errors
  System.out.println(user.result() + posts.result());
  }
  ```
- Scoped Values (Final)
  _A safer, faster alternative to ThreadLocal for sharing immutable context data._
  ```java
  static final ScopedValue<String> REQ_ID = ScopedValue.newInstance();
  ScopedValue.where(REQ_ID, "abc123").run(() ->
  System.out.println("Request " + REQ_ID.get())
  );
  ```
- Primitive Patterns (Preview)
  _Extends pattern matching to primitive types (int, long, etc.)._
  ```java
  Object value = 42;
  switch (value) {
  case int i -> System.out.println("int: " + i);
  case double d -> System.out.println("double: " + d);
  default -> System.out.println("other");
  }
  ```
- Compact Object Headers (Preview)
  _Optimizes JVM object header layout to cut memory use and boost GC efficiency._
  (JVM-level change — no direct code example.)
- Vector API (Incubator)
  _Explicit SIMD operations for high-performance numerical and data processing._
  ```java
  VectorSpecies<Float> SPEC = FloatVector.SPECIES_PREFERRED;
  float[] a = {1,2,3,4}, b = {5,6,7,8}, c = new float[4];
  FloatVector va = FloatVector.fromArray(SPEC, a, 0);
  FloatVector vb = FloatVector.fromArray(SPEC, b, 0);
  va.add(vb).intoArray(c, 0); // [6,8,10,12]
  ```
- Key Derivation Function (KDF) API
  _Provides standard algorithms for deriving cryptographic keys from shared secrets or passwords._
  ```java
  KeyDerivationFunction kdf = KeyDerivationFunction.getInstance("HKDF");
  SecretKey key = kdf.deriveKey(secret, params);
  ```
- Generational Shenandoah GC
  _Adds a generational mode to Shenandoah GC for better throughput and pause times._
  (Runtime-level enhancement.)
- Module Import Declarations (Preview)
  _Lets you explicitly import entire modules in source files._
  ```java
  import module com.example.utils;
  ```
- Flexible Constructors (Preview)
  _Relaxes rules on constructor super()/this() calls, making initialization order more flexible._
  ```java
  class Box {
  final int size;
  Box(int s) { if (s < 0) throw new IllegalArgumentException(); this.size = s; }
  }
  ```
- Ahead-of-Time (AOT) Enhancements
  _Improved ergonomics and profiling for faster startup and optimized builds._
  ```bash
  java --aot Main.java
  ```

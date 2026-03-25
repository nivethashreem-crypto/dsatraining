# CollectionsAndStreams.java — Detailed Explanation

> **File:** `CollectionsAndStreams.java`
> **Run:** `javac CollectionsAndStreams.java && java CollectionsAndStreams`
> **Topics:** Collections framework architecture, Iterator, fail-fast vs fail-safe, Comparable vs Comparator, Collections utility, Stream pipeline, intermediate and terminal operations, map vs flatMap, reduce, functional interfaces, method references, Collectors, Optional, parallel streams, real-world pipelines, common mistakes, performance

---

## Table of Contents

1. [What is the Collections Framework?](#1-what-is-the-collections-framework)
2. [Program Entry Point](#2-program-entry-point)
3. [Section 1 — Collections Framework Overview](#3-section-1--collections-framework-overview)
4. [Section 2 — Iterable and Iterator](#4-section-2--iterable-and-iterator)
5. [Section 3 — Fail-Fast vs Fail-Safe](#5-section-3--fail-fast-vs-fail-safe)
6. [Section 4 — Comparable vs Comparator](#6-section-4--comparable-vs-comparator)
7. [Section 5 — Collections Utility Class](#7-section-5--collections-utility-class)
8. [Section 6 — Stream Pipeline Basics](#8-section-6--stream-pipeline-basics)
9. [Section 7 — Intermediate Operations](#9-section-7--intermediate-operations)
10. [Section 8 — Terminal Operations](#10-section-8--terminal-operations)
11. [Section 9 — map vs flatMap](#11-section-9--map-vs-flatmap)
12. [Section 10 — Reduce and Collect](#12-section-10--reduce-and-collect)
13. [Section 11 — Functional Interfaces](#13-section-11--functional-interfaces)
14. [Section 12 — Method References](#14-section-12--method-references)
15. [Section 13 — Collectors](#15-section-13--collectors)
16. [Section 14 — Optional](#16-section-14--optional)
17. [Section 15 — Parallel Streams](#17-section-15--parallel-streams)
18. [Section 16 — Real World: Log Processing](#18-section-16--real-world-log-processing)
19. [Section 17 — Real World: Sales Pipeline](#19-section-17--real-world-sales-pipeline)
20. [Section 18 — Common Mistakes](#20-section-18--common-mistakes)
21. [Section 19 — Performance Comparison](#21-section-19--performance-comparison)
22. [Section 20 — Interview Summary](#22-section-20--interview-summary)
23. [Key Takeaways](#23-key-takeaways)

---

## 1. What is the Collections Framework?

The Java Collections Framework is a unified architecture for storing, manipulating, and accessing groups of objects. It has three pillars:

- **Interfaces** — abstract types (`List`, `Set`, `Queue`, `Map`)
- **Implementations** — concrete classes (`ArrayList`, `HashSet`, `HashMap`)
- **Algorithms** — static methods in `java.util.Collections`

### Full interface hierarchy

```
java.lang.Iterable<T>
  └── java.util.Collection<T>
        ├── List<T>         ordered, indexed, duplicates allowed
        │     ├── ArrayList        — resizable array, O(1) get
        │     └── LinkedList       — doubly linked, O(1) head/tail
        ├── Set<T>          no duplicates
        │     ├── HashSet          — hash table, O(1), no order
        │     ├── LinkedHashSet    — hash + linked list, insertion order
        │     └── TreeSet          — Red-Black Tree, O(log n), sorted
        └── Queue<T>        ordered for processing
              ├── LinkedList       — FIFO queue
              ├── PriorityQueue    — min-heap (always dequeues smallest)
              └── Deque<T>         — double-ended
                    └── ArrayDeque — resizable circular array, faster than LinkedList

java.util.Map<K,V>  (NOT a Collection — separate hierarchy)
  ├── HashMap              — hash table, O(1), no order
  ├── TreeMap              — Red-Black Tree, O(log n), sorted keys
  ├── LinkedHashMap        — hash + linked, insertion or access order
  └── ConcurrentHashMap    — thread-safe, O(1)
```

---

## 2. Program Entry Point

```java
public static void main(String[] args) throws InterruptedException {
    section1_CollectionsFrameworkOverview();
    // ... 20 sections
}
```

`throws InterruptedException` is declared because section 15 uses `Thread.join()`. Each section is independent — comment out all but one during focused study.

---

## 3. Section 1 — Collections Framework Overview

### List — ordered, indexed, duplicates allowed

```java
List<String> list = new ArrayList<>(List.of("Banana", "Apple", "Cherry", "Apple"));
list.get(1);   // "Apple" — O(1) direct index access
```

Order of insertion is preserved. Duplicates ("Apple" appears twice) are both stored.

### Set — no duplicates

```java
Set<String> hashSet = new HashSet<>(list);    // {"Apple", "Banana", "Cherry"} — no order
Set<String> treeSet = new TreeSet<>(list);    // [Apple, Banana, Cherry] — sorted
```

Both reject the second "Apple". The difference is iteration order: HashSet is unordered, TreeSet is always alphabetically sorted.

### Queue — FIFO (first-in, first-out)

```java
Queue<String> queue = new LinkedList<>(List.of("Task1", "Task2", "Task3"));
queue.poll();  // removes and returns "Task1" (head)
```

`poll()` removes the head (oldest element). `offer()` adds to the tail. `peek()` inspects head without removal.

### Deque — double-ended queue

```java
Deque<String> deque = new ArrayDeque<>();
deque.addFirst("A");  // adds to head
deque.addLast("C");   // adds to tail
```

`ArrayDeque` is backed by a circular resizable array — faster than `LinkedList` for both queue and stack operations because of CPU cache locality.

### PriorityQueue — always dequeues the smallest element

```java
PriorityQueue<Integer> pq = new PriorityQueue<>(List.of(5, 1, 3, 2, 4));
pq.poll();  // 1 — minimum first (min-heap by default)
```

Internally a min-heap: the root is always the minimum. For a max-heap: `new PriorityQueue<>(Comparator.reverseOrder())`.

---

## 4. Section 2 — Iterable and Iterator

### The Iterable contract

Every `Collection` implements `java.lang.Iterable<T>`, which has exactly one method:

```java
Iterator<T> iterator();
```

This single method is what enables the for-each loop:

```java
for (T item : collection) { ... }
// Desugars to:
Iterator<T> it = collection.iterator();
while (it.hasNext()) {
    T item = it.next();
    ...
}
```

### Iterator methods

```java
boolean hasNext()   // true if more elements remain
T       next()      // returns current element AND advances cursor
void    remove()    // removes the element last returned by next() — safe removal
```

`remove()` is the only safe way to remove an element from a collection during iteration. It removes the element that was returned by the most recent `next()` call, and updates the internal `modCount` to keep it in sync with the iterator's `expectedModCount`.

### ListIterator — extended for List only

```java
ListIterator<String> lit = list.listIterator();
```

`ListIterator` extends `Iterator` with:

| Method | Description |
|---|---|
| `hasPrevious()` | true if there is a previous element |
| `previous()` | returns previous element, moves cursor back |
| `nextIndex()` | index of element that `next()` would return |
| `previousIndex()` | index of element that `previous()` would return |
| `add(E e)` | inserts element at current position |
| `set(E e)` | replaces the element last returned by `next()` or `previous()` |

The `set()` method allows in-place replacement during iteration — something the basic `Iterator` doesn't support.

### Cursor position

The ListIterator cursor sits **between** elements, not on them:

```
Elements:  [ A ]  [ B ]  [ C ]  [ D ]
Positions: 0     1     2     3     4
                    ↑
                 cursor here
          previous()=B    next()=C
```

`nextIndex()` returns 2 and `previousIndex()` returns 1 when the cursor is at position 2.

---

## 5. Section 3 — Fail-Fast vs Fail-Safe

### How fail-fast works

Every structurally modifiable `java.util` collection maintains an internal `modCount` (modification count). The count increments on any structural change (add, remove, clear — not set/update).

When you create an iterator:
```java
Iterator<Integer> it = list.iterator();
// Internally: this.expectedModCount = list.modCount;
```

On every `next()` call, the iterator checks:
```java
if (modCount != expectedModCount)
    throw new ConcurrentModificationException();
```

This is a **best-effort** safeguard. It catches the bug during development but is not guaranteed to trigger in every case (e.g., in concurrent contexts it might miss the change).

### The three correct removal approaches

**Iterator.remove()** — classic, works in Java 7+:
```java
Iterator<Integer> it = list.iterator();
while (it.hasNext()) {
    if (it.next() == 3) it.remove(); // updates modCount + expectedModCount atomically
}
```

**removeIf(Predicate)** — Java 8+, most concise:
```java
list.removeIf(v -> v == 3); // internally uses iterator, but in bulk
```

**stream().filter().collect()** — functional, creates a new list:
```java
list = list.stream().filter(v -> v != 3).collect(Collectors.toList());
```

### Fail-safe: CopyOnWriteArrayList

```java
List<Integer> failSafe = new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4, 5));
```

On every write (`add`, `remove`, `set`), `CopyOnWriteArrayList` copies the entire internal array. The iterator works on the snapshot captured at iterator creation time — it never sees concurrent writes and never throws `ConcurrentModificationException`.

```
Write operation:
  1. Copy array: oldArray → newArray (O(n))
  2. Modify newArray
  3. Set internal array reference to newArray

Existing iterators:
  Still hold reference to oldArray snapshot — unaffected
```

Trade-off: reads are O(1) with no locking; writes are O(n) with full array copy. Use only for small, read-heavy, rarely-written lists (e.g., event listener lists).

---

## 6. Section 4 — Comparable vs Comparator

### Comparable — natural ordering, built into the class

```java
record Employee(...) implements Comparable<Employee> {
    @Override
    public int compareTo(Employee other) {
        return this.name.compareTo(other.name);  // natural order = alphabetical name
    }
}
```

`Comparable` defines exactly ONE natural ordering for the class. Used automatically by `Collections.sort()`, `Arrays.sort()`, `TreeSet`, and `TreeMap` with no extra configuration.

`compareTo()` contract:
- Negative: `this` comes before `other`
- Zero: considered equal (should be consistent with `equals()`)
- Positive: `this` comes after `other`

### Comparator — external ordering, can have many

```java
Comparator<Employee> bySalaryDesc =
    Comparator.comparingInt(Employee::salary).reversed();
```

`Comparator` lives outside the class and defines an alternative ordering. You can have as many as you need. Pass it to `Collections.sort(list, comp)`, `list.sort(comp)`, `new TreeSet<>(comp)`, or `new TreeMap<>(comp)`.

### Chaining with `thenComparing`

```java
Comparator<Employee> byDeptThenSalary =
    Comparator.comparing(Employee::dept)           // primary: department alphabetically
              .thenComparingInt(Employee::salary)  // secondary: salary numerically
              .reversed();                         // flip both
```

`thenComparing` is only invoked when the primary comparator returns zero. The `.reversed()` at the end flips the entire composed comparator — not just the last part.

### `Comparator.nullsFirst` and `nullsLast`

```java
list.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
```

When sorting a list that may contain null values, `nullsFirst` moves nulls to the beginning and `nullsLast` moves them to the end. Without this wrapper, a null in the list causes a `NullPointerException` during comparison.

---

## 7. Section 5 — Collections Utility Class

`java.util.Collections` provides static algorithms that operate on any `Collection` or `List`. It is the equivalent of a standard library for collections.

### Key methods

| Method | Description | Complexity |
|---|---|---|
| `sort(list)` | Natural order sort (TimSort) | O(n log n) |
| `sort(list, comp)` | Custom sort | O(n log n) |
| `binarySearch(list, key)` | Find index in sorted list | O(log n) |
| `reverse(list)` | Reverse element order | O(n) |
| `shuffle(list)` | Random permutation | O(n) |
| `rotate(list, n)` | Shift elements right by n positions | O(n) |
| `min(collection)` | Minimum element | O(n) |
| `max(collection)` | Maximum element | O(n) |
| `frequency(coll, obj)` | Count occurrences of obj | O(n) |
| `disjoint(c1, c2)` | True if no common elements | O(n) |
| `swap(list, i, j)` | Swap two elements | O(1) |
| `fill(list, obj)` | Replace all elements | O(n) |
| `copy(dest, src)` | Copy src into dest | O(n) |
| `nCopies(n, obj)` | Immutable list of n copies | O(1) |
| `unmodifiableList(list)` | Immutable view wrapper | O(1) |
| `synchronizedList(list)` | Synchronized wrapper | O(1) |
| `emptyList()` | Empty immutable list | O(1) |
| `singletonList(obj)` | One-element immutable list | O(1) |

### `binarySearch` requirements

`Collections.binarySearch()` requires the list to be **sorted** before calling it. The result is undefined on an unsorted list. If the key appears multiple times, there is no guarantee which occurrence's index is returned.

### `rotate()` example

```java
Collections.rotate(List.of(1, 2, 3, 4, 5), 2);
// Result: [4, 5, 1, 2, 3]
```

Positive distance shifts right (elements wrap from end to beginning). Negative distance shifts left.

### `unmodifiableList` vs `List.of()`

Both produce non-modifiable lists, but:
- `unmodifiableList(list)` wraps an existing list — if the underlying list changes, the view reflects those changes (it is a view, not a copy)
- `List.of(...)` creates a truly immutable independent list — no changes ever

---

## 8. Section 6 — Stream Pipeline Basics

### What a Stream is and is not

| | Collection | Stream |
|---|---|---|
| Purpose | Store data | Process data |
| Storage | Yes — holds elements | No — traverses a source |
| Reusable | Yes — can iterate multiple times | No — consumed once |
| Lazy | No — data always present | Yes — operations execute on demand |
| Mutates source | N/A | Never — non-interfering |

### The three-part pipeline

```
Source → [Intermediate Op 1] → [Intermediate Op 2] → Terminal Op
```

Nothing executes until the terminal operation is reached. This is **laziness** — it enables short-circuit evaluation and avoids unnecessary work.

### Laziness demonstrated

```java
numbers.stream()
    .filter(n -> { System.out.print("filter(" + n + ") "); return n > 7; })
    .count();
```

Output: `filter(1) filter(2) filter(3) ... filter(10)` — every element is checked.

But with `findFirst()`:
```java
numbers.stream()
    .filter(n -> { System.out.print("check(" + n + ") "); return n > 5; })
    .findFirst();
```

Output: `check(1) check(2) check(3) check(4) check(5) check(6)` — stops at 6, never checks 7–10. This is short-circuit evaluation.

### Primitive streams — avoid boxing

```java
IntStream.rangeClosed(1, 10).sum()     // no Integer objects created
Stream<Integer> version ...           // creates Integer object per element
```

`IntStream`, `LongStream`, and `DoubleStream` operate on primitives directly. Use `mapToInt()`, `mapToLong()`, `mapToDouble()` to convert a `Stream<T>` to a primitive stream. Use `.boxed()` to go back.

### Creating infinite streams

```java
// Stream.iterate — each element computed from previous
Stream.iterate(1, n -> n * 2).limit(8);   // [1, 2, 4, 8, 16, 32, 64, 128]

// Stream.generate — stateless supplier
Stream.generate(Math::random).limit(5);   // 5 random doubles
```

Infinite streams **must** be bounded with `limit()` or a short-circuit terminal (`findFirst`, `anyMatch`, `takeWhile`) before a non-short-circuit terminal. Without a bound, operations like `count()` or `collect()` will run forever.

---

## 9. Section 7 — Intermediate Operations

### Stateless vs stateful — critical for parallel streams

**Stateless** operations process each element independently, with no memory of other elements:
- `filter`, `map`, `flatMap`, `peek`, `mapToInt`
- Safe for parallel streams — each chunk of elements can be processed independently

**Stateful** operations must see multiple elements (or all elements) to produce a result:
- `sorted` — must see all elements before emitting any (sorts the whole stream)
- `distinct` — must remember all seen elements to detect duplicates
- `limit`, `skip` — must count elements

In a sequential stream this is invisible. In parallel, stateful operations impose synchronization overhead — they partially undo the parallelism benefit.

### `sorted()` — full buffering

```java
words.stream().distinct().sorted(Comparator.comparingInt(String::length))
```

`sorted()` must consume the entire stream into an internal buffer before emitting the first sorted element. For large streams, this means holding all elements in memory at once. If you need only the smallest k elements, consider using a `PriorityQueue` instead of sorting.

### `peek()` — for debugging, not for logic

```java
.peek(w -> System.out.print("after-filter: " + w))
```

`peek()` is an intermediate operation that runs a `Consumer` on each element without changing them. It is designed as a debugging tool to inspect elements mid-pipeline. Do not use it for real side effects (like writing to a database) — its execution is not guaranteed in all cases (e.g., it may be skipped if the terminal operation short-circuits).

### `takeWhile` and `dropWhile` — Java 9+

```java
List.of(1, 2, 3, 4, 5, 6).stream().takeWhile(n -> n < 4);  // [1, 2, 3]
List.of(1, 2, 3, 4, 5, 6).stream().dropWhile(n -> n < 4);  // [4, 5, 6]
```

Unlike `filter` which scans the whole stream, `takeWhile` stops at the first element that fails the predicate (short-circuit). This makes it efficient for sorted or ordered streams where all relevant elements are at the front.

### `IntSummaryStatistics` — multiple stats in one pass

```java
IntSummaryStatistics stats = words.stream().mapToInt(String::length).summaryStatistics();
stats.getMin()     // minimum word length
stats.getMax()     // maximum
stats.getAverage() // double average
stats.getSum()     // long sum
stats.getCount()   // long count
```

`summaryStatistics()` computes all five statistics in a single traversal — more efficient than five separate terminal operations.

---

## 10. Section 8 — Terminal Operations

### Short-circuit terminal operations

These stop processing as soon as the answer is known:

| Operation | Stops when |
|---|---|
| `findFirst()` | First element found |
| `findAny()` | Any element found (prefers first in sequential) |
| `anyMatch(pred)` | First element matches |
| `allMatch(pred)` | First element does NOT match |
| `noneMatch(pred)` | First element matches |
| `limit(n)` | n elements have been emitted |

In a sequential stream, `findFirst()` and `findAny()` behave identically. In a parallel stream, `findAny()` is faster because it returns whichever thread finds a match first, without enforcing encounter order.

### `toArray()` with constructor reference

```java
Integer[] arr = stream.toArray(Integer[]::new);
// equivalent to: stream.toArray(size -> new Integer[size])
```

The no-arg `toArray()` returns `Object[]`. The version with a `IntFunction<T[]>` returns a properly typed array — use a constructor reference for clean syntax.

### Consuming a stream twice

```java
Stream<Integer> stream = nums.stream().filter(n -> n > 5);
stream.count();   // consumes the stream
stream.count();   // IllegalStateException: stream has already been operated upon or closed
```

Once any terminal operation is invoked, the stream is in a "consumed" state. You must create a new stream from the source to process it again. Store the *pipeline* (a method that returns a new stream) not the stream object itself.

---

## 11. Section 9 — map vs flatMap

### The core difference

```
Input stream: [A, B, C]

map(fn):
  fn(A) → X      fn(B) → Y      fn(C) → Z
  Output: [X, Y, Z]                    (same number of elements)

flatMap(fn):
  fn(A) → [x1, x2]   fn(B) → [y1]    fn(C) → [z1, z2, z3]
  Output: [x1, x2, y1, z1, z2, z3]   (flattened — total from all)
```

`map` is one-to-one. `flatMap` is one-to-many, and then all "many" results are merged into a single flat stream.

### When `map` returns `Stream` — the problem

```java
// WRONG — map produces Stream<Stream<Integer>>
Stream<Stream<Integer>> bad = nested.stream()
    .map(innerList -> innerList.stream());

// CORRECT — flatMap flattens into Stream<Integer>
Stream<Integer> good = nested.stream()
    .flatMap(innerList -> innerList.stream());
// or: .flatMap(Collection::stream)
```

### Real-world use: sentences → words

```java
List<String> sentences = List.of("hello world", "java is great");

// Each sentence → array of words → flatMap merges all
List<String> words = sentences.stream()
    .flatMap(s -> Arrays.stream(s.split(" ")))
    .distinct()
    .sorted()
    .collect(Collectors.toList());
```

`Arrays.stream(s.split(" "))` produces a `Stream<String>` of words for each sentence. `flatMap` merges all these per-sentence streams into one word stream.

### Real-world use: orders → items

```java
record Order(String id, List<String> items) {}

List<String> allItems = orders.stream()
    .flatMap(o -> o.items().stream())   // Order → Stream<String> of items
    .distinct()
    .sorted()
    .collect(Collectors.toList());
```

This pattern — flattening a list of lists — is the most common use of `flatMap`.

---

## 12. Section 10 — Reduce and Collect

### `reduce` — immutable accumulation

```java
// Two-argument: identity + associative operator
int sum = nums.stream().reduce(0, Integer::sum);

// One-argument: no identity → Optional (empty if stream empty)
Optional<Integer> max = nums.stream().reduce(Integer::max);
```

The `identity` value must satisfy `fn(identity, x) == x` for all x. For addition: `0 + x == x`. For multiplication: `1 * x == x`. Using a wrong identity produces an incorrect result for empty streams.

### Why `collect` is preferred for building collections

`reduce` creates intermediate results by value — for example, to build a list by reduce, it would create a new list on every step:
```
step 1: [] + 1 = [1]        (new list allocated)
step 2: [1] + 2 = [1, 2]    (new list allocated)
step 3: [1, 2] + 3 = [1, 2, 3]  (new list allocated)
...
```

`collect` uses a **mutable result container** that is updated in place:
```
step 1: [1]       ← add to existing list
step 2: [1, 2]    ← add to same list
step 3: [1, 2, 3] ← add to same list
...
```
No intermediate objects are created. This is O(n) time and O(1) allocations vs O(n²) time and O(n) allocations for naive reduce-based list building.

### Prefer `IntStream.sum()` over `reduce()` for numbers

```java
// Slower — boxes int to Integer for each element
int sum = nums.stream().reduce(0, Integer::sum);

// Faster — no boxing
int sum = nums.stream().mapToInt(Integer::intValue).sum();
```

---

## 13. Section 11 — Functional Interfaces

### The four core interfaces

```
java.util.function
  Predicate<T>      T → boolean          test(T t)
  Function<T,R>     T → R                apply(T t)
  Consumer<T>       T → void             accept(T t)
  Supplier<T>       () → T               get()
```

Every lambda you write in a stream pipeline is an instance of one of these (or a variant).

### Predicate composition

```java
Predicate<Integer> isEven     = n -> n % 2 == 0;
Predicate<Integer> isPositive = n -> n > 0;

isEven.and(isPositive)   // both must be true
isEven.or(isPositive)    // at least one must be true
isEven.negate()          // logical NOT
```

`and()`, `or()`, and `negate()` return new `Predicate` objects — they do not modify the originals. This allows building complex predicates from simple reusable building blocks.

### Function composition

```java
Function<String, Integer> strlen = String::length;
Function<Integer, String> toStr  = Object::toString;

// andThen(g): first apply this (f), then apply g → result is g(f(x))
Function<String, String> lenThenStr = strlen.andThen(toStr);
lenThenStr.apply("hello");  // "5"

// compose(g): first apply g, then apply this (f) → result is f(g(x))
// strlen.compose(fn) means fn runs first, strlen runs second
```

`andThen` is `f then g` (left-to-right). `compose` is `g then f` (right-to-left). Use `andThen` for most pipelines as it reads in natural execution order.

### Consumer chaining

```java
Consumer<String> print    = System.out::println;
Consumer<String> printLen = s -> System.out.println("len=" + s.length());

Consumer<String> both = print.andThen(printLen);
both.accept("Java");
// prints: Java
//         len=4
```

`andThen` on `Consumer` runs both consumers in sequence. Both `accept` the same input value.

### `UnaryOperator` and `BinaryOperator`

```java
UnaryOperator<String>   toUpper  = String::toUpperCase;  // T → T
BinaryOperator<Integer> multiply = (a, b) -> a * b;      // (T,T) → T
```

`UnaryOperator<T>` extends `Function<T,T>` — a function from a type to itself. `BinaryOperator<T>` extends `BiFunction<T,T,T>` — combines two values of the same type into one. Both are used extensively in `stream.map()`, `reduce()`, and `Collections` operations.

---

## 14. Section 12 — Method References

### The four types

| Type | Syntax | Lambda equivalent |
|---|---|---|
| Static method | `ClassName::staticMethod` | `(args) -> ClassName.staticMethod(args)` |
| Instance on specific | `instance::method` | `(args) -> instance.method(args)` |
| Instance on arbitrary | `ClassName::instanceMethod` | `(obj) -> obj.method()` |
| Constructor | `ClassName::new` | `(args) -> new ClassName(args)` |

### Type 3 explained — the most confusing

```java
names.stream().map(String::toUpperCase)
// equivalent to:
names.stream().map(s -> s.toUpperCase())
```

`String::toUpperCase` looks like a static method reference but it's not — `toUpperCase()` is an instance method. The difference is that the stream element **itself** is the target object. The element (type `String`) is passed as the implicit `this`. This is why the lambda equivalent is `s -> s.toUpperCase()` not `(s) -> String.toUpperCase(s)`.

### Constructor reference with `Supplier`

```java
Supplier<StringBuilder> sbFactory = StringBuilder::new;
StringBuilder sb = sbFactory.get();  // new StringBuilder()
```

This creates a factory object — a `Supplier` that produces a new `StringBuilder` every time `get()` is called. Useful for dependency injection, lazy initialization, and test fixture setup.

### When method references are better than lambdas

Method references are preferred when:
- The lambda simply delegates to an existing method: `n -> Integer.parseInt(n)` → `Integer::parseInt`
- The intent is clearer with the method name: `s -> s.isEmpty()` → `String::isEmpty`

Lambda is preferred when:
- The method reference would require a temporary variable to work
- The logic involves multiple steps: `s -> s.trim().toLowerCase()`

---

## 15. Section 13 — Collectors

### `groupingBy` — the most powerful Collector

```java
// Basic: Map<K, List<T>>
Collectors.groupingBy(Product::category)

// With downstream: Map<K, Long> (count per group)
Collectors.groupingBy(Product::category, Collectors.counting())

// With downstream: Map<K, Double> (average per group)
Collectors.groupingBy(Product::category, Collectors.averagingDouble(Product::price))

// With downstream: Map<K, List<String>> (map items before grouping)
Collectors.groupingBy(Product::category,
    Collectors.mapping(Product::name, Collectors.toList()))
```

The second parameter (downstream collector) is applied to each group after the grouping. Any Collector can be used as a downstream — including another `groupingBy` for multi-level grouping.

### `partitioningBy` — binary split

```java
Collectors.partitioningBy(p -> p.price() >= 300)
```

Always returns a `Map<Boolean, List<T>>` with exactly two entries. `true` = matches predicate, `false` = doesn't. The downstream collector applies to each partition.

### `toMap` — pitfalls

```java
// Throws IllegalStateException on duplicate keys
Collectors.toMap(Product::name, Product::price)

// Safe — merge function resolves duplicates
Collectors.toMap(
    Product::name,
    Product::price,
    (existing, newVal) -> existing  // keep existing on duplicate
)
```

Always provide a merge function when there is any possibility of duplicate keys. The two-argument `toMap` throws on the first duplicate.

### `joining` — for strings

```java
// Simple concatenation
Collectors.joining()

// With delimiter
Collectors.joining(", ")

// With delimiter, prefix, suffix
Collectors.joining(", ", "[", "]")
// Produces: [Apple, Banana, Cherry]
```

More efficient than `stream.reduce("", (a, b) -> a + ", " + b)` because `joining` uses a `StringBuilder` internally.

### `collectingAndThen` — transform the result

```java
List<String> immutable = stream.collect(
    Collectors.collectingAndThen(
        Collectors.toList(),
        Collections::unmodifiableList  // applied after toList()
    )
);
```

The second argument is a `Function` applied to the result of the first Collector. Useful for wrapping results in an immutable view, or converting to a different type after collection.

### `summarizingDouble` — full statistics in one pass

```java
DoubleSummaryStatistics stats = products.stream()
    .collect(Collectors.summarizingDouble(Product::price));
// stats.getMin(), getMax(), getAverage(), getSum(), getCount()
```

More efficient than five separate streams, since it computes all statistics in a single traversal.

---

## 16. Section 14 — Optional

### Why Optional exists

Before Optional, methods returned `null` to indicate absence. This caused:
- `NullPointerExceptions` when the caller forgot to check
- Ambiguity: does `null` mean "absent" or "error"?
- `if (result != null)` scattered everywhere

`Optional<T>` makes absence **explicit in the return type**. The caller cannot ignore it without deliberately calling `.get()` (which still throws, but the intent is clear).

### Creation

```java
Optional.of(value)           // value must be non-null — NPE if null
Optional.ofNullable(value)   // null-safe — returns empty if value is null
Optional.empty()             // explicitly empty
```

`Optional.of()` is appropriate when null would be a programming error. `Optional.ofNullable()` is for values that might legitimately be absent.

### `orElse` vs `orElseGet` — a performance difference

```java
// orElse: ALWAYS evaluates the default expression, even if present
optional.orElse(expensiveComputation());   // expensiveComputation() runs regardless

// orElseGet: LAZY — only calls supplier if Optional is empty
optional.orElseGet(() -> expensiveComputation());   // only runs if empty
```

For cheap defaults (literals, constants), `orElse` is fine. For expensive computations or object creation, always use `orElseGet`.

### Chaining with `map` and `filter`

```java
Optional<String> upper = present
    .filter(s -> s.length() > 3)    // keeps value if condition true, else empty
    .map(String::toUpperCase);       // transforms if present, else empty
```

This chain eliminates nested null checks. If any step produces "empty", all subsequent steps are skipped. The final result is either the transformed value or empty.

### `Optional.stream()` — bridge to Stream API

```java
List<Optional<String>> optionals = List.of(Optional.of("a"), Optional.empty(), Optional.of("b"));

List<String> present = optionals.stream()
    .flatMap(Optional::stream)   // Optional.stream() = Stream of 0 or 1 element
    .collect(Collectors.toList());   // ["a", "b"]
```

`Optional.stream()` produces a `Stream<T>` with 0 elements (if empty) or 1 element (if present). `flatMap` merges all these into one stream, effectively filtering out empty Optionals.

### Best practices

- **Use as return type** — not as field type or parameter type
- **Never `get()` without `isPresent()`** — always prefer `orElse`/`orElseGet`/`orElseThrow`
- **Don't use Optional for primitives** — use `OptionalInt`, `OptionalLong`, `OptionalDouble`
- **Don't use for collections** — return empty collection instead of `Optional<List<T>>`

---

## 17. Section 15 — Parallel Streams

### How parallel streams work

```
source.parallelStream()
         |
    Split source into chunks (Spliterator)
         |
    ┌────┴────┐────────┐────────┐
    │  CPU 0  │  CPU 1 │  CPU 2 │ ...   (ForkJoinPool.commonPool())
    │ chunk 1 │ chunk 2│ chunk 3│
    └────┬────┘────────┘────────┘
         |
    Merge results (combiner function)
         |
    Final result
```

The work is split using `Spliterator` — a splittable iterator that divides the source recursively until each chunk is small enough for one thread to process. Results are merged back using the combiner function provided by the `Collector` or `reduce` operation.

### When parallel helps — the requirements checklist

All of the following should be true for parallel to help:

- **Large data** — typically 100,000+ elements; benchmark for your specific case
- **CPU-bound** — computationally expensive per element (not I/O waiting)
- **Stateless operations** — no shared mutable state between elements
- **Associative reduction** — the merge step must be commutative and associative (`a + b == b + a`)
- **Splittable source** — `ArrayList`, arrays, `IntStream.range` split well; `LinkedList`, I/O streams do not

### The shared mutable state bug

```java
List<Integer> unsafeList = new ArrayList<>();

// WRONG — ArrayList is not thread-safe
IntStream.range(0, 1000).parallel().forEach(unsafeList::add);
// unsafeList.size() is < 1000 due to lost writes
```

The problem: two threads simultaneously find the internal array has room, both write to the same slot, one is overwritten. The fix is to never write to shared state inside a parallel lambda — use `collect()` instead:

```java
List<Integer> safe = IntStream.range(0, 1000).parallel()
    .boxed().collect(Collectors.toList());  // thread-safe internally
```

`collect()` uses a per-thread accumulator that is merged at the end — no sharing during processing.

### Why parallel can be slower for small data

Parallel streams have unavoidable overhead:
- Splitting the source into chunks
- Thread scheduling and context switching
- Merging results from multiple threads

For 100 elements, this overhead exceeds the computation time. The work is done in microseconds but thread coordination takes milliseconds. Always benchmark on realistic data sizes.

### ForkJoinPool thread count

```java
Runtime.getRuntime().availableProcessors()  // number of CPU cores
```

The common ForkJoinPool has `availableProcessors - 1` threads. You can change this with the system property `-Djava.util.concurrent.ForkJoinPool.common.parallelism=N`.

---

## 18. Section 16 — Real World: Log Processing

### Why streams fit log processing

Log files are a natural fit for stream pipelines:
- **Filter** to relevant records (level, time range, component)
- **Map** to extract specific fields
- **GroupingBy** to aggregate per component, level, or time window
- **Counting** to find most frequent events
- **Sorting** to find top offenders

### Pattern: multi-level aggregation

```java
// Most problematic component: chain groupingBy → entrySet → max
Optional<Map.Entry<String, Long>> topProblem = logs.stream()
    .filter(l -> !l.level().equals("INFO"))           // only non-info
    .collect(Collectors.groupingBy(LogEntry::component, Collectors.counting()))
    .entrySet().stream()                              // switch to entry stream
    .max(Map.Entry.comparingByValue());               // find max by count
```

This pattern — `collect into map → entrySet().stream() → further processing` — is a powerful way to do two-level aggregation without multiple passes.

---

## 19. Section 17 — Real World: Sales Pipeline

### Chaining groupingBy with summingDouble

```java
sales.stream()
    .collect(Collectors.groupingBy(
        SaleRecord::region,
        Collectors.summingDouble(SaleRecord::total)))
    .entrySet().stream()
    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
    .forEach(...)
```

The type witness `<String, Double>` on `comparingByValue()` is needed because Java's type inference sometimes cannot infer the generic types of static methods in chained calls without a hint.

### `mapToDouble().sum()` for revenue

```java
double totalRevenue = sales.stream().mapToDouble(SaleRecord::total).sum();
```

`mapToDouble()` converts to `DoubleStream`, avoiding `Double` boxing. `.sum()` is a terminal operation that efficiently sums all values using floating-point arithmetic.

---

## 20. Section 18 — Common Mistakes

### Mistake 1 — side effects in lambdas

```java
// WORKS but fragile — breaks in parallel
nums.stream().filter(...).forEach(result::add);

// CORRECT — let collect handle thread safety
List<Integer> correct = nums.stream().filter(...).collect(Collectors.toList());
```

The non-interfering contract says: lambda functions must not modify the stream source or any shared state. `collect()` handles result accumulation thread-safely.

### Mistake 3 — infinite stream without bound

```java
// CORRECT — limit guards against infinite loop
Stream.iterate(0, n -> n + 1).filter(n -> n % 7 == 0).limit(5).count();

// WRONG — count() must see all elements, never terminates
Stream.iterate(0, n -> n + 1).filter(n -> n % 7 == 0).count(); // hangs forever
```

Only short-circuit terminals (`findFirst`, `anyMatch`, `takeWhile`) are safe on infinite streams without `limit`. All other terminals need to see every element.

### Mistake 5 — `Stream<Integer>` vs `IntStream`

```java
// Boxes every int into Integer object — unnecessary allocation and GC pressure
Stream.iterate(1, n -> n + 1).limit(N).mapToLong(Integer::longValue).sum()

// No boxing — work directly with primitives
LongStream.rangeClosed(1, N).sum()
```

For numeric operations on large datasets, always use `IntStream`, `LongStream`, or `DoubleStream`. The benchmark in the code typically shows primitive streams 2–5× faster due to eliminated boxing.

### Mistake 6 — forEach for transformation

```java
// WRONG idiom — forEach is for side effects, not transformation
List<String> bad = new ArrayList<>();
words.forEach(w -> bad.add(w.toUpperCase()));

// CORRECT — use map + collect
List<String> good = words.stream().map(String::toUpperCase).collect(Collectors.toList());
```

`forEach` is for when you genuinely want a side effect (print, write to DB, update UI). Transformation should always use `map` + `collect` — it communicates intent clearly and is parallel-safe.

---

## 21. Section 19 — Performance Comparison

### What is benchmarked

Filter even numbers, square them, sum the results — on 2 million integers. Three implementations: traditional for-loop, sequential stream, parallel stream.

### Typical results

```
For-loop:         ~20 ms   — close to sequential stream
Sequential stream: ~25 ms  — slight overhead from stream machinery  
Parallel stream:   ~8 ms   — ~3x speedup on quad-core machine
```

### Why the loop and sequential stream are similar

The JIT (Just-In-Time) compiler optimizes both paths similarly at runtime. Streams add a small amount of overhead from the pipeline infrastructure, but for simple operations this is negligible.

### Why parallel wins for large, CPU-bound work

The 2 million element sum-of-squares is a CPU-bound, embarrassingly parallel problem — no shared state, fully associative. Four cores can each process 500,000 elements independently and sum their local results. The theoretical speedup is n_cores × (work fraction) — in practice 2–4× for typical machines.

---

## 22. Section 20 — Interview Summary

### Key interview questions answered

**Q: Difference between Collection and Stream?**

`Collection` is a data **storage** structure — it holds elements persistently, can be iterated multiple times, and has a defined size. `Stream` is a data **processing** pipeline — it does not store data, it processes elements from a source lazily, and is consumed exactly once.

**Q: Why are streams lazy?**

Laziness enables short-circuit evaluation — if a terminal operation can produce a result before processing all elements (e.g., `findFirst`, `anyMatch`), processing stops early. It also avoids creating unnecessary intermediate objects. Without laziness, each intermediate operation would produce a full intermediate collection.

**Q: What is a functional interface?**

An interface with exactly one abstract method (SAM — Single Abstract Method). It can be annotated with `@FunctionalInterface` (optional but enforced). Any such interface can be implemented with a lambda or method reference. All interfaces in `java.util.function` are functional interfaces.

**Q: Difference between map and flatMap?**

`map` applies a one-to-one transformation: each input element produces exactly one output element. `flatMap` applies a one-to-many transformation: each input element produces a `Stream` of output elements, and all those streams are flattened (merged) into one. Use `flatMap` when your mapping function returns a collection or optional that you want to unwrap.

**Q: When should you NOT use parallelStream?**

Small data sets (overhead exceeds work), I/O-bound tasks (threads block, not compute), operations with shared mutable state (race conditions), and ordered operations on sources that don't split well (e.g., `LinkedList`, file streams).

---

## 23. Key Takeaways

### Stream pipeline quick reference

```
Source: collection.stream(), Stream.of(), IntStream.range(), Stream.iterate(), Stream.generate()

Intermediate (lazy, return Stream):
  filter(pred)        → keep elements matching pred
  map(fn)             → transform 1-to-1
  flatMap(fn)         → transform 1-to-many + flatten
  sorted()            → natural order (stateful, O(n log n))
  distinct()          → remove dups (stateful)
  limit(n)            → keep first n (short-circuit)
  skip(n)             → drop first n
  peek(consumer)      → debug side-effect
  mapToInt/Long/Double → primitive stream (no boxing)
  takeWhile(pred)     → Java 9+, short-circuit
  dropWhile(pred)     → Java 9+

Terminal (eager, consume stream):
  collect(Collector)  → gather result
  forEach(consumer)   → side effect
  reduce(id, op)      → fold to single value
  count()             → number of elements
  min/max(comp)       → Optional
  findFirst/Any()     → Optional (short-circuit)
  anyMatch/allMatch/noneMatch(pred) → boolean (short-circuit)
  toArray()           → Object[] or T[]
```

### Functional interfaces quick reference

| Interface | Signature | Use in streams |
|---|---|---|
| `Predicate<T>` | `T → boolean` | `filter()` |
| `Function<T,R>` | `T → R` | `map()` |
| `Consumer<T>` | `T → void` | `forEach()`, `peek()` |
| `Supplier<T>` | `() → T` | `Stream.generate()`, `orElseGet()` |
| `BinaryOperator<T>` | `(T,T) → T` | `reduce()` |
| `Comparator<T>` | `(T,T) → int` | `sorted()`, `min()`, `max()` |

### Golden rules

1. **Prefer `collect()` over `forEach()`** for building result containers
2. **Use `IntStream`/`LongStream`/`DoubleStream`** to avoid boxing on numeric operations
3. **Never modify source or external state** inside a lambda
4. **Never reuse a consumed stream** — create a new one from the source
5. **Always add `limit()` or use short-circuit terminal** on infinite streams
6. **Prefer `orElseGet()`** over `orElse()` for expensive defaults
7. **Use method references** when they are clearer than lambdas
8. **Benchmark before using `parallelStream()`** — it is not always faster
9. **Fail-fast detects bugs, not prevents concurrency** — use ConcurrentHashMap for real thread safety
10. **`sorted()` buffers the whole stream** — avoid it if you only need the minimum or top-k
11. **`peek()` is a debug tool** — not a side-effect mechanism
12. **`filter` + `findFirst` is O(n) worst-case but short-circuits** — unlike `sorted` which is always O(n log n)

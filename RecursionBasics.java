// ================================================================
//   RECURSION & MATHEMATICAL THINKING IN JAVA
//   2 Clear Examples
// ================================================================
//   Compile:  javac RecursionBasics.java
//   Run:      java RecursionBasics
// ================================================================

public class RecursionBasics {

    // ================================================================
    //   BEFORE WE START — What IS Recursion?
    // ================================================================
    //
    //   Recursion = A function that calls ITSELF to solve a problem
    //   by breaking it into a SMALLER version of the same problem.
    //
    //   Think of it like Russian nesting dolls (Matryoshka):
    //   You open a doll → find a smaller doll → open that → smaller doll...
    //   Until you reach the TINY doll that doesn't open → that's the BASE CASE.
    //
    //   EVERY recursive function needs exactly 2 things:
    //
    //   1. BASE CASE   → The stopping condition. Without this = INFINITE LOOP 💀
    //   2. RECURSIVE CASE → Call yourself with a SMALLER / SIMPLER input
    //
    //   Golden Rule: Each recursive call MUST get closer to the base case.
    // ================================================================


    // ================================================================
    //   EXAMPLE 1 — FACTORIAL
    // ================================================================
    //
    //   What is Factorial?
    //   5! = 5 × 4 × 3 × 2 × 1 = 120
    //   4! = 4 × 3 × 2 × 1     = 24
    //   1! = 1
    //   0! = 1  (by mathematical definition)
    //
    //   ── MATHEMATICAL RECURRENCE RELATION ──
    //
    //   Notice the pattern:
    //     5! = 5 × 4!
    //     4! = 4 × 3!
    //     3! = 3 × 2!
    //     2! = 2 × 1!
    //     1! = 1  ← STOP HERE (base case)
    //
    //   So we can write:
    //     factorial(n) = 1              if n == 0 or n == 1  ← BASE CASE
    //     factorial(n) = n × factorial(n-1)                  ← RECURSIVE CASE
    //
    //   ── CALL STACK TRACE for factorial(4) ──
    //
    //   factorial(4)
    //     └─ returns 4 × factorial(3)
    //                    └─ returns 3 × factorial(2)
    //                                   └─ returns 2 × factorial(1)
    //                                                  └─ returns 1  ← BASE CASE
    //                                   └─ 2 × 1 = 2
    //                    └─ 3 × 2 = 6
    //     └─ 4 × 6 = 24  ✓
    //
    //   The call stack UNWINDS from bottom to top — like stacking plates,
    //   then taking them off one by one.
    //
    //   ── WHAT HAPPENS WITHOUT A BASE CASE? ──
    //   factorial(4) → factorial(3) → factorial(2) → factorial(1)
    //   → factorial(0) → factorial(-1) → factorial(-2) → ... FOREVER
    //   → Java throws: StackOverflowError  💀
    // ================================================================

    static long factorial(int n) {

        // ── BASE CASE ──
        // n=0 or n=1 → we know the answer directly, no more recursion needed
        if (n == 0 || n == 1) {
            return 1;
        }

        // ── RECURSIVE CASE ──
        // We don't know n! directly, but we know: n! = n × (n-1)!
        // So we TRUST the recursive call to compute (n-1)! correctly
        // and we just multiply n on top of it.
        return n * factorial(n - 1);
    }

    // ── ITERATIVE VERSION (for comparison) ──
    // This does the same thing using a loop instead of recursion.
    // Both give the same answer — recursion just mirrors the math more naturally.

    static long factorialIterative(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }


    // ================================================================
    //   EXAMPLE 2 — FIBONACCI SEQUENCE
    // ================================================================
    //
    //   What is Fibonacci?
    //   Each number = sum of the two numbers before it.
    //
    //   Sequence: 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55 ...
    //   Index:    0  1  2  3  4  5  6   7   8   9  10
    //
    //   Where does it appear in real life?
    //   → Flower petals, spiral shells, tree branches, stock market patterns,
    //     computer algorithms, even your SignBridge signal patterns!
    //
    //   ── MATHEMATICAL RECURRENCE RELATION ──
    //
    //     fib(0) = 0             ← BASE CASE 1
    //     fib(1) = 1             ← BASE CASE 2
    //     fib(n) = fib(n-1) + fib(n-2)   ← RECURSIVE CASE
    //
    //   ── CALL TREE TRACE for fib(5) ──
    //
    //                       fib(5)
    //                     /        \
    //                 fib(4)       fib(3)
    //                /     \       /    \
    //            fib(3)  fib(2) fib(2) fib(1)=1
    //            /   \    /  \   /  \
    //         fib(2) f(1) f(1)f(0) f(1)f(0)
    //          /  \   =1   =1  =0   =1  =0
    //        f(1) f(0)
    //         =1   =0
    //
    //   Adding up from the bottom:
    //   fib(2)=1, fib(3)=2, fib(4)=3, fib(5)=5  ✓
    //
    //   ── WHY TWO BASE CASES? ──
    //   Fibonacci depends on TWO previous values.
    //   If we only had fib(0)=0, then fib(1) would call fib(0)+fib(-1) → problem!
    //   We need BOTH fib(0) and fib(1) defined to anchor the whole sequence.
    //
    //   ── IMPORTANT WARNING: Performance ──
    //   Look at the tree above — fib(3) is computed TWICE, fib(2) THREE times!
    //   For fib(50), you'd make 2^50 ≈ 1 TRILLION calls. Very slow!
    //   Solution: Memoization — store results so we never repeat work.
    // ================================================================

    // ── SIMPLE RECURSIVE VERSION (easy to understand, slow for large n) ──
    static long fibRecursive(int n) {

        // ── BASE CASES ──
        if (n == 0) return 0;    // fib(0) = 0
        if (n == 1) return 1;    // fib(1) = 1

        // ── RECURSIVE CASE ──
        // fib(n) = sum of the two numbers before it
        return fibRecursive(n - 1) + fibRecursive(n - 2);
    }

    // ── MEMOIZED VERSION (fast — stores previous results) ──
    // memo[i] stores fib(i) once computed. Next time, we just look it up.
    static long[] memo = new long[50];
    static boolean[] computed = new boolean[50];

    static long fibMemo(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        // If already computed, return stored result instantly
        if (computed[n]) return memo[n];

        // Compute, STORE it, then return
        memo[n] = fibMemo(n - 1) + fibMemo(n - 2);
        computed[n] = true;
        return memo[n];
    }


    // ================================================================
    //   MAIN METHOD — Run everything and see the output
    // ================================================================

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║     RECURSION & MATHEMATICAL THINKING IN JAVA        ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");


        // ── FACTORIAL DEMO ──────────────────────────────────────────
        System.out.println("\n┌──────────────────────────────────────────────────────┐");
        System.out.println("│  EXAMPLE 1: FACTORIAL                                │");
        System.out.println("└──────────────────────────────────────────────────────┘");

        System.out.println("\n  Step-by-step trace for factorial(5):");
        System.out.println("  factorial(5)");
        System.out.println("    = 5 × factorial(4)");
        System.out.println("    = 5 × 4 × factorial(3)");
        System.out.println("    = 5 × 4 × 3 × factorial(2)");
        System.out.println("    = 5 × 4 × 3 × 2 × factorial(1)");
        System.out.println("    = 5 × 4 × 3 × 2 × 1   ← base case reached");
        System.out.println("    = 120  ✓");

        System.out.println("\n  Results table:");
        System.out.println("  ┌────┬────────────────┬──────────────────┐");
        System.out.println("  │  n │  Recursive     │  Iterative       │");
        System.out.println("  ├────┼────────────────┼──────────────────┤");
        for (int i = 0; i <= 10; i++) {
            System.out.printf("  │ %2d │ %14d │ %16d │%n",
                    i, factorial(i), factorialIterative(i));
        }
        System.out.println("  └────┴────────────────┴──────────────────┘");
        System.out.println("\n  ✓ Both methods give identical results.");
        System.out.println("  ✓ Recursive mirrors math: n! = n × (n-1)!");
        System.out.println("  ✓ Iterative is slightly more memory-efficient.");


        // ── FIBONACCI DEMO ───────────────────────────────────────────
        System.out.println("\n┌──────────────────────────────────────────────────────┐");
        System.out.println("│  EXAMPLE 2: FIBONACCI SEQUENCE                       │");
        System.out.println("└──────────────────────────────────────────────────────┘");

        System.out.println("\n  First 15 Fibonacci numbers:");
        System.out.print("  ");
        for (int i = 0; i < 15; i++) {
            System.out.print(fibRecursive(i));
            if (i < 14) System.out.print(" → ");
        }
        System.out.println();

        System.out.println("\n  Results table:");
        System.out.println("  ┌────┬────────────────┬──────────────────┐");
        System.out.println("  │  n │  Slow (naive)  │  Fast (memoized) │");
        System.out.println("  ├────┼────────────────┼──────────────────┤");
        for (int i = 0; i <= 15; i++) {
            System.out.printf("  │ %2d │ %14d │ %16d │%n",
                    i, fibRecursive(i), fibMemo(i));
        }
        System.out.println("  └────┴────────────────┴──────────────────┘");

        // Show performance difference
        System.out.println("\n  ── Performance Comparison for fib(40) ──");

        long start1 = System.currentTimeMillis();
        long r1 = fibRecursive(40);
        long time1 = System.currentTimeMillis() - start1;
        System.out.println("  Naive recursive : " + r1 + "  →  Time: " + time1 + " ms  (makes ~300 million calls!)");

        long start2 = System.currentTimeMillis();
        long r2 = fibMemo(40);
        long time2 = System.currentTimeMillis() - start2;
        System.out.println("  Memoized        : " + r2 + "  →  Time: " + time2 + " ms  (makes only 40 calls!)");


        // ── KEY TAKEAWAYS ────────────────────────────────────────────
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║   KEY TAKEAWAYS                                       ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║                                                      ║");
        System.out.println("║  1. Always define BASE CASE first — prevents        ║");
        System.out.println("║     infinite recursion (StackOverflowError)         ║");
        System.out.println("║                                                      ║");
        System.out.println("║  2. Each call must move TOWARD the base case        ║");
        System.out.println("║     (n decreases, problem gets simpler)             ║");
        System.out.println("║                                                      ║");
        System.out.println("║  3. Recursion = Elegant code that mirrors math      ║");
        System.out.println("║     f(n) = n × f(n-1) translates DIRECTLY to code  ║");
        System.out.println("║                                                      ║");
        System.out.println("║  4. Watch performance — naive recursion can be      ║");
        System.out.println("║     exponentially slow. Memoization is the fix.    ║");
        System.out.println("║                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }
}

# Compose Animation & Performance Training

An Android codelab project that walks through Jetpack Compose techniques across a simulated shopping journey (`HomeScreen` → `ProductDetailScreen` → `CheckoutScreen` → `CheckoutSuccessScreen`).

---

## Training Sessions & Branch Structure

The repository is organized into progressive training modules across dedicated git branches:

| Module | Branch | Description |
| :--- | :--- | :--- |
| **Sesi 1: Animation Starter** | `master` | Starter code with uncompleted animation `TODO` comments. |
| **Sesi 1: Animation Final** | `session-1-final` | Full working animation solution (baseline for Session 2). |
| **Sesi 2: Performance Starter** | `exercise/performance` | Starter code for performance codelab with intentional anti-patterns & `// TODO: Step [1..N]`. |
| **Sesi 2: Performance Final** | `final/performance` | Fully optimized solution (skipped recompositions, stable classes, compiler metrics). |

---

## Session 2: Compose for Performance

### Topics Covered:
1. **Jetpack Compose Phases:** Skipping Composition & Layout phases via **Lambda Modifiers** (`Modifier.graphicsLayer { ... }`).
2. **Conditional State Updates:** Eliminating redundant evaluations with **`derivedStateOf`**.
3. **State Read Deferral:** Limiting recomposition scope by passing lambda providers (`() -> State`).
4. **Computation Caching:** Preventing repetitive formatting/parsing with **`remember`**.
5. **Lazy List Optimization:** Preserving item identity and recycling layout pools via **`key`** and **`contentType`**.
6. **Stability Mastery:**
   - Preferring `val` over `var` on UI models.
   - Using `ImmutableList<T>` from `kotlinx.collections.immutable`.
   - Stabilizing standard Java types (`java.time.LocalDate`) via **`compose_compiler_config.conf`**.
7. **Recomposition Debugging Tools:**
   - **Android Studio Layout Inspector** (Recomposition & Skipped counts).
   - **Compose Compiler Reports** (`app-classes.txt` and `app-composables.txt`).
   - **Compose Stability Analyzer** IDE plugin.

---

## Session 1: Animation Primitives Covered
- **HomeScreen** — Simple animation for sliding and breathing banner effects plus animated page indicators.
<br><br>![Banner](./assets/banner.gif)

- **ProductDetailScreen** — Visibility animation with `fadeIn`/`fadeOut` and `expandVertically`/`shrinkVertically`, wrapped in `animateContentSize`.
<br><br>![Expandable](./assets/expandables.gif)

- **Cart Badge** — Content animation for number transitions with slide, fade, and scale.
<br><br>![Badge](./assets/badge.gif)

- **CheckoutScreen** — State transitions animating color, radius, scale, and rotation, plus a keyframe-based shake on failure.
<br><br>![Success](./assets/update-transition-success.gif)
<br><br>![Failed](./assets/update-transition-failed.gif)

- **CheckoutSuccessScreen** — Canvas-drawn confetti driven by `rememberInfiniteTransition`.
<br><br>![Confetti](./assets/confetti.gif)

---

## Running the Project
1. Open the repository in Android Studio Meerkat or newer.
2. Select your desired module branch:
   ```bash
   # For Session 2 Performance Exercise:
   git checkout exercise/performance

   # For Session 2 Final Solution:
   git checkout final/performance
   ```
3. Sync Gradle (Kotlin `2.0.21` with official Compose Compiler Gradle plugin).
4. Run the `app` configuration on an emulator or device running Android 8.0 (API 26) or above.

Happy coding!

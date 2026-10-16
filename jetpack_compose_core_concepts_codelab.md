summary: Comprehensive guide to Day 1: Compose Basic and Best Practice by Team D1 (Eric, Fawwazi, Tio): deep dive into Compose & Navigation core concepts, 3 rendering phases, UDF & state management, coroutines & 8 side effects APIs, Material Design 3 theming, Layout Inspector, migration strategy from XML, technical experience session, and an end-to-end MVI case study with interactive quizzes.
id: compose-basic-best-practice
categories: Android, Kotlin, Architecture, Foundation, Core Concepts, Best Practices
environments: Web
status: Published
feedback link: https://code.nbs.dev/nbs-mobile/bcatrainingcodelab
author: NBS Mobile Team (Eric, Fawwazi, Tio)

# Day 1: Compose Basic and Best Practice

## Delve Deeper into Jetpack Navigation Core Concept

Duration: 0:08:00

Welcome to **Day 1: Compose Basic and Best Practice**, prepared by **Team D1 (Eric, Fawwazi, Tio)**!

In this opening module, we will explore the architectural foundation of Jetpack Compose, the internal mechanics of the UI rendering pipeline, and how navigation concepts and UI tree hierarchies connect directly to modern Android lifecycle management.

```
+-----------------------------------------------------------------------------------+
|               DAY 1: COMPOSE BASIC AND BEST PRACTICE — TEAM D1 SYLLABUS           |
+-----------------------------------------------------------------------------------+
| 1. Delve Deeper into Jetpack Navigation Core Concept (Foundations & 3 Phases)     |
| 2. UDF, State Management and Thinking in Compose (Declarative Mindset & Hoisting) |
| 3. Coroutines and Side Effects (Mastering 8 Controlled Side-Effects APIs)        |
| 4. Theming, Layouts, Backstack and Navigation (M3, Inspector & Type-Safe Nav)     |
| 5. Migration Strategy (Incremental Adoption from Android Views / XML)             |
| 6. Technical Experience Session (Production Best Practices & Performance Traps)   |
| 7. Case Study (Hands-on MVI Architecture Implementation)                          |
| 8. Quiz (Interactive Self-Evaluation & Knowledge Check)                          |
+-----------------------------------------------------------------------------------+
```

### Core Philosophy of Jetpack Compose

**Jetpack Compose** is Google's recommended modern toolkit for building native Android UIs:
* **Functions, Not Layouts:** All visual elements are defined using pure Kotlin functions annotated with `@Composable`.
* **Declarative Paradigm:** You describe *what* the UI should look like for any given state ($UI = f(State)$), rather than manually mutating widget trees using imperative setters and `findViewById`.
* **State-Driven:** The user interface automatically updates whenever the underlying data changes.
* **Direct Access to Platform APIs:** Seamlessly integrates with Android coroutines, reactive flows, and built-in support for **Material Design 3**.

### The Three Rendering Phases of Jetpack Compose (*Compose Phases*)

The transformation from a `@Composable` Kotlin function into rendered pixels on the screen is executed across **Three Distinct Phases**:

```
+-----------------------------------------------------------------------------------+
|                         THREE PHASES OF JETPACK COMPOSE                           |
|                                                                                   |
|   +-----------------------+   +-----------------------+   +-------------------+   |
|   |    1. COMPOSITION     |   |       2. LAYOUT       |   |    3. DRAWING     |   |
|   |                       |   |                       |   |                   |   |
|   |   "What UI to show"   |-->|  "Where to place UI"  |-->|  "How it renders" |   |
|   |                       |   |                       |   |                   |   |
|   | Executes composables  |   | Measures children     |   | Renders pixels,   |   |
|   | & constructs UI       |   | & determines (x, y)   |   | text, shapes,     |   |
|   | Node Tree blueprint   |   | coordinates           |   | & canvas elements |   |
|   +-----------------------+   +-----------------------+   +-------------------+   |
+-----------------------------------------------------------------------------------+
```

#### 1. Composition Phase — "What UI to show"
Compose executes Composable functions, reads data parameters, and constructs an in-memory tree structure known as the **UI Node Tree**. This tree serves as the definitive blueprint for the subsequent Layout and Drawing phases.

#### 2. Layout Phase — "Where to place UI"
Compose traverses the UI Node Tree using a **Depth-First Search (DFS)** algorithm. Each parent node:
1. **Measures its children** subject to incoming constraints.
2. **Decides on its own size** (width and height) based on those child measurements.
3. **Places its children** at specific $(x, y)$ coordinates relative to the parent.

> aside positive
> **Single-Pass Measurement Principle:** Unlike traditional Android View hierarchies that frequently trigger recursive, multi-pass measurements ($O(2^N)$ layout overhead), Compose strictly enforces *single-pass measurement*. Every node is measured exactly once, guaranteeing smooth $O(N)$ rendering performance.

#### 3. Drawing Phase — "How it renders"
Compose traverses the node tree once more, dispatching GPU drawing commands to render background colors, typography glyphs, borders, elevation shadows, and vector graphics directly onto display canvas buffers.

### Connecting the Three Phases to Navigation

When navigating between screens:
1. Destination changes dictate which branches of the UI tree *Enter* and which *Leave* the composition.
2. By deferring state reads to the Layout or Drawing phase (e.g., using `Modifier.offset { ... }` or graphic layer transforms), animations and screen transition offsets can completely bypass the Composition phase, achieving buttery-smooth 60/120 FPS transitions without frame drops.

```kotlin
// Watch official deep dive video on Compose Phases from Google Android Developers:
// https://youtu.be/0yK7KoruhSM
```

[Watch Video: From data to UI: Compose phases - MAD Skills on YouTube](https://youtu.be/0yK7KoruhSM)

<video id="0yK7KoruhSM"></video>

---

## UDF, State Management and Thinking in Compose

Duration: 0:15:00

Building reliable Compose applications requires shifting from an imperative mindset to a declarative one, governed by **Unidirectional Data Flow (UDF)**.

### Thinking in Compose: The Mindset Shift

```
[Traditional Imperative Pipeline]
Data Changes ──> Find Widget (findViewById) ──> Call Setter (setText) ──> Risk of State Inconsistency

[Modern Declarative Pipeline]
State Changes ──> Automatic Recomposition ──> Execute Composable f(State) ──> Accurate UI Emitted
```

### The Compose Lifecycle

Every Composable within the UI hierarchy undergoes three lifecycle stages:

```
        +------------------------------------------------------------+
        |                     COMPOSABLE LIFECYCLE                   |
        +------------------------------------------------------------+
                                      │
                                      ▼
                      +-------------------------------+
                      | 1. ENTER THE COMPOSITION       |
                      | Executed for the first time   |
                      +-------------------------------+
                                      │
                                      ▼ ◄─────────────────────────┐
                      +-------------------------------+           │
                      | 2. RECOMPOSE                  |           │
                      | Re-executed 0 or more times   | ──────────┘
                      | when observed state changes   | (Recomposition Loop)
                      +-------------------------------+
                                      │
                                      ▼
                      +-------------------------------+
                      | 3. LEAVE THE COMPOSITION      |
                      | Node removed from memory      |
                      | Local remembered state cleared|
                      | DisposableEffect cleaned up   |
                      +-------------------------------+
```

1. **Enter the Composition:** The Composable is executed for the first time and its node is attached to the UI tree (e.g., a screen opens, or an `if (isLoggedIn)` condition becomes `true`).
2. **Recompose (0 or multiple times):** The Composable re-executes whenever the state or input parameters it reads change. Compose intelligently skips composables whose inputs have not changed (*Smart Skipping*).
3. **Leave the Composition:** The Composable is removed from the UI tree when no longer needed. Local remembered state is cleared from RAM and `DisposableEffect.onDispose` callbacks execute.

### 6 Triggers of Recomposition

Recomposition is initiated by any of the following 6 factors:
1. **State Mutation:** A change in an observed `MutableState<T>` object.
2. **Parameter Changes:** The Composable receives updated arguments from its parent.
3. **Configuration Changes:** Device rotation, locale updates, or Dark Mode toggle.
4. **CompositionLocal Changes:** Values provided upstream by `CompositionLocalProvider` update.
5. **Parent Recomposition:** An upstream parent recomposes and passes non-stable parameters down.
6. **Side-Effects API Triggers:** APIs such as `produceState` emit new values into Compose state.

### State Classification: In vs Outside Composable

```
+---------------------------------------------------------------------------------------+
|                                COMPOSE STATE CLASSIFICATION                           |
+---------------------------------------------------------------------------------------+
| Criterion            | State In Composables             | State Outside Composable    |
+----------------------+----------------------------------+-----------------------------+
| State Owner          | Composable function              | ViewModel / Domain Layer    |
| Lifespan             | Short-lived                      | Long-lived                  |
| Responsibility       | Local UI interaction behavior    | Business logic & API data   |
| Configuration Change | `remember`: Reset to initial     | Preserved across rotation   |
|                      | `rememberSaveable`: Preserved    | and screen navigation       |
| Observation Syntax   | `remember { mutableStateOf() }`  | `StateFlow.collectAsStateWithLifecycle()` |
| Destruction          | When leaving the composition     | When ViewModel is cleared   |
+---------------------------------------------------------------------------------------+
```

> aside negative
> **ViewModel Rule:** Never call `remember` inside a ViewModel class! A ViewModel is already bound to a lifecycle that survives Activity configuration changes.

### Memoization: remember vs rememberSaveable

* `remember`: Stores values in memory across recompositions. Values are **lost** on configuration changes (e.g., device rotation) and process death.
* `rememberSaveable`: Stores values in the Android `SavedStateRegistry` Bundle. Values **survive** recomposition, device rotation, and OS process recreation.

```kotlin
// Input survives device rotation
var accountNumber by rememberSaveable { mutableStateOf("") }
```

### Unidirectional Data Flow (UDF) & State Hoisting

**Unidirectional Data Flow (UDF)** dictates that state flows down, while events flow up:

```
                 +-----------------------------------------------+
                 |             PARENT COMPOSABLE                 |
                 |             (State Ownership)                 |
                 +-----------------------------------------------+
                               │                  ▲
                               │                  │
                    State Flow │                  │ Event Flow
                      Downward │                  │   Upward
                  (Data/Flags) │                  │ (Clicks, Text,
                               │                  │  User Actions)
                               ▼                  │
                 +-----------------------------------------------+
                 |             CHILD COMPOSABLE                  |
                 |             (Stateless UI)                    |
                 +-----------------------------------------------+
```

**State Hoisting** is the architectural practice of moving state up to a parent component, transforming children into **Stateless Composables**:
* **Stateless Composables** only accept state parameters and emit events via lambda callbacks. They are highly reusable and easily unit-tested.
* **Stateful Composables** manage and own state containers, wiring business logic to child components.

```kotlin
// STATELESS COMPOSABLE: Reusable & easy to test
@Composable
fun AccountCard(
    accountName: String,
    balance: String,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(accountName, style = MaterialTheme.typography.titleMedium)
                Text(balance, style = MaterialTheme.typography.bodyLarge)
            }
            IconButton(onClick = onFavoriteToggle) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite"
                )
            }
        }
    }
}
```

---

## Coroutines and Side Effects

Duration: 0:15:00

### What is a Side Effect in Jetpack Compose?

> A **Side Effect** is any mutation to application or system state that occurs **outside the scope of a Composable function** (e.g., triggering network requests, logging analytics, navigating screens, writing to disk, or displaying Snackbars).

Composable functions must remain **pure functions**. Running side effects directly inside the Composable body leads to critical issues:
1. **Repetitive Execution:** The effect will run unconditionally on *every single recomposition*.
2. **Infinite Recomposition Loops:** If the side effect updates a state, it triggers another recomposition, which triggers the side effect again indefinitely.
3. **Memory Leaks:** Uncontrolled coroutines and system listeners remain active long after the screen has closed.

### Deep Dive: 8 Official Side Effects APIs

Compose provides 8 specialized APIs to execute side effects in a controlled, lifecycle-aware manner:

```
+----------------------------------------------------------------------------------------+
|                                8 COMPOSE SIDE EFFECTS APIS                             |
+----------------------------------------------------------------------------------------+
| API Name                 | Execution Scope             | Primary Use Case              |
+--------------------------+-----------------------------+-------------------------------+
| 1. LaunchedEffect        | Suspend (Coroutine)         | Fetch data, timers, navigation|
| 2. DisposableEffect      | Non-suspend + Cleanup       | BroadcastReceiver, Sensors, WS|
| 3. rememberCoroutineScope| Coroutine via User Callback | Button click -> Show Snackbar |
| 4. rememberUpdatedState  | Stable reference capture    | Prevent restarting long effect|
| 5. SideEffect            | After every successful frame| Analytics, sync non-Compose   |
| 6. produceState          | Non-Compose -> State<T>     | Sensor/callback conversion    |
| 7. derivedStateOf        | Cached derived calculation  | Filtering, scroll-to-top flag |
| 8. snapshotFlow          | State<T> -> Kotlin Flow     | Search debounce, text tracking|
+----------------------------------------------------------------------------------------+
```

#### 1. LaunchedEffect
Launches a coroutine when entering the composition, and cancels/restarts it whenever its `key` parameter changes:

```kotlin
// Runs exactly once on initial screen entry
LaunchedEffect(Unit) {
    viewModel.loadInitialData()
}

// Restarts whenever accountId changes
LaunchedEffect(accountId) {
    viewModel.fetchAccountDetails(accountId)
}
```

#### 2. DisposableEffect
For side effects requiring setup on entry and **mandatory teardown (*cleanup*)** via `onDispose` when leaving the composition:

```kotlin
DisposableEffect(context) {
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Handle connectivity update
        }
    }
    context.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

    onDispose {
        context.unregisterReceiver(receiver) // Prevents memory leaks!
    }
}
```

#### 3. rememberCoroutineScope
Provides a `CoroutineScope` bound to the Composable's lifecycle to safely launch coroutines from **user event callbacks** (e.g., button clicks):

```kotlin
val scope = rememberCoroutineScope()
val snackbarHostState = remember { SnackbarHostState() }

Button(onClick = {
    scope.launch {
        snackbarHostState.showSnackbar("Transaction processed successfully!")
    }
}) {
    Text("Confirm Transfer")
}
```

#### 4. rememberUpdatedState
Captures a stable reference to a frequently updated lambda or value without restarting a long-running effect (such as an active delay or animation loop):

```kotlin
@Composable
fun AutoLogoutTimer(timeoutMillis: Long, onTimeout: () -> Unit) {
    val currentOnTimeout by rememberUpdatedState(onTimeout)

    LaunchedEffect(Unit) {
        delay(timeoutMillis)
        currentOnTimeout() // Always invokes the freshest callback instance!
    }
}
```

#### 5. SideEffect
Executes after every successful recomposition to publish Compose state changes to non-Compose architectures (such as Firebase Analytics or external SDKs):

```kotlin
SideEffect {
    analyticsTracker.logScreenView("AccountDashboard", userTier)
}
```

#### 6. produceState
Converts non-Compose asynchronous data streams (such as Android system callbacks or sensor listeners) into a Compose `State<T>`:

```kotlin
val batteryLevel by produceState(initialValue = 100) {
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, intent: Intent?) {
            value = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 100) ?: 100
        }
    }
    context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    awaitDispose { context.unregisterReceiver(receiver) }
}
```

#### 7. derivedStateOf
Produces a state derived from one or more other states, recalculating **only when the resulting calculation actually changes**, rather than on every recomposition:

```kotlin
val listState = rememberLazyListState()
// derivedStateOf prevents thousands of unnecessary recompositions while scrolling
val showScrollToTopButton by remember {
    derivedStateOf { listState.firstVisibleItemIndex > 5 }
}
```

#### 8. snapshotFlow
Converts a Compose `State<T>` into a cold Kotlin `Flow<T>`, granting full access to reactive operators like `debounce()`, `distinctUntilChanged()`, and `filter()`:

```kotlin
var searchQuery by remember { mutableStateOf("") }

LaunchedEffect(Unit) {
    snapshotFlow { searchQuery }
        .debounce(300)
        .distinctUntilChanged()
        .collect { query ->
            viewModel.searchTransactions(query)
        }
}
```

---

## Theming, Layouts, Backstack and Navigation

Duration: 0:15:00

### 1. Material Design 3 Theming & Dynamic Colors

Compose provides complete support for **Material Design 3 (M3 / Material You)** across four design pillars:
* **Color Scheme:** Built from 5 Key Colors yielding a 13-tone tonal palette.
* **Dynamic Color:** Automatically derives custom palettes from the user's wallpaper on **Android 12 (API level 31)+** via `dynamicLightColorScheme` and `dynamicDarkColorScheme`.
* **Typography:** Adheres to M3's 15 type scales (`Display`, `Headline`, `Title`, `Body`, `Label`).
* **Shapes:** Standardized corner radiuses (`extraSmall` 4.dp to `extraLarge` 24.dp).

```kotlin
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
```

### 2. Implicit Data Passing with CompositionLocal

**CompositionLocal** enables passing data down the UI tree implicitly, eliminating **Prop Drilling** (the tedious practice of manually passing identical arguments through every intermediate Composable layer):

```kotlin
// Token Declaration
val LocalSpacing = staticCompositionLocalOf { AppSpacing() }

// Root Provider
CompositionLocalProvider(LocalSpacing provides AppSpacing(medium = 12.dp)) {
    // Any descendant can directly read the value
    val spacing = LocalSpacing.current
}
```

> aside negative
> **Strict Anti-Pattern:** Never use `CompositionLocal` to provide business dependencies (Repositories, ViewModels, or dynamic mutable state streams). Business dependencies must be injected using explicit Dependency Injection (Koin / Hilt)!

### 3. Layout Inspector: Recomposition & Skip Count Diagnostics

The Android Studio **Layout Inspector** provides two vital performance metrics:
* **Recomposition Count:** The number of times a Composable has been re-executed.
* **Skip Count:** The number of times Compose successfully **skipped** execution because all parameters were unchanged and stable.

If a Composable consistently shows *Skip Count: 0*, inspect its parameter data classes. Annotate classes containing standard collections with `@Immutable` or `@Stable` so the Compose Compiler can verify their stability.

### 4. Backstack & Modern Type-Safe Navigation (Navigation Compose 2.8+)

Modern Compose navigation replaces fragile string-based URLs (`"profile/{id}"`) with **Navigation Compose 2.8+ Type-Safe Navigation**, powered by **Kotlinx Serialization (`@Serializable`)**:

```kotlin
import androidx.navigation.compose.*
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

// 1. Type-Safe Route Declarations
@Serializable object DashboardRoute
@Serializable data class TransactionDetailRoute(val transactionId: String)

// 2. Generic NavHost Setup
@Composable
fun MainAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = DashboardRoute) {
        composable<DashboardRoute> {
            DashboardScreen(
                onNavigateToDetail = { id ->
                    // 3. Navigate with typed instances
                    navController.navigate(TransactionDetailRoute(transactionId = id))
                }
            )
        }

        composable<TransactionDetailRoute> { backStackEntry ->
            // 4. Automated typed argument extraction
            val args = backStackEntry.toRoute<TransactionDetailRoute>()
            TransactionDetailScreen(transactionId = args.transactionId)
        }
    }
}
```

---

## Migration Strategy

Duration: 0:10:00

For large-scale enterprise banking applications, attempting a "big-bang rewrite" from XML to Jetpack Compose poses severe stability risks. Instead, adopt an **Incremental Migration Strategy**.

```
+-----------------------------------------------------------------------------------+
|                         ENTERPRISE MIGRATION ROADMAP                              |
+-----------------------------------------------------------------------------------+
|  Phase 1: Design Tokens (Color, Type, Shape mapped to Compose MaterialTheme)     |
|                                     │                                             |
|                                     ▼                                             |
|  Phase 2: Build New Features 100% in Jetpack Compose                              |
|                                     │                                             |
|                                     ▼                                             |
|  Phase 3: Bottom-Up: Replace Complex Views (RecyclerView -> LazyColumn via Compose)|
|                                     │                                             |
|                                     ▼                                             |
|  Phase 4: Top-Down: Wrap Legacy XML Screens using AndroidView                     |
|                                     │                                             |
|  Phase 5: Replace Fragment Navigation with Type-Safe Navigation Compose          |
+-----------------------------------------------------------------------------------+
```

### 1. Bi-directional Interoperability Patterns

#### A. Embedding Composables in Legacy XML Layouts (`ComposeView`)
Used to embed modern Compose components inside existing XML layouts or Fragments:

```kotlin
val composeView = view.findViewById<ComposeView>(R.id.compose_container)
composeView.apply {
    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
    setContent {
        MaterialTheme {
            AccountBalanceWidget(balance = "$25,000.00")
        }
    }
}
```

#### B. Embedding Legacy Android Views in Composables (`AndroidView`)
Used when a screen requires platform components that lack native Compose equivalents (such as `MapView`, custom camera previews, or barcode scanners):

```kotlin
@Composable
fun LegacyCustomChart(chartData: List<Float>) {
    AndroidView(
        factory = { context ->
            CustomLegacyChartView(context).apply {
                setData(chartData)
            }
        },
        update = { view ->
            view.updateData(chartData) // Called when data changes
        },
        modifier = Modifier.fillMaxWidth().height(200.dp)
    )
}
```

### 2. UI Architecture Layer Migration

* **From LiveData to StateFlow:** Convert `MutableLiveData<T>` in ViewModels to `MutableStateFlow<T>`. In Compose, observe flows using `collectAsStateWithLifecycle()` from `androidx.lifecycle:lifecycle-runtime-compose`.
* **Deprecating ViewBinding:** Gradually eliminate ViewBinding references as entire XML layouts transition to pure Composable functions.

---

## Technical Experience Session

Duration: 0:10:00

Based on real-world production experience from the NBS Mobile engineering team, here are crucial best practices and performance traps to master:

### 1. Production Do's and Don'ts

* **DO:** Expose `modifier: Modifier = Modifier` as an optional parameter on all public Composables, and apply it to the outermost root node.
* **DON'T:** Instantiate heavy objects (such as `SimpleDateFormat`, regex matchers, or cryptographic instances) directly inside the Composable body without wrapping them in `remember`.
* **DO:** Adhere to the Single Responsibility Principle by breaking large screens into focused, modular composable functions.
* **DON'T:** Ignore the `innerPadding` parameter provided by `Scaffold`. Ignoring it causes content to render behind the `TopAppBar` or `BottomBar`.

### 2. Optimizing Performance with Deferred State Reads

Notice the dramatic performance difference when animating layout offsets:

```kotlin
// POOR: Reads scrollOffset in the Composition Phase!
// Triggers full Recomposition on every single pixel movement!
Box(modifier = Modifier.offset(y = scrollOffset.dp))

// OPTIMAL: Defers state reading to the Layout Phase via lambda!
// Bypasses the Composition phase entirely and executes directly in Layout!
Box(modifier = Modifier.offset { IntOffset(x = 0, y = scrollOffset) })
```

### 3. Resolving Kotlin Collection Stability (`List<T>`)

By default, the Compose Compiler marks standard Kotlin collection interfaces (`List<T>`, `Set<T>`, `Map<K, V>`) as **Unstable**, because their concrete runtime implementations might be mutable collections (like `ArrayList`).

**Solutions:**
1. Wrap collections in an `@Immutable` data class:
```kotlin
@Immutable
data class AccountListWrapper(val accounts: List<AccountItem>)
```
2. Or use `kotlinx.collections.immutable` (e.g., `ImmutableList<AccountItem>`).

---

## Case Study

Duration: 0:15:00

Let us integrate all the core concepts learned today into an **End-to-End MVI (Model-View-Intent) Architecture Case Study**: A Banking Transaction Management screen featuring favoriting and centralized reset functionality.

```
                  +───────────────────────────────────────────+
                  |               VIEWMODEL                   |
                  |  _uiState: MutableStateFlow<UiState>      |
                  |  fun onIntent(intent: TransactionIntent)  |
                  +───────────────────────────────────────────+
                                ▲               │
                      User      │               │ State Flows Down
                      Action    │               │ (uiState: State<UiState>)
                    (Intent)    │               │
                                │               ▼
                  +───────────────────────────────────────────+
                  |             COMPOSE UI LAYER              |
                  |  - LaunchedEffect (Initial data load)     |
                  |  - rememberCoroutineScope (Show Snackbar) |
                  |  - snapshotFlow (Debounced search input)  |
                  |  - Type-Safe Navigation to Detail screen  |
                  +───────────────────────────────────────────+
```

### 1. UiState and Intent Definitions

```kotlin
data class TransactionItem(
    val id: String,
    val title: String,
    val amount: String,
    val isFavorite: Boolean = false
)

data class TransactionUiState(
    val transactions: List<TransactionItem> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: String? = null
)

sealed class TransactionIntent {
    object LoadTransactions : TransactionIntent()
    data class ToggleFavorite(val transactionId: String) : TransactionIntent()
    object ResetAllFavorites : TransactionIntent()
    object ClearMessage : TransactionIntent()
}
```

### 2. ViewModel with Pure Reducer Logic

```kotlin
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    fun onIntent(intent: TransactionIntent) {
        when (intent) {
            is TransactionIntent.LoadTransactions -> loadData()
            is TransactionIntent.ToggleFavorite -> toggleFavorite(intent.transactionId)
            is TransactionIntent.ResetAllFavorites -> resetFavorites()
            is TransactionIntent.ClearMessage -> _uiState.update { it.copy(userMessage = null) }
        }
    }

    private fun loadData() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            // Simulated repository fetch
            val dummy = listOf(
                TransactionItem("1", "Transfer to BCA - Alice", "$500.00"),
                TransactionItem("2", "Electric Utility Bill", "$120.00"),
                TransactionItem("3", "Online Merchant Payment", "$45.50")
            )
            _uiState.update { it.copy(transactions = dummy, isLoading = false) }
        }
    }

    private fun toggleFavorite(id: String) {
        _uiState.update { state ->
            val updated = state.transactions.map { item ->
                if (item.id == id) item.copy(isFavorite = !item.isFavorite) else item
            }
            state.copy(transactions = updated)
        }
    }

    private fun resetFavorites() {
        _uiState.update { state ->
            val updated = state.transactions.map { it.copy(isFavorite = false) }
            state.copy(transactions = updated, userMessage = "All favorites have been reset!")
        }
    }
}
```

### 3. Composable Screen Implementation

```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionManagementScreen(
    onNavigateToDetail: (String) -> Unit,
    vm: TransactionViewModel = viewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Side Effect 1: LaunchedEffect for one-shot initial data loading
    LaunchedEffect(Unit) {
        vm.onIntent(TransactionIntent.LoadTransactions)
    }

    // Side Effect 2: Showing feedback Snackbars safely
    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            vm.onIntent(TransactionIntent.ClearMessage)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction History") },
                actions = {
                    IconButton(onClick = { vm.onIntent(TransactionIntent.ResetAllFavorites) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset Favorites")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.transactions, key = { it.id }) { item ->
                        TransactionRowItem(
                            item = item,
                            onItemClick = { onNavigateToDetail(item.id) },
                            onToggleFavorite = {
                                vm.onIntent(TransactionIntent.ToggleFavorite(item.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

// STATELESS ROW ITEM
@Composable
fun TransactionRowItem(
    item: TransactionItem,
    onItemClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        onClick = onItemClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.amount,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (item.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
```

---

## Quiz

Duration: 0:10:00

### Interactive Self-Evaluation Quiz: Day 1 Best Practices

Test your mastery of the concepts covered in **Day 1: Compose Basic and Best Practice** with these 10 interactive questions:

#### Question 1: Compose Rendering Phases
During which rendering phase does Compose traverse the UI Node Tree using Depth-First Search (DFS) to measure children (*single-pass measurement*) and determine $(x, y)$ coordinate placement?

* [ ] Composition Phase
* [x] Layout Phase
* [ ] Drawing Phase
* [ ] Reconciliation Phase

> aside positive
> **Explanation 1:** The Layout Phase measures each child node exactly once and assigns width, height, and relative $(x, y)$ coordinates. The Composition Phase builds the tree, while the Drawing Phase emits GPU render commands to pixels.

#### Question 2: Lifecycle & State Memoization
What is the fundamental difference between `remember` and `rememberSaveable` when a device undergoes an orientation change (Portrait to Landscape)?

* [ ] `remember` retains its state, while `rememberSaveable` resets to its initial default
* [x] `remember` resets its value to initial default, while `rememberSaveable` preserves its value using the Android SavedStateRegistry Bundle
* [ ] Both automatically clear their stored values from memory upon configuration changes
* [ ] `remember` can only be used inside ViewModels, while `rememberSaveable` is for Composables

> aside positive
> **Explanation 2:** `remember` memoizes values only while a Composable remains in the composition tree; its value is lost when the Activity is recreated. `rememberSaveable` serializes values into an Android Bundle, surviving configuration changes and OS process death.

#### Question 3: Unidirectional Data Flow & State Hoisting
Which of the following statements BEST describes a *Stateless Composable* after applying State Hoisting?

* [ ] The Composable manages and stores internal `mutableStateOf` variables inside its own body
* [x] The Composable holds no internal state; it receives data via parameters and communicates user interactions upward via lambda callbacks
* [ ] The Composable cannot be recomposed by the Compose runtime
* [ ] The Composable must inherit directly from the `ViewModel` class

> aside positive
> **Explanation 3:** A Stateless Composable delegates state ownership to an upstream parent (*state flows down, events flow up*). This decoupling makes the component easy to unit-test and reuse across different contexts.

#### Question 4: Selecting the Appropriate Side-Effects API
A user clicks a "Submit Payment" button, and the application must launch a coroutine to display a confirmation `Snackbar`. Which API MUST be used?

* [ ] `LaunchedEffect(Unit)`
* [ ] `SideEffect`
* [x] `rememberCoroutineScope`
* [ ] `produceState`

> aside positive
> **Explanation 4:** `rememberCoroutineScope` provides a lifecycle-aware `CoroutineScope` specifically intended to launch coroutines from *user event callbacks* (like button `onClick`). In contrast, `LaunchedEffect` executes upon entering the composition, not from click callbacks.

#### Question 5: Resource Cleanup with DisposableEffect
Why should you choose `DisposableEffect` over `LaunchedEffect` when registering a hardware sensor listener or system `BroadcastReceiver`?

* [ ] Because `DisposableEffect` executes automatically on background IO threads
* [x] Because `DisposableEffect` mandates an `onDispose` cleanup block to unregister listeners when leaving the composition, preventing memory leaks
* [ ] Because `LaunchedEffect` cannot access `LocalContext.current`
* [ ] Because `DisposableEffect` enables alpha transparency on views

> aside positive
> **Explanation 5:** `DisposableEffect` is specifically designed for side effects that require pair setup and teardown via `onDispose` when the Composable leaves the composition or keys change.

#### Question 6: Performance Optimization with derivedStateOf
When is using `derivedStateOf` strongly recommended in a Compose application?

* [ ] When executing HTTP network requests in a background worker
* [x] When a state calculation depends on another frequently changing state (such as scroll positions), but recomposition should only trigger when the resulting condition actually changes
* [ ] When saving relational records to a local Room Database
* [ ] When displaying a platform AlertDialog

> aside positive
> **Explanation 6:** `derivedStateOf` acts as a state update filter. Even if scroll offsets change on every pixel, `derivedStateOf` ensures recompositions are triggered only when the boolean condition (e.g., `firstVisibleItemIndex > 5`) flips.

#### Question 7: CompositionLocal Anti-Patterns
Which scenario represents a SEVERE ANTI-PATTERN when using `CompositionLocal`?

* [ ] Providing custom design tokens such as `LocalAppSpacing` or `LocalCustomColors`
* [ ] Reading screen orientation configurations using `LocalConfiguration.current`
* [x] Passing business logic dependencies like `UserRepository`, `PaymentViewModel`, or frequently mutating `MutableStateFlow` streams
* [ ] Providing a boolean flag indicating whether the app is executing inside Android Studio Previews

> aside positive
> **Explanation 7:** `CompositionLocal` is designed for stable, ambient visual UI context tokens. Injecting business repositories or frequently mutating data streams violates clean architecture, complicates unit testing, and triggers unpredictable recompositions. Use Dependency Injection (Koin / Hilt) instead.

#### Question 8: Modern Type-Safe Navigation (Navigation Compose 2.8+)
How does modern Type-Safe Navigation in Navigation Compose 2.8+ improve upon legacy string-based routing?

* [ ] Modern navigation relies on legacy XML NavGraphs parsed by the Activity
* [x] Modern navigation leverages Kotlinx Serialization (`@Serializable`), ensuring routes and arguments are validated at compile time and extracted safely via `toRoute<T>()`
* [ ] Modern navigation requires all route parameters to be formatted as encrypted URL query strings
* [ ] Modern navigation only supports transitions between Activities, not Composables

> aside positive
> **Explanation 8:** Navigation Compose 2.8+ models destinations as typed Kotlin classes or objects annotated with `@Serializable`. This eliminates runtime string typos and extracts parameters automatically with full compile-time safety.

#### Question 9: Deferred State Reads
Which syntax represents the most efficient technique to animate or adjust a Composable's position without triggering recomposition on every frame?

* [ ] `Modifier.padding(top = scrollOffset.dp)`
* [ ] `Modifier.offset(y = scrollOffset.dp)`
* [x] `Modifier.offset { IntOffset(x = 0, y = scrollOffset) }`
* [ ] `Modifier.background(MaterialTheme.colorScheme.primary)`

> aside positive
> **Explanation 9:** Using the lambda variant `Modifier.offset { ... }` defers state reading directly to the Layout Phase. This bypasses the Composition Phase completely, avoiding redundant Composable function re-executions during continuous animations.

#### Question 10: XML & Compose Interoperability Strategy
If you have an existing legacy Fragment XML layout and wish to embed a new card component written 100% in Jetpack Compose, which Android container must be placed in that XML layout?

* [ ] `AndroidView`
* [x] `ComposeView`
* [ ] `SurfaceView`
* [ ] `RecyclerView`

> aside positive
> **Explanation 10:** `ComposeView` is the official Android ViewGroup container used to host and render Composable functions inside legacy XML layouts and Fragments. Conversely, `AndroidView` is used to embed legacy View widgets inside a Composable hierarchy.

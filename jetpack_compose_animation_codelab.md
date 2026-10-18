summary: Comprehensive, hands-on guide to Jetpack Compose Animation for Engineers by Khukuh (Mobile Software Engineer @ Nusantara Beta Studio): motion design principles, MonotonicFrameClock rendering mechanics, 5 core animation categories, hands-on e-commerce shopping flow exercises (BannerSlider, ProductDetail, CartBadge, CheckoutState updateTransition & keyframe shake, Canvas confetti), graphicsLayer vs layout performance, and interactive quizzes.
id: compose-animation-for-engineers
categories: Android, Kotlin, Jetpack Compose, Animation, UI/UX, Performance
environments: Web
status: Published
feedback link: https://code.nbs.dev/nbs-mobile/bcatrainingcodelab
author: Khukuh (Mobile Software Engineer @ Nusantara Beta Studio)

# Jetpack Compose Animation for Engineers

## Introduction & Motion Design Foundations

Duration: 0:10:00

Welcome to **Jetpack Compose Animation for Engineers**, prepared by **Khukuh (Mobile Software Engineer @ Nusantara Beta Studio)**!

<!-- [PLACEHOLDER: Insert Slide Deck URL / QR Code for Presentation Slides here] -->

```
+-----------------------------------------------------------------------------------------+
|                  JETPACK COMPOSE ANIMATION FOR ENGINEERS — WORKSHOP SYLLABUS            |
+-----------------------------------------------------------------------------------------+
| 1. Introduction & Motion Design Foundations                                (0:10:00)    |
| 2. Under the Hood: The Compose Animation Engine                            (0:10:00)    |
| 3. Environment Setup & Project Architecture                                (0:08:00)    |
| 4. Module 1: Simple Animation — Breathing Banner & Page Indicators         (0:15:00)    |
| 5. Module 2: Visibility & Content Size — Expandable Product Specs          (0:15:00)    |
| 6. Module 3: Content Animation — Animated Cart Counter                     (0:15:00)    |
| 7. Module 4: Advance State Transition & Error Shake — Checkout Flow        (0:20:00)    |
| 8. Module 5: Custom Infinite Canvas Animation — Confetti Celebration       (0:15:00)    |
| 9. Best Practices & Performance Optimization                               (0:10:00)    |
| 10. Interactive Self-Evaluation Quiz & Workshop Wrap-Up                    (0:10:00)    |
+-----------------------------------------------------------------------------------------+
```

### What you'll learn

* The functional communication principles of modern **Motion Design** in mobile UIs.
* Why Jetpack Compose replaces the brittle complexities and Lottie shortcuts of the legacy Android View system.
* How Compose coordinates frame timing under the hood using `MonotonicFrameClock` and the Android `Choreographer`.
* The **5 Core Categories of Animation in Jetpack Compose** and how to select the right tool for each scenario.
* How to take existing static screens and **sew animated components** into real product pages:
  1. **HomeScreen:** Breathing scale and parallax offset for promo banners + animated page indicators using `animateFloatAsState`.
  2. **ProductDetailScreen:** Smooth expandable specification details using `AnimatedVisibility` and `Modifier.animateContentSize()`.
  3. **CartBadge:** Numeric shopping cart increment animations using `AnimatedContent` with custom slide, fade, and scale transitions.
  4. **CheckoutScreen:** Synchronized multi-property state transitions (`updateTransition`) and physical error feedback using `Animatable` with `keyframes`.
  5. **CheckoutSuccessScreen:** High-performance particle confetti celebration rendered directly on a GPU `Canvas` driven by `rememberInfiniteTransition`.
* Production performance rules: **Draw Phase (`Modifier.graphicsLayer`) vs. Layout Phase**, custom modifiers with `Modifier.Node`, and tooling with **Android Studio Animation Preview**.

### 1. Motion Design and Communication

#### Why Motion Matters in Modern UI
In modern enterprise mobile applications, motion is never mere decorative eye-candy. It is an essential functional communication medium that makes the interface:
* **More Informative:** Highlights critical real-time status updates instantly so users notice transactions or errors without confusion.
* **Easier to Understand:** Directs user visual attention and clearly demonstrates spatial relationships and state transitions between UI components.
* **More Natural:** Eliminates jarring, instantaneous visual jumps by mimicking real-world physical transitions and spring inertia.

#### The Designer vs. Engineer Gap
* **Designers focus on:** Storytelling, emotional flow, timing, easing curves, and delightful micro-interactions (prototyped in Figma, After Effects, or Principle).
* **Engineers focus on:** Rendering performance, 60/120 FPS frame budgets, device fragmentation, maintainability, and implementation constraints.
* **The Conflict:** Friction arises when design expectations collide with platform limitations or verbose boilerplate.
* **The Solution:** Jetpack Compose acts as a common, shared language between design intent and engineering execution, enabling fluid motion without complex XML or fragile animator adapters.

<!-- [PLACEHOLDER: Insert Design System / Figma Token Reference Link here] -->

---

### 2. View System vs. Jetpack Compose Animation

#### The Legacy View System Challenge
In the traditional Android View system (XML/Java/Kotlin), implementing fluid, non-trivial animations was notoriously painful:
1. **Verbose XML & Multi-class Boilerplate:** Required `AnimatorSet`, `ValueAnimator`, `ObjectAnimator`, or custom XML animator files.
2. **State Desynchronization:** If an animation was interrupted or canceled, View state and underlying business data frequently went out of sync.
3. **The Lottie Shortcut:** Facing tight delivery deadlines, teams frequently defaulted to exporting Lottie JSON files. While Lottie simplified designer handoff, it introduced distinct trade-offs:
   * Difficult dynamic state integration (e.g. reacting dynamically to app theme colors or localized typography).
   * Increased APK asset footprint.
   * CPU/memory overhead when rendering multiple simultaneous vector layers.

#### The Jetpack Compose Advantage
In Jetpack Compose, the user interface is a pure function of state ($UI = f(State)$). Animation is no longer an external driver that imperatively mutates views; instead:
* Animation values are simply **state-driven values** that smoothly transition between initial and target states over time.
* Modifiers and composables react automatically to state changes, eliminating listener boilerplate and out-of-sync UI states.

```
[Legacy Android View System]
Event Trigger ──> Instantiate ObjectAnimator ──> Add Listeners ──> Mutate View Properties ──> Risk of Desync

[Jetpack Compose Declarative Motion]
State Mutation ──> Target State Changes ──> animate*AsState Interpolates Value ──> UI Automatically Updates
```

---

## Under the Hood: How Compose Animation Works

Duration: 0:10:00

Before writing animation code, understanding the internal mechanics of Compose's animation pipeline is crucial for writing smooth, 60/120 FPS animations without frame drops.

<!-- [PLACEHOLDER: Presentation Slide Reference for Frame Loop Diagram] -->

### 1. The Rendering Mechanics

A Compose animation is fundamentally a **time-driven value generator** synchronized directly with the platform display frame clock:

1. **MonotonicFrameClock:** The core Jetpack Compose coroutine context interface that provides a strictly monotonic time source synchronized to display frames.
2. **Platform Implementation:** On Android, `MonotonicFrameClock` connects directly to the Android **`Choreographer`**, receiving VSYNC pulses with nanosecond precision.

```
+-----------------------------------------------------------------------------------------+
|                    THE JETPACK COMPOSE ANIMATION FRAME LOOP                             |
|                                                                                         |
|       +-------------------------------------------------------------------------+       |
|       |                                                                         |       |
|       ▼                                                                         |       |
|  [VSYNC Pulse] ──> [Get Frame Time] ──> [Calculate Interpolation via Spec]     |       |
|                           (Choreographer)              (Easing & Duration)      |       |
|                                                                 │               |       |
|                                                                 ▼               |       |
|                    [Repeat Next Frame] ◄── [Render Frame] ◄── [Update State]    |       |
|                                             (Draw Phase)      (Snapshot State)  |       |
|                                                                                 |       |
+-----------------------------------------------------------------------------------------+
```

Every display frame:
1. Compose reads the current frame timestamp from the `Choreographer`.
2. Based on the elapsed play time and your animation curve/easing, it calculates the next interpolated value.
3. The Compose Snapshot state system updates with the new value.
4. The layout/draw system renders the new frame on screen.
5. The loop repeats until the target state is reached or canceled.

---

### 2. The 3-Tier API Layer Hierarchy

Jetpack Compose provides a stratified hierarchy of animation APIs, allowing you to choose the ideal balance between simplicity and granular control:

```
+-----------------------------------------------------------------------------------------+
|                            COMPOSE ANIMATION API HIERARCHY                              |
+-----------------------------------------------------------------------------------------+
|  HIGH-LEVEL APIs (Declarative & State-Driven)                                           |
|  • animate*AsState (animateFloatAsState, animateColorAsState, animateDpAsState)         |
|  • AnimatedVisibility (fadeIn, fadeOut, expandVertically, shrinkVertically)             |
|  • AnimatedContent, Crossfade                                                           |
+-----------------------------------------------------------------------------------------+
|  MIDDLE-LEVEL APIs (Multi-Property & State Orchestration)                               |
|  • updateTransition (Coordinated multi-value transitions based on state enum)          |
|  • rememberInfiniteTransition (Continuous loops: pulsing, rotating, marquee)            |
|  • Animatable (Imperative coroutine-based control with animateTo and snapTo)            |
+-----------------------------------------------------------------------------------------+
|  LOW-LEVEL APIs (Frame Timing & Physics Calculation)                                    |
|  • Animation, TargetBasedAnimation, DecayAnimation                                     |
|  • AnimationSpec (tween, spring, keyframes, repeatable, snap)                           |
+-----------------------------------------------------------------------------------------+
```

---

### 3. Core Categories of Animation in Jetpack Compose

| Category | Description | Primary APIs | Real-world Use Case in our App |
| :--- | :--- | :--- | :--- |
| **1. Simple Animation** | Smoothly interpolates a single property value between current and target state. | `animate*AsState()` (`Float`, `Color`, `Dp`, `Offset`) | Home Screen: Promo banner breathing scale and slider indicator size. |
| **2. Visibility Animation** | Animates the appearance or disappearance of UI components without manual height calculations. | `AnimatedVisibility`, `animateContentSize()` | Product Detail Screen: Expanding/collapsing product specifications. |
| **3. Content Animation** | Smoothly transitions between different content nodes based on a target state. | `AnimatedContent`, `Crossfade` | TopAppBar: Shopping cart badge count numeric transitions. |
| **4. Advance Animation** | Synchronizes multiple properties driven by a single unified parent state. | `updateTransition`, `Animatable` | Checkout Screen: Payment status card transitions (Idle $\rightarrow$ Processing $\rightarrow$ Success/Failed). |
| **5. Custom Animation** | Low-level imperative timing, custom curves, and continuous particle drawing. | `rememberInfiniteTransition`, `Canvas` | Checkout Success Screen: GPU-drawn confetti celebration. |

---

## Environment Setup & Project Architecture

Duration: 0:08:00

To give you a realistic software engineering experience, this codelab provides an e-commerce shopping journey that is already structured into screens and components. Your objective is to take the static UI and **sew animated components into their dedicated pages**.

<!-- [PLACEHOLDER: Repository Clone URL / Branch instructions] -->

### 1. Git Repository & Branches

This project includes pre-configured git branches:
* **`2026/starter`:** The starting branch containing the complete static UI, layouts, and `// TODO:` markers. This is where you will write your code!

To begin, ensure you are on the `2026/starter` branch:

```bash
git checkout 2026/starter
```

### 2. Project Requirements
* **Android Studio:** Android Studio Meerkat (2024.3+) or Ladybug.
* **JDK:** Java 17+.
* **Min SDK:** Android 7.0 (API level 24).
* **Target SDK:** Android 14 / 15 (API level 34+).
* **Compose BOM:** Already configured in `app/build.gradle.kts`.

### 3. Application Flow Overview

```
+------------------+         +-----------------------+         +------------------+         +-------------------------+
|                  |  Tap    |                       |  Tap    |                  | Success |                         |
|   HomeScreen     |───────> |  ProductDetailScreen  |───────> |  CheckoutScreen  |────────>|  CheckoutSuccessScreen  |
| (Banner & Badge) | Banner  | (Expandable Specs)    |Checkout | (State Machine)  | Payment | (Confetti Celebration)  |
+------------------+         +-----------------------+         +------------------+         +-------------------------+
```

```
app/src/main/java/com/sample/animation/
├── app/
│   └── MainActivity.kt        <-- Activity entry point
├── data/
│   ├── DummyData.kt
│   ├── Product.kt
│   └── PromoBanner.kt
├── navigation/
│   └── AppNavGraph.kt         <-- Wires destinations together
└── ui/
    ├── components/
    │   ├── BannerSlider.kt    <-- Module 1: Simple Animation
    │   └── CartBadge.kt       <-- Module 3: Content Animation
    ├── detail/
    │   └── ProductDetailScreen.kt <-- Module 2: Visibility & Content Size
    ├── home/
    │   └── HomeScreen.kt      <-- Hosts BannerSlider & CartBadge
    ├── checkout/
    │   └── CheckoutScreen.kt  <-- Module 4: Advance State Transition
    ├── success/
    │   └── CheckoutSuccessScreen.kt <-- Module 5: Infinite Canvas Animation
    └── theme/
```

---

## Module 1: Simple Animation — Breathing Banner & Page Indicators

Duration: 0:15:00

In this module, you will implement **Simple Value Animations** using Compose's `animate*AsState` API.

### 1. Visual Target & Motion Intent

Take a look at the target motion for the home banner slider:

![Banner](./assets/banner.gif)

Notice two subtle motion behaviors:
1. **Breathing Banner:** As the active banner changes every 2 seconds, the image card smoothly breathes in (`scale = 1.04f`) and shifts horizontal offset (`offset = 8f`), then relaxes back (`scale = 1f`, `offset = -8f`) using an 800ms tween curve.
2. **Active Page Indicator:** The dot corresponding to the active index scales up to `1.2f`, while inactive dots scale down to `0.8f` over 250ms.

---

### 2. Page Context: Sewing BannerSlider into HomeScreen

Open `HomeScreen.kt`. Notice how `HomeScreen` owns the state `currentIndex` and automatically advances it every 2 seconds via `LaunchedEffect`:

### app/src/main/java/com/sample/animation/ui/home/HomeScreen.kt
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    cartCount: Int,
    onBannerClick: () -> Unit,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(currentIndex) {
        delay(2000)
        currentIndex = (currentIndex + 1) % promoBanners.size
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Compose Animation Training", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "Home",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light)
                        )
                    }
                },
                actions = {
                    CartBadge(count = cartCount, onClick = onCartClick)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            BannerSlider(
                banners = promoBanners,
                currentIndex = currentIndex,
                onBannerClick = { onBannerClick() },
                onIndicatorSelect = { index -> currentIndex = index },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
```

---

### 3. Hands-on Exercise: Implement BannerSlider Animations

Now open `ui/components/BannerSlider.kt`. Currently, `scale`, `offset`, and `indicatorScale` are static dummy states!

```kotlin
// In 2026/starter (ui/components/BannerSlider.kt):
// =========================================================================
// TODO: 1.1 Simple animation for sliding and breathing banner effects.
// Replace these static states with `animateFloatAsState` driven by `breathe`.
// - Scale target: 1.04f when breathing, 1f when relaxed (tween duration 800ms)
// - Offset target: 8f when breathing, -8f when relaxed (tween duration 800ms)
// =========================================================================
val scale = remember { mutableStateOf(1f) }
val offset = remember { mutableStateOf(0f) }
...
// =========================================================================
// TODO: 1.2 Simple animation for page indicators effects.
// Replace this static state with `animateFloatAsState`.
// - Scale target: 1.2f when active (index == currentIndex), 0.8f otherwise (tween 250ms)
// =========================================================================
val indicatorScale = remember { mutableStateOf(1f) }
```

#### Step-by-Step Implementation:

1. Import `animateFloatAsState` and `tween` from `androidx.compose.animation.core.*`.
2. Replace static `scale` and `offset` with `animateFloatAsState`. Driven by the `breathe` boolean state, interpolate scale between `1.04f` and `1f`, and offset between `8f` and `-8f` with `tween(durationMillis = 800)`.
3. Replace `indicatorScale` with `animateFloatAsState`. When `index == currentIndex`, scale to `1.2f`, otherwise `0.8f`, with `tween(250)`.

### app/src/main/java/com/sample/animation/ui/components/BannerSlider.kt
```kotlin
package com.sample.animation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.sample.animation.data.PromoBanner
import kotlinx.coroutines.delay

@Composable
fun BannerSlider(
    banners: List<PromoBanner>,
    currentIndex: Int,
    onBannerClick: (PromoBanner) -> Unit,
    onIndicatorSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (banners.isEmpty()) return
    val activeBanner = banners[currentIndex]
    var breathe by remember { mutableStateOf(false) }

    LaunchedEffect(activeBanner) {
        while (true) {
            breathe = !breathe
            delay(1200)
        }
    }

    // 1.1 Simple animation for sliding and breathing banner effects
    val scale = animateFloatAsState(
        targetValue = if (breathe) 1.04f else 1f,
        animationSpec = tween(durationMillis = 800),
        label = "BannerScale"
    )
    val offset = animateFloatAsState(
        targetValue = if (breathe) 8f else -8f,
        animationSpec = tween(durationMillis = 800),
        label = "BannerOffset"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .graphicsLayer(
                    translationX = offset.value,
                    scaleX = scale.value,
                    scaleY = scale.value
                )
                .clickable { onBannerClick(activeBanner) },
            colors = CardDefaults.cardColors(containerColor = activeBanner.color),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = activeBanner.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "Discover great offers today",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            banners.forEachIndexed { index, _ ->
                // 1.2 Simple animation for page indicators
                val indicatorScale = animateFloatAsState(
                    targetValue = if (index == currentIndex) 1.2f else 0.8f,
                    animationSpec = tween(250),
                    label = "IndicatorScale"
                )
                val color = if (index == currentIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                Spacer(
                    modifier = Modifier
                        .size(10.dp)
                        .graphicsLayer(
                            scaleX = indicatorScale.value,
                            scaleY = indicatorScale.value
                        )
                        .clickable { onIndicatorSelect(index) }
                        .background(color, shape = RoundedCornerShape(50))
                )
            }
        }
    }
}
```

> aside positive
> **Performance Tip:** Notice that `scale` and `offset` are applied inside `Modifier.graphicsLayer { ... }` rather than `Modifier.size()` or `Modifier.offset(dp)`. This ensures that animations execute exclusively during the **Draw Phase**, without triggering expensive re-measurement of the parent layout!

<!-- [PLACEHOLDER: Add checkpoint / Live Demo tip for Module 1 here] -->

---

## Module 2: Visibility & Content Size — Expandable Product Specs

Duration: 0:15:00

In this module, you will learn how to animate content entering and leaving the composition using `AnimatedVisibility`, combined with `Modifier.animateContentSize()`.

### 1. Visual Target & Motion Intent

Take a look at the product detail expandable motion:

![Expandable](./assets/expandables.gif)

Notice:
* Clicking the "More Info" button expands the additional specification bullet points (`Warranty`, `Shipping`).
* The items don't abruptly pop into existence; they **fade in** while simultaneously **expanding vertically** (`fadeIn() + expandVertically()`).
* On collapse, they **fade out** and **shrink vertically** (`fadeOut() + shrinkVertically()`).
* The surrounding card smoothly adjusts its height to avoid clipping or jarring layout pops via `Modifier.animateContentSize()`.

---

### 2. Page Context: Sewing Expandable Specs into ProductDetailScreen

Open `ProductDetailScreen.kt`. Notice the structure:
* The screen displays the product title, price, primary specs, and an "Add to Cart" button.
* Underneath the primary specs, there is a toggle button:
  `Button(onClick = { showMoreInfo = !showMoreInfo })`
* In `2026/starter`, the extra info column is statically displayed or toggled instantaneously with no animation:

```kotlin
// In 2026/starter (ui/detail/ProductDetailScreen.kt):
// =========================================================================
// TODO: 2.1 Visibility animation with `fadeIn`/`fadeOut` and
// `expandVertically`/`shrinkVertically`, wrapped in `animateContentSize()`.
// Default state should start collapsed (false) when implementing.
// =========================================================================
var showMoreInfo by remember { mutableStateOf(true) }
...
// =========================================================================
// TODO: 2.2 Wrap this content with AnimatedVisibility:
//   visible = showMoreInfo,
//   enter = fadeIn() + expandVertically(),
//   exit = fadeOut() + shrinkVertically()
//
// And add Modifier.animateContentSize() to the Column to ensure
// smooth container height transitions without clipping or layout jumping.
// =========================================================================
Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
    verticalArrangement = Arrangement.spacedBy(6.dp)
) {
    Text(
        text = "Experience immersive sound with plush ear cushions and adaptive noise cancelling.",
        style = MaterialTheme.typography.bodyMedium
    )
    Text(text = "Warranty: 1 year", style = MaterialTheme.typography.bodySmall)
    Text(text = "Ships in 2-3 business days", style = MaterialTheme.typography.bodySmall)
}
```

---

### 3. Hands-on Exercise: Implement AnimatedVisibility & Content Size

#### Step-by-Step Implementation:

1. In `ProductDetailScreen.kt`, change the initial value of `showMoreInfo` to `false`.
2. Wrap the secondary specification `Column` inside `AnimatedVisibility(visible = showMoreInfo)`.
3. Set composite enter and exit transitions:
   * `enter = fadeIn() + expandVertically()`
   * `exit = fadeOut() + shrinkVertically()`
4. Add `Modifier.animateContentSize()` to the inner `Column` to ensure smooth container height resizing.

### app/src/main/java/com/sample/animation/ui/detail/ProductDetailScreen.kt
```kotlin
package com.sample.animation.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sample.animation.data.Product
import com.sample.animation.ui.components.CartBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    cartCount: Int,
    onBack: () -> Unit,
    onAddToCart: () -> Unit,
    onCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 2.1 Default state is collapsed (false)
    var showMoreInfo by remember { mutableStateOf(false) }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            TopAppBar(
                title = { Text(text = product.name, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    CartBadge(count = cartCount, onClick = onCheckout)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = product.color.copy(alpha = 0.2f),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = product.name, style = MaterialTheme.typography.headlineSmall)
                    Text(text = "Rp ${product.price}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
                    Text(text = product.description, style = MaterialTheme.typography.bodyMedium)
                    HorizontalDivider()
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "Specs", style = MaterialTheme.typography.titleSmall)
                        product.specs.forEach { spec ->
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                                Text(text = spec, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { showMoreInfo = !showMoreInfo },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(text = if (showMoreInfo) "Hide More Info" else "More Info")
                    }

                    // 2.2 AnimatedVisibility with composite transitions + animateContentSize()
                    AnimatedVisibility(
                        visible = showMoreInfo,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .animateContentSize(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Experience immersive sound with plush ear cushions and adaptive noise cancelling.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(text = "Warranty: 1 year", style = MaterialTheme.typography.bodySmall)
                            Text(text = "Ships in 2-3 business days", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onAddToCart,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Add to Cart")
                }
                Button(
                    onClick = onCheckout,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = "Checkout")
                }
            }
        }
    }
}
```

> aside negative
> **Common Pitfall:** Don't forget that `AnimatedVisibility` controls whether the child composable is part of the composition. If you only apply `animateContentSize()` without `AnimatedVisibility`, an empty `Column` might still consume layout space or jump abruptly when items are conditionally added.

---

## Module 3: Content Animation — Animated Cart Counter

Duration: 0:15:00

In this module, you will master `AnimatedContent` for smooth transitions between different data values (e.g., number increments).

### 1. Visual Target & Motion Intent

Take a look at the shopping cart badge animation:

![Badge](./assets/badge.gif)

Notice:
* Whenever the user taps **"Add to Cart"**, the badge count increments ($1 \rightarrow 2 \rightarrow 3$).
* Instead of the text instantly changing, the new number **slides in from the bottom** while scaling and fading in.
* The old number simultaneously **slides out toward the top**, scaling down and fading out.

---

### 2. Page Context: Sewing CartBadge across App Screens

Notice how `CartBadge` is reused consistently in the `TopAppBar` across three screens in our app:
1. `HomeScreen` (line 57): `CartBadge(count = cartCount, onClick = onCartClick)`
2. `ProductDetailScreen` (line 67): `CartBadge(count = cartCount, onClick = onCheckout)`
3. `CheckoutScreen` (line 123): `CartBadge(count = cartCount, onClick = {})`

All screens read `cartCount` from the top-level `AppNavGraph.kt` state:
```kotlin
// In MainActivity.kt / AppNavGraph.kt:
var cartCount by rememberSaveable { mutableIntStateOf(0) }
```
When `CartBadge` updates, every screen using it automatically inherits the animation!

---

### 3. Hands-on Exercise: Implement AnimatedContent in CartBadge

Open `ui/components/CartBadge.kt`. Currently, it displays the count inside a static `Box`:

```kotlin
// In 2026/starter (ui/components/CartBadge.kt):
// =========================================================================
// TODO: 3.1 Content animation for number transitions with slide, fade, and scale.
// Wrap the count display inside `AnimatedContent(targetState = count, label = "CartCount")`.
// Define transitionSpec using `togetherWith`:
//   (slideInVertically { it } + fadeIn(tween(300)) + scaleIn()) togetherWith
//   (slideOutVertically { -it } + fadeOut(tween(300)) + scaleOut())
// Make sure to render the lambda value { value -> Text(text = "$value", ...) }
// =========================================================================
BadgedBox(
    badge = {
        if (count > 0) {
            Badge(containerColor = MaterialTheme.colorScheme.secondary) {
                Box(modifier = Modifier.size(16.dp), contentAlignment = Alignment.Center) {
                    Text(text = "$count", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    },
    modifier = modifier
)
```

#### Step-by-Step Implementation:

1. Import `AnimatedContent`, `slideInVertically`, `slideOutVertically`, `scaleIn`, `scaleOut`, `fadeIn`, `fadeOut`, and `togetherWith` from `androidx.compose.animation.*`.
2. Wrap the count `Box` inside `AnimatedContent(targetState = count, label = "CartCount")`.
3. Construct the `transitionSpec`:
   * **Enter Transition:** `slideInVertically { it } + fadeIn(tween(300)) + scaleIn()`
   * **Exit Transition:** `slideOutVertically { -it } + fadeOut(tween(300)) + scaleOut()`
   * Combine them using the modern infix operator `togetherWith`.

### app/src/main/java/com/sample/animation/ui/components/CartBadge.kt
```kotlin
package com.sample.animation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CartBadge(
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BadgedBox(
        badge = {
            if (count > 0) {
                Badge(containerColor = MaterialTheme.colorScheme.secondary) {
                    // 3.1 AnimatedContent with custom slide + fade + scale specs
                    AnimatedContent(
                        targetState = count,
                        transitionSpec = {
                            (slideInVertically { it } + fadeIn(animationSpec = tween(300)) + scaleIn()) togetherWith
                                (slideOutVertically { -it } + fadeOut(animationSpec = tween(300)) + scaleOut())
                        },
                        label = "CartCount"
                    ) { value ->
                        Box(modifier = Modifier.size(16.dp), contentAlignment = Alignment.Center) {
                            Text(text = "$value", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        },
        modifier = modifier
    ) {
        IconButton(onClick = onClick) {
            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart")
        }
    }
}
```

> aside positive
> **Pro Tip on AnimatedContent:** Notice the lambda parameter `{ value -> ... }`. Always render `value` (the animated target/initial value provided by the lambda scope) rather than referencing the external `count` variable directly. This guarantees that Compose animates out the old number while animating in the new number.

---

## Module 4: Advance State Transition & Error Shake — Checkout Flow

Duration: 0:20:00

In this module, you will tackle multi-property state orchestration using `updateTransition`, plus imperative physical error feedback using `Animatable` with `keyframes`.

### 1. Visual Target & Motion Intent

Take a look at the two possible checkout outcomes:

#### Payment Approved (Success):
![Success](./assets/update-transition-success.gif)

#### Payment Rejected (Failed):
![Failed](./assets/update-transition-failed.gif)

Observe the motion design:
1. **Multi-property Orchestration (`updateTransition`):**
   * **Idle:** White surface, 24dp corner radius, 1.0x scale, 0° rotation.
   * **Processing:** Secondary container color, 32dp corner radius, 1.05x scale, +2° rotation tilt.
   * **Success:** Vibrant green background (`#B2FF59`), 20dp corner radius, 1.02x scale, 0° rotation.
   * **Failed:** Soft red background (`#FFCDD2`), 18dp corner radius, 0.98x scale, -2° rotation.
2. **Keyframe Horizontal Shake:**
   * When payment fails, the card violently shakes horizontally (-10px $\rightarrow$ +10px $\rightarrow$ -6px $\rightarrow$ +3px $\rightarrow$ 0px) over a 500ms duration, giving visceral feedback of failure before returning to rest.

---

### 2. Page Context: Sewing the State Card into CheckoutScreen

Open `CheckoutScreen.kt`. Notice the checkout state machine:
```kotlin
enum class CheckoutState { Idle, Processing, Success, Failed }
```
When the user taps the **"Pay Now"** button, a coroutine launches to simulate network verification:
```kotlin
scope.launch {
    state = CheckoutState.Processing
    delay(2500)
    state = if (Random.nextFloat() <= 0.8f) CheckoutState.Success else CheckoutState.Failed
    if (state == CheckoutState.Success) {
        delay(600)
        onSuccess() // Navigates to CheckoutSuccessScreen
    }
}
```

In `2026/starter`, the card color, corner radius, scale, and rotation are bound to static `when` expressions, and the shake animation block is empty:

```kotlin
// In 2026/starter (ui/checkout/CheckoutScreen.kt):
// =========================================================================
// TODO: 4.1 Synchronized multi-property state transitions with `updateTransition`.
// Replace these static `when` expressions by declaring:
//   val transition = updateTransition(targetState = state, label = "CheckoutTransition")
//
// Then animate each property from `transition`:
//   - backgroundColor via transition.animateColor(label = "BackgroundColor") { ... }
//   - cornerRadius via transition.animateFloat(label = "CornerRadius") { ... }
//   - scale via transition.animateFloat(label = "Scale") { ... }
//   - rotation via transition.animateFloat(label = "Rotation") { ... }
// =========================================================================
val backgroundColor = when (state) {
    CheckoutState.Idle -> MaterialTheme.colorScheme.surface
    CheckoutState.Processing -> MaterialTheme.colorScheme.secondaryContainer
    CheckoutState.Success -> Color(0xFFB2FF59)
    CheckoutState.Failed -> Color(0xFFFFCDD2)
}
val cornerRadius = when (state) {
    CheckoutState.Idle -> 24f
    CheckoutState.Processing -> 32f
    CheckoutState.Success -> 20f
    CheckoutState.Failed -> 18f
}
val scale = when (state) {
    CheckoutState.Processing -> 1.05f
    CheckoutState.Success -> 1.02f
    CheckoutState.Failed -> 0.98f
    else -> 1f
}
val rotation = when (state) {
    CheckoutState.Processing -> 2f
    CheckoutState.Failed -> -2f
    else -> 0f
}

// =========================================================================
// TODO: 4.2 Imperative physics-based shake on payment failure.
// Inside LaunchedEffect(state):
//   - If state == CheckoutState.Failed, animate `shake.animateTo(0f)` using
//     keyframes:
//       durationMillis = 500
//       -10f at 0
//       10f at 100
//       -6f at 200
//       3f at 300
//       0f at 500
//   - Otherwise, snap back: shake.snapTo(0f)
// =========================================================================
val shake = remember { Animatable(0f) }
LaunchedEffect(state) {
    // Implement shake keyframes here
}
```

---

### 3. Hands-on Exercise: Implement updateTransition and Shake Animatable

#### Step-by-Step Implementation:

1. Create a `transition = updateTransition(targetState = state, label = "CheckoutTransition")`.
2. Animate four properties from `transition`:
   * `backgroundColor` using `transition.animateColor`
   * `cornerRadius` using `transition.animateFloat`
   * `scale` using `transition.animateFloat`
   * `rotation` using `transition.animateFloat`
3. Declare `val shake = remember { Animatable(0f) }`.
4. Inside `LaunchedEffect(state)`:
   * If `state == CheckoutState.Failed`, animate `shake` using `keyframes` over 500ms:
     `-10f at 0`, `10f at 100`, `-6f at 200`, `3f at 300`, `0f at 500`.
   * Otherwise, snap `shake` back to `0f` using `shake.snapTo(0f)`.
5. Apply these animated values to `Modifier.graphicsLayer` and `Surface`.

### app/src/main/java/com/sample/animation/ui/checkout/CheckoutScreen.kt
```kotlin
package com.sample.animation.ui.checkout

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.sample.animation.ui.components.CartBadge
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class CheckoutState { Idle, Processing, Success, Failed }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartCount: Int,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var state by remember { mutableStateOf(CheckoutState.Idle) }
    val scope = rememberCoroutineScope()

    // 4.1 Coordinated multi-property transition based on state
    val transition = updateTransition(targetState = state, label = "CheckoutTransition")

    val backgroundColor by transition.animateColor(label = "BackgroundColor") { target ->
        when (target) {
            CheckoutState.Idle -> MaterialTheme.colorScheme.surface
            CheckoutState.Processing -> MaterialTheme.colorScheme.secondaryContainer
            CheckoutState.Success -> Color(0xFFB2FF59)
            CheckoutState.Failed -> Color(0xFFFFCDD2)
        }
    }

    val cornerRadius by transition.animateFloat(label = "CornerRadius") { target ->
        when (target) {
            CheckoutState.Idle -> 24f
            CheckoutState.Processing -> 32f
            CheckoutState.Success -> 20f
            CheckoutState.Failed -> 18f
        }
    }

    val scale by transition.animateFloat(label = "Scale") { target ->
        when (target) {
            CheckoutState.Processing -> 1.05f
            CheckoutState.Success -> 1.02f
            CheckoutState.Failed -> 0.98f
            else -> 1f
        }
    }

    val rotation by transition.animateFloat(label = "Rotation") { target ->
        when (target) {
            CheckoutState.Processing -> 2f
            CheckoutState.Failed -> -2f
            else -> 0f
        }
    }

    // 4.2 Imperative physics-based shake on failure
    val shake = remember { Animatable(0f) }
    LaunchedEffect(state) {
        if (state == CheckoutState.Failed) {
            shake.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 500
                    -10f at 0
                    10f at 100
                    -6f at 200
                    3f at 300
                    0f at 500
                }
            )
        } else {
            shake.snapTo(0f)
        }
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            TopAppBar(
                title = { Text(text = "Checkout") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    CartBadge(count = cartCount, onClick = {})
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(
                        translationX = shake.value,
                        scaleX = scale,
                        scaleY = scale,
                        rotationZ = rotation
                    )
                    .clip(RoundedCornerShape(cornerRadius.dp))
                    .padding(4.dp),
                color = backgroundColor,
                tonalElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Status: ${state.name}", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = when (state) {
                            CheckoutState.Idle -> "Ready to process your payment."
                            CheckoutState.Processing -> "Processing payment, please wait..."
                            CheckoutState.Success -> "Payment approved!"
                            CheckoutState.Failed -> "Payment failed. Please try again."
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = {
                        if (state == CheckoutState.Processing) return@Button
                        scope.launch {
                            state = CheckoutState.Processing
                            delay(2500)
                            state = if (Random.nextFloat() <= 0.8f) CheckoutState.Success else CheckoutState.Failed
                            if (state == CheckoutState.Success) {
                                delay(600)
                                onSuccess()
                            }
                        }
                    },
                    enabled = state != CheckoutState.Success,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = if (state == CheckoutState.Processing) "Processing..." else "Pay Now")
                }
            }
        }
    }
}
```

> aside positive
> **Why updateTransition?** When you have multiple properties driven by a single state (like `CheckoutState`), `updateTransition` guarantees that all properties animate simultaneously with full awareness of interrupts. Even better, Android Studio's **Animation Preview** can inspect and scrub through `updateTransition` frame-by-frame!

---

## Module 5: Custom Infinite Canvas Animation — Confetti Celebration

Duration: 0:15:00

In this module, you will implement a **Custom Continuous Animation** combining `rememberInfiniteTransition` with direct GPU rendering using `Canvas`.

### 1. Visual Target & Motion Intent

Take a look at the checkout celebration:

![Confetti](./assets/confetti.gif)

Notice:
* Once payment succeeds, the app navigates to `CheckoutSuccessScreen`.
* Multiple colorful geometric particles (circles, rectangles, triangles) fall continuously from top to bottom.
* Each piece moves at its own distinct speed, rotates dynamically along its axis, and wraps around when reaching the bottom.

---

### 2. Page Context: Sewing Confetti Canvas into CheckoutSuccessScreen

Open `CheckoutSuccessScreen.kt`. Notice how the screen generates a list of randomized particles:
```kotlin
data class ConfettiItem(
    val xPosition: Float,
    val size: Float,
    val speed: Float,
    val color: Color,
    val shape: ConfettiShape
)
enum class ConfettiShape { Circle, Rectangle, Triangle }
```
In `2026/starter`, `val animationProgress by remember { mutableStateOf(0f) }` is static, so the confetti remains frozen:

```kotlin
// In 2026/starter (ui/success/CheckoutSuccessScreen.kt):
// =========================================================================
// TODO: 5.1 Continuous progress using rememberInfiniteTransition:
// - Declare val infiniteTransition = rememberInfiniteTransition(label = "ConfettiTransition")
// - Animate `animationProgress` from 0f to 1f using:
//     infiniteRepeatable(
//         animation = tween(durationMillis = 4000, easing = LinearEasing)
//     )
// =========================================================================
val animationProgress by remember { mutableStateOf(0f) }

...
// Inside Canvas:
// =========================================================================
// TODO: 5.2 Dynamic particle wrapping with modulo:
// - Calculate yOffset = (size.height * animationProgress + piece.speed * index) % size.height
// - Calculate rotation = (animationProgress * 360f + index * 15) % 360f
// =========================================================================
val yOffset = size.height * animationProgress
val x = piece.xPosition * size.width
val rotation = animationProgress * 360f
```

---

### 3. Hands-on Exercise: Implement Infinite Canvas Confetti

#### Step-by-Step Implementation:

1. Declare `val infiniteTransition = rememberInfiniteTransition(label = "ConfettiTransition")`.
2. Animate `animationProgress` from `0f` to `1f` using:
   `infiniteRepeatable(animation = tween(durationMillis = 4000, easing = LinearEasing))`.
3. Inside the `Canvas` draw loop, calculate dynamic offsets for each piece:
   * `yOffset = (size.height * animationProgress + piece.speed * index) % size.height`
   * `x = piece.xPosition * size.width`
   * `rotation = (animationProgress * 360f + index * 15) % 360f`
4. Use `rotate(degrees = rotation, pivot = Offset(x, yOffset))` to draw circles, rectangles, and triangle paths.

### app/src/main/java/com/sample/animation/ui/success/CheckoutSuccessScreen.kt
```kotlin
package com.sample.animation.ui.success

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.random.Random

data class ConfettiItem(
    val xPosition: Float,
    val size: Float,
    val speed: Float,
    val color: Color,
    val shape: ConfettiShape
)

enum class ConfettiShape { Circle, Rectangle, Triangle }

@Composable
fun CheckoutSuccessScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val confetti = remember {
        List(Random.nextInt(15, 25)) {
            ConfettiItem(
                xPosition = Random.nextFloat(),
                size = Random.nextFloat().coerceIn(6f, 18f),
                speed = Random.nextFloat().coerceIn(30f, 90f),
                color = listOf(
                    Color(0xFFFF6F61),
                    Color(0xFF64B5F6),
                    Color(0xFF81C784),
                    Color(0xFFFFD54F)
                ).random(),
                shape = ConfettiShape.values().random()
            )
        }
    }

    // 5.1 Continuous progress using rememberInfiniteTransition
    val infiniteTransition = rememberInfiniteTransition(label = "ConfettiTransition")
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing)
        ),
        label = "Progress"
    )

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Payment Successful!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    confetti.forEachIndexed { index, piece ->
                        // Modulo ensures continuous wrapping across the height
                        val yOffset = (size.height * animationProgress + piece.speed * index) % size.height
                        val x = piece.xPosition * size.width
                        val rotation = (animationProgress * 360f + index * 15) % 360f

                        rotate(degrees = rotation, pivot = Offset(x, yOffset)) {
                            when (piece.shape) {
                                ConfettiShape.Circle -> drawCircle(
                                    color = piece.color,
                                    radius = piece.size,
                                    center = Offset(x, yOffset)
                                )
                                ConfettiShape.Rectangle -> drawRect(
                                    color = piece.color,
                                    topLeft = Offset(x - piece.size / 2, yOffset - piece.size / 2),
                                    size = androidx.compose.ui.geometry.Size(piece.size, piece.size * 1.4f)
                                )
                                ConfettiShape.Triangle -> {
                                    val path = Path().apply {
                                        moveTo(x, yOffset - piece.size)
                                        lineTo(x - piece.size, yOffset + piece.size)
                                        lineTo(x + piece.size, yOffset + piece.size)
                                        close()
                                    }
                                    drawPath(path = path, color = piece.color)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onContinue) {
                Text(text = "Back to Home")
            }
        }
    }
}
```

---

## Best Practices & Performance Optimization

Duration: 0:10:00

Creating smooth 60/120 FPS animations requires mastering how Compose renders frames under the hood.

### 1. Performance: Draw Phase vs. Layout Phase

The single most impactful optimization in Compose animation is **deferring state reads to the Draw Phase**:

```
+-----------------------------------------------------------------------------------------+
|                  COMPOSE RENDERING PHASES & ANIMATION PERFORMANCE                       |
+-----------------------------------------------------------------------------------------+
| Phase 1: COMPOSITION   ──> Builds UI Node Tree (Recomposition)                          |
| Phase 2: LAYOUT        ──> Measures child nodes & determines (x, y) coordinates         |
| Phase 3: DRAWING       ──> Emits GPU draw commands to display pixels                    |
+-----------------------------------------------------------------------------------------+
```

| Technique | Phase Involved | Recomposition Impact | Usage Context |
| :--- | :--- | :--- | :--- |
| **`Modifier.graphicsLayer`** | **Draw Phase only** | **Zero Recompositions!** (Bypasses Layout & Composition completely) | Scale, alpha, rotation, translation that does NOT alter adjacent layout positioning (e.g. our Banners, Cart Badge, and Checkout Shake). |
| **Layout / Size Modifiers** (`Modifier.size`, `Modifier.offset(dp)`) | **Measurement, Layout & Draw Phase** | **High Overhead** (Triggers structural layout recalculation) | Expanding/collapsing sections that intentionally shift neighboring UI elements (e.g. our Expandable Specs). |

```kotlin
// POOR: Recomposes on every single frame!
Box(modifier = Modifier.offset(x = animatedOffset.dp))

// OPTIMAL: Executes purely in the Draw Phase without recomposition!
Box(modifier = Modifier.graphicsLayer { translationX = animatedOffset })
```

---

### 2. Architectural Patterns & Reusable Modifiers

* **Encapsulate Common Animations:** Package frequently reused animations (such as our payment card shake) into custom reusable modifiers:
```kotlin
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.keyframes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.requireDensity
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ShakeController {
    internal var node: ShakeNode? = null

    fun shake() {
        node?.shake()
    }
}

@Composable
fun rememberShakeController(): ShakeController {
    return remember { ShakeController() }
}

fun Modifier.shake(
    controller: ShakeController,
    shakeOffset: Dp = 10.dp,
    durationMillis: Int = 400
): Modifier = this.then(
    ShakeElement(
        controller = controller,
        shakeOffset = shakeOffset,
        durationMillis = durationMillis
    )
)

private data class ShakeElement(
    val controller: ShakeController,
    val shakeOffset: Dp,
    val durationMillis: Int
) : ModifierNodeElement<ShakeNode>() {
    override fun create(): ShakeNode {
        return ShakeNode(controller, shakeOffset, durationMillis)
    }

    override fun update(node: ShakeNode) {
        node.controller = controller
        node.shakeOffset = shakeOffset
        node.durationMillis = durationMillis
        controller.node = node
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "shake"
        properties["shakeOffset"] = shakeOffset
        properties["durationMillis"] = durationMillis
    }
}

class ShakeNode(
    var controller: ShakeController,
    var shakeOffset: Dp,
    var durationMillis: Int
) : Modifier.Node(), LayoutModifierNode {
    private val animatable = Animatable(0f)

    override fun onAttach() {
        super.onAttach()
        controller.node = this
    }

    override fun onDetach() {
        super.onDetach()
        if (controller.node == this) {
            controller.node = null
        }
    }

    fun shake() {
        coroutineScope.launch {
            val offsetPx = with(requireDensity()) { shakeOffset.toPx() }

            // Keyframe sequence for a quick left-right decaying shake
            val animationSpec: AnimationSpec<Float> = keyframes {
                durationMillis = this@ShakeNode.durationMillis
                0.0f at 0
                -offsetPx at (durationMillis * 0.15f).toInt()
                offsetPx at (durationMillis * 0.30f).toInt()
                (-offsetPx * 0.7f) at (durationMillis * 0.45f).toInt()
                (offsetPx * 0.7f) at (durationMillis * 0.60f).toInt()
                (-offsetPx * 0.3f) at (durationMillis * 0.75f).toInt()
                0.0f at durationMillis
            }
            animatable.snapTo(0f)
            animatable.animateTo(
                targetValue = 0f,
                animationSpec = animationSpec
            )
        }
    }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)

        return layout(placeable.width, placeable.height) {
            // Apply offset on the placement layer to prevent recomposition/relayout
            placeable.placeWithLayer(
                x = animatable.value.roundToInt(),
                y = 0
            )
        }
    }
}
```
* **Prefer `Modifier.Node` over `composed {}`:** Modern Google guidelines advise replacing legacy `composed { ... }` wrappers with `Modifier.Node` implementations. `Modifier.Node` allocates once and updates directly, eliminating expensive recomposition tracking overhead.

---

### 3. Tooling: Android Studio Animation Preview

Android Studio features a dedicated **Animation Preview** inspector:
1. Annotate your preview composables with `@Preview`.
2. Click the **Start Animation Preview** icon (fan-shaped icon) next to the Preview canvas.
3. You can inspect animations driven by `updateTransition` or `AnimatedVisibility`, scrub through the timeline frame-by-frame, and inspect individual curve easings in slow motion.

---

### 4. Rules of Thumb for Motion Engineering

* **Purposeful Motion:** Use motion to clarify hierarchy, affordances, and state changes. Never animate purely for decoration if it slows down user workflows.
* **Optimal Durations:** Keep micro-interactions tight, typically between **$150\text{ ms}$ and $250\text{ ms}$**. Long animations ($>400\text{ ms}$) make apps feel sluggish to daily users.
* **Natural Easing:** Favor physical spring curves (`spring()`) for interactive touch feedback, and smooth cubic-bezier (`tween(easing = FastOutSlowInEasing)`) for transitions.
* **Avoid Over-Animation:** Don't animate every property simultaneously; choose one or two focal properties (e.g. scale and alpha).
* **State-Driven Integrity:** Always tie motion to definitive state changes rather than fire-and-forget imperative callbacks.
* **Low-End Hardware Profiling:** Always profile animations on lower-tier target devices using the Android Studio Profiler (GPU rendering inspection).

<!-- [PLACEHOLDER: Add enterprise-specific coding standards or lint checks here] -->

---

## Interactive Self-Evaluation Quiz & Workshop Wrap-Up

Duration: 0:10:00

Test your knowledge with these 10 questions covering everything learned today:

### Interactive Self-Evaluation Quiz

#### Question 1: Frame Timing & Mechanics
Which Android platform component does Jetpack Compose's `MonotonicFrameClock` interface connect with to synchronize display frame timestamps with nanosecond precision?

* [ ] `AlarmManager`
* [ ] `WorkManager`
* [x] `Choreographer`
* [ ] `SensorManager`

> aside positive
> **Explanation 1:** On Android, `MonotonicFrameClock` leverages `Choreographer` to obtain synchronized VSYNC frame timing directly from the display hardware.

#### Question 2: Simple Animation API Selection
You want to animate a button's elevation from 2.dp to 8.dp whenever it is hovered or pressed. Which API is the simplest and most idiomatic choice?

* [ ] `rememberInfiniteTransition`
* [x] `animateDpAsState`
* [ ] `TargetBasedAnimation`
* [ ] `updateTransition`

> aside positive
> **Explanation 2:** `animate*AsState` (here `animateDpAsState`) is the high-level declarative API designed specifically for smooth interpolation of single property values between states.

#### Question 3: Container Height Animation
In our Product Detail screen, which modifier did we apply to the specification container to ensure that the surrounding card smoothly animated its height changes without abrupt layout jumps?

* [ ] `Modifier.fillMaxSize()`
* [ ] `Modifier.graphicsLayer()`
* [x] `Modifier.animateContentSize()`
* [ ] `Modifier.clipToBounds()`

> aside positive
> **Explanation 3:** `Modifier.animateContentSize()` automatically tracks size changes of its child content across recompositions and smoothly animates the container's width and height.

#### Question 4: Numeric Badge Counter Transitions
When building the shopping cart badge in Module 3, why did we choose `AnimatedContent` over `Crossfade`?

* [ ] Because `Crossfade` cannot render text composables
* [x] Because `AnimatedContent` allows combining slide, fade, and scale transitions via `transitionSpec`, whereas `Crossfade` only supports opacity fading
* [ ] Because `AnimatedContent` runs on background coroutine dispatchers
* [ ] Because `Crossfade` requires root activity permissions

> aside positive
> **Explanation 4:** While `Crossfade` only transitions opacity, `AnimatedContent` allows rich, customizable spatial transitions including vertical slides, scaling, and custom duration specs.

#### Question 5: Multi-Property State Synchronization
Which Compose API is specifically designed to synchronize multiple animated properties (such as color, corner radius, scale, and rotation) driven by a single unified parent enum state?

* [ ] `Animatable`
* [ ] `animateFloatAsState`
* [x] `updateTransition`
* [ ] `infiniteRepeatable`

> aside positive
> **Explanation 5:** `updateTransition` coordinates multiple child animations driven by a single target state object, ensuring all properties interpolate in lockstep with full Android Studio Animation Preview support.

#### Question 6: Imperative Error Shake Feedback
When creating the card shake effect upon checkout failure in Module 4, why was `Animatable` combined with `keyframes` used instead of `animateFloatAsState`?

* [ ] `animateFloatAsState` does not support float numbers
* [x] `Animatable` allows imperative coroutine execution (`animateTo`, `snapTo`) with complex multi-timestamp `keyframes`, ideal for non-linear physical shakes
* [ ] `Animatable` can only be used on Android TV
* [ ] `keyframes` cannot be evaluated in Compose

> aside positive
> **Explanation 6:** `Animatable` gives granular imperative coroutine control, allowing developers to execute a precise sequence of multi-point offset keys on demand and snap back to resting position.

#### Question 7: Performance Optimization & Render Phases
Why is animating translation and scale via `Modifier.graphicsLayer` significantly faster and more battery-efficient than animating `Modifier.offset(x = offset.dp)`?

* [ ] `Modifier.graphicsLayer` uses Java native interface (JNI)
* [x] `Modifier.graphicsLayer` executes purely in the Draw Phase, completely bypassing the Layout and Composition phases
* [ ] `Modifier.offset` cannot run on devices with 120Hz displays
* [ ] `Modifier.graphicsLayer` converts composables into SVG images

> aside positive
> **Explanation 7:** Reading animation state in `Modifier.graphicsLayer` defers evaluation to the Draw Phase. The node does not need to be recomposed or re-measured, preserving high frame rates.

#### Question 8: Continuous Particle Rendering
In Module 5, what Compose API did we use to drive the continuous, non-stopping 0f to 1f progress loop for the confetti celebration?

* [ ] `LaunchedEffect(Unit)` with `Thread.sleep()`
* [ ] `ValueAnimator.ofFloat()`
* [x] `rememberInfiniteTransition` with `infiniteRepeatable`
* [ ] `produceState`

> aside positive
> **Explanation 8:** `rememberInfiniteTransition` is the standard Compose API for creating indefinite repeating animation loops (like pulsing glows, rotating spinners, or falling particles).

#### Question 9: Modern Reusable Modifier Guidelines
According to official modern Android and Compose engineering best practices, what should replace the legacy `composed { ... }` factory pattern when creating custom animation modifiers?

* [ ] Custom XML Animators
* [ ] Imperative Java setters
* [x] `Modifier.Node`
* [ ] `BroadcastReceiver`

> aside positive
> **Explanation 9:** `Modifier.Node` reduces recomposition tracking overhead and object allocations, and is Google's recommended architecture for high-performance custom Compose modifiers.

#### Question 10: Interaction Timing Rule of Thumb
What is the recommended duration range for UI micro-interactions (such as button presses, badge updates, or selection states) in production apps?

* [ ] $10\text{ ms} - 50\text{ ms}$ (virtually imperceptible)
* [x] $150\text{ ms} - 250\text{ ms}$ (crisp, responsive, and natural)
* [ ] $800\text{ ms} - 1200\text{ ms}$ (deliberately slow)
* [ ] $2000\text{ ms}+$ (cinematic pause)

> aside positive
> **Explanation 10:** Micro-interactions feel best when kept between 150ms and 250ms. Shorter than 100ms feels abrupt; longer than 350ms makes an app feel laggy and unresponsive to user touch.

---

### What we've covered

Congratulations on completing the **Jetpack Compose Animation for Engineers** codelab!

```
+-----------------------------------------------------------------------------------------+
|                                    WORKSHOP SUMMARY                                     |
+-----------------------------------------------------------------------------------------+
|  ✔ Understood functional motion design communication and the Choreographer frame loop   |
|  ✔ Implemented Simple Animations with animateFloatAsState (Banner & Indicators)         |
|  ✔ Implemented Visibility & Layout Animations with AnimatedVisibility & animateContentSize|
|  ✔ Mastered Content Animations with AnimatedContent (Shopping Cart Badge)              |
|  ✔ Orchestrated complex Multi-Property Transitions with updateTransition                |
|  ✔ Built imperative physical error shakes using Animatable & keyframes                  |
|  ✔ Rendered GPU Canvas particle confetti celebrations with rememberInfiniteTransition   |
|  ✔ Applied Draw Phase (graphicsLayer) performance optimizations                         |
+-----------------------------------------------------------------------------------------+
```

### Next Steps & Further Exploration
* Explore **Shared Element Transitions** (`Modifier.sharedElement`) introduced in recent Compose versions.
* Inspect your animations in real-time using **Android Studio Animation Preview**.
* Happy animating in Jetpack Compose!

<!-- [PLACEHOLDER: Insert Feedback Form Link / Post-Workshop Survey QR Code here] -->

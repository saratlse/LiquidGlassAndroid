# Liquid Glass — Android 🧊

Apple's iOS-26 **Liquid Glass** material, recreated on **Android with Jetpack Compose**:
real **refraction**, **chromatic light dispersion** 🌈 and an **elastic touch** — not a fake blur.

![demo](docs/demo.png)

## ✨ What it does
- A **glass bubble** you drag over the content → the text behind it **bends** (lens refraction) and
  the **light splits into colours** at the edges (chromatic aberration).
- A **glass button** that **squishes elastically** when you press &amp; hold it (bouncy spring), with a
  live click counter.

## 🔧 How it works
There is **no native Liquid Glass on Android** (it's iOS-only). The real refraction here comes from
the **[Kyant0 `backdrop`](https://github.com/Kyant0/AndroidLiquidGlass)** library and its `lens()`
effect (AGSL shaders) — a captured backdrop that the glass element refracts.

```kotlin
val backdrop = rememberLayerBackdrop()

// 1) the background = the captured "source"
Box(Modifier.layerBackdrop(backdrop).background(Color.White)) { /* content */ }

// 2) the glass element refracts that backdrop
Box(Modifier.drawBackdrop(
    backdrop = backdrop,
    shape = { CircleShape },
    effects = {
        vibrancy()
        blur(1.dp.toPx())
        lens(28.dp.toPx(), 56.dp.toPx(), depthEffect = true, chromaticAberration = true) // 🌈
    },
))
```

## ⚙️ Requirements
- `compileSdk 37`
- Runs on **Android 13+ (API 33)** — the effect uses AGSL `RuntimeShader`.

## 📚 Credits
Powered by **[Kyant0/AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass)**
(`io.github.kyant0:backdrop`).

---
Built with Jetpack Compose · 100% Kotlin

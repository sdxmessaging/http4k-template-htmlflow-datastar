# http4k-template-htmlflow-datastar

Type-safe Kotlin DSL for [Datastar](https://data-star.dev/) attributes in [HtmlFlow](https://github.com/xmlet/HtmlFlow) views, designed for [http4k](https://www.http4k.org/) server applications.

Write Datastar-powered HTML in idiomatic Kotlin — every plugin, action, and modifier maps directly to Datastar's own vocabulary.

## Quick Example

```kotlin
val search = signal("search", "")
val loading = signal("loading", false)

div {
    dataSignals(search, loading)

    input {
        dataBind(search)
        dataOn("input") {
            expression(get("/search"))
            debounce(300)
        }
    }

    div {
        dataClass("active", search.expr.isNotEmpty())
        dataShow(!loading.expr)
    }

    button {
        dataIndicator("loading")
        dataOn("click", !loading.expr and post("/save"))
        dataAttr("disabled", loading)
        text("Save")
    }
}
```

If you know Datastar, you already know this DSL. Every function maps 1:1 to a Datastar plugin or action. Signals are typed Kotlin values — typo a signal name and the compiler catches it.

## Status

**Phase 1 and 2 complete** — core attribute DSL and action expressions. See [roadmap](#roadmap) below.

## Installation

```kotlin
dependencies {
    implementation(platform("org.http4k:http4k-bom:<version>"))
    implementation("org.http4k:http4k-template-htmlflow")
    implementation("org.http4k:http4k-web-datastar")

    // This module (once published):
    implementation("com.sdxmessaging:http4k-template-htmlflow-datastar:<version>")
}
```

## API Reference

### Plugins

Every Datastar plugin has a corresponding extension function:

| Datastar attribute | Kotlin DSL | Example |
|---|---|---|
| `data-bind:email` | `dataBind("email")` | `input { dataBind("email") }` |
| `data-on:click="expr"` | `dataOn("click", expr)` | `button { dataOn("click", post("/save")) }` |
| `data-class:active="expr"` | `dataClass("active", expr)` | `div { dataClass("active", "\$tab === 'home'") }` |
| `data-attr:disabled="expr"` | `dataAttr("disabled", expr)` | `button { dataAttr("disabled", "\$loading") }` |
| `data-text="expr"` | `dataText(expr)` | `span { dataText("\$count") }` |
| `data-show="expr"` | `dataShow(expr)` | `div { dataShow("\$hasResults") }` |
| `data-signals="{...}"` | `dataSignals("{...}")` | `div { dataSignals("{count: 0}") }` |
| `data-init="expr"` | `dataInit(expr)` | `div { dataInit(get("/load")) }` |
| `data-indicator:fetching` | `dataIndicator("fetching")` | `button { dataIndicator("fetching") }` |
| `data-ref="name"` | `dataRef("name")` | `input { dataRef("searchInput") }` |

### Actions

Datastar actions (`@get`, `@post`, etc.) are expression builders — use them anywhere an expression is expected:

```kotlin
get("/path")      // → "@get('/path')"
post("/path")     // → "@post('/path')"
put("/path")      // → "@put('/path')"
patch("/path")    // → "@patch('/path')"
delete("/path")   // → "@delete('/path')"
```

Compose naturally with plugins:

```kotlin
button { dataOn("click", post("/save")) }
div { dataInit(get("/load")) }
```

### Event Modifiers

Datastar supports modifiers on event handlers. Use the builder form of `dataOn`:

```kotlin
input {
    dataOn("input") {
        expression = get("/search")
        debounce(300)        // __debounce_300ms
    }
}

button {
    dataOn("click") {
        expression = post("/submit")
        prevent()            // __prevent
        once()               // __once
    }
}
```

Available modifiers: `debounce(ms)`, `throttle(ms)`, `once()`, `prevent()`, `stop()`, `capture()`, `self()`, `window()`, `document()`.

### Fragments (SSE Patch-Elements)

Build HTML fragments for Datastar's SSE `patch-elements` events using the type-safe DSL instead of raw strings:

```kotlin
// One-off fragment
val element = fragment {
    div {
        attrId("progress-log")
        attrClass("log-area")
        dataClass("has-content", "\$running")
        text(logContent)
    }
}
sse.sendPatchElements(element)

// Or use the SSE convenience directly
sse.sendPatchFragment {
    div { attrId("status"); dataText("\$message") }
}
```

For reusable typed fragments with a model:

```kotlin
data class ProgressModel(val content: String) : ViewModel

val progressFragment: HtmlView<ProgressModel> = view {
    div {
        attrId("progress-log")
        attrClass("log-area has-content")
        dyn { model: ProgressModel -> text(model.content) }
    }
}

// In SSE handler:
sse.sendPatchFragment(progressFragment, ProgressModel("Searching..."))
```

## Design Principles

**Datastar's vocabulary is the API.** Every function name, parameter, and concept maps directly to Datastar's own documentation. When Datastar adds a new plugin, the DSL mapping should be obvious. When you read the Kotlin code, you should be able to mentally translate it to the HTML `data-*` attributes without thinking.

**Actions are expressions, not plugins.** In Datastar, `@post('/save')` is an expression used *inside* plugins like `data-on:click` or `data-init`. The DSL reflects this — `post("/save")` returns a string, and you pass it to `dataOn()` or `dataInit()`. No compound function names like `dataOnPost`.

**http4k and HtmlFlow are implementation details.** This module depends on both, but their conventions don't leak into the API surface. A Datastar developer picking up this DSL shouldn't need to know anything about http4k's routing or HtmlFlow's visitor pattern.

## Roadmap

### Done

- [x] **Core plugins** — `dataBind`, `dataOn`, `dataClass`, `dataAttr`, `dataText`, `dataShow`, `dataSignals`, `dataInit`, `dataIndicator`, `dataRef`, `dataComputed`, `dataEffect`, `dataStyle`, `dataOnIntersect`, `dataOnInterval`, `dataOnSignalPatch`, `dataIgnore`, `dataPreserveAttr`
- [x] **Action expressions** — `get()`, `post()`, `put()`, `patch()`, `delete()` returning composable `Expression` objects
- [x] **Event modifiers** — `debounce`, `throttle`, `delay`, `once`, `prevent`, `stop`, `capture`, `passive`, `self`, `outside`, `window`, `document`, `viewTransition`
- [x] **Typed signals** — `val count = signal("count", 0)` with `Signal<T>` references usable in plugin functions: `dataText(count)`, `dataClass("active", count)`, `dataBind(count)`, `dataSignals(count, name, loading)`
- [x] **Expression composition** — `count.expr gt 0`, `!loading.expr and post("/save")`, `search.expr.isEmpty()`, `dark.expr.ternary("dark", "light")`

- [x] **Fragment helpers** — `fragment { div { ... } }` renders HtmlFlow DSL to Datastar `Element` for SSE patch-elements. Typed views via `HtmlView<T>.toElement(model)`. SSE convenience: `sse.sendPatchFragment { ... }`

### Next

- [ ] **Worked examples** — port Datastar's own examples (Active Search, Click to Edit, etc.) using this DSL + http4k, with side-by-side comparison to raw HTML
- [ ] **Publishing** — Maven Central publication under `com.sdxmessaging`

## Background

This project emerged from building internal tools at [SDX Messaging](https://www.sdxmessaging.com/) using http4k with Datastar. We maintain three versions of the same investigation app (Kotlin/http4k, Rust/Axum, Slint desktop) and wanted type-safe HTML generation for the Kotlin web frontend.

The existing [HtmlFlow-Datastar](https://github.com/xmlet/HtmlFlow-Datastar) project provides excellent Datastar support for HtmlFlow, but its route-binding mechanism uses Jakarta `@Path` annotations — designed for Ktor and Spring. http4k uses functional composition for routing (no annotations), so we built this lightweight bridge that works with http4k's model while keeping Datastar's vocabulary front and centre.

## License

Apache 2.0 — see [LICENSE](LICENSE)

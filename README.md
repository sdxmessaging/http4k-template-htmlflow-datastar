# http4k-template-htmlflow-datastar

Type-safe Datastar attribute DSL for HtmlFlow views in http4k.

This module bridges two existing http4k modules:
- **[http4k-template-htmlflow](https://www.http4k.org/ecosystem/http4k/reference/templating/)** — HtmlFlow integration for http4k's templating system
- **[http4k-web-datastar](https://www.http4k.org/ecosystem/http4k/reference/datastar/)** — Datastar SSE signals and patch-elements for http4k

Together they let you build reactive server-driven web apps with http4k + Datastar, but there is currently no type-safe way to write Datastar `data-*` attributes in HtmlFlow views. You end up writing:

```kotlin
button {
    addAttr("data-on:click", "@post('/investigate')")
    addAttr("data-attr:disabled", "\$running")
    text("Go")
}
```

This module provides idiomatic Kotlin extension functions so you can write:

```kotlin
button {
    dataOn("click", "@post('/investigate')")
    dataAttr("disabled", "\$running")
    text("Go")
}
```

## Status

**Early development** — API design in progress. See the [roadmap](#roadmap) below.

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

## Motivation

When building Datastar apps with http4k and HtmlFlow, there are three layers:

| Layer | Module | What it does |
|---|---|---|
| HTML generation | `http4k-template-htmlflow` | Type-safe HTML via HtmlFlow's Kotlin DSL |
| Datastar transport | `http4k-web-datastar` | SSE patch-elements, patch-signals, `DatastarElementRenderer` |
| Datastar attributes | **this module** | Type-safe `data-on:click`, `data-bind:`, `data-class:`, etc. |

The first two layers exist today. This module fills the gap — letting you write Datastar attributes with the same type safety as the HTML structure itself.

### Why not htmlflow-datastar-core?

The [xmlet/HtmlFlow-Datastar](https://github.com/xmlet/HtmlFlow-Datastar) project provides a Datastar DSL for HtmlFlow, but it's designed primarily for Ktor/Jakarta and requires `@Path` annotations for route references. http4k uses a different routing model (functional composition, not annotations), so most of that DSL doesn't work with http4k out of the box.

This module takes a simpler, http4k-native approach: extension functions that generate the correct attribute strings, with no reflection or annotation scanning.

## Roadmap

### Phase 1: Core attribute extensions

Extension functions on HtmlFlow element types for every Datastar plugin:

- [ ] `dataBind(signal)` — generates `data-bind:{signal}`
- [ ] `dataOn(event, expression)` — generates `data-on:{event}="{expression}"`
- [ ] `dataOn(event) { }` — builder variant with modifier support (debounce, throttle, etc.)
- [ ] `dataClass(className, expression)` — generates `data-class:{className}="{expression}"`
- [ ] `dataAttr(attrName, expression)` — generates `data-attr:{attrName}="{expression}"`
- [ ] `dataText(expression)` — generates `data-text="{expression}"`
- [ ] `dataShow(expression)` — generates `data-show="{expression}"`
- [ ] `dataSignals(json)` — generates `data-signals="{json}"`
- [ ] `dataInit(expression)` — generates `data-init="{expression}"`
- [ ] `dataIndicator(name)` — generates `data-indicator:{name}`
- [ ] `dataRef(name)` — generates `data-ref="{name}"`

### Phase 2: Route-aware helpers

Convenience functions for common Datastar actions that reference server routes:

- [ ] `dataOnGet(event, path)` — generates `data-on:{event}="@get('{path}')"`
- [ ] `dataOnPost(event, path)` — generates `data-on:{event}="@post('{path}')"`
- [ ] `dataInitGet(path)` — generates `data-init="@get('{path}')"`
- [ ] Modifier builder: `dataOn("click") { get("/path"); debounce(200.ms) }`

### Phase 3: Typed signal support

- [ ] `Signal<T>` type for compile-time signal name tracking
- [ ] Signal references in expressions: `dataText { +mySignal }`
- [ ] Signal declarations: `val count = dataSignal("count", 0)` returning `Signal<Int>`
- [ ] Type-safe expression composition with `and`, `or`, `not`

### Phase 4: DatastarElementRenderer integration

- [ ] HtmlFlow fragment helpers for SSE patch-elements
- [ ] `renderFragment { div { ... } }` returning an `Element` for use with `sendPatchElements`

### Phase 5: Documentation and examples

- [ ] Port a Datastar example (e.g. Active Search) using this module + http4k
- [ ] Side-by-side comparison: raw `addAttr` vs typed DSL
- [ ] Integration guide for existing http4k + Datastar projects

## Background

This project emerged from building an internal tool at [SDX Messaging](https://www.sdxmessaging.com/) using http4k with Datastar. We maintain three versions of the same app (Kotlin/http4k, Rust/Axum, Slint desktop) and wanted type-safe HTML generation for the Kotlin web frontend. The existing options didn't quite fit http4k's functional routing model, so we're building the bridge.

## License

Apache 2.0

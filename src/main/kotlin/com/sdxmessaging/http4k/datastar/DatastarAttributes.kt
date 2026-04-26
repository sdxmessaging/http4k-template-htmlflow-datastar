package com.sdxmessaging.http4k.datastar

import org.xmlet.htmlapifaster.Element

/**
 * Type-safe Datastar attribute extensions for HtmlFlow elements.
 *
 * These extension functions generate the correct `data-*` attributes
 * for Datastar plugins, using the colon separator format (e.g. `data-on:click`).
 *
 * Attributes are written via HtmlFlow's visitor API (`getVisitor().visitAttribute()`),
 * which is the same mechanism used internally by all HtmlFlow attribute methods.
 *
 * Usage within an HtmlFlow view:
 * ```kotlin
 * button {
 *     dataOn("click", "@post('/investigate')")
 *     dataAttr("disabled", "\$running")
 *     text("Go")
 * }
 * ```
 */

/** Write a custom attribute via HtmlFlow's visitor. */
private fun <T : Element<T, *>> T.attr(name: String, value: String): T {
    visitor.visitAttribute(name, value)
    return self()
}

// ── data-bind ───────────────────────────────────────────────────────────────

/**
 * Two-way binding between an element and a Datastar signal.
 *
 * Generates: `data-bind:{signal}`
 *
 * ```kotlin
 * input { dataBind("email") }
 * // → <input data-bind:email>
 * ```
 */
fun <T : Element<T, *>> T.dataBind(signal: String): T =
    attr("data-bind:$signal", "")

// ── data-on ─────────────────────────────────────────────────────────────────

/**
 * Attach a Datastar event handler.
 *
 * Generates: `data-on:{event}="{expression}"`
 *
 * ```kotlin
 * button { dataOn("click", "@post('/save')") }
 * // → <button data-on:click="@post('/save')">
 * ```
 */
fun <T : Element<T, *>> T.dataOn(event: String, expression: String): T =
    attr("data-on:$event", expression)

/**
 * Attach a Datastar event handler using a builder for modifiers.
 *
 * ```kotlin
 * input {
 *     dataOn("input") {
 *         expression = "@get('/search')"
 *         debounce(200)
 *     }
 * }
 * // → <input data-on:input__debounce_200ms="@get('/search')">
 * ```
 */
fun <T : Element<T, *>> T.dataOn(event: String, block: DatastarEventBuilder.() -> Unit): T {
    val builder = DatastarEventBuilder().apply(block)
    val suffix = builder.buildModifierSuffix()
    val attrName = if (suffix.isEmpty()) "data-on:$event" else "data-on:$event$suffix"
    return attr(attrName, builder.expression)
}

// ── data-class ──────────────────────────────────────────────────────────────

/**
 * Conditionally toggle a CSS class based on a Datastar expression.
 *
 * Generates: `data-class:{className}="{expression}"`
 *
 * ```kotlin
 * div { dataClass("active", "\$tab === 'home'") }
 * // → <div data-class:active="$tab === 'home'">
 * ```
 */
fun <T : Element<T, *>> T.dataClass(className: String, expression: String): T =
    attr("data-class:$className", expression)

// ── data-attr ───────────────────────────────────────────────────────────────

/**
 * Dynamically set an HTML attribute based on a Datastar expression.
 *
 * Generates: `data-attr:{attrName}="{expression}"`
 *
 * ```kotlin
 * button { dataAttr("disabled", "\$running") }
 * // → <button data-attr:disabled="$running">
 * ```
 */
fun <T : Element<T, *>> T.dataAttr(attrName: String, expression: String): T =
    attr("data-attr:$attrName", expression)

// ── data-text ───────────────────────────────────────────────────────────────

/**
 * Set element text content from a Datastar expression.
 *
 * Generates: `data-text="{expression}"`
 *
 * ```kotlin
 * span { dataText("\$status") }
 * // → <span data-text="$status">
 * ```
 */
fun <T : Element<T, *>> T.dataText(expression: String): T =
    attr("data-text", expression)

// ── data-show ───────────────────────────────────────────────────────────────

/**
 * Conditionally show/hide an element based on a Datastar expression.
 *
 * Generates: `data-show="{expression}"`
 *
 * ```kotlin
 * div { dataShow("\$reportUrl !== ''") }
 * // → <div data-show="$reportUrl !== ''">
 * ```
 */
fun <T : Element<T, *>> T.dataShow(expression: String): T =
    attr("data-show", expression)

// ── data-signals ────────────────────────────────────────────────────────────

/**
 * Declare Datastar signals on an element.
 *
 * Generates: `data-signals="{json}"`
 *
 * ```kotlin
 * div { dataSignals("{count: 0, name: ''}") }
 * // → <div data-signals="{count: 0, name: ''}">
 * ```
 */
fun <T : Element<T, *>> T.dataSignals(json: String): T =
    attr("data-signals", json)

// ── data-init ───────────────────────────────────────────────────────────────

/**
 * Run a Datastar expression when the element is first seen.
 *
 * Generates: `data-init="{expression}"`
 *
 * ```kotlin
 * div { dataInit("@get('/load')") }
 * // → <div data-init="@get('/load')">
 * ```
 */
fun <T : Element<T, *>> T.dataInit(expression: String): T =
    attr("data-init", expression)

// ── data-indicator ──────────────────────────────────────────────────────────

/**
 * Bind a boolean signal that tracks in-flight requests.
 *
 * Generates: `data-indicator:{name}`
 *
 * ```kotlin
 * button { dataIndicator("fetching") }
 * // → <button data-indicator:fetching>
 * ```
 */
fun <T : Element<T, *>> T.dataIndicator(name: String): T =
    attr("data-indicator:$name", "")

// ── data-ref ────────────────────────────────────────────────────────────────

/**
 * Create a named reference to an element for use in expressions.
 *
 * Generates: `data-ref="{name}"`
 *
 * ```kotlin
 * input { dataRef("searchInput") }
 * // → <input data-ref="searchInput">
 * ```
 */
fun <T : Element<T, *>> T.dataRef(name: String): T =
    attr("data-ref", name)

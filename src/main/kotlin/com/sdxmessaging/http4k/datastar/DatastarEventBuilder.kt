package com.sdxmessaging.http4k.datastar

/**
 * Builder for Datastar event handlers with modifier support.
 *
 * Modifiers are appended to the attribute name using Datastar's
 * double-underscore syntax: `data-on:input__debounce.200ms`
 *
 * ```kotlin
 * input {
 *     dataOn("input") {
 *         expression = get("/search")
 *         debounce(200)
 *     }
 * }
 * // → <input data-on:input__debounce.200ms="@get('/search')">
 * ```
 *
 * @see <a href="https://data-star.dev/reference/attributes#data-on">data-on modifier reference</a>
 */
class DatastarEventBuilder {
    /** The Datastar expression to execute. Accepts String or Expression.toString(). */
    var expression: String = ""

    private val modifiers = mutableListOf<String>()

    /** Set expression from an [Expression] object. */
    fun expression(expr: Expression) { expression = expr.js }

    // ── Timing modifiers ────────────────────────────────────────────────

    /** Debounce the event. Generates `__debounce.{ms}ms` */
    fun debounce(ms: Int) { modifiers.add("debounce.${ms}ms") }

    /** Debounce with leading edge. Generates `__debounce.{ms}ms.leading` */
    fun debounceLeading(ms: Int) { modifiers.add("debounce.${ms}ms.leading") }

    /** Throttle the event. Generates `__throttle.{ms}ms` */
    fun throttle(ms: Int) { modifiers.add("throttle.${ms}ms") }

    /** Delay the event. Generates `__delay.{ms}ms` */
    fun delay(ms: Int) { modifiers.add("delay.${ms}ms") }

    // ── Event behaviour modifiers ───────────────────────────────────────

    /** Only fire the event once. */
    fun once() { modifiers.add("once") }

    /** Prevent the default browser action. */
    fun prevent() { modifiers.add("prevent") }

    /** Stop event propagation. */
    fun stop() { modifiers.add("stop") }

    /** Capture the event during the capture phase. */
    fun capture() { modifiers.add("capture") }

    /** Use passive event listener. */
    fun passive() { modifiers.add("passive") }

    /** Only fire if the event target is the element itself. */
    fun self() { modifiers.add("self") }

    /** Trigger when the event occurs outside the element. */
    fun outside() { modifiers.add("outside") }

    // ── Target modifiers ────────────────────────────────────────────────

    /** Attach listener to window instead of the element. */
    fun window() { modifiers.add("window") }

    /** Attach listener to document instead of the element. */
    fun document() { modifiers.add("document") }

    // ── View transition ─────────────────────────────────────────────────

    /** Wrap expression in `document.startViewTransition()`. */
    fun viewTransition() { modifiers.add("viewtransition") }

    // ── Custom ──────────────────────────────────────────────────────────

    /** Add a raw modifier string for anything not covered above. */
    fun modifier(value: String) { modifiers.add(value) }

    internal fun buildModifierSuffix(): String =
        if (modifiers.isEmpty()) "" else modifiers.joinToString("__", prefix = "__")
}

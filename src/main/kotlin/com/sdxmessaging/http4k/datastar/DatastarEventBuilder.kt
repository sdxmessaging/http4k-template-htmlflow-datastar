package com.sdxmessaging.http4k.datastar

/**
 * Builder for Datastar event handlers with modifier support.
 *
 * Modifiers are appended to the attribute name using Datastar's
 * double-underscore syntax: `data-on:input__debounce_200ms`
 *
 * ```kotlin
 * input {
 *     dataOn("input") {
 *         expression = "@get('/search')"
 *         debounce(200)
 *     }
 * }
 * ```
 */
class DatastarEventBuilder {
    /** The Datastar expression to execute (e.g. "@post('/save')") */
    var expression: String = ""

    private val modifiers = mutableListOf<String>()

    /** Debounce the event by the given number of milliseconds. */
    fun debounce(ms: Int) { modifiers.add("debounce_${ms}ms") }

    /** Throttle the event by the given number of milliseconds. */
    fun throttle(ms: Int) { modifiers.add("throttle_${ms}ms") }

    /** Only fire the event once. */
    fun once() { modifiers.add("once") }

    /** Prevent the default browser action. */
    fun prevent() { modifiers.add("prevent") }

    /** Stop event propagation. */
    fun stop() { modifiers.add("stop") }

    /** Capture the event during the capture phase. */
    fun capture() { modifiers.add("capture") }

    /** Only fire if the event target is the element itself. */
    fun self() { modifiers.add("self") }

    /** Fire on window instead of the element. */
    fun window() { modifiers.add("window") }

    /** Fire on document instead of the element. */
    fun document() { modifiers.add("document") }

    /** Add a custom modifier string. */
    fun modifier(value: String) { modifiers.add(value) }

    internal fun buildModifierSuffix(): String =
        if (modifiers.isEmpty()) "" else modifiers.joinToString("__", prefix = "__")
}

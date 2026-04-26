package com.sdxmessaging.http4k.datastar

/**
 * A typed reference to a Datastar signal.
 *
 * Signals are the reactive state primitives in Datastar. Declaring them
 * as typed Kotlin values gives you compile-time name tracking and prevents
 * typos in signal references across your view.
 *
 * ```kotlin
 * val count = signal("count", 0)
 * val search = signal("search", "")
 * val loading = signal("loading", false)
 *
 * div {
 *     dataSignals(count, search, loading)
 *     // → data-signals="{count: 0, search: '', loading: false}"
 *
 *     span { dataText(count) }
 *     // → <span data-text="$count">
 *
 *     div { dataShow(count.expr gt 0) }
 *     // → <div data-show="$count > 0">
 * }
 * ```
 *
 * @see <a href="https://data-star.dev/reference/attributes#data-signals">data-signals reference</a>
 */
class Signal<T>(val name: String, val default: T) {
    /** Reference this signal in a Datastar expression: `$name` */
    val expr: Expression get() = Expression("\$$name")
}

/** Create a typed Datastar signal. */
fun <T> signal(name: String, default: T): Signal<T> = Signal(name, default)

/** Convert a value to a JavaScript literal for use in data-signals JSON. */
internal fun jsLiteral(value: Any?): String = when (value) {
    is Expression -> value.js
    is Signal<*> -> "\$${value.name}"
    is String -> "'$value'"
    is Boolean -> value.toString()
    is Number -> value.toString()
    null -> "null"
    else -> "'$value'"
}

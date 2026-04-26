package com.sdxmessaging.http4k.datastar

import org.xmlet.htmlapifaster.Element

/**
 * Type-safe Datastar attribute extensions for HtmlFlow elements.
 *
 * Every Datastar plugin has a corresponding Kotlin extension function.
 * The naming follows Datastar's own vocabulary: `data-on:click` → `dataOn("click", ...)`.
 *
 * Overloads accept [String] (raw expressions), [Expression] (composed expressions),
 * and [Signal] (typed signal references) where appropriate.
 *
 * @see <a href="https://data-star.dev/reference/attributes">Datastar attribute reference</a>
 */

/** Write a custom attribute via HtmlFlow's visitor. */
private fun <T : Element<T, *>> T.attr(name: String, value: String): T {
    visitor.visitAttribute(name, value)
    return self()
}

// ── data-bind ───────────────────────────────────────────────────────────────

/** Two-way binding: `data-bind:{signal}` */
fun <T : Element<T, *>> T.dataBind(signal: String): T = attr("data-bind:$signal", "")
/** Two-way binding using a typed [Signal]. */
fun <T : Element<T, *>> T.dataBind(signal: Signal<*>): T = dataBind(signal.name)

// ── data-on ─────────────────────────────────────────────────────────────────

/** Event handler: `data-on:{event}="{expression}"` */
fun <T : Element<T, *>> T.dataOn(event: String, expression: String): T = attr("data-on:$event", expression)
/** Event handler with [Expression]. */
fun <T : Element<T, *>> T.dataOn(event: String, expression: Expression): T = dataOn(event, expression.js)
/** Event handler with modifier builder. */
fun <T : Element<T, *>> T.dataOn(event: String, block: DatastarEventBuilder.() -> Unit): T {
    val builder = DatastarEventBuilder().apply(block)
    val suffix = builder.buildModifierSuffix()
    val attrName = if (suffix.isEmpty()) "data-on:$event" else "data-on:$event$suffix"
    return attr(attrName, builder.expression)
}

// ── data-class ──────────────────────────────────────────────────────────────

/** Conditional CSS class: `data-class:{className}="{expression}"` */
fun <T : Element<T, *>> T.dataClass(className: String, expression: String): T = attr("data-class:$className", expression)
/** Conditional CSS class with [Expression]. */
fun <T : Element<T, *>> T.dataClass(className: String, expression: Expression): T = dataClass(className, expression.js)
/** Conditional CSS class with boolean [Signal]. */
fun <T : Element<T, *>> T.dataClass(className: String, signal: Signal<Boolean>): T = dataClass(className, "\$${signal.name}")

// ── data-attr ───────────────────────────────────────────────────────────────

/** Dynamic HTML attribute: `data-attr:{attrName}="{expression}"` */
fun <T : Element<T, *>> T.dataAttr(attrName: String, expression: String): T = attr("data-attr:$attrName", expression)
/** Dynamic HTML attribute with [Expression]. */
fun <T : Element<T, *>> T.dataAttr(attrName: String, expression: Expression): T = dataAttr(attrName, expression.js)
/** Dynamic HTML attribute with [Signal]. */
fun <T : Element<T, *>> T.dataAttr(attrName: String, signal: Signal<*>): T = dataAttr(attrName, "\$${signal.name}")

// ── data-text ───────────────────────────────────────────────────────────────

/** Reactive text content: `data-text="{expression}"` */
fun <T : Element<T, *>> T.dataText(expression: String): T = attr("data-text", expression)
/** Reactive text content with [Expression]. */
fun <T : Element<T, *>> T.dataText(expression: Expression): T = dataText(expression.js)
/** Reactive text content with [Signal]. */
fun <T : Element<T, *>> T.dataText(signal: Signal<*>): T = dataText("\$${signal.name}")

// ── data-show ───────────────────────────────────────────────────────────────

/** Conditional visibility: `data-show="{expression}"` */
fun <T : Element<T, *>> T.dataShow(expression: String): T = attr("data-show", expression)
/** Conditional visibility with [Expression]. */
fun <T : Element<T, *>> T.dataShow(expression: Expression): T = dataShow(expression.js)
/** Conditional visibility with boolean [Signal]. */
fun <T : Element<T, *>> T.dataShow(signal: Signal<Boolean>): T = dataShow("\$${signal.name}")

// ── data-signals ────────────────────────────────────────────────────────────

/** Declare signals with raw JSON: `data-signals="{json}"` */
fun <T : Element<T, *>> T.dataSignals(json: String): T = attr("data-signals", json)
/** Declare signals from typed [Signal] objects. */
fun <T : Element<T, *>> T.dataSignals(vararg signals: Signal<*>): T {
    val json = signals.joinToString(", ", "{", "}") { "${it.name}: ${jsLiteral(it.default)}" }
    return dataSignals(json)
}

// ── data-computed ───────────────────────────────────────────────────────────

/** Computed signal: `data-computed:{name}="{expression}"` */
fun <T : Element<T, *>> T.dataComputed(name: String, expression: String): T = attr("data-computed:$name", expression)
/** Computed signal with [Expression]. */
fun <T : Element<T, *>> T.dataComputed(name: String, expression: Expression): T = dataComputed(name, expression.js)

// ── data-init ───────────────────────────────────────────────────────────────

/** Run on init: `data-init="{expression}"` */
fun <T : Element<T, *>> T.dataInit(expression: String): T = attr("data-init", expression)
/** Run on init with [Expression]. */
fun <T : Element<T, *>> T.dataInit(expression: Expression): T = dataInit(expression.js)

// ── data-effect ─────────────────────────────────────────────────────────────

/** Side-effect on signal change: `data-effect="{expression}"` */
fun <T : Element<T, *>> T.dataEffect(expression: String): T = attr("data-effect", expression)
/** Side-effect with [Expression]. */
fun <T : Element<T, *>> T.dataEffect(expression: Expression): T = dataEffect(expression.js)

// ── data-indicator ──────────────────────────────────────────────────────────

/** Request in-flight signal: `data-indicator:{name}` */
fun <T : Element<T, *>> T.dataIndicator(name: String): T = attr("data-indicator:$name", "")

// ── data-ref ────────────────────────────────────────────────────────────────

/** Element reference signal: `data-ref="{name}"` */
fun <T : Element<T, *>> T.dataRef(name: String): T = attr("data-ref", name)

// ── data-style ──────────────────────────────────────────────────────────────

/** Reactive inline style: `data-style:{property}="{expression}"` */
fun <T : Element<T, *>> T.dataStyle(property: String, expression: String): T = attr("data-style:$property", expression)
/** Reactive inline style with [Expression]. */
fun <T : Element<T, *>> T.dataStyle(property: String, expression: Expression): T = dataStyle(property, expression.js)

// ── data-on-intersect ───────────────────────────────────────────────────────

/** Viewport intersection: `data-on-intersect="{expression}"` */
fun <T : Element<T, *>> T.dataOnIntersect(expression: String): T = attr("data-on-intersect", expression)
/** Viewport intersection with [Expression]. */
fun <T : Element<T, *>> T.dataOnIntersect(expression: Expression): T = dataOnIntersect(expression.js)

// ── data-on-interval ────────────────────────────────────────────────────────

/** Periodic execution: `data-on-interval="{expression}"` */
fun <T : Element<T, *>> T.dataOnInterval(expression: String): T = attr("data-on-interval", expression)
/** Periodic execution with [Expression]. */
fun <T : Element<T, *>> T.dataOnInterval(expression: Expression): T = dataOnInterval(expression.js)

// ── data-on-signal-patch ────────────────────────────────────────────────────

/** React to signal changes: `data-on-signal-patch="{expression}"` */
fun <T : Element<T, *>> T.dataOnSignalPatch(expression: String): T = attr("data-on-signal-patch", expression)

// ── data-ignore ─────────────────────────────────────────────────────────────

/** Tell Datastar to skip this element and its descendants. */
fun <T : Element<T, *>> T.dataIgnore(): T = attr("data-ignore", "")

// ── data-preserve-attr ──────────────────────────────────────────────────────

/** Preserve attributes during morphing: `data-preserve-attr="{attrs}"` */
fun <T : Element<T, *>> T.dataPreserveAttr(attrs: String): T = attr("data-preserve-attr", attrs)

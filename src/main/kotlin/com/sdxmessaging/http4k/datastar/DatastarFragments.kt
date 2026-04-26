package com.sdxmessaging.http4k.datastar

import htmlflow.HtmlPage
import htmlflow.HtmlView
import htmlflow.view
import org.http4k.datastar.Element
import org.http4k.sse.Sse
import org.http4k.sse.sendPatchElements
import org.http4k.template.ViewModel
import org.http4k.template.renderer

/**
 * Fragment helpers for building Datastar SSE patch-elements using HtmlFlow.
 *
 * In Datastar, the server sends HTML fragments via SSE to update parts of
 * the page. These helpers let you build those fragments with HtmlFlow's
 * type-safe DSL instead of raw HTML strings.
 *
 * ```kotlin
 * // Instead of:
 * sse.sendPatchElements(Element.of("""<div id="status">Done</div>"""))
 *
 * // Write:
 * sse.sendPatchFragment {
 *     div { attrId("status"); text("Done") }
 * }
 * ```
 *
 * @see <a href="https://data-star.dev/reference/sse_events#datastar-patch-elements">Datastar patch-elements reference</a>
 */

// ── One-off fragments ───────────────────────────────────────────────────────

/**
 * Render an HtmlFlow DSL block to a Datastar [Element].
 *
 * The block receives an [HtmlPage] — call element methods like `div {}`,
 * `span {}`, `table {}` directly. No `<html>` or `<body>` wrapper is added.
 *
 * ```kotlin
 * val element = fragment {
 *     div {
 *         attrId("progress")
 *         attrClass("log-area")
 *         text(logContent)
 *     }
 * }
 * sse.sendPatchElements(element)
 * ```
 */
fun fragment(block: HtmlPage.() -> Unit): Element {
    val tempView: HtmlView<Unit> = view(block)
    val html = tempView.render(Unit)
    return Element.of(html)
}

// ── Typed view fragments ────────────────────────────────────────────────────

/**
 * Render a typed [HtmlView] with a model to a Datastar [Element].
 *
 * Use this for reusable fragment views that depend on data.
 *
 * ```kotlin
 * data class StatusModel(val message: String, val type: String) : ViewModel
 *
 * val statusFragment: HtmlView<StatusModel> = view {
 *     div {
 *         attrId("status")
 *         dyn { model: StatusModel ->
 *             attrClass("status-${model.type}")
 *             text(model.message)
 *         }
 *     }
 * }
 *
 * // In SSE handler:
 * sse.sendPatchElements(statusFragment.toElement(StatusModel("Done", "success")))
 * ```
 */
fun <T> HtmlView<T>.toElement(model: T): Element {
    val html = this.render(model)
    return Element.of(html)
}

/**
 * Create a reusable element renderer from an [HtmlView].
 *
 * Returns a function that takes a model and produces a Datastar [Element].
 * Useful when you render the same fragment type repeatedly (e.g. in a stream).
 *
 * ```kotlin
 * val renderProgress = progressFragment.toElementRenderer()
 *
 * // In SSE handler:
 * logLines.forEach { line ->
 *     sse.sendPatchElements(renderProgress(ProgressModel(line)))
 * }
 * ```
 */
fun <T> HtmlView<T>.toElementRenderer(): (T) -> Element = { model -> toElement(model) }

// ── SSE convenience extensions ──────────────────────────────────────────────

/**
 * Build and send a fragment as a Datastar patch-elements SSE event.
 *
 * ```kotlin
 * sse.sendPatchFragment {
 *     div { attrId("counter"); text(count.toString()) }
 * }
 * ```
 */
fun Sse.sendPatchFragment(block: HtmlPage.() -> Unit): Sse =
    sendPatchElements(fragment(block))

/**
 * Render a typed view and send it as a Datastar patch-elements SSE event.
 *
 * ```kotlin
 * sse.sendPatchFragment(statusFragment, StatusModel("Loading...", "info"))
 * ```
 */
fun <T> Sse.sendPatchFragment(view: HtmlView<T>, model: T): Sse =
    sendPatchElements(view.toElement(model))

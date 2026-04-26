package com.sdxmessaging.examples

import com.sdxmessaging.http4k.datastar.*
import htmlflow.html
import htmlflow.view
import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Helidon
import org.http4k.server.asServer
import org.http4k.template.ViewModel
import org.http4k.template.renderer
import org.xmlet.htmlapifaster.*

/**
 * Counter — the simplest Datastar example.
 *
 * Pure client-side signals, no SSE. Demonstrates:
 * - [signal] and [dataSignals] for declaring reactive state
 * - [dataOn] for event handlers
 * - [dataText] for reactive text
 * - [dataAttr] for dynamic attributes
 *
 * Equivalent Datastar HTML:
 * ```html
 * <div data-signals="{count: 0}">
 *     <button data-on:click="$count--" data-attr:disabled="$count <= 0">-</button>
 *     <span data-text="$count">0</span>
 *     <button data-on:click="$count++">+</button>
 * </div>
 * ```
 *
 * Run: ./gradlew :examples:counter
 * Open: http://localhost:8080
 */

data object CounterPage : ViewModel

val count = signal("count", 0)

val counterView = view<CounterPage> {
    html {
        head {
            title { text("Counter — Datastar + HtmlFlow + http4k") }
            script {
                addAttr("type", "module")
                attrSrc("https://cdn.jsdelivr.net/gh/starfederation/datastar@v1.0.1/bundles/datastar.js")
            }
            style { text(COUNTER_CSS) }
        }
        body {
            h1 { text("Counter") }
            div {
                attrClass("counter")
                dataSignals(count)

                button {
                    attrClass("btn")
                    dataOn("click", "\$count--")
                    dataAttr("disabled", count.expr lte 0)
                    text("\u2212")
                }
                span {
                    attrClass("count")
                    dataText(count)
                    text("0")
                }
                button {
                    attrClass("btn")
                    dataOn("click", "\$count++")
                    text("+")
                }
            }
            p {
                attrClass("note")
                text("No server round-trips \u2014 pure client-side Datastar signals.")
            }
        }
    }
}

val counterRenderer = counterView.renderer()

fun main() {
    val app = routes("/" bind GET to { Response(OK).body(counterRenderer(CounterPage)) })
    app.asServer(Helidon(8080)).start()
    println("Counter running at http://localhost:8080")
}

private const val COUNTER_CSS = """
body { font-family: system-ui, sans-serif; max-width: 400px; margin: 60px auto; text-align: center; background: #1a1d21; color: #e8eaed; }
h1 { font-weight: 300; margin-bottom: 30px; }
.counter { display: flex; align-items: center; justify-content: center; gap: 20px; }
.btn { width: 48px; height: 48px; border-radius: 50%; border: 1px solid rgba(255,255,255,0.15); background: transparent; color: #e8eaed; font-size: 22px; cursor: pointer; transition: all 0.2s; }
.btn:hover:not(:disabled) { background: rgba(255,255,255,0.08); border-color: rgba(255,255,255,0.3); }
.btn:disabled { opacity: 0.3; cursor: not-allowed; }
.count { font-size: 48px; font-weight: 200; min-width: 80px; }
.note { margin-top: 40px; font-size: 13px; color: #6b7280; }
"""

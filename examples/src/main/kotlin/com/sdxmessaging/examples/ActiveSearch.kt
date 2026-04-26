package com.sdxmessaging.examples

import com.sdxmessaging.http4k.datastar.*
import htmlflow.HtmlView
import htmlflow.div
import htmlflow.dyn
import htmlflow.html
import htmlflow.view
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.Query
import org.http4k.lens.DATASTAR_MODEL
import org.http4k.routing.bind
import org.http4k.routing.poly
import org.http4k.sse.SseResponse
import org.http4k.server.Helidon
import org.http4k.server.asServer
import org.http4k.routing.sse.bind as sseBind
import org.http4k.template.ViewModel
import org.http4k.template.renderer
import org.xmlet.htmlapifaster.*

/**
 * Active Search — SSE-powered live search with debounce.
 *
 * Demonstrates:
 * - [dataBind] for two-way input binding
 * - [dataOn] with [debounce] modifier
 * - [get] action expression
 * - [fragment] for building SSE patch-elements
 * - [sendPatchFragment] for streaming results
 * - Typed [HtmlView] fragments with [dyn] for model data
 *
 * Equivalent Datastar HTML:
 * ```html
 * <input data-bind:search data-on:input__debounce.300ms="@get('/search')">
 * <div id="results">Type to search...</div>
 * ```
 *
 * Run: ./gradlew :examples:activeSearch
 * Open: http://localhost:8080
 */

// ── Signals ─────────────────────────────────────────────────────────────────

val search = signal("search", "")

// ── Page view ───────────────────────────────────────────────────────────────

data object SearchPage : ViewModel

val searchPageView = view<SearchPage> {
    html {
        head {
            title { text("Active Search — Datastar + HtmlFlow + http4k") }
            script {
                addAttr("type", "module")
                attrSrc("https://cdn.jsdelivr.net/gh/starfederation/datastar@v1.0.1/bundles/datastar.js")
            }
            style { text(SEARCH_CSS) }
        }
        body {
            h1 { text("Active Search") }
            div {
                dataSignals(search)

                input {
                    attrType(EnumTypeInputType.TEXT)
                    attrPlaceholder("Search contacts...")
                    attrClass("search-input")
                    dataBind(search)
                    dataOn("input") {
                        expression(get("/search"))
                        debounce(300)
                    }
                }

                div {
                    attrId("results")
                    attrClass("results")
                    text("Type to search...")
                }
            }
            p {
                attrClass("note")
                text("Try: Alice, Bob, Charlie, Diana, Eve")
            }
        }
    }
}

val searchPageRenderer = searchPageView.renderer()

// ── Results fragment ────────────────────────────────────────────────────────

data class Contact(val firstName: String, val lastName: String, val email: String)

data class SearchResults(val contacts: List<Contact>, val query: String) : ViewModel

val resultsFragment: HtmlView<SearchResults> = view {
    div {
        attrId("results")
        attrClass("results")
        dyn { model: SearchResults ->
            if (model.contacts.isEmpty()) {
                p { attrClass("empty"); text("No contacts match '${model.query}'") }
            } else {
                table {
                    thead {
                        tr {
                            th { text("First Name") }
                            th { text("Last Name") }
                            th { text("Email") }
                        }
                    }
                    tbody {
                        model.contacts.forEach { contact ->
                            tr {
                                td { text(contact.firstName) }
                                td { text(contact.lastName) }
                                td { text(contact.email) }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Sample data ─────────────────────────────────────────────────────────────

val CONTACTS = listOf(
    Contact("Alice", "Johnson", "alice@example.com"),
    Contact("Bob", "Smith", "bob@example.com"),
    Contact("Charlie", "Brown", "charlie@example.com"),
    Contact("Diana", "Prince", "diana@example.com"),
    Contact("Eve", "Davis", "eve@example.com"),
    Contact("Frank", "Miller", "frank@example.com"),
    Contact("Grace", "Lee", "grace@example.com"),
    Contact("Henry", "Wilson", "henry@example.com"),
)

fun searchContacts(query: String): List<Contact> {
    if (query.isBlank()) return emptyList()
    val q = query.lowercase()
    return CONTACTS.filter {
        it.firstName.lowercase().contains(q) ||
            it.lastName.lowercase().contains(q) ||
            it.email.lowercase().contains(q)
    }
}

// ── Server ──────────────────────────────────────────────────────────────────

fun main() {
    val app = poly(
        // SSE: search and return results fragment
        "/search" sseBind { req: Request ->
            SseResponse { sse ->
                // Datastar sends signals as JSON in the "datastar" query parameter
                val signalsJson = Query.DATASTAR_MODEL(req) ?: "{}"
                val query = signalsJson
                    .substringAfter("\"search\":")
                    .substringAfter("\"")
                    .substringBefore("\"")
                val results = searchContacts(query)
                sse.sendPatchFragment(resultsFragment, SearchResults(results, query))
                sse.close()
            }
        },
        // HTTP: serve the page
        "/" bind GET to {
            Response(OK)
                .header("Content-Type", "text/html; charset=utf-8")
                .body(searchPageRenderer(SearchPage))
        },
    )

    app.asServer(Helidon(8080)).start()
    println("Active Search running at http://localhost:8080")
}

private const val SEARCH_CSS = """
body { font-family: system-ui, sans-serif; max-width: 600px; margin: 60px auto; padding: 0 20px; background: #1a1d21; color: #e8eaed; }
h1 { font-weight: 300; margin-bottom: 24px; }
.search-input { width: 100%; padding: 12px 16px; border-radius: 8px; border: 1px solid rgba(255,255,255,0.1); background: #22262b; color: #e8eaed; font-size: 15px; font-family: inherit; outline: none; transition: border-color 0.2s; }
.search-input:focus { border-color: rgba(74,140,42,0.5); }
.search-input::placeholder { color: #6b7280; }
.results { margin-top: 20px; min-height: 100px; }
.empty { color: #6b7280; font-style: italic; padding: 20px 0; }
table { width: 100%; border-collapse: collapse; }
th { text-align: left; padding: 8px 12px; font-size: 11px; text-transform: uppercase; letter-spacing: 0.05em; color: #6b7280; border-bottom: 1px solid rgba(255,255,255,0.08); }
td { padding: 10px 12px; border-bottom: 1px solid rgba(255,255,255,0.04); font-size: 14px; }
tr:hover td { background: rgba(255,255,255,0.03); }
.note { margin-top: 30px; font-size: 13px; color: #6b7280; }
"""

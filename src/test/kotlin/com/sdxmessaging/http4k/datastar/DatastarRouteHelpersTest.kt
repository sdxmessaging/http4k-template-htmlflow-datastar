package com.sdxmessaging.http4k.datastar

import htmlflow.doc
import htmlflow.html
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.string.shouldContain
import org.xmlet.htmlapifaster.body
import org.xmlet.htmlapifaster.button
import org.xmlet.htmlapifaster.div

class DatastarRouteHelpersTest : DescribeSpec({

    fun render(block: org.xmlet.htmlapifaster.Body<*>.() -> Unit): String =
        StringBuilder().apply {
            doc {
                html {
                    body { block() }
                }
            }
        }.toString()

    describe("dataOnGet") {
        it("generates data-on:{event} with @get expression") {
            val html = render {
                button { dataOnGet("click", "/refresh") }
            }
            html shouldContain """data-on:click="@get('/refresh')""""
        }
    }

    describe("dataOnPost") {
        it("generates data-on:{event} with @post expression") {
            val html = render {
                button { dataOnPost("click", "/save") }
            }
            html shouldContain """data-on:click="@post('/save')""""
        }
    }

    describe("dataOnPut") {
        it("generates data-on:{event} with @put expression") {
            val html = render {
                button { dataOnPut("click", "/update") }
            }
            html shouldContain """data-on:click="@put('/update')""""
        }
    }

    describe("dataOnPatch") {
        it("generates data-on:{event} with @patch expression") {
            val html = render {
                button { dataOnPatch("click", "/partial") }
            }
            html shouldContain """data-on:click="@patch('/partial')""""
        }
    }

    describe("dataOnDelete") {
        it("generates data-on:{event} with @delete expression") {
            val html = render {
                button { dataOnDelete("click", "/remove") }
            }
            html shouldContain """data-on:click="@delete('/remove')""""
        }
    }

    describe("dataInitGet") {
        it("generates data-init with @get expression") {
            val html = render {
                div { dataInitGet("/load-content") }
            }
            html shouldContain """data-init="@get('/load-content')""""
        }
    }
})

package com.sdxmessaging.http4k.datastar

import htmlflow.doc
import htmlflow.html
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.xmlet.htmlapifaster.body
import org.xmlet.htmlapifaster.button
import org.xmlet.htmlapifaster.div

class DatastarActionsTest : DescribeSpec({

    fun render(block: org.xmlet.htmlapifaster.Body<*>.() -> Unit): String =
        StringBuilder().apply {
            doc {
                html {
                    body { block() }
                }
            }
        }.toString()

    describe("action expressions") {
        it("get() builds @get expression") {
            get("/search") shouldBe "@get('/search')"
        }

        it("post() builds @post expression") {
            post("/save") shouldBe "@post('/save')"
        }

        it("put() builds @put expression") {
            put("/update") shouldBe "@put('/update')"
        }

        it("patch() builds @patch expression") {
            patch("/partial") shouldBe "@patch('/partial')"
        }

        it("delete() builds @delete expression") {
            delete("/remove") shouldBe "@delete('/remove')"
        }
    }

    describe("actions composed with dataOn") {
        it("dataOn with post reads naturally") {
            val html = render {
                button { dataOn("click", post("/save")) }
            }
            html shouldContain """data-on:click="@post('/save')""""
        }

        it("dataOn with get reads naturally") {
            val html = render {
                button { dataOn("click", get("/refresh")) }
            }
            html shouldContain """data-on:click="@get('/refresh')""""
        }
    }

    describe("actions composed with dataInit") {
        it("dataInit with get reads naturally") {
            val html = render {
                div { dataInit(get("/load-content")) }
            }
            html shouldContain """data-init="@get('/load-content')""""
        }
    }
})

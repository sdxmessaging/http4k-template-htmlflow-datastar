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
        StringBuilder().apply { doc { html { body { block() } } } }.toString()

    describe("action expressions") {
        it("get() builds @get expression") {
            get("/search").js shouldBe "@get('/search')"
        }
        it("post() builds @post expression") {
            post("/save").js shouldBe "@post('/save')"
        }
        it("put() builds @put expression") {
            put("/update").js shouldBe "@put('/update')"
        }
        it("patch() builds @patch expression") {
            patch("/partial").js shouldBe "@patch('/partial')"
        }
        it("delete() builds @delete expression") {
            delete("/remove").js shouldBe "@delete('/remove')"
        }
    }

    describe("actions composed with dataOn") {
        it("post reads naturally") {
            render { button { dataOn("click", post("/save")) } } shouldContain
                """data-on:click="@post('/save')""""
        }
        it("get reads naturally") {
            render { button { dataOn("click", get("/refresh")) } } shouldContain
                """data-on:click="@get('/refresh')""""
        }
    }

    describe("actions composed with dataInit") {
        it("get reads naturally") {
            render { div { dataInit(get("/load")) } } shouldContain
                """data-init="@get('/load')""""
        }
    }

    describe("actions composed with logical operators") {
        it("not signal and action") {
            val loading = signal("loading", false)
            render { button { dataOn("click", !loading.expr and post("/save")) } } shouldContain
                """data-on:click="!${'$'}loading && @post('/save')""""
        }
    }
})

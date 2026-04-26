package com.sdxmessaging.http4k.datastar

import htmlflow.doc
import htmlflow.html
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.string.shouldContain
import org.xmlet.htmlapifaster.body
import org.xmlet.htmlapifaster.button
import org.xmlet.htmlapifaster.div
import org.xmlet.htmlapifaster.input
import org.xmlet.htmlapifaster.pre
import org.xmlet.htmlapifaster.span
import org.xmlet.htmlapifaster.textarea

class DatastarAttributesTest : DescribeSpec({

    fun render(block: org.xmlet.htmlapifaster.Body<*>.() -> Unit): String =
        StringBuilder().apply { doc { html { body { block() } } } }.toString()

    describe("dataBind") {
        it("with string name") {
            render { input { dataBind("email") } } shouldContain "data-bind:email"
        }
        it("with typed Signal") {
            val email = signal("email", "")
            render { input { dataBind(email) } } shouldContain "data-bind:email"
        }
        it("on textarea") {
            render { textarea { dataBind("notes") } } shouldContain "data-bind:notes"
        }
    }

    describe("dataOn") {
        it("with string expression") {
            render { button { dataOn("click", "@post('/save')") } } shouldContain
                """data-on:click="@post('/save')""""
        }
        it("with Expression") {
            render { button { dataOn("click", post("/save")) } } shouldContain
                """data-on:click="@post('/save')""""
        }
        it("builder with debounce") {
            render {
                input {
                    dataOn("input") {
                        expression = "@get('/search')"
                        debounce(200)
                    }
                }
            } shouldContain """data-on:input__debounce.200ms="@get('/search')""""
        }
        it("builder with multiple modifiers") {
            render {
                button {
                    dataOn("click") {
                        expression = "@post('/save')"
                        prevent()
                        once()
                    }
                }
            } shouldContain """data-on:click__prevent__once="@post('/save')""""
        }
        it("builder with delay") {
            render {
                div {
                    dataOn("click") {
                        expression = "\$count++"
                        delay(500)
                    }
                }
            } shouldContain """data-on:click__delay.500ms"""
        }
    }

    describe("dataClass") {
        it("with string expression") {
            render { div { dataClass("active", "\$tab === 'home'") } } shouldContain
                """data-class:active="${'$'}tab === 'home'""""
        }
        it("with Expression") {
            val tab = signal("tab", "home")
            render { div { dataClass("active", tab.expr eq "home") } } shouldContain
                """data-class:active="${'$'}tab === 'home'""""
        }
        it("with boolean Signal") {
            val running = signal("running", false)
            render { div { dataClass("active", running) } } shouldContain
                """data-class:active="${'$'}running""""
        }
        it("hyphenated class name") {
            render { div { dataClass("status-success", "\$ok") } } shouldContain
                """data-class:status-success"""
        }
    }

    describe("dataAttr") {
        it("with string") {
            render { button { dataAttr("disabled", "\$running") } } shouldContain
                """data-attr:disabled="${'$'}running""""
        }
        it("with Signal") {
            val running = signal("running", false)
            render { button { dataAttr("disabled", running) } } shouldContain
                """data-attr:disabled="${'$'}running""""
        }
    }

    describe("dataText") {
        it("with string") {
            render { span { dataText("\$status") } } shouldContain """data-text="${'$'}status""""
        }
        it("with Signal") {
            val status = signal("status", "Ready")
            render { span { dataText(status) } } shouldContain """data-text="${'$'}status""""
        }
        it("with Expression") {
            val count = signal("count", 0)
            render { span { dataText(count.expr) } } shouldContain """data-text="${'$'}count""""
        }
    }

    describe("dataShow") {
        it("with string") {
            render { div { dataShow("\$visible") } } shouldContain """data-show="${'$'}visible""""
        }
        it("with boolean Signal") {
            val visible = signal("visible", true)
            render { div { dataShow(visible) } } shouldContain """data-show="${'$'}visible""""
        }
        it("with Expression") {
            val count = signal("count", 0)
            render { div { dataShow(count.expr gt 0) } } shouldContain """data-show="${'$'}count > 0""""
        }
    }

    describe("dataSignals") {
        it("with raw JSON") {
            render { div { dataSignals("{count: 0}") } } shouldContain
                """data-signals="{count: 0}""""
        }
        it("with typed Signals") {
            val count = signal("count", 0)
            val name = signal("name", "")
            val active = signal("active", true)
            render { div { dataSignals(count, name, active) } } shouldContain
                """data-signals="{count: 0, name: '', active: true}""""
        }
    }

    describe("dataComputed") {
        it("with string") {
            render { div { dataComputed("total", "\$price * \$quantity") } } shouldContain
                """data-computed:total"""
        }
        it("with Expression") {
            val price = signal("price", 0)
            render { div { dataComputed("display", price.expr gt 0) } } shouldContain
                """data-computed:display"""
        }
    }

    describe("dataInit") {
        it("with string") {
            render { div { dataInit("@get('/load')") } } shouldContain
                """data-init="@get('/load')""""
        }
        it("with Expression") {
            render { div { dataInit(get("/load")) } } shouldContain
                """data-init="@get('/load')""""
        }
    }

    describe("dataEffect") {
        it("generates data-effect") {
            render { div { dataEffect("\$total = \$price * \$qty") } } shouldContain
                """data-effect"""
        }
    }

    describe("dataIndicator") {
        it("generates data-indicator:{name}") {
            render { button { dataIndicator("fetching") } } shouldContain "data-indicator:fetching"
        }
    }

    describe("dataRef") {
        it("generates data-ref") {
            render { input { dataRef("searchInput") } } shouldContain """data-ref="searchInput""""
        }
    }

    describe("dataStyle") {
        it("generates data-style:{property}") {
            render { div { dataStyle("background-color", "\$red ? 'red' : 'blue'") } } shouldContain
                """data-style:background-color"""
        }
    }

    describe("dataOnIntersect") {
        it("generates data-on-intersect") {
            render { div { dataOnIntersect("\$visible = true") } } shouldContain
                """data-on-intersect"""
        }
    }

    describe("dataOnInterval") {
        it("generates data-on-interval") {
            render { div { dataOnInterval("\$count++") } } shouldContain
                """data-on-interval"""
        }
    }

    describe("dataIgnore") {
        it("generates data-ignore") {
            render { div { dataIgnore() } } shouldContain "data-ignore"
        }
    }

    describe("dataPreserveAttr") {
        it("generates data-preserve-attr") {
            render { div { dataPreserveAttr("open class") } } shouldContain
                """data-preserve-attr="open class""""
        }
    }
})

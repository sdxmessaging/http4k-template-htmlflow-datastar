package com.sdxmessaging.http4k.datastar

import htmlflow.doc
import htmlflow.html
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.string.shouldContain
import org.xmlet.htmlapifaster.body
import org.xmlet.htmlapifaster.button
import org.xmlet.htmlapifaster.div
import org.xmlet.htmlapifaster.input
import org.xmlet.htmlapifaster.span
import org.xmlet.htmlapifaster.textarea

class DatastarAttributesTest : DescribeSpec({

    fun render(block: org.xmlet.htmlapifaster.Body<*>.() -> Unit): String =
        StringBuilder().apply {
            doc {
                html {
                    body { block() }
                }
            }
        }.toString()

    describe("dataBind") {
        it("generates data-bind:{signal} attribute") {
            val html = render {
                input { dataBind("email") }
            }
            html shouldContain """data-bind:email"""
        }

        it("works on textarea elements") {
            val html = render {
                textarea { dataBind("pastetext") }
            }
            html shouldContain """data-bind:pastetext"""
        }
    }

    describe("dataOn") {
        it("generates data-on:{event} with expression") {
            val html = render {
                button { dataOn("click", "@post('/save')") }
            }
            html shouldContain """data-on:click="@post('/save')""""
        }

        it("supports builder with debounce modifier") {
            val html = render {
                input {
                    dataOn("input") {
                        expression = "@get('/search')"
                        debounce(200)
                    }
                }
            }
            html shouldContain """data-on:input__debounce_200ms="@get('/search')""""
        }

        it("supports multiple modifiers") {
            val html = render {
                button {
                    dataOn("click") {
                        expression = "@post('/save')"
                        prevent()
                        once()
                    }
                }
            }
            html shouldContain """data-on:click__prevent__once="@post('/save')""""
        }

        it("builder with no modifiers works like simple form") {
            val html = render {
                button {
                    dataOn("click") {
                        expression = "@get('/refresh')"
                    }
                }
            }
            html shouldContain """data-on:click="@get('/refresh')""""
        }
    }

    describe("dataClass") {
        it("generates data-class:{className} with expression") {
            val html = render {
                div { dataClass("active", "\$tab === 'home'") }
            }
            html shouldContain """data-class:active="${'$'}tab === 'home'""""
        }

        it("supports hyphenated class names") {
            val html = render {
                div { dataClass("status-success", "\$ok") }
            }
            html shouldContain """data-class:status-success="${'$'}ok""""
        }
    }

    describe("dataAttr") {
        it("generates data-attr:{name} with expression") {
            val html = render {
                button { dataAttr("disabled", "\$running") }
            }
            html shouldContain """data-attr:disabled="${'$'}running""""
        }

        it("works for href attributes") {
            val html = render {
                div { dataAttr("href", "\$url") }
            }
            html shouldContain """data-attr:href="${'$'}url""""
        }
    }

    describe("dataText") {
        it("generates data-text attribute") {
            val html = render {
                span { dataText("\$status") }
            }
            html shouldContain """data-text="${'$'}status""""
        }
    }

    describe("dataShow") {
        it("generates data-show attribute") {
            val html = render {
                div { dataShow("\$visible") }
            }
            html shouldContain """data-show="${'$'}visible""""
        }
    }

    describe("dataSignals") {
        it("generates data-signals attribute with JSON") {
            val html = render {
                div { dataSignals("{count: 0, name: ''}") }
            }
            html shouldContain """data-signals="{count: 0, name: ''}""""
        }
    }

    describe("dataInit") {
        it("generates data-init attribute") {
            val html = render {
                div { dataInit("@get('/load')") }
            }
            html shouldContain """data-init="@get('/load')""""
        }
    }

    describe("dataIndicator") {
        it("generates data-indicator:{name} attribute") {
            val html = render {
                button { dataIndicator("fetching") }
            }
            html shouldContain """data-indicator:fetching"""
        }
    }

    describe("dataRef") {
        it("generates data-ref attribute") {
            val html = render {
                input { dataRef("searchInput") }
            }
            html shouldContain """data-ref="searchInput""""
        }
    }
})

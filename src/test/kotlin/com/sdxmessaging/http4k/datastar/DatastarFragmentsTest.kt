package com.sdxmessaging.http4k.datastar

import htmlflow.HtmlView
import htmlflow.div
import htmlflow.dyn
import htmlflow.span
import htmlflow.view
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.http4k.template.ViewModel

class DatastarFragmentsTest : DescribeSpec({

    describe("fragment") {
        it("renders a simple div to an Element") {
            val element = fragment {
                div { attrId("status"); text("Done") }
            }
            element.value shouldContain """id="status""""
            element.value shouldContain "Done"
        }

        it("renders with Datastar attributes") {
            val element = fragment {
                div {
                    attrId("progress-log")
                    attrClass("log-area has-content")
                    dataClass("active", "\$running")
                    text("Loading...")
                }
            }
            element.value shouldContain """id="progress-log""""
            element.value shouldContain "data-class:active"
            element.value shouldContain "Loading..."
        }

        it("renders without html/body wrapper") {
            val element = fragment {
                span { text("just a span") }
            }
            element.value shouldContain "<span>"
            element.value shouldContain "just a span"
            element.value shouldNotContain "<!DOCTYPE"
            element.value shouldNotContain "<html"
            element.value shouldNotContain "<body"
        }
    }

    describe("HtmlView.toElement") {
        data class StatusModel(val message: String) : ViewModel

        val statusFragment: HtmlView<StatusModel> = view {
            div {
                attrId("status")
                dyn { model: StatusModel ->
                    text(model.message)
                }
            }
        }

        it("renders typed view with model to Element") {
            val element = statusFragment.toElement(StatusModel("Investigation complete"))
            element.value shouldContain """id="status""""
            element.value shouldContain "Investigation complete"
        }

        it("renders different models to different Elements") {
            val e1 = statusFragment.toElement(StatusModel("Loading"))
            val e2 = statusFragment.toElement(StatusModel("Done"))
            e1.value shouldContain "Loading"
            e2.value shouldContain "Done"
        }
    }

    describe("HtmlView.toElementRenderer") {
        data class CountModel(val count: Int) : ViewModel

        val counterFragment: HtmlView<CountModel> = view {
            span {
                attrId("counter")
                dyn { model: CountModel ->
                    text(model.count.toString())
                }
            }
        }

        it("creates reusable renderer function") {
            val render = counterFragment.toElementRenderer()
            val e1 = render(CountModel(1))
            val e2 = render(CountModel(42))
            e1.value shouldContain "1"
            e2.value shouldContain "42"
        }
    }
})

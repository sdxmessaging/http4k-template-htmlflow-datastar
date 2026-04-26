package com.sdxmessaging.http4k.datastar

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SignalAndExpressionTest : DescribeSpec({

    describe("Signal") {
        it("creates expression reference with dollar prefix") {
            val count = signal("count", 0)
            count.expr.js shouldBe "\$count"
        }

        it("preserves name and default") {
            val name = signal("userName", "Alice")
            name.name shouldBe "userName"
            name.default shouldBe "Alice"
        }
    }

    describe("Expression comparison operators") {
        val count = signal("count", 0).expr

        it("eq") { (count eq 10).js shouldBe "\$count === 10" }
        it("neq") { (count neq 0).js shouldBe "\$count !== 0" }
        it("gt") { (count gt 0).js shouldBe "\$count > 0" }
        it("lt") { (count lt 100).js shouldBe "\$count < 100" }
        it("gte") { (count gte 1).js shouldBe "\$count >= 1" }
        it("lte") { (count lte 99).js shouldBe "\$count <= 99" }
        it("eq with string") { (count eq "hello").js shouldBe "\$count === 'hello'" }
    }

    describe("Expression logical operators") {
        val loading = signal("loading", false).expr
        val count = signal("count", 0).expr

        it("and") { (count gt 0 and !loading).js shouldBe "\$count > 0 && !\$loading" }
        it("or") { (count eq 0 or loading).js shouldBe "\$count === 0 || \$loading" }
        it("not") { (!loading).js shouldBe "!\$loading" }
        it("not() function") { not(loading).js shouldBe "!\$loading" }
    }

    describe("Expression string methods") {
        val search = signal("search", "").expr

        it("trim") { search.trim().js shouldBe "\$search.trim()" }
        it("isEmpty") { search.isEmpty().js shouldBe "\$search.trim() === ''" }
        it("isNotEmpty") { search.isNotEmpty().js shouldBe "\$search.trim() !== ''" }
    }

    describe("Expression ternary") {
        val dark = signal("dark", false).expr

        it("ternary with strings") {
            dark.ternary("dark", "light").js shouldBe "\$dark ? 'dark' : 'light'"
        }
        it("ternary with numbers") {
            dark.ternary(1, 0).js shouldBe "\$dark ? 1 : 0"
        }
    }

    describe("Expression composition with actions") {
        val loading = signal("loading", false).expr

        it("not signal and action") {
            (!loading and post("/save")).js shouldBe "!\$loading && @post('/save')"
        }
    }

    describe("jsLiteral") {
        it("strings get quoted") { jsLiteral("hello") shouldBe "'hello'" }
        it("numbers stay raw") { jsLiteral(42) shouldBe "42" }
        it("booleans stay raw") { jsLiteral(true) shouldBe "true" }
        it("null becomes null") { jsLiteral(null) shouldBe "null" }
        it("signals become references") {
            jsLiteral(signal("x", 0)) shouldBe "\$x"
        }
        it("expressions pass through") {
            jsLiteral(Expression("\$foo")) shouldBe "\$foo"
        }
    }
})

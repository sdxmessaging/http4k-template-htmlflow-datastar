package com.sdxmessaging.http4k.datastar

/**
 * A Datastar expression — wraps a JavaScript expression string with
 * type-safe composition operators.
 *
 * Expressions are the values inside Datastar `data-*` attributes.
 * This class lets you build them using Kotlin operators instead of
 * string concatenation.
 *
 * ```kotlin
 * val count = signal("count", 0)
 * val loading = signal("loading", false)
 *
 * // Comparison
 * count.expr gt 0          // → "$count > 0"
 * count.expr eq 10         // → "$count === 10"
 *
 * // Logical
 * !loading.expr            // → "!$loading"
 * count.expr gt 0 and !loading.expr
 *                          // → "$count > 0 && !$loading"
 *
 * // Compose with actions
 * !loading.expr and post("/save")
 *                          // → "!$loading && @post('/save')"
 * ```
 *
 * @see <a href="https://data-star.dev/guide/datastar_expressions">Datastar expressions guide</a>
 */
class Expression(val js: String) {
    override fun toString() = js

    // ── Comparison ──

    /** `===` strict equality */
    infix fun eq(other: Any?): Expression = Expression("$js === ${jsLiteral(other)}")
    /** `!==` strict inequality */
    infix fun neq(other: Any?): Expression = Expression("$js !== ${jsLiteral(other)}")
    /** `>` greater than */
    infix fun gt(other: Any?): Expression = Expression("$js > ${jsLiteral(other)}")
    /** `<` less than */
    infix fun lt(other: Any?): Expression = Expression("$js < ${jsLiteral(other)}")
    /** `>=` greater than or equal */
    infix fun gte(other: Any?): Expression = Expression("$js >= ${jsLiteral(other)}")
    /** `<=` less than or equal */
    infix fun lte(other: Any?): Expression = Expression("$js <= ${jsLiteral(other)}")

    // ── Logical ──

    /** `&&` logical AND */
    infix fun and(other: Expression): Expression = Expression("$js && ${other.js}")
    /** `||` logical OR */
    infix fun or(other: Expression): Expression = Expression("$js || ${other.js}")
    /** `!` logical NOT */
    operator fun not(): Expression = Expression("!$js")

    // ── String methods (common in Datastar expressions) ──

    /** `.trim()` */
    fun trim(): Expression = Expression("$js.trim()")
    /** `.trim() === ''` */
    fun isEmpty(): Expression = Expression("$js.trim() === ''")
    /** `.trim() !== ''` */
    fun isNotEmpty(): Expression = Expression("$js.trim() !== ''")

    // ── Ternary ──

    /** JavaScript ternary: `condition ? then : otherwise` */
    fun ternary(then: Any?, otherwise: Any?): Expression =
        Expression("$js ? ${jsLiteral(then)} : ${jsLiteral(otherwise)}")
}

/** Negate a Datastar expression: `!expr` */
fun not(expr: Expression): Expression = !expr

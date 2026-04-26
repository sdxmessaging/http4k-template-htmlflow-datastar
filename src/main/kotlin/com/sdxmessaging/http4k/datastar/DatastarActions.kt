package com.sdxmessaging.http4k.datastar

/**
 * Datastar action expression builders.
 *
 * In Datastar, actions like `@get('/path')` and `@post('/path')` are
 * expressions used inside plugin attributes. These functions return
 * [Expression] objects that compose naturally with plugins and operators.
 *
 * ```kotlin
 * button { dataOn("click", post("/save")) }
 * // → <button data-on:click="@post('/save')">
 *
 * div { dataInit(get("/load")) }
 * // → <div data-init="@get('/load')">
 *
 * // Compose with logical operators
 * button { dataOn("click", !loading.expr and post("/save")) }
 * // → <button data-on:click="!$loading && @post('/save')">
 * ```
 *
 * @see <a href="https://data-star.dev/reference/action_plugins">Datastar action plugins</a>
 */

/** Build a `@get('/path')` action expression. */
fun get(path: String): Expression = Expression("@get('$path')")

/** Build a `@post('/path')` action expression. */
fun post(path: String): Expression = Expression("@post('$path')")

/** Build a `@put('/path')` action expression. */
fun put(path: String): Expression = Expression("@put('$path')")

/** Build a `@patch('/path')` action expression. */
fun patch(path: String): Expression = Expression("@patch('$path')")

/** Build a `@delete('/path')` action expression. */
fun delete(path: String): Expression = Expression("@delete('$path')")

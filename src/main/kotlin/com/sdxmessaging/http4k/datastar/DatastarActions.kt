package com.sdxmessaging.http4k.datastar

/**
 * Datastar action expression builders.
 *
 * In Datastar, actions like `@get('/path')` and `@post('/path')` are
 * expressions used inside plugin attributes such as `data-on:click`
 * and `data-init`. These functions build those expression strings.
 *
 * ```kotlin
 * button { dataOn("click", post("/save")) }
 * // → <button data-on:click="@post('/save')">
 *
 * div { dataInit(get("/load")) }
 * // → <div data-init="@get('/load')">
 * ```
 *
 * @see <a href="https://data-star.dev/reference/action_plugins">Datastar Action Plugins</a>
 */

/** Build a `@get('/path')` action expression. */
fun get(path: String): String = "@get('$path')"

/** Build a `@post('/path')` action expression. */
fun post(path: String): String = "@post('$path')"

/** Build a `@put('/path')` action expression. */
fun put(path: String): String = "@put('$path')"

/** Build a `@patch('/path')` action expression. */
fun patch(path: String): String = "@patch('$path')"

/** Build a `@delete('/path')` action expression. */
fun delete(path: String): String = "@delete('$path')"

package com.sdxmessaging.http4k.datastar

import org.xmlet.htmlapifaster.Element

/**
 * Convenience functions for common Datastar route actions.
 *
 * These wrap the `dataOn` and `dataInit` functions with
 * pre-built `@get()` / `@post()` expressions.
 */

// ── Route-aware event handlers ──────────────────────────────────────────────

/**
 * On event, perform a Datastar GET to the given path.
 *
 * Generates: `data-on:{event}="@get('{path}')"`
 *
 * ```kotlin
 * button { dataOnGet("click", "/refresh") }
 * // → <button data-on:click="@get('/refresh')">
 * ```
 */
fun <T : Element<T, *>> T.dataOnGet(event: String, path: String): T =
    dataOn(event, "@get('$path')")

/**
 * On event, perform a Datastar POST to the given path.
 *
 * Generates: `data-on:{event}="@post('{path}')"`
 *
 * ```kotlin
 * button { dataOnPost("click", "/save") }
 * // → <button data-on:click="@post('/save')">
 * ```
 */
fun <T : Element<T, *>> T.dataOnPost(event: String, path: String): T =
    dataOn(event, "@post('$path')")

/**
 * On event, perform a Datastar PUT to the given path.
 *
 * Generates: `data-on:{event}="@put('{path}')"`
 */
fun <T : Element<T, *>> T.dataOnPut(event: String, path: String): T =
    dataOn(event, "@put('$path')")

/**
 * On event, perform a Datastar PATCH to the given path.
 *
 * Generates: `data-on:{event}="@patch('{path}')"`
 */
fun <T : Element<T, *>> T.dataOnPatch(event: String, path: String): T =
    dataOn(event, "@patch('$path')")

/**
 * On event, perform a Datastar DELETE to the given path.
 *
 * Generates: `data-on:{event}="@delete('{path}')"`
 */
fun <T : Element<T, *>> T.dataOnDelete(event: String, path: String): T =
    dataOn(event, "@delete('$path')")

// ── Route-aware init ────────────────────────────────────────────────────────

/**
 * On init, perform a Datastar GET to the given path.
 *
 * Generates: `data-init="@get('{path}')"`
 *
 * ```kotlin
 * div { dataInitGet("/load-content") }
 * // → <div data-init="@get('/load-content')">
 * ```
 */
fun <T : Element<T, *>> T.dataInitGet(path: String): T =
    dataInit("@get('$path')")

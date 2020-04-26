package com.kdz.topstories.extensions

import io.reactivex.Maybe

/**
 * Return a Maybe that finishes if the contained list is null or empty.
 */

fun <T> Maybe<out List<T>?>.listOrEmpty(): Maybe<List<T>?> {
    return flatMap {
        if (it.isNullOrEmpty()) {
            Maybe.empty()
        } else {
            Maybe.just(it)
        }
    }
}

/**
 * Convert a list into a Maybe that either emits the list, or finishes if the list is null or empty.
 */
fun <T> List<T>?.toMaybe(): Maybe<List<T>?> {
    if(isNullOrEmpty()) {
        return Maybe.empty()
    }

    return Maybe.just(this)
}
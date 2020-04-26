package com.kdz.topstories.models

enum class Section(val value: String) {
    ARTS("arts"),
    HOME("home"),
    SCIENCE("science"),
    US("us"),
    WORLD("world");

    companion object {
        fun create(string: String): Section {
            values().forEach {
                if(string.equals(it.value)) return it
            }

            return WORLD
        }
    }
}
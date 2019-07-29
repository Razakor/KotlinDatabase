package com.vntu.main

object Parser {

    fun processQuote(s: String): String {
        return s.replace("'".toRegex(), "\''")
    }

    fun processBrackets(s: String): String {
        return s.replace("\\[".toRegex(), "").replace("]".toRegex(), "")
    }
}

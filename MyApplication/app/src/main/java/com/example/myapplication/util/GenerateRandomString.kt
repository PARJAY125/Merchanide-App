package com.example.myapplication.util

import kotlin.random.Random

class GenerateRandomString {
    companion object {
        fun generateRandomString(length: Int): String {
            val charPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            return (1..length)
                .map { Random.nextInt(0, charPool.length) }
                .map(charPool::get)
                .joinToString("")
        }
    }
}
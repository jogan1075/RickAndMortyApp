package com.jmc.details.utils

import android.text.SpannableString
import java.util.*
import java.util.concurrent.ThreadLocalRandom


object RandomFactory {

    fun generateString(): String = UUID.randomUUID().toString()
    fun generateDouble(): Double = Math.random()
    fun generateInt(): Int = ThreadLocalRandom.current().nextInt(0, 1000 + 1)
    fun generateBoolean(): Boolean = Math.random() < 0.5
    fun generateInt(min: Int = 0, max: Int): Int = ThreadLocalRandom.current().nextInt(min, max)
    fun generateLong(): Long = Random().nextLong()
    fun generateLong(min: Long = 0, max: Long = 1): Long =
        ThreadLocalRandom.current().nextLong(min, max)

    fun generateArrayString() = (0..5).map { generateString() }
    fun generateArrayLong() = (0..5).map { generateLong() }
    fun generateHashMapString() = run {
        val hashMap = hashMapOf<String, String>()
        (0..5).map {
            hashMap[generateString()] = generateString()
        }
        hashMap
    }

    fun generateSpannableString() = SpannableString(generateString())
    fun generateColorHex(): String {
        val random = Random()
        val nextInt = random.nextInt(0xffffff + 1)
        return String.format("#%06x", nextInt)
    }

    fun generateRandomValidEmail() =
        "${generateStringWithoutDash()}@${generateStringWithoutDashAndSubSequence(0, 4)}.com"
    fun generateRandomPhoneNumber() = "${generateLong(56900000000, 56999999999)}"
    fun generateRandomRut() = "${generateInt(3000000, 22000000)}-${generateInt(0, 11)}"

    private fun generateStringWithoutDash() = generateString().replace("-", "")
    private fun generateStringWithoutDashAndSubSequence(start: Int, end: Int) =
        generateStringWithoutDash().subSequence(start, end)


}
package com.example.stopwatch

import android.graphics.Color


class ColorsList {
    private val list = listOf(
        0xFFd9ed92.toInt(),
        0xFFb5e48c.toInt(),
        0xFf99d98c.toInt(),
        0xFF76c893.toInt(),
        0xFF52b69a.toInt(),
        0xFF34a0a4.toInt(),
        0xff168aad.toInt(),
        0xff1a759f.toInt(),
        0xff1e6091.toInt(),
        0xff184e77.toInt(),
        0xff03071e.toInt(),
        0xff370617.toInt(),
        0xff6a040f.toInt(),
        0xff9d0208.toInt(),
        0xffd00000.toInt(),
        0xffdc2f02.toInt(),
        0xffe85d04.toInt(),
        0xfff48c06.toInt(),
        0xfffaa307.toInt(),
        0xffffba08.toInt()
    )

    private val size = list.size

    fun getNextIndex(i: Int): Int {
        return if (i < size - 1) i + 1 else 0
    }

    operator fun get(i: Int): Int = list[i]
}
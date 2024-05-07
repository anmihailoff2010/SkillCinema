package com.example.skillcinema.app

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.skillcinema.R
import com.example.skillcinema.data.Month

fun ImageView.loadImage(imageUrl: String) {
    Glide
        .with(this)
        .load(imageUrl)
        .placeholder(R.drawable.no_poster)
        .into(this)
}

fun Int.converterInMonth(): String {
    var textMonth = ""
    if (this <= 0 || this > 12)
        textMonth = Month.AUGUST.name
    else
        Month.values().forEach { month ->
            if (this == month.count) textMonth = month.name
        }
    return textMonth
}

fun <T> List<T>.prepareToShow(size: Int): List<T> {
    val resultList = mutableListOf<T>()

    if (isEmpty()) {
        return resultList  // Возвращаем пустой список, если исходный список пуст
    }

    if (size >= this.size) {
        return this.toList()  // Возвращаем весь список, если он не превышает нужный размер
    }

    for (i in 0 until size) {
        resultList.add(this[i])
    }
    resultList.add(this[0])
    return resultList
}


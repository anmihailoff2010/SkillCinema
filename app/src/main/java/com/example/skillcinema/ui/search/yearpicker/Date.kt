package com.example.skillcinema.ui.search.yearpicker

import android.graphics.RectF

class Date(
    var date: Int,
    var rect: RectF,
    var status: State = State.NOT_SELECT,
)
package com.example.purritoplanner

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CalendarItem(
    val weekday: String, val date: Int,
    val breakfast: String,
    val lunch: String,
    val dinner: String
) : Parcelable
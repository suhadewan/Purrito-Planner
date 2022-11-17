package com.example.purritoplanner

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ingredient(val name: String,
                      val quantity: String = "",
                      val purchased: Boolean = false): Parcelable
package com.example.purritoplanner

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipeItem(var title: String = "Error", var ingredients: String = "Error"): Parcelable
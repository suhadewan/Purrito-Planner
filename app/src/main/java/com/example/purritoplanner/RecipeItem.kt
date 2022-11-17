package com.example.purritoplanner

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipeItem(var title: String, var ingredients: String): Parcelable
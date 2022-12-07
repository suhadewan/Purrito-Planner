package com.example.purritoplanner

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipeItem(var title: String = "Error", var ingredients: ArrayList<Ingredient> = ArrayList(), var categories: ArrayList<String> =
    ArrayList(), var recipeLink: String = "", var cookingNotes: String = "", var recipeImage: String = ""): Parcelable
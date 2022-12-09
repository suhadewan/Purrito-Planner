package com.example.purritoplanner

import android.net.Uri
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {
    private lateinit var ingredient: Ingredient
    private var ingredientList: MutableList<Ingredient> = java.util.ArrayList()
    private lateinit var recipe: RecipeItem

    private var categories = ArrayList<String>()
    private var imageUri: Uri? = null

    fun setIngredient(ingre: Ingredient) {
        ingredient = ingre
        ingredientList.add(ingre)
    }

    fun setAllIngredients(newIngredients: ArrayList<Ingredient>) {
        ingredientList = newIngredients
    }

    fun getIngredient(): Ingredient {
        return ingredient
    }

    fun getIngredientList(): MutableList<Ingredient> {
        return ingredientList
    }

    fun deleteIngredient(pos: Int) {
        ingredientList.removeAt(pos)
    }

    fun clearIngredientList() {
        ingredientList.clear()
    }

    fun getRecipe(): RecipeItem {
        return recipe
    }

    fun setRecipe(rec: RecipeItem) {
        recipe = rec
    }

    fun getCategories(): ArrayList<String> {
        return categories
    }

    fun setCategories(updatedCategories: ArrayList<String>) {
        categories = updatedCategories
    }

    fun getImageUri(): Uri? {
        return imageUri
    }

    fun setImageUri(newUri: Uri?) {
        imageUri = newUri
    }

}
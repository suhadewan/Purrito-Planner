package com.example.purritoplanner

import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {
    private lateinit var ingredient: Ingredient
    private var ingredientList: MutableList<Ingredient> = java.util.ArrayList()
    private lateinit var recipe: RecipeItem

    fun setIngredient(ingre: Ingredient) {
        ingredient = ingre
        ingredientList.add(ingre)
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

    fun getRecipe(): RecipeItem {
        return recipe
    }

    fun setRecipe(rec: RecipeItem) {
        recipe = rec
    }

}
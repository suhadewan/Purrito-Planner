package com.example.purritoplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {
    var newMeals: ArrayList<String> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun onNavigationClick(view: View) {
        if (view is ImageButton) {
            val navController = findNavController(R.id.main_screen_fragment_holder)
            navController.popBackStack(R.id.homeScreenFragment, false)
            when (view.id) {
                R.id.homeButton -> {
                    navController.navigate(R.id.homeScreenFragment)
                }
                R.id.planButton -> {
                    navController.navigate(R.id.calendarFragment)
                }
                R.id.mealButton -> {
                    //We haven't implemented this page yet!
                    //TEMPORARY FOR ME WHILE WORKING: It's going to a recipe page
                    navController.navigate(R.id.recipeListFragment)
                }
                R.id.shopButton -> {
                    navController.navigate(R.id.shoppingListFragment)
                }
            }
        }
    }
}
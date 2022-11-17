package com.example.purritoplanner

import android.app.AlertDialog
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController


class NewRecipeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Sets up the ingredients recyclerview.
        //TODO: Add this.

        //Set up the recipe categories dropdown.
        val dropDownText = view.findViewById<TextView>(R.id.recipe_category_text_views)
        val categoriesOptions = arrayOf("Favorites", "Breakfast", "Lunch", "Snack", "Dinner", "Drinks", "Quick and Easy", "On a Budget")
        val selectionList: ArrayList<Int> = ArrayList()
        val selectedOptions = BooleanArray(categoriesOptions.size)
        dropDownText.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Choose your recipe categories")
                builder.setCancelable(false);

                builder.setMultiChoiceItems(categoriesOptions, selectedOptions,
                    OnMultiChoiceClickListener { dialogInterface, pos, selected ->
                        if (selected) selectionList.add(pos) else selectionList.remove(Integer.valueOf(pos))
                    })

                builder.setPositiveButton("OK") { dialogInterface, i ->
                    val stringBuilder = StringBuilder()
                    for (index in selectionList) {
                        stringBuilder.append("${categoriesOptions[index]}, ")
                    }
                    dropDownText.text = stringBuilder.toString().replace(",\\s\$".toRegex(), "")
                }

                builder.setNegativeButton(
                    "Cancel"
                ) { dialogInterface, i -> // dismiss dialog
                    dialogInterface.dismiss()
                }

                builder.show()

            }

        })

        //Allow for the user to add a new ingredient.
        view.findViewById<Button>(R.id.new_ingredient_button).setOnClickListener {
            it.findNavController().navigate(R.id.action_newRecipeFragment_to_newGroceryItemFragment)
        }

        //Set up the edit text scroll settings.
        //view.findViewById<EditText>(R.id.recipe_edit_text).movementMethod = null

        //eText.setMovementMethod(null);

    }
}
package com.example.purritoplanner

import android.app.AlertDialog
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File


class NewRecipeFragment : Fragment() {

    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        cancelButton = view.findViewById(R.id.cancel_button)
        saveButton = view.findViewById(R.id.save_button)

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
                builder.setCancelable(false)

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

        cancelButton.setOnClickListener {
            it.findNavController().navigateUp()
        }

        saveButton.setOnClickListener {
            //save final stuff to firebase
            it.findNavController().navigateUp()
        }

        val recipeImageHolder = view.findViewById<ImageView>(R.id.new_recipe_image)
        val pickImages = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
            uri?.let { it ->
                Log.d("test", it.path!!)
                Glide.with(this@NewRecipeFragment)
                    .load(it)
                    .fitCenter()
                    .apply(RequestOptions().override(recipeImageHolder.width, recipeImageHolder.height))
                    .into(recipeImageHolder)
            }
        }
        recipeImageHolder.setOnClickListener {
            pickImages.launch("image/*")
        }

        //Set up the edit text scroll settings.
        //view.findViewById<EditText>(R.id.recipe_edit_text).movementMethod = null

        //eText.setMovementMethod(null)

    }
}
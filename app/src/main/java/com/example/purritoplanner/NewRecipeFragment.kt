package com.example.purritoplanner

import android.app.AlertDialog
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*


class NewRecipeFragment : Fragment() {

    private var ingredientList: MutableList<Ingredient> = java.util.ArrayList()
    private val adapter = IngredientListAdapter()
    private lateinit var model: MyViewModel
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var editRecipeTitle: EditText
    private lateinit var editRecipeURL: EditText
    private lateinit var editCookingNotes: EditText

    private lateinit var database: DatabaseReference

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
        model = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)

        //Sets up the ingredients recyclerview.
        val recyclerView = view.findViewById<RecyclerView>(R.id.ingredients_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        ingredientList = java.util.ArrayList()
        adapter.setIngredients(ingredientList as java.util.ArrayList<Ingredient>)
        editRecipeTitle = view.findViewById(R.id.recipe_title_edit_text)
        editRecipeURL = view.findViewById(R.id.recipe_link_edit_text)
        editCookingNotes = view.findViewById(R.id.recipe_edit_text)
        populateIngredientList()


        val categories = ArrayList<String>()
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
                        categories.add(categoriesOptions[index])
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
            val recipeTitle = editRecipeTitle.text.toString()
            val recipeURL = editRecipeURL.text.toString()
            val cookingNotes = editCookingNotes.text.toString()
            val ingredients = model.getIngredientList()
            //categories
            val newRecipeItem = RecipeItem(recipeTitle,
                ingredients as ArrayList<Ingredient>, categories, recipeURL, cookingNotes, "Bleh")
            database = FirebaseDatabase.getInstance("https://purrito-planner-default-rtdb.firebaseio.com/").reference
            database.child("Test Recipes").child(newRecipeItem.title).setValue(newRecipeItem).addOnFailureListener {
                Log.d("testFail", "failed to upload")
            }
            it.findNavController().navigateUp()
        }

        //Set up the edit text scroll settings.
        //view.findViewById<EditText>(R.id.recipe_edit_text).movementMethod = null

        //eText.setMovementMethod(null)

    }

    private fun populateIngredientList() {
        val ingredients = model.getIngredientList()
        for (ingredient in ingredients) {
            ingredientList.add(ingredient)
        }
        adapter.setIngredients(ingredientList as java.util.ArrayList<Ingredient>)
    }

    inner class IngredientListAdapter :
        RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder>() {
        private var ingredients = mutableListOf<Ingredient>()

        fun removeElement(pos: Int) {
            ingredients.removeAt(pos)
            adapter.notifyItemRemoved(pos)
        }

        internal fun setIngredients(groceries: java.util.ArrayList<Ingredient>) {
            this.ingredients = groceries
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return ingredients.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view_shop_edit, parent, false)
            return IngredientViewHolder(v)
        }

        override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
            val item = ingredients[position]
            val checkBox = holder.view.findViewById<CheckBox>(R.id.ingredient_check_box)
            val shoppingQuantity = if (item.quantity != "") ("(${item.quantity})") else ""
            val shoppingText = "${item.name} ${shoppingQuantity}"
            val deleteIcon = holder.view.findViewById<ImageView>(R.id.delete_icon)

            checkBox.text = shoppingText
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                checkBox.apply {
                    paintFlags = if (isChecked) (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG) else (paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                }
            }

            deleteIcon.setOnClickListener {
                removeElement(position)
                model.deleteIngredient(position)
            }

        }

        inner class IngredientViewHolder(val view: View) : RecyclerView.ViewHolder(view),
            View.OnClickListener {
            override fun onClick(view: View?) {
            }
        }

    }
}
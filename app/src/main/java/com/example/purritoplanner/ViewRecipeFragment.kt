package com.example.purritoplanner

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class ViewRecipeFragment : Fragment() {

    private val adapter = IngredientsListAdapter()
    private var ingredientsList: MutableList<Ingredient> = ArrayList()
    private lateinit var model: MyViewModel
    private lateinit var recipeTitle: TextView
    private lateinit var recipeTags: TextView
    private lateinit var cookingNotes: TextView
    private lateinit var recipeImage: ImageView

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        database =
            FirebaseDatabase.getInstance("https://purrito-planner-default-rtdb.firebaseio.com/").reference

        //Sets up the ingredients recyclerview.
        model = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        val recipeItem = model.getRecipe()
        val recyclerView = view.findViewById<RecyclerView>(R.id.ingredients_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        ingredientsList = ArrayList()
        adapter.setIngredients(ingredientsList as ArrayList<Ingredient>)
        populateIngredientsList(recipeItem)
        recipeTitle = view.findViewById(R.id.recipe_title_text)
        recipeTags = view.findViewById(R.id.recipe_tags_text)
        cookingNotes = view.findViewById(R.id.cooking_notes)
        recipeImage = view.findViewById(R.id.imageView)

        val stringBuilder = StringBuilder()
        for (category in recipeItem.categories) {
            stringBuilder.append("${category}, ")
        }
        recipeTags.text = stringBuilder.toString().replace(",\\s\$".toRegex(), "")
        recipeTitle.text = recipeItem.title
        cookingNotes.text = recipeItem.cookingNotes
        if (recipeItem.recipeImage != "") {
            Glide.with(this@ViewRecipeFragment)
                .load(Uri.parse(recipeItem.recipeImage))
                .fitCenter()
                .apply(
                    RequestOptions().override(
                        recipeImage.width,
                        recipeImage.height
                    )
                )
                .into(recipeImage)
        }


        //Allows the recipe link to be clickable.
        val recipeLink = view.findViewById<TextView>(R.id.recipe_link_text)
        recipeLink.movementMethod = LinkMovementMethod.getInstance()

        //Allows us to navigate to the "edit recipe" page.
        view.findViewById<Button>(R.id.recipe_edit_button).setOnClickListener {
            val selectedElementData = Bundle()
            selectedElementData.putParcelable("recipeToEdit", recipeItem)
            it.findNavController().navigate(R.id.action_viewRecipeFragment_to_newRecipeFragment, selectedElementData)
        }

        //Allows us to add ingredients to the shopping list.
        view.findViewById<Button>(R.id.recipe_to_grocery_button).setOnClickListener {
            for (newGroceryItem in recipeItem.ingredients) {
                database.child("Grocery List").child(newGroceryItem.name).setValue(newGroceryItem)
            }
            Toast.makeText(
                activity,
                "Ingredients added to shopping list!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun populateIngredientsList(recipe: RecipeItem) {
        val ingredients = recipe.ingredients
        for (ingredient in ingredients) {
            ingredientsList.add(ingredient)
        }
    }

    inner class IngredientsListAdapter :
        RecyclerView.Adapter<IngredientsListAdapter.IngredientViewHolder>() {
        private var ingredients = mutableListOf<Ingredient>()

        internal fun setIngredients(ingredients: ArrayList<Ingredient>) {
            this.ingredients = ingredients
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return ingredients.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view_shop, parent, false)
            return IngredientViewHolder(v)
        }

        override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
            val item = ingredients[position]
            val checkBox = holder.view.findViewById<CheckBox>(R.id.ingredient_check_box)
            val ingredientQuantity = if (item.quantity != "") ("(${item.quantity})") else ""
            val ingredientText = "${item.name} ${ingredientQuantity}"

            checkBox.text = ingredientText
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                checkBox.apply {
                    paintFlags = if (isChecked) (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG) else (paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                }
            }

        }

        inner class IngredientViewHolder(val view: View) : RecyclerView.ViewHolder(view),
            View.OnClickListener {
            override fun onClick(view: View?) {
            }
        }
    }

}
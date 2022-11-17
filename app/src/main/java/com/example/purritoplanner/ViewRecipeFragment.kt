package com.example.purritoplanner

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class ViewRecipeFragment : Fragment() {

    private val adapter = IngredientsListAdapter()
    private var ingredientsList: MutableList<Ingredient> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Sets up the ingredients recyclerview.
        val recyclerView = view.findViewById<RecyclerView>(R.id.ingredients_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        ingredientsList = ArrayList()
        adapter.setIngredients(ingredientsList as ArrayList<Ingredient>)
        populateIngredientsList()

        //Allows the recipe link to be clickable.
        val recipeLink = view.findViewById<TextView>(R.id.recipe_link_text)
        recipeLink.movementMethod = LinkMovementMethod.getInstance()

        //Allows us to navigate to the "edit recipe" page.
        //TODO: We need to make recipes parcelable and send the recipe over in a bundle.
        view.findViewById<Button>(R.id.recipe_edit_button).setOnClickListener {
            it.findNavController().navigate(R.id.action_viewRecipeFragment_to_newRecipeFragment)
        }
    }

    private fun populateIngredientsList() {
        ingredientsList.add(Ingredient("Uncooked bacon", "6 strips"))
        ingredientsList.add(Ingredient("Unsalted butter", "3 tbsp"))
        ingredientsList.add(Ingredient("Medium yellow onion"))
        ingredientsList.add(Ingredient("Minced garlic cloves", "3 large"))
        ingredientsList.add(Ingredient("Gold potatoes", "2 1/2 lbs"))
        ingredientsList.add(Ingredient("Chicken broth", "4 cups"))
        ingredientsList.add(Ingredient("Milk", "2 cups"))
        ingredientsList.add(Ingredient("Heavy cream", "2/3 cup"))
    }

    inner class IngredientsListAdapter :
        RecyclerView.Adapter<IngredientsListAdapter.IngredientViewHolder>() {
        private var ingredients = mutableListOf<Ingredient>()

        fun removeElement(pos: Int) {
            //TODO: Remove from database when that gets hooked up
            ingredients.removeAt(pos)
            adapter.notifyItemRemoved(pos);
        }

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
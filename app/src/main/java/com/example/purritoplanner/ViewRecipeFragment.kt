package com.example.purritoplanner

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val recyclerView = view.findViewById<RecyclerView>(R.id.ingredients_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        ingredientsList = ArrayList()
        ingredientsList.add(Ingredient("Test", "1 cup"))
        ingredientsList.add(Ingredient("Test", "1 cup"))
        ingredientsList.add(Ingredient("Test", "1 cup"))
        ingredientsList.add(Ingredient("Test", "1 cup"))
        ingredientsList.add(Ingredient("Test", "1 cup"))
        ingredientsList.add(Ingredient("Test", "1 cup"))
        ingredientsList.add(Ingredient("Test", "1 cup"))
        ingredientsList.add(Ingredient("Test", "1 cup"))
        adapter.setIngredients(ingredientsList as ArrayList<Ingredient>)

        //it.findNavController()
        //.navigate(R.id.action_listFragment_to_detailFragment, selectedElementData)

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
            val ingredientText = "${item.name} (${item.quantity})"

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
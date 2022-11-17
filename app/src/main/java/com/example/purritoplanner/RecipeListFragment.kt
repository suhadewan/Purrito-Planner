package com.example.purritoplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecipeListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private val recipeHeaders = ArrayList<String>()
    private lateinit var addRecipeButton: ImageView
    private lateinit var settingsButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initArray(recipeHeaders)
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)
        recyclerView = view.findViewById(R.id.recipes)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        viewAdapter = RecyclerViewAdapter(recipeHeaders, activity as MainActivity)
        recyclerView.adapter = viewAdapter
        addRecipeButton = view.findViewById(R.id.Add_Recipe_Button)
        settingsButton = view.findViewById(R.id.Recipe_Settings_Button)

        addRecipeButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_recipeListFragment_to_newRecipeFragment)
        }

        settingsButton.setOnClickListener {
            view.findNavController().navigate(R.id.settingsFragment)
        }

        return view
    }

    private fun initArray(myDataset: MutableList<String>){
        myDataset.clear()

        myDataset.add("Favorites")
        myDataset.add("Breakfast")
        myDataset.add("Lunch")
        myDataset.add("Snack")
        myDataset.add("Dinner")
        myDataset.add("Drinks")
        myDataset.add("Quick and Easy")
        myDataset.add("On a Budget")

    }

}

class RecyclerViewAdapter(private val myDataset: ArrayList<String>, private val activity: MainActivity) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_header_view, parent, false)
        return ViewHolder(v, activity)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(myDataset[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

    class ViewHolder(private val view: View, private val activity: MainActivity) : RecyclerView.ViewHolder(view) {
        fun bindItems(recipeHeaders: String) {
            val recipeHeader: TextView = itemView.findViewById(R.id.Recipe_Category)
            recipeHeader.text = recipeHeaders
            val recipeRecyclerView = view.findViewById<RecyclerView>(R.id.recipes)
            val recipes = ArrayList<RecipeItem>()
            recipes.add(RecipeItem("Soup", "Carrots, Mushroom, Cream, Meat"))
            val childRecipeAdapter = ChildRecyclerViewAdapter(recipes, activity as MainActivity)
            recipeRecyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            recipeRecyclerView.adapter = childRecipeAdapter

        }
    }
}

class ChildRecyclerViewAdapter(private val myRecipes: ArrayList<RecipeItem>, private val activity: MainActivity) :
    RecyclerView.Adapter<ChildRecyclerViewAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ChildRecyclerViewAdapter.RecipeViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_card_view, parent, false)
        return RecipeViewHolder(v, activity)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myRecipes.size

    class RecipeViewHolder(private val view: View, private val activity: MainActivity) : RecyclerView.ViewHolder(view) {
        fun bindItems(recipe: RecipeItem) {
            val recipeTitle: TextView = itemView.findViewById(R.id.Recipe_Title)
            val recipeIngredients: TextView = itemView.findViewById(R.id.Recipe_Ingredients)
            val recipeImage: ImageView = itemView.findViewById(R.id.Recipe_Image)
            recipeTitle.text = recipe.title
            recipeIngredients.text = recipe.ingredients
            recipeImage.setImageResource(R.drawable.potato_soup)
            view.setOnClickListener {
                view.findNavController().navigate(R.id.action_recipeListFragment_to_viewRecipeFragment)
            }

        }
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bindItems(myRecipes[position])
    }
}

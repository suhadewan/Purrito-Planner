package com.example.purritoplanner

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.*

class RecipeListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private val recipeHeaders = ArrayList<String>()
    private lateinit var addRecipeButton: ImageView
    private lateinit var settingsButton: ImageView

    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = FirebaseDatabase.getInstance("https://purrito-planner-default-rtdb.firebaseio.com/").reference
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)
        recyclerView = view.findViewById(R.id.recipes)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        viewAdapter = RecyclerViewAdapter(recipeHeaders, activity as MainActivity)
        recyclerView.adapter = viewAdapter
        addRecipeButton = view.findViewById(R.id.Add_Recipe_Button)
        settingsButton = view.findViewById(R.id.Recipe_Settings_Button)
        initArray(recipeHeaders)



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
        viewAdapter.notifyDataSetChanged()

        //to add into recyclerview rn
        var ingredients = ArrayList<Ingredient>()
        ingredients.add(Ingredient("Carrot"))
        ingredients.add(Ingredient("Corn"))
        var categories = ArrayList<String>()
        categories.add("Drinks")
        categories.add("Quick and Easy")
        val recipe = RecipeItem("Soup", ingredients, categories)
        val recipe2 = RecipeItem("Brownies", ingredients, categories)
        database.child("Recipes").child(recipe.title).setValue(recipe)
        database.child("Recipes").child(recipe2.title).setValue(recipe2)
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
        private lateinit var database: DatabaseReference
        private val recipes = mutableListOf<RecipeItem>()
        fun bindItems(recipeHeaders: String) {
            val recipeHeader: TextView = itemView.findViewById(R.id.Recipe_Category)
            recipeHeader.text = recipeHeaders
            recipes.clear()
            val recipeRecyclerView = view.findViewById<RecyclerView>(R.id.recipes)
            database = FirebaseDatabase.getInstance().reference
            recipeRecyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            recipeRecyclerView.adapter = EmptyRecyclerViewAdapter(activity as MainActivity)
            when (recipeHeaders) {
                "Favorites" -> initRecyclerView("Favorites", recipeRecyclerView)
                "Breakfast" -> initRecyclerView("Breakfast", recipeRecyclerView)
                "Lunch" -> initRecyclerView("Lunch", recipeRecyclerView)
                "Snack" -> initRecyclerView("Snack", recipeRecyclerView)
                "Dinner" -> initRecyclerView("Dinner", recipeRecyclerView)
                "Drinks" -> initRecyclerView("Drinks", recipeRecyclerView)
                "Quick and Easy" -> initRecyclerView("Quick and Easy", recipeRecyclerView)
                "On a Budget" -> initRecyclerView("On a Budget", recipeRecyclerView)
            }
        }

        private fun initRecyclerView(pathString: String, recipeRecyclerView: RecyclerView) {
            database.child("Recipes")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val recipeItem: RecipeItem? = snapshot.getValue(RecipeItem::class.java)
                            if (recipeItem!!.categories.contains(pathString)) {
                                Log.d("testItems", pathString + ": " +recipeItem!!.title)
                                recipes += recipeItem
                            }
                        }
                        Log.d("testInit", recipes.toString())
                        val childRecipeAdapter = ChildRecyclerViewAdapter(recipes, activity as MainActivity)
                        recipeRecyclerView.adapter = childRecipeAdapter
                        childRecipeAdapter.notifyDataSetChanged()
                    }
                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }

    }
}

class ChildRecyclerViewAdapter(private val myRecipes: MutableList<RecipeItem>, private val activity: MainActivity) :
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
            var ingredientList = ""
            for (ingredient in recipe.ingredients) {
                ingredientList = ingredientList + ingredient.name.toString() + " "
            }
            recipeIngredients.text = ingredientList
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

class EmptyRecyclerViewAdapter(private val activity: MainActivity) :
    RecyclerView.Adapter<EmptyRecyclerViewAdapter.EmptyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): EmptyRecyclerViewAdapter.EmptyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_card_view, parent, false)
        return EmptyViewHolder(v, activity)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = 0

    class EmptyViewHolder(private val view: View, private val activity: MainActivity) : RecyclerView.ViewHolder(view) {
        fun bindItems(recipe: RecipeItem) {
        }
    }

    override fun onBindViewHolder(holder: EmptyViewHolder, position: Int) {
    }
}



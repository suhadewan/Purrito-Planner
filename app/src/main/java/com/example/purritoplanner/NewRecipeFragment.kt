package com.example.purritoplanner

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


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
    private lateinit var storageRef: StorageReference
    private var imageUri: Uri? = null
    private var recipeToEdit: RecipeItem? =  null
    private var prevRecipeToEdit: RecipeItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Get a reference to where we'll be storing the photos.
        storageRef = FirebaseStorage.getInstance().reference

        cancelButton = view.findViewById(R.id.cancel_button)
        saveButton = view.findViewById(R.id.save_button)
        model = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)

        //Sets up the some references we'll be needing later.
        val recyclerView = view.findViewById<RecyclerView>(R.id.ingredients_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        ingredientList = java.util.ArrayList()
        adapter.setIngredients(ingredientList as java.util.ArrayList<Ingredient>)
        editRecipeTitle = view.findViewById(R.id.recipe_title_edit_text)
        editRecipeURL = view.findViewById(R.id.recipe_link_edit_text)
        editCookingNotes = view.findViewById(R.id.recipe_edit_text)
        val recipeImageHolder = view.findViewById<ImageView>(R.id.new_recipe_image)
        var categories = ArrayList<String>()
        val dropDownText = view.findViewById<TextView>(R.id.recipe_category_text_views)

        //Now, check to see if we were given a parcel that we can use.
        recipeToEdit = arguments?.getParcelable<RecipeItem>("recipeToEdit")
        prevRecipeToEdit = arguments?.getParcelable<RecipeItem>("recipeToEdit")
        if (recipeToEdit != null) {
            //Populate some of the fields, if we need to.
            model.setCategories(recipeToEdit!!.categories)
            if (recipeToEdit!!.recipeImage != "") {
                Glide.with(this@NewRecipeFragment)
                    .load(Uri.parse(recipeToEdit!!.recipeImage))
                    .fitCenter()
                    .apply(
                        RequestOptions().override(
                            recipeImageHolder.width,
                            recipeImageHolder.height
                        )
                    )
                    .into(recipeImageHolder)
            }
            model.setAllIngredients(recipeToEdit!!.ingredients)
            editRecipeTitle.setText(recipeToEdit!!.title)
            editRecipeURL.setText(recipeToEdit!!.recipeLink)
            editCookingNotes.setText(recipeToEdit!!.cookingNotes)
        }

        populateIngredientList()

        //If not, check if we have data stored in the viewmodel to use.
        if (model.getCategories().size > 0) {
            categories = model.getCategories()
            dropDownText.text = categories.joinToString(separator = ", ")
        }
        if (model.getImageUri() != null) {
            Glide.with(this@NewRecipeFragment)
                .load(model.getImageUri())
                .fitCenter()
                .apply(
                    RequestOptions().override(
                        recipeImageHolder.width,
                        recipeImageHolder.height
                    )
                )
                .into(recipeImageHolder)
        }

        //Set up the recipe categories dropdown.
        val categoriesOptions = arrayOf(
            "Favorites",
            "Breakfast",
            "Lunch",
            "Snack",
            "Dinner",
            "Drinks",
            "Quick and Easy",
            "On a Budget"
        )
        val selectionList: ArrayList<Int> = ArrayList()
        val selectedOptions = BooleanArray(categoriesOptions.size)
        if (model.getCategories().size > 0) {
            for (category in model.getCategories()) {
                selectedOptions[categoriesOptions.indexOf(category)] = true
                selectionList.add(categoriesOptions.indexOf(category))
            }
        }
        dropDownText.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Choose your recipe categories")
                builder.setCancelable(false)

                builder.setMultiChoiceItems(categoriesOptions, selectedOptions,
                    OnMultiChoiceClickListener { dialogInterface, pos, selected ->
                        if (selected) selectionList.add(pos) else selectionList.remove(
                            Integer.valueOf(
                                pos
                            )
                        )
                    })

                builder.setPositiveButton("OK") { dialogInterface, i ->
                    val stringBuilder = StringBuilder()
                    categories = ArrayList<String>()
                    for (index in selectionList) {
                        stringBuilder.append("${categoriesOptions[index]}, ")
                        categories.add(categoriesOptions[index])
                    }
                    model.setCategories(categories)
                    dropDownText.text = categories.joinToString(separator = ", ")
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
            model.clearIngredientList()
            model.setCategories(ArrayList<String>())
            model.setImageUri(null)
            //it.findNavController().navigateUp()
            it.findNavController().navigate(R.id.action_newRecipeFragment_to_recipeListFragment)
        }

        saveButton.setOnClickListener {
            //Start by ensuring that everything we need has been populated.
            //We need at least one category and a recipe title.
            //If we meet these conditions, then go ahead and save/close.
            if (editRecipeTitle.text.toString() == "" || categories.size == 0) {
                Toast.makeText(
                    activity,
                    "You must enter a recipe title and >1 category!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                model.setCategories(ArrayList<String>())
                model.setImageUri(null)
                saveToDatabase(it, categories)
            }
        }

        //Handle picking an image from the gallery.
        val pickImages =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let { it ->
                    Log.d("test", it.path!!)
                    Glide.with(this@NewRecipeFragment)
                        .load(it)
                        .fitCenter()
                        .apply(
                            RequestOptions().override(
                                recipeImageHolder.width,
                                recipeImageHolder.height
                            )
                        )
                        .into(recipeImageHolder)
                    imageUri = it
                    model.setImageUri(it)
                }
            }
        recipeImageHolder.setOnClickListener {
            pickImages.launch("image/*")
        }

    }

    private fun saveToDatabase(view: View, categories: ArrayList<String>) {

        //Put a loading symbol on the screen while waiting on an upload.
        val progress = ProgressDialog.show(requireActivity(), "Saving recipe", "Please wait...")

        //Start by uploading the recipe image to firebase, if we have one.
        //If we do, wait until we have the image link to continue. Otherwise,
        //just put an empty string in place of the image link.
        if (imageUri != null) {
            val fileName = imageUri?.pathSegments?.last()
            val uploadTask = storageRef.child("images/$fileName").putFile(imageUri!!)
            uploadTask.addOnSuccessListener {
                it.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    saveRecipeToFirebase(view, uri.toString(), categories, progress)
                }
            }
        } else {
            val newImageLink = if (recipeToEdit != null) recipeToEdit!!.recipeImage else ""
            saveRecipeToFirebase(view, newImageLink, categories, progress)
        }

    }

    private fun saveRecipeToFirebase(
        view: View,
        imageLink: String,
        categories: ArrayList<String>,
        progress: ProgressDialog
    ) {

        //Collect all the data we need to store in firebase.
        val recipeTitle = editRecipeTitle.text.toString()
        val recipeURL = editRecipeURL.text.toString()
        val cookingNotes = editCookingNotes.text.toString()
        val ingredients = model.getIngredientList()
        val newRecipeItem = RecipeItem(
            recipeTitle,
            ingredients as ArrayList<Ingredient>, categories, recipeURL, cookingNotes, imageLink
        )
        database =
            FirebaseDatabase.getInstance("https://purrito-planner-default-rtdb.firebaseio.com/").reference
        if (prevRecipeToEdit != null) {
            var firebasePostRef = database.child("Recipes").child(prevRecipeToEdit!!.title)
            firebasePostRef.removeValue()
        }
        database.child("Recipes").child(newRecipeItem.title).setValue(newRecipeItem)
            .addOnFailureListener {
                Log.d("testFail", "failed to upload")
            }.addOnSuccessListener {
            progress.dismiss()
            model.setRecipe(newRecipeItem)
            model.clearIngredientList()
            view.findNavController().navigate(R.id.action_newRecipeFragment_to_recipeListFragment)
        }
    }

    private fun populateIngredientList() {
        val ingredients = model.getIngredientList() as ArrayList<Ingredient>
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
            notifyDataSetChanged()
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
                    paintFlags =
                        if (isChecked) (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG) else (paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
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
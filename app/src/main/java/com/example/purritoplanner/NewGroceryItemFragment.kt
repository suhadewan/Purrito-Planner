package com.example.purritoplanner

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class NewGroceryItemFragment : Fragment() {

    private lateinit var model: MyViewModel
    private lateinit var editNewItem: EditText
    private lateinit var editNewQuantity: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_grocery_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        model = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        val saveButton = view.findViewById<Button>(R.id.save_button)
        editNewItem = view.findViewById(R.id.new_item_edit_text)
        editNewQuantity = view.findViewById(R.id.quantity_edit_text)
        saveButton.setOnClickListener {
            if (editNewItem.text.toString() != "") {

                val newIngredientItem = Ingredient(editNewItem.text.toString().lowercase()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }, editNewQuantity.text.toString())
                model.setIngredient(newIngredientItem)
                it.findNavController().navigateUp()
            }
            else {
                Toast.makeText(getActivity(),"Enter Item Name!", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            it.findNavController().navigateUp()
        }

    }

}
package com.example.purritoplanner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class NewShoppingItemFragment : Fragment() {
    private lateinit var editNewItem: EditText
    private lateinit var editNewQuantity: EditText
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_shopping_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val saveButton = view.findViewById<Button>(R.id.save_button)
        editNewItem = view.findViewById(R.id.new_item_edit_text)
        editNewQuantity = view.findViewById(R.id.quantity_edit_text)
        saveButton.setOnClickListener {
            if (editNewItem.text.toString() != "") {

                val newGroceryItem = Shopping(editNewItem.text.toString().lowercase()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }, editNewQuantity.text.toString())
                database = FirebaseDatabase.getInstance("https://purrito-planner-default-rtdb.firebaseio.com/").reference
                database.child("Grocery List").child(newGroceryItem.name).setValue(newGroceryItem).addOnFailureListener {
                    Log.d("testFail", "failed to upload")
                }
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
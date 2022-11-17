package com.example.purritoplanner

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

class NewGroceryItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_grocery_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val saveButton = view.findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener {
            //TODO: Have this save the data in some way
            it.findNavController().navigateUp()
        }

        view.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            it.findNavController().navigateUp()
        }

    }

}
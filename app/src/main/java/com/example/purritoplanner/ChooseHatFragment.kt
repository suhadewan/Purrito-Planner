package com.example.purritoplanner

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.findNavController

class ChooseHatFragment : Fragment() {

    private lateinit var exitButton: Button
    private lateinit var topHatIcon: ImageView
    private lateinit var pirateHatIcon: ImageView
    private lateinit var jesterCapIcon: ImageView
    private lateinit var tiaraIcon: ImageView
    private lateinit var bowIcon: ImageView
    private lateinit var nothingIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_choose_hat, container, false)
        exitButton = view.findViewById(R.id.Exit_Button)
        topHatIcon = view.findViewById(R.id.Top_Hat_Image)
        pirateHatIcon = view.findViewById(R.id.Pirate_Hat_Image)
        jesterCapIcon = view.findViewById(R.id.Jester_Cap_Image)
        tiaraIcon = view.findViewById(R.id.Tiara_Image)
        bowIcon = view.findViewById(R.id.Bow_Image)
        nothingIcon = view.findViewById(R.id.Nothing_Image)

        exitButton.setOnClickListener {
            view.findNavController().navigate(R.id.homeScreenFragment)
        }

        val preferenceAccess = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editPreferences: SharedPreferences.Editor = preferenceAccess.edit()
        val selectedHat = preferenceAccess.getString("selectedHat", "Nothing")
        when (selectedHat) {
            "TopHat" -> topHatIcon.setBackgroundColor(Color.argb(75, 214, 53, 4))
            "Nothing" -> nothingIcon.setBackgroundColor(Color.argb(75, 214, 53, 4))
        }

        topHatIcon.setOnClickListener {
            unselectAll()
            topHatIcon.setBackgroundColor(Color.argb(75, 214, 53, 4))
            editPreferences.putString("selectedHat", "TopHat")
            editPreferences.commit()
        }

        pirateHatIcon.setOnClickListener {

        }

        jesterCapIcon.setOnClickListener {

        }

        tiaraIcon.setOnClickListener {

        }

        bowIcon.setOnClickListener {

        }

        nothingIcon.setOnClickListener {
            unselectAll()
            nothingIcon.setBackgroundColor(Color.argb(75, 214, 53, 4))
            editPreferences.putString("selectedHat", "Nothing")
            editPreferences.commit()
        }

        return view
    }

    fun unselectAll() {
        topHatIcon.setBackgroundColor(Color.argb(0, 0, 0, 0))
        nothingIcon.setBackgroundColor(Color.argb(0, 0, 0, 0))
    }
}
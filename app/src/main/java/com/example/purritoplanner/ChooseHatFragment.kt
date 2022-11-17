package com.example.purritoplanner

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

        topHatIcon.setOnClickListener {

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

        }

        return view
    }
}
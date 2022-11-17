package com.example.purritoplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.navigation.findNavController

class HomeScreenFragment : Fragment() {
    private lateinit var hatButton: ImageView
    private lateinit var settingButton: ImageView
    private lateinit var catPicture: ImageView
    private lateinit var objective1Checkbox: CheckBox
    private lateinit var objective2Checkbox: CheckBox
    private lateinit var objective3Checkbox: CheckBox
    private lateinit var objective4Checkbox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_screen, container, false)
        hatButton = view.findViewById(R.id.Hat_Icon)
        settingButton = view.findViewById(R.id.Settings_Icon)
        objective1Checkbox = view.findViewById(R.id.Objective_1)
        objective2Checkbox = view.findViewById(R.id.Objective_2)
        objective3Checkbox = view.findViewById(R.id.Objective_3)
        objective4Checkbox = view.findViewById(R.id.Objective_4)

        hatButton.setOnClickListener {
            view.findNavController().navigate(R.id.chooseHatFragment)
        }

        settingButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeScreenFragment_to_settingsFragment)
        }

        objective1Checkbox.setOnClickListener {

        }
        objective2Checkbox.setOnClickListener {

        }
        objective3Checkbox.setOnClickListener {

        }
        objective4Checkbox.setOnClickListener {

        }
        return view
    }

}
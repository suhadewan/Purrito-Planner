package com.example.purritoplanner

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController


class HomeScreenFragment : Fragment() {
    private lateinit var hatButton: ImageView
    private lateinit var settingButton: ImageView
    private lateinit var catPicture: ImageView
    private lateinit var objective1Checkbox: CheckBox
    private lateinit var objective2Checkbox: CheckBox
    private lateinit var objective3Checkbox: CheckBox
    private lateinit var objective4Checkbox: CheckBox
    private lateinit var progressBar: ProgressBar

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
        progressBar = view.findViewById(R.id.Objective_Progress_Bar)

        hatButton.setOnClickListener {
            view.findNavController().navigate(R.id.chooseHatFragment)
        }

        settingButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_homeScreenFragment_to_settingsFragment)
        }

        val preferenceAccess = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editPreferences: SharedPreferences.Editor = preferenceAccess.edit()
        objective1Checkbox.text = preferenceAccess.getString("objective1", "Eat at least 1 meal a day")
        objective2Checkbox.text = preferenceAccess.getString("objective2", "Try one new meal")
        objective3Checkbox.text = preferenceAccess.getString("objective3", "Go grocery shopping")
        objective4Checkbox.text = preferenceAccess.getString("objective4", "Pet cat")
        objective1Checkbox.isChecked = preferenceAccess.getBoolean("objective1Status", false)
        objective2Checkbox.isChecked = preferenceAccess.getBoolean("objective2Status", false)
        objective3Checkbox.isChecked = preferenceAccess.getBoolean("objective3Status", false)
        objective4Checkbox.isChecked = preferenceAccess.getBoolean("objective4Status", false)
        objective1Checkbox.apply {
            paintFlags = if (objective1Checkbox.isChecked) (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG) else (paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
        }
        objective2Checkbox.apply {
            paintFlags = if (objective2Checkbox.isChecked) (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG) else (paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
        }
        objective3Checkbox.apply {
            paintFlags = if (objective3Checkbox.isChecked) (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG) else (paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
        }
        objective4Checkbox.apply {
            paintFlags = if (objective4Checkbox.isChecked) (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG) else (paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
        }
        progressBar.progress = preferenceAccess.getInt("objectivesProgress", 0)
        view.findViewById<TextView>(R.id.Ccompletion_Level).text = (progressBar.progress.toString() + "% Completed")

        objective1Checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            progressBar.progress = progressBar.progress + (if (isChecked) 25 else -25)
            editPreferences.putInt("objectivesProgress", progressBar.progress)
            editPreferences.putBoolean("objective1Status", isChecked)
            editPreferences.commit()
            objective1Checkbox.apply {
                paintFlags = if (isChecked) (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG) else (paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
            }
            view.findViewById<TextView>(R.id.Ccompletion_Level).text = (progressBar.progress.toString() + "% Completed")
        }
        objective2Checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            progressBar.progress = progressBar.progress + (if (isChecked) 25 else -25)
            editPreferences.putInt("objectivesProgress", progressBar.progress)
            editPreferences.putBoolean("objective2Status", isChecked)
            editPreferences.commit()
            objective2Checkbox.apply {
                paintFlags = if (isChecked) (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG) else (paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
            }
            view.findViewById<TextView>(R.id.Ccompletion_Level).text = (progressBar.progress.toString() + "% Completed")
        }
        objective3Checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            progressBar.progress = progressBar.progress + (if (isChecked) 25 else -25)
            editPreferences.putInt("objectivesProgress", progressBar.progress)
            editPreferences.putBoolean("objective3Status", isChecked)
            editPreferences.commit()
            objective3Checkbox.apply {
                paintFlags = if (isChecked) (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG) else (paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
            }
            view.findViewById<TextView>(R.id.Ccompletion_Level).text = (progressBar.progress.toString() + "% Completed")

        }
        objective4Checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            progressBar.progress = progressBar.progress + (if (isChecked) 25 else -25)
            editPreferences.putInt("objectivesProgress", progressBar.progress)
            editPreferences.putBoolean("objective4Status", isChecked)
            editPreferences.commit()
            objective4Checkbox.apply {
                paintFlags = if (isChecked) (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG) else (paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
            }
            view.findViewById<TextView>(R.id.Ccompletion_Level).text = (progressBar.progress.toString() + "% Completed")
        }

        val selectedHat = preferenceAccess.getString("selectedHat", "Nothing")
        val hatImage = view.findViewById<ImageView>(R.id.Hat_Image_Holder)
        when (selectedHat) {
            "TopHat" -> hatImage.setBackgroundResource(R.drawable.top_hat_thumbnail)
            "Nothing" -> hatImage.visibility = View.GONE
        }

        val selectedTheme = preferenceAccess.getBoolean("nightMode", false)
        when (selectedTheme) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }


        return view
    }

}
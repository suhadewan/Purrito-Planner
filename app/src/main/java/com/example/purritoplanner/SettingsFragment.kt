package com.example.purritoplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class SettingFragment : Fragment() {
    private lateinit var objective1EditText: EditText
    private lateinit var objective2EditText: EditText
    private lateinit var objective3EditText: EditText
    private lateinit var objective4EditText: EditText
    private lateinit var objective1ConfirmButton: Button
    private lateinit var objective2ConfirmButton: Button
    private lateinit var objective3ConfirmButton: Button
    private lateinit var objective4ConfirmButton: Button
    private lateinit var pushNotificationsSwitch: Switch
    private lateinit var themeGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        objective1EditText = view.findViewById(R.id.Objective_1_Text)
        objective2EditText = view.findViewById(R.id.Objective_2_Text)
        objective3EditText = view.findViewById(R.id.Objective_3_Text)
        objective4EditText = view.findViewById(R.id.Objective_4_Text)
        objective1ConfirmButton = view.findViewById(R.id.Confirm_Objective_1_Button)
        objective2ConfirmButton = view.findViewById(R.id.Confirm_Objective_2_Button)
        objective3ConfirmButton = view.findViewById(R.id.Confirm_Objective_3_Button)
        objective4ConfirmButton = view.findViewById(R.id.Confirm_Objective_4_Button)
        pushNotificationsSwitch = view.findViewById(R.id.Push_On_Off)
        themeGroup = view.findViewById(R.id.Theme_Group)

        pushNotificationsSwitch.setOnClickListener {

        }
        objective1ConfirmButton.setOnClickListener {

        }
        objective2ConfirmButton.setOnClickListener {

        }
        objective3ConfirmButton.setOnClickListener {

        }
        objective4ConfirmButton.setOnClickListener {

        }

        themeGroup.setOnCheckedChangeListener { group, id ->
            val themeButton = view.findViewById<RadioButton>(id)
        }

        return view
    }
}
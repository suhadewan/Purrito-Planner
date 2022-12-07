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
import android.widget.Toast
import androidx.navigation.findNavController

class ChooseHatFragment : Fragment() {

    private lateinit var exitButton: Button
    private lateinit var topHatIcon: ImageView
    private lateinit var pirateHatIcon: ImageView
    private lateinit var jesterCapIcon: ImageView
    private lateinit var tiaraIcon: ImageView
    private lateinit var bowIcon: ImageView
    private lateinit var nothingIcon: ImageView
    private lateinit var pirateHatLockIcon: ImageView
    private lateinit var jesterHatLockIcon: ImageView
    private lateinit var tiaraHatLockIcon: ImageView
    private lateinit var bowHatLockIcon: ImageView

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
        pirateHatLockIcon = view.findViewById(R.id.Pirate_Hat_Lock)
        jesterHatLockIcon = view.findViewById(R.id.Jester_Lock)
        tiaraHatLockIcon = view.findViewById(R.id.Tiara_Lock)
        bowHatLockIcon = view.findViewById(R.id.Bow_Lock)


        exitButton.setOnClickListener {
            view.findNavController().navigate(R.id.homeScreenFragment)
        }

        val preferenceAccess = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editPreferences: SharedPreferences.Editor = preferenceAccess.edit()
        val selectedHat = preferenceAccess.getString("selectedHat", "Nothing")
        val pirateLock = preferenceAccess.getBoolean("pirateLock", true)
        val jesterLock = preferenceAccess.getBoolean("jesterLock", true)
        val tiaraLock = preferenceAccess.getBoolean("tiaraLock", true)
        val bowLock = preferenceAccess.getBoolean("bowLock", true)
        when (selectedHat) {
            "TopHat" -> topHatIcon.setBackgroundColor(Color.argb(75, 214, 53, 4))
            "Nothing" -> nothingIcon.setBackgroundColor(Color.argb(75, 214, 53, 4))
            "PirateHat" -> pirateHatIcon.setBackgroundColor(Color.argb(75, 214, 53, 4))
            "JesterCap" -> jesterCapIcon.setBackgroundColor(Color.argb(75, 214, 53, 4))
            "Tiara" -> tiaraIcon.setBackgroundColor(Color.argb(75, 214, 53, 4))
            "Bow" -> bowIcon.setBackgroundColor(Color.argb(75, 214, 53, 4))
        }
        //Set visibility of Locks on Hats
        if (!pirateLock) {
            pirateHatLockIcon.visibility = View.GONE
        }
        else {
            pirateHatLockIcon.visibility = View.VISIBLE
        }
        if (!jesterLock) {
            jesterHatLockIcon.visibility = View.GONE
        }
        else {
            jesterHatLockIcon.visibility = View.VISIBLE
        }
        if (!tiaraLock) {
            tiaraHatLockIcon.visibility = View.GONE
        }
        else {
            tiaraHatLockIcon.visibility = View.VISIBLE
        }
        if (!bowLock) {
            bowHatLockIcon.visibility = View.GONE
        }
        else {
            bowHatLockIcon.visibility = View.VISIBLE
        }

        topHatIcon.setOnClickListener {
            unselectAll()
            topHatIcon.setBackgroundColor(Color.argb(75, 214, 53, 4))
            editPreferences.putString("selectedHat", "TopHat")
            editPreferences.commit()
        }

        pirateHatIcon.setOnClickListener {
            if (pirateLock) {
                Toast.makeText(getActivity(),"Need to ... for Pirate Hat", Toast.LENGTH_SHORT).show()
            }
            else {
                unselectAll()
                pirateHatIcon.setBackgroundColor(Color.argb(75, 214, 53, 4))
                editPreferences.putString("selectedHat", "PirateHat")
                editPreferences.commit()
            }
        }

        jesterCapIcon.setOnClickListener {
            if (jesterLock) {
                Toast.makeText(getActivity(),"Need to ... for Jester Cap", Toast.LENGTH_SHORT).show()
            }
            else {
                unselectAll()
                jesterCapIcon.setBackgroundColor(Color.argb(75, 214, 53, 4))
                editPreferences.putString("selectedHat", "JesterCap")
                editPreferences.commit()

            }
        }

        tiaraIcon.setOnClickListener {
            if (tiaraLock) {
                Toast.makeText(getActivity(), "Need to ... for Tiara", Toast.LENGTH_SHORT).show()
            } else {
                unselectAll()
                tiaraIcon.setBackgroundColor(Color.argb(75, 214, 53, 4))
                editPreferences.putString("selectedHat", "Tiara")
                editPreferences.commit()
            }
        }

        bowIcon.setOnClickListener {
            if (bowLock) {
                Toast.makeText(getActivity(),"Need to ... for Bow", Toast.LENGTH_SHORT).show()
            }
            else {
                unselectAll()
                bowIcon.setBackgroundColor(Color.argb(75, 214, 53, 4))
                editPreferences.putString("selectedHat", "Bow")
                editPreferences.commit()
            }
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
        pirateHatIcon.setBackgroundColor(Color.argb(0, 0, 0, 0))
        jesterCapIcon.setBackgroundColor(Color.argb(0, 0, 0, 0))
        tiaraIcon.setBackgroundColor(Color.argb(0, 0, 0, 0))
        bowIcon.setBackgroundColor(Color.argb(0, 0, 0, 0))
    }
}
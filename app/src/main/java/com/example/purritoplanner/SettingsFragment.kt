package com.example.purritoplanner

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import java.util.*


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
    private lateinit var hatLockResetButton: Button
    private lateinit var theme: SwitchCompat

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
        hatLockResetButton = view.findViewById(R.id.hat_lock_reset_button)
        theme = view.findViewById(R.id.themeSwitch)

        val preferenceAccess = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editPreferences: SharedPreferences.Editor = preferenceAccess.edit()
        objective1EditText.setText(preferenceAccess.getString("objective1", "Eat at least 1 meal a day"))
        objective2EditText.setText(preferenceAccess.getString("objective2", "Try one new meal"))
        objective3EditText.setText(preferenceAccess.getString("objective3", "Go grocery shopping"))
        objective4EditText.setText(preferenceAccess.getString("objective4", "Pet cat"))

        val isPush: Boolean = preferenceAccess.getBoolean("isPushNotif", false)
        if (isPush) {
            pushNotificationsSwitch.text="Turn off"
            pushNotificationsSwitch.isChecked = true

        }
        else {
            pushNotificationsSwitch.text="Turn on"
            pushNotificationsSwitch.isChecked = false
        }
        pushNotificationsSwitch.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                true -> {
                    //turn on push notifs
                    Log.d("test", "Turned on push notifications")

                    //This stuff would be used to make the notifications go off
                    //At a certain time of day, but for demo purposes I've set them
                    //to trigger more often instead.
                    val calendar: Calendar = Calendar.getInstance()
                    calendar.timeInMillis = System.currentTimeMillis()
                    //calendar.set(Calendar.HOUR_OF_DAY, 5)
                    //calendar.set(Calendar.MINUTE, 30)
                    //calendar.set(Calendar.SECOND, 0)

                    val dialogIntent = Intent(requireContext(), PushReceiver::class.java)
                    val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, dialogIntent, PendingIntent.FLAG_IMMUTABLE)
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, 60000, pendingIntent)

                    //pushNotificationsSwitch.isChecked = true
                    pushNotificationsSwitch.text="Turn off"
                    editPreferences.putBoolean("isPushNotif", true)
                    editPreferences.commit()
                }
                false -> {
                    Log.d("test", "turned off push notifications")
                    val cancelNotify = Intent(requireContext(), PushReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(context, 0, cancelNotify, PendingIntent.FLAG_IMMUTABLE)
                    val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmManager.cancel(pendingIntent)
                    //pushNotificationsSwitch.isChecked = false
                    pushNotificationsSwitch.text="Turn on"
                    editPreferences.putBoolean("isPushNotif", false)
                    editPreferences.commit()
                }
            }

        }
        objective1ConfirmButton.setOnClickListener {
            editPreferences.putString("objective1", objective1EditText.text.toString())
            editPreferences.commit()
        }
        objective2ConfirmButton.setOnClickListener {
            editPreferences.putString("objective2", objective2EditText.text.toString())
            editPreferences.commit()
        }
        objective3ConfirmButton.setOnClickListener {
            editPreferences.putString("objective3", objective3EditText.text.toString())
            editPreferences.commit()
        }
        objective4ConfirmButton.setOnClickListener {
            editPreferences.putString("objective4", objective4EditText.text.toString())
            editPreferences.commit()
        }

        val bool: Boolean = preferenceAccess.getBoolean("nightMode", true)
        if (bool) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            theme.isChecked = true
            theme.setText("Night Mode")
            editPreferences.putBoolean("jesterLock", false)
            editPreferences.commit()
            Log.d("jesterLock", "Got Here")
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            theme.isChecked = false
            theme.setText("Day Mode")
        }
        hatLockResetButton.setOnClickListener {
            val preferenceAccess = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
            val editPreferences: SharedPreferences.Editor = preferenceAccess.edit()
            editPreferences.putBoolean("pirateLock", true)
            editPreferences.putBoolean("jesterLock", true)
            editPreferences.putBoolean("tiaraLock", true)
            editPreferences.putBoolean("bowLock", true)
            editPreferences.commit()
        }

        theme.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    theme.isChecked = true
                    editPreferences.putBoolean("nightMode", true)
                    editPreferences.commit()
                }
                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    theme.isChecked = false
                    editPreferences.putBoolean("nightMode", false)
                    editPreferences.commit()
                }
            }

        }

        return view
    }

}
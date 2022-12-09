package com.example.purritoplanner

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.JsonReader
import android.util.JsonWriter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import java.io.*
import java.util.ArrayList

class CalendarFragment : Fragment() {

    private lateinit var activityMain: MainActivity
    private lateinit var mealArr: Array<ArrayList<String>>
    private lateinit var createView : View
    private var days = arrayOf<String>()
    private lateinit var list: Array<CalendarList>
    private var addMeal: ArrayList<String> = arrayListOf()
    private var idx = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mealArr = arrayOf(arrayListOf(), arrayListOf(),
            arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf(),arrayListOf())
        readingJSON()
        activityMain = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        createView = inflater.inflate(R.layout.add_meal, container, false)
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        val addButton: Button = view.findViewById(R.id.addButton)
        val clearButton : Button = view.findViewById(R.id.clear)

        addButton.setOnClickListener {
            if (activityMain.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val editT : EditText = createView.findViewById(R.id.editText)
                val builder = context?.let { AlertDialog.Builder(it) }
                builder?.setView(createView)
                builder?.setPositiveButton("Done"
                ) { _, _ ->
                    val index = weekdayClicked()
                    val textString = editT.text.toString()
                    update(index, textString)
                }?.setNegativeButton("Cancel", null)?.create()
                builder?.show()
            } else {
                val editT = EditText(context)
                val builder = context?.let { AlertDialog.Builder(it) }
                builder?.setTitle("Add Meal?")
                builder?.setSingleChoiceItems(days, -1) { _, i ->
                    idx = i
                }
                builder?.setPositiveButton("Done"
                ) { _, _ ->
                    update(idx, editT.text.toString())
                }
                builder?.setNegativeButton("Cancel", null)?.setView(editT)
                builder?.create()?.show()
            }
        }
        clearButton.setOnClickListener{
            for (m in list) {
                m.meals.clear()
                val adapter = context?.let {
                    ArrayAdapter<String>(
                        it,
                        R.layout.activity_listview, m.meals
                    )
                }
                m.listview_var.adapter = adapter
            }
        }


        createList(view)
        showText()
        return view
    }

    override fun onStop() {
        super.onStop()
        val file = context?.openFileOutput("calendar.json", Context.MODE_PRIVATE)
        val writer = JsonWriter(OutputStreamWriter(file as OutputStream))
        writer.setIndent("  ")
        writer.beginObject()
        for (i in list.indices) {
            writer.name(dayStr(i))
            writer.beginArray()
            for (meal in list[i].meals) {
                writer.value(meal)
            }
            writer.endArray()
        }
        writer.endObject()
        writer.close()
        activityMain.newMeals = addMeal
    }

    private fun weekdayClicked() : Int{
        var dIdx = -1
        val monText :TextView = createView.findViewById(R.id.monButton)
        val tuesText:TextView = createView.findViewById(R.id.tuesButton)
        val wedText:TextView = createView.findViewById(R.id.wedButton)
        val thursText:TextView = createView.findViewById(R.id.thursButton)
        val friText:TextView = createView.findViewById(R.id.friButton)
        val satText:TextView = createView.findViewById(R.id.satButton)
        val sunText:TextView = createView.findViewById(R.id.sunButton)

        val small = arrayOf(monText.text.toString(), tuesText.text, wedText.text,
            thursText.text, friText.text, satText.text, sunText.text)

        val group : RadioGroup = createView.findViewById(R.id.radioButtons)
        val button : RadioButton = createView.findViewById(group.checkedRadioButtonId)
        for (i in small.indices) {
            if (small[i] == button.text) {
                dIdx = i
            }
        }
        return dIdx
    }


    private fun createList(view: View) {
        val mon: ListView = view.findViewById(R.id.mon_listview)
        val tu: ListView = view.findViewById(R.id.tue_listview)
        val wed: ListView = view.findViewById(R.id.wed_listview)
        val th: ListView = view.findViewById(R.id.thur_listview)
        val fr: ListView = view.findViewById(R.id.fri_listview)
        val sat: ListView = view.findViewById(R.id.sat_listview)
        val sun: ListView = view.findViewById(R.id.sun_listview)

        val mText : TextView = view.findViewById(R.id.mon_text)
        val tueText : TextView = view.findViewById(R.id.tue_text)
        val wedText : TextView = view.findViewById(R.id.wed_text)
        val thurText : TextView = view.findViewById(R.id.thur_text)
        val friText : TextView = view.findViewById(R.id.fri_text)
        val satText : TextView = view.findViewById(R.id.sat_text)
        val sunText : TextView = view.findViewById(R.id.sun_text)

        days = arrayOf(mText.text.toString(), tueText.text.toString(),
            wedText.text.toString(), thurText.text.toString(), friText.text.toString(),
            satText.text.toString(), sunText.text.toString())

        list = arrayOf(
            CalendarList(mealArr[0], mon, mText),
            CalendarList(mealArr[1], tu, tueText),
            CalendarList(mealArr[2], wed, wedText),
            CalendarList(mealArr[3], th, thurText),
            CalendarList(mealArr[4], fr, friText),
            CalendarList(mealArr[5], sat, satText),
            CalendarList(mealArr[6], sun, sunText)
        )
    }
    private fun showText() {
        for (i in list.indices) {
            val adapter = context?.let {
                ArrayAdapter<String>(
                    it,
                    R.layout.activity_listview, list[i].meals
                )
            }
            list[i].listview_var.adapter = adapter
        }
    }

    private fun dayStr(index : Int) : String {
        var day = ""
        when(index) {
            0 -> day = "Monday"
            1 -> day = "Tuesday"
            2 -> day = "Wednesday"
            3 -> day = "Thursday"
            4 -> day = "Friday"
            5 -> day = "Saturday"
            6 -> day = "Sunday"
        }
        return day
    }


    private fun getIdx(day: String) : Int {
        var index = -1
        when(day) {
            "Monday" -> index = 0
            "Tuesday" -> index = 1
            "Wednesday" -> index = 2
            "Thursday" -> index = 3
            "Friday" -> index = 4
            "Saturday" -> index = 5
            "Sunday" -> index = 6
        }
        return index
    }

    private fun readingJSON() {
        try {
            val file = context?.openFileInput("calendar.json")
            val reader = JsonReader(InputStreamReader(file as InputStream))
            Log.d("Reading", "file")

            reader.beginObject()
            while (reader.hasNext()) {

                val name = reader.nextName()
                Log.d("Week day", name)
                idx = getIdx(name)
                reader.beginArray()
                while(reader.hasNext()) {
                    val meal = reader.nextString()
                    Log.d("item", meal)
                    mealArr[idx].add(meal)
                }
                reader.endArray()
            }
            reader.endObject()
            reader.close()
            Log.d("Closing", "File")

        } catch (e: FileNotFoundException) {
            Log.d("item", "no items")
        } catch (e: EOFException) {
            Log.d("nothing", "in file")
        }
    }

    private fun update(num :Int, text: String) {
        Log.d("$num", text)
        if (idx != -1) {
            list[num].meals.add(text)
            addMeal.add(text)
            val weekdayAdapter = context?.let {
                ArrayAdapter<String>(
                    it,
                    R.layout.activity_listview, list[num].meals
                )
            }
            list[num].listview_var.adapter = weekdayAdapter

        }
    }




}
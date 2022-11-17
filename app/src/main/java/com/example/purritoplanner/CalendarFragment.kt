package com.example.purritoplanner

import android.graphics.Paint
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class CalendarFragment : Fragment() {

    private val adapter = CalendarListAdapter()
    private var calendarList: MutableList<CalendarItem> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Sets up the calendar recyclerview.
        val recyclerView = view.findViewById<RecyclerView>(R.id.calendar_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        calendarList = ArrayList()
        adapter.setCalendarItems(calendarList as ArrayList<CalendarItem>)
        populateCalendar()

    }

    private fun populateCalendar() {
        calendarList.add(CalendarItem("Monday", 14, "Avocado and Egg Toast", "Chickpea Salad", "Roasted Chicken and Veggies"))
        calendarList.add(CalendarItem("Tuesday", 15, "Cereal", "Crackers", "Soup"))
        calendarList.add(CalendarItem("Wednesday", 16, "Scrambled Eggs", "Sleeve of saltines", "Pizza"))
        calendarList.add(CalendarItem("Thursday", 17, "Oats", "A single mushroom", "Burrito"))
        calendarList.add(CalendarItem("Friday", 18, "Grits", "Apples!! :)", "Sushi"))
        calendarList.add(CalendarItem("Saturday", 19, "Milk ONLY", "Chickpea Salad (again)", "Potatoes"))
    }

    inner class CalendarListAdapter :
        RecyclerView.Adapter<CalendarListAdapter.CalendarViewHolder>() {
        private var calendarItems = mutableListOf<CalendarItem>()

        internal fun setCalendarItems(calendarItems: ArrayList<CalendarItem>) {
            this.calendarItems = calendarItems
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return calendarItems.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view_days, parent, false)
            return CalendarViewHolder(v)
        }

        override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
            val item = calendarItems[position]

            holder.view.findViewById<TextView>(R.id.home_card_date).text = item.date.toString()
            holder.view.findViewById<TextView>(R.id.home_card_day).text = item.weekday

            holder.view.findViewById<TextView>(R.id.breakfast).text = item.breakfast
            holder.view.findViewById<TextView>(R.id.lunch).text = item.lunch
            holder.view.findViewById<TextView>(R.id.dinner).text = item.dinner

        }

        inner class CalendarViewHolder(val view: View) : RecyclerView.ViewHolder(view),
            View.OnClickListener {
            override fun onClick(view: View?) {
            }
        }
    }

}
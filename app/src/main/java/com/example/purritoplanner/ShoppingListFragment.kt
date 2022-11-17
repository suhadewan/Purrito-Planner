package com.example.purritoplanner

import android.graphics.Paint
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class ShoppingListFragment : Fragment() {
    private var shoppingList: MutableList<Shopping> = ArrayList()
    private val adapter = ShoppingListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Sets up the ingredients recyclerview.
        val recyclerView = view.findViewById<RecyclerView>(R.id.shoppingListRecycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        shoppingList = ArrayList()
        adapter.setGroceries(shoppingList as ArrayList<Shopping>)
        populateShoppingList()

        //Allows us to navigate to the "edit recipe" page.
        //TODO: We need to make recipes parcelable and send the recipe over in a bundle.
        view.findViewById<Button>(R.id.editButton).setOnClickListener {
            it.findNavController().navigate(R.id.action_shoppingListFragment_to_editShoppingListFragment)
        }
    }

    private fun populateShoppingList() {
        shoppingList.add(Shopping("Butter", "2 sticks"))
        shoppingList.add(Shopping("Chicken", "1"))
        shoppingList.add(Shopping("Sugar", "1 pack"))
        shoppingList.add(Shopping("Carrots", "2"))
        shoppingList.add(Shopping("Bread", "1 pack"))
        shoppingList.add(Shopping("Onions", "1 pack"))
        shoppingList.add(Shopping("Cucumber", "3"))
        shoppingList.add(Shopping("Garlic", "1"))
        //shoppingList.add(Shopping("Avocado", "1 pack"))

    }
    inner class ShoppingListAdapter :
        RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder>() {
        private var groceries = mutableListOf<Shopping>()

        fun removeElement(pos: Int) {
            //TODO: Remove from database when that gets hooked up
            groceries.removeAt(pos)
            adapter.notifyItemRemoved(pos);
        }

        internal fun setGroceries(groceries: java.util.ArrayList<Shopping>) {
            this.groceries = groceries
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return groceries.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view_shop, parent, false)
            return ShoppingViewHolder(v)
        }

        override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
            val item = groceries[position]
            val checkBox = holder.view.findViewById<CheckBox>(R.id.ingredient_check_box)
            val shoppingQuantity = if (item.quantity != "") ("(${item.quantity})") else ""
            val shoppingText = "${item.name} ${shoppingQuantity}"

            checkBox.text = shoppingText
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                checkBox.apply {
                    paintFlags = if (isChecked) (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG) else (paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                }
            }

        }

        inner class ShoppingViewHolder(val view: View) : RecyclerView.ViewHolder(view),
            View.OnClickListener {
            override fun onClick(view: View?) {
            }
        }

    }


}
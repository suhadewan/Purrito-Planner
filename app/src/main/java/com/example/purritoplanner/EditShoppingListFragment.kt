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
import com.google.firebase.database.*
import java.util.ArrayList

class EditShoppingListFragment : Fragment() {
    private var shoppingList: MutableList<Shopping> = ArrayList()
    private val adapter = ShoppingListAdapter()

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_shopping_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Sets up the ingredients recyclerview.
        val recyclerView = view.findViewById<RecyclerView>(R.id.editShoppingListRecycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        shoppingList = ArrayList()
        adapter.setGroceries(shoppingList as ArrayList<Shopping>)
        database = FirebaseDatabase.getInstance().reference
        populateShoppingList()

        view.findViewById<Button>(R.id.doneButton).setOnClickListener {
            //it.findNavController().navigate(R.id.action_editShoppingListFragment_to_shoppingListFragment)
            it.findNavController().navigateUp()
        }
        view.findViewById<ImageView>(R.id.editShop_Settings_Button).setOnClickListener {
            it.findNavController().navigate(R.id.action_editShoppingListFragment_to_settingsFragment)
        }
    }

    private fun populateShoppingList() {
        database.child("Grocery List").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val groceryItem: Shopping? = snapshot.getValue(Shopping::class.java)
                    shoppingList.add(groceryItem!!)
                }
                adapter.setGroceries(shoppingList as ArrayList<Shopping>)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }
    inner class ShoppingListAdapter :
        RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder>() {
        private var groceries = mutableListOf<Shopping>()

        fun removeElement(pos: Int): Shopping {
            var shoppingItem: Shopping = groceries.get(pos)
            groceries.removeAt(pos)
            adapter.notifyItemRemoved(pos)
            return shoppingItem
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
                .inflate(R.layout.card_view_shop_edit, parent, false)
            return ShoppingViewHolder(v)
        }

        override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
            val item = groceries[position]
            val checkBox = holder.view.findViewById<CheckBox>(R.id.ingredient_check_box)
            val shoppingQuantity = if (item.quantity != "") ("(${item.quantity})") else ""
            val shoppingText = "${item.name} ${shoppingQuantity}"
            val deleteIcon = holder.view.findViewById<ImageView>(R.id.delete_icon)

            checkBox.text = shoppingText
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                checkBox.apply {
                    paintFlags = if (isChecked) (paintFlags or Paint.STRIKE_THRU_TEXT_FLAG) else (paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                }
            }
            deleteIcon.setOnClickListener {
                var deletedShoppingItem = removeElement(position)
                var firebasePostRef = database.child("Grocery List").child(deletedShoppingItem.name)
                firebasePostRef.removeValue()
            }

        }

        inner class ShoppingViewHolder(val view: View) : RecyclerView.ViewHolder(view),
            View.OnClickListener {
            override fun onClick(view: View?) {
            }
        }

    }

}
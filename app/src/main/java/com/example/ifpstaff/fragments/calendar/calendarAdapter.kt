package com.example.ifpstaff.fragments.calendar

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ifpstaff.R
import com.example.ifpstaff.model.ModelCalendar

class calendarAdapter (
    private val onDeleteClickListener: (ModelCalendar) -> Unit,
    private val onUpdateClickListener: (ModelCalendar) -> Unit
    ) : RecyclerView.Adapter<calendarAdapter.ViewHolder>() {

        val items = mutableListOf<ModelCalendar>()

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val note: TextView = view.findViewById(R.id.tvNote)
            val updateButton: ImageView = view.findViewById(R.id.btnUpdate)
            val deleteButton: ImageView = view.findViewById(R.id.btnDelete)
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.calendar_main_items, viewGroup, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            with(viewHolder) {
                note.text = items[position].note
                deleteButton.setOnClickListener {

                    val builder = AlertDialog.Builder(it.context)
                    builder.setMessage("Are you sure you want to Delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes") { dialog, id ->
                            // Delete selected note from database
                            onDeleteClickListener.invoke(items[position])
                        }
                        .setNegativeButton("No") { dialog, id ->
                            // Dismiss the dialog
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()

                }
                updateButton.setOnClickListener {
                    onUpdateClickListener.invoke(items[position])
                }
            }
        }

        override fun getItemCount() = items.size
}
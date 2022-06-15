package com.example.ifpstaff

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ifpstaff.model.ModelCalendarOverview
import android.os.Bundle
import com.example.ifpstaff.fragments.calendar.fragmentCalendarMain
import com.example.ifpstaff.model.ModelCalendar


class CalendarOverviewAdapter(
    private val onClickEachDay: (ModelCalendarOverview) -> Unit
) : RecyclerView.Adapter<CalendarOverviewAdapter.ViewHolder>() {

    val items = mutableListOf<ModelCalendarOverview>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btn: Button = view.findViewById(R.id.btnEachDay)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_overview_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {

            btn.text = items[position].day
            btn.setBackgroundColor(Color.parseColor("#cccccc"))
            btn.setOnClickListener {
                onClickEachDay.invoke(items[position])
            }

            if(items[position].count.toInt() !=0)
                btn.setBackgroundColor(Color.parseColor("#e5da00"))

        }
    }

    override fun getItemCount() = items.size
}
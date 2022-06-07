package com.example.ifpstaff

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.ifpstaff.model.ModelCalendarOverview

class CalendarOverviewAdapter() : RecyclerView.Adapter<CalendarOverviewAdapter.ViewHolder>() {

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

            if(items[position].count.toInt() !=0)
                btn.setBackgroundColor(Color.parseColor("#e5da00"))

            /*
            //Get Click Handling of Button
            var row_index: Int = 0
            btn.setOnClickListener(View.OnClickListener {
                row_index = position
                btn.setBackgroundColor(Color.parseColor("#cccccc"))
            })
            */
        }
    }

    override fun getItemCount() = items.size
}
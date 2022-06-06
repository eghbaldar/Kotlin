package com.example.ifpstaff

import android.R.attr
import android.app.ActionBar
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ifpstaff.databinding.MainActivityBinding
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.lifecycle.ViewModelProvider
import com.example.ifpstaff.databinding.ActivityDialogeInfoContactBinding
import android.widget.LinearLayout
import androidx.core.view.marginTop
import android.R.attr.right

import android.R.attr.left
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.ifpstaff.fragments.calendar.calendarAdapter
import com.example.ifpstaff.model.ModelCalendar
import com.example.ifpstaff.model.ModelCalendarOverview
import androidx.recyclerview.widget.GridLayoutManager

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    // Create Variable of Binding Structure
    private lateinit var binding: MainActivityBinding
    private lateinit var bindingInfo: ActivityDialogeInfoContactBinding

    // Create Variable of ViewModel Class (MainActivity)
    private lateinit var MainAtivityViewMode: MainActivtyViewModel

    //temp
    private var adapterOverView = CalendarOverviewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // It's clear
        binding = MainActivityBinding.inflate(layoutInflater)
        bindingInfo = ActivityDialogeInfoContactBinding.inflate(layoutInflater)

        // Setup DrawerLayout
        drawerLayout = binding.drawerlayout

        // Create a sample of Variable ViewModel Class (MainActivity)
        MainAtivityViewMode = ViewModelProvider(this).get(MainActivtyViewModel::class.java)

        // It's clear
        setContentView(binding.root)

        //Make visible Toggle Icon (Three-Line [DrawerNavigation] icon on actionbar)
        val toogle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.myToolbar,
            R.string.app_name,
            R.string.app_name
        )
        toogle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        //Change the color and font of an item in drawer navigation through ViewModel
        MainAtivityViewMode.ChangeColorSizeMenuItem(
            this,
            binding.navView,
            "Developer",
            0.9F,
            R.color.gray
        )
        MainAtivityViewMode.ChangeColorSizeMenuItem(
            this,
            binding.navView,
            "Alimohammad Eghbaldar",
            0.9F,
            R.color.gray
        )
        MainAtivityViewMode.ChangeColorSizeMenuItem(
            this,
            binding.navView,
            "© 2022",
            0.9F,
            R.color.gray
        )

        //for click on NavigationItems (با این دستور، برنامه آماده شنیدن فراخوان برای دگمه است که در تابع زیر گرفته میشود)
        binding.navView.setNavigationItemSelectedListener(this)

        //Click on Button (Task Button)
        binding.btnTask.setOnClickListener {
            val intent = Intent(this, ActivityCalendar::class.java)
            startActivity(intent)
        }

        // TODO: Set CalendarOverview Adapter
        // Observing...
        MainAtivityViewMode.returnCalendarOverview.observe(this, Observer { newValue ->
            //First the RecyclerView is not visible and waiting for loading (after progressBar)
            binding.CalendarOverviewRecyclerView.setVisibility(View.GONE)
            //Second, Start the Loading of DATA from database
            displayOverviewCalendars(newValue)
        })
        //Set adapter to RecyclerView
        binding.CalendarOverviewRecyclerView.adapter= adapterOverView
        // due to the following code, recyclerView items show in GRID mode!
        binding.CalendarOverviewRecyclerView.layoutManager = GridLayoutManager(this, 6)
        //TODO: End..
    }

    override fun onResume() {
        super.onResume()
        //First the RecyclerView is not visible and waiting for updated (after progressBar)
        binding.CalendarOverviewRecyclerView.setVisibility(View.GONE)
        binding.progressBar.setVisibility(View.VISIBLE)
        //Second, Start the *UPDATING* of DATA from database
        MainAtivityViewMode.fetchCalendarOverview()
    }

    private fun displayOverviewCalendars(calOverview: List<ModelCalendarOverview>) {

        adapterOverView.items.clear()
        adapterOverView.items.addAll(calOverview)
        adapterOverView.notifyDataSetChanged()
        // After completing RecyclerView items: RecyclerView will be shown and ProgressBar will be GONE!
        binding.CalendarOverviewRecyclerView.setVisibility(View.VISIBLE)
        binding.progressBar.setVisibility(View.GONE)
    }

    //region menuTopRight

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Three dot corner in TOP-RIGHT on "ActionBar"
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // if the below code is exist, other codes not working!
        //return super.onOptionsItemSelected(item)

        var web: website = website(this)
        when (item.itemId) {
            R.id.topMenuExit -> ExitDialoge()
            R.id.topMenuWebsite -> web.open("http://iranfilmport.com")
        }
        return true
    }

    //endregion

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.staffEghbaldar -> {
                showDefaultDialog(
                    getString(R.string.staffEghbaldar),
                    getString(R.string.staffEghbaldarTell)
                )
            }
            R.id.staffArmantalab -> {
                showDefaultDialog(
                    getString(R.string.staffArmantalab),
                    getString(R.string.staffArmantalabTell)
                )
            }
            R.id.staffFadaei -> {
                showDefaultDialog(
                    getString(R.string.staffFadaei),
                    getString(R.string.staffFadaeiTell)
                )
            }
            R.id.staffSarraf -> {
                showDefaultDialog(
                    getString(R.string.staffSarraf),
                    getString(R.string.staffSarraftell)
                )
            }
            R.id.staffPeyvaste -> {
                showDefaultDialog(
                    getString(R.string.staffPeyvaste),
                    getString(R.string.staffPeyvasteTell)
                )
            }
            R.id.staffAsal -> {
                showDefaultDialog(getString(R.string.staffAsal), getString(R.string.staffAsalTell))
            }
            R.id.staffToroghi -> {
                showDefaultDialog(
                    getString(R.string.staffToroghi),
                    getString(R.string.staffToroghiTell)
                )
            }
            R.id.staffRava -> {
                showDefaultDialog(
                    getString(R.string.staffRava),
                    getString(R.string.staffRavaTell)
                )
            }
        }
        return true

    }

    //region exit event
    override fun onBackPressed() {
        ExitDialoge()
    }

    fun ExitDialoge() {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage("Do you want to close this application ?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Proceed", DialogInterface.OnClickListener { dialog, id ->
                finish()
            })
            // negative button text and action
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Exit?")
        // show alert dialog
        alert.show()
    }
    //endregion

    private fun showDefaultDialog(name: String, tell: String) {
        var dialogue: dialogue = dialogue(this)
        dialogue.showDefaultDialog(name, tell)
    }


}
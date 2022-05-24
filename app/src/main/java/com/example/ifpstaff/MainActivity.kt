package com.example.ifpstaff

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ifpstaff.databinding.MainActivityBinding
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.lifecycle.ViewModelProvider
import com.example.ifpstaff.databinding.ActivityDialogeInfoContactBinding


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout

    // Create Variable of Binding Structure
    private lateinit var binding: MainActivityBinding
    private lateinit var bindingInfo: ActivityDialogeInfoContactBinding

    // Create Variable of ViewModel Class (MainActivity)
    private lateinit var MainAtivityViewMode: MainActivtyViewModel

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
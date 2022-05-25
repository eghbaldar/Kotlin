package com.example.ifpstaff

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ifpstaff.databinding.ActivityCalendarBinding
import com.google.android.material.navigation.NavigationView

class ActivityCalendar : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding : ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //It's Clear
        binding = ActivityCalendarBinding.inflate(layoutInflater)

        // Setup DrawerLayout
        drawerLayout = binding.drawerlayout

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

        //for click on NavigationItems (با این دستور، برنامه آماده شنیدن فراخوان برای دگمه است که در تابع زیر گرفته میشود)
        binding.navView.setNavigationItemSelectedListener(this)

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
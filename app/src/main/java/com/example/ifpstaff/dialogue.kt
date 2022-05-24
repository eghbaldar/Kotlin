package com.example.ifpstaff

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.example.ifpstaff.databinding.ActivityDialogeInfoContactBinding

class dialogue(context: Context) : Dialog(context) {

    private lateinit var bindingInfo: ActivityDialogeInfoContactBinding

    fun showDefaultDialog(name: String, tell: String) {

        bindingInfo = ActivityDialogeInfoContactBinding.inflate(layoutInflater)
        val customDialog = AlertDialog.Builder(context).create()

        // The following codes are extra when using Inherited Dialogue Class
        /*
        customDialog.setOnKeyListener { _, keyCode, _ ->
            if(keyCode == KeyEvent.KEYCODE_BACK) {
                if(customDialog.isShowing) {
                    customDialog.dismiss()
                    customDialog.cancel()
                }
            }
            true
        }*/

        customDialog.apply {
            setView(bindingInfo.root)
            setCancelable(false)
            setCanceledOnTouchOutside(true)
        }.show()

        //Set First Value such as onCreate() Method
        val _tvName = customDialog.findViewById<TextView>(bindingInfo.tvName.id)
        _tvName.text = name

        //Get Handle of Buttons
        bindingInfo.btnCall.setOnClickListener {
            callPhone(tell)
        }
        bindingInfo.btnWhatsapp.setOnClickListener {
            callWhatsapp(tell)
        }
    }

    private fun callPhone(tell: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$tell")
        startActivity(context, dialIntent, null)
    }

    private fun callWhatsapp(tell: String) {
        val dialIntent = Intent(Intent.ACTION_VIEW)
        dialIntent.data = Uri.parse("https://api.whatsapp.com/send?phone=$tell")
        startActivity(context, dialIntent, null)
    }

}
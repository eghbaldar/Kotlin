package com.example.ifpstaff

import android.content.Context
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import android.net.Uri

class website(var context: Context) {
        fun open(link: String)
        {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(context,browserIntent,null)
        }
}
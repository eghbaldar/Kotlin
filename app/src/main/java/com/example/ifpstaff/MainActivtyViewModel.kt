package com.example.ifpstaff

import android.content.Context
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.navigation.NavigationView

class MainActivtyViewModel : ViewModel() {

    init {
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun ChangeColorSizeMenuItem(context: Context,
                                navV: NavigationView,
                                textItem: String,
                                fontItem : Float,
                                colorItem : Int )
    {
        //Change a specific menu item in Drawer Navigation Menu
        val menu = navV.menu
        for (i in 0 until menu.size()) {
            val menuItem = navV.menu.getItem(i)

            if (menuItem.title == textItem)
            {
                val spanString = SpannableString(menuItem.title.toString())
                //Size
                spanString.setSpan( RelativeSizeSpan(fontItem), 0,spanString.length, 0)
                //Color
                spanString.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            context,
                            colorItem
                        )
                    ), 0, spanString.length, 0
                )
                menuItem.title= spanString
            }

            if (menuItem.hasSubMenu()) {

                for (z in 0 until menuItem.subMenu.size()) {

                    val subMenuItem = menuItem.subMenu.getItem(z)
                    if (subMenuItem.title == textItem)
                    {
                        val spanString = SpannableString(subMenuItem.title.toString())
                        //Size
                        spanString.setSpan( RelativeSizeSpan(fontItem), 0,spanString.length, 0)
                        //Color
                        spanString.setSpan(
                            ForegroundColorSpan(
                                ContextCompat.getColor(
                                    context,
                                    colorItem
                                )
                            ), 0, spanString.length, 0
                        )
                        subMenuItem.title= spanString
                    }
                }
            }
        }


    }
}
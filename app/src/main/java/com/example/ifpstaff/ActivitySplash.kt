package com.example.ifpstaff

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ifpstaff.databinding.ActivitySplashBinding
import android.content.SharedPreferences




class ActivitySplash : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var ActivitySplashViewModel: ActivitySplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //It's Clear
        binding = ActivitySplashBinding.inflate(layoutInflater)

        //It's Clear
        ActivitySplashViewModel =
            ViewModelProvider(this).get(com.example.ifpstaff.ActivitySplashViewModel::class.java)

        setContentView(binding.root)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //GetVersionApp
        binding.tvVersion.text = "V${ActivitySplashViewModel.getLastVersionOfApp().toString()}"

    }

    //TODO: دستورات زیر در آنرزیوم نوشته شده است چون اگر بعد از رفتن به صفحه مرورگر، کاربر میل برگشت از طریق دکمه هوم را داشت، باز هم ورژن ها بررسی شود
    override fun onResume() {
        //Does new version available on server for download?
        ActivitySplashViewModel.returnDoesNewApkVersion.observe(this, Observer { newValue ->
            startForClosingActivity(newValue)
        })
        super.onResume()
    }

    private fun startForClosingActivity(type: Boolean) {
        //TODO: اگر ورژن برنامه تغییر کرده باشه مقدار فالس و اگه تغییر نکرده باشه مقدار ترو خواهد ب.د
        when (type) {
            true -> {
                // we used the postDelayed(Runnable, time) method
                // to send a message with a delayed time.
                Handler().postDelayed({
                    val intent = Intent(this, ActivityLogin::class.java)
                    startActivity(intent)
                    finish()
                }, 2000) // 2000 is the delayed time in milliseconds.
            }
            false -> {
                Toast.makeText(
                    this,
                    "The new version is available right now, and you are being redirected to it!",
                    Toast.LENGTH_SHORT
                ).show()
                // we used the postDelayed(Runnable, time) method
                // to send a message with a delayed time.
                Handler().postDelayed({
                    website(this).open("http://iranfilmport.com/files/uploadFiles/apk/staff/${ActivitySplashViewModel.returnNewApkVersion.value}")
                }, 5000) // 2000 is the delayed time in milliseconds.
            }
        }
    }



}
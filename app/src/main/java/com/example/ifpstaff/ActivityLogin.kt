package com.example.ifpstaff

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ifpstaff.databinding.ActivityLoginBinding

class ActivityLogin : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewmode : ActivityLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //It's Clear
        binding = ActivityLoginBinding.inflate(layoutInflater)

        //It's Clear
        viewmode = ViewModelProvider(this).get(ActivityLoginViewModel::class.java)

        //It's Clear
        setContentView(binding.root)

        //CHECK variable for validation moment-by-moment!
        //TODO: ماجرا سه وضعیت پایین اینکه چون در هر بار لود شدن صفحه یک بار وضعیت چک میشود، هر بار پیام و ویبره به نمایش در میومد! اینو در سه وضعیت نوشتم که جلوی این گرفته بشه
        viewmode.returnCheck.observe(this   , Observer { it->
            when(it)
            {
                "Validated" -> {
                    val intent = Intent (this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // After switch this activity to MainActivity through the code above, this activity will be closed!
                }
                "NoValidated_WithClick" -> {
                    binding.etPassword.text.clear()
                    binding.etUsername.text.clear()
                    showMsgMakeVibrate()
                }
                "NoValidated_CheckedSharedPreference" -> {
                    binding.etPassword.text.clear()
                    binding.etUsername.text.clear()
                }
            }
        })

        //Get click of validation
        binding.btnCheck.setOnClickListener {
            viewmode.checkValidationUsers(binding.etUsername.text.toString(),binding.etPassword.text.toString(),this)
        }

    }

    private fun showMsgMakeVibrate()
    {
        Toast.makeText(this, "Och! No no no! You're a bad baby!", Toast.LENGTH_LONG)
            .show()

        //TODO: Make a Vibrate!
        val v = (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator)
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(
                VibrationEffect.createOneShot(500,
                    VibrationEffect.DEFAULT_AMPLITUDE))
        }
        else {
            v.vibrate(500)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.etPassword.text.clear()
        binding.etUsername.text.clear()
        viewmode.loadPreferences(this) //everytime after showing this activity, User's authentication will be checked!
    }

}
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

        //Set moment-by-moment the CHECK variable for validation!
        viewmode.returnCheck.observe(this   , Observer { it->
            when(it)
            {
                true -> {
                    val intent = Intent (this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // After switch this activity to MainActivity through the code above, this activity will be closed!
                }
                false -> {

                    binding.etPassword.text.clear()
                    binding.etUsername.text.clear()

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
            }
        })

        //Get click of validation
        binding.btnCheck.setOnClickListener {
            viewmode.checkValidationUsers(binding.etUsername.text.toString(),binding.etPassword.text.toString())
        }

    }

    override fun onResume() {
        super.onResume()
        binding.etPassword.text.clear()
        binding.etUsername.text.clear()
    }
}
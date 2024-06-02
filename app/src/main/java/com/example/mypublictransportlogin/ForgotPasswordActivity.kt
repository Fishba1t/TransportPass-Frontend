package com.example.mypublictransportlogin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {
    private var confirmPasswordEditText: EditText? = null
    private var confirmPasswordIconImageView: ImageView? = null
    private var isConfirmPasswordVisible = false // Initially set to invisible

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }

        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        confirmPasswordIconImageView = findViewById(R.id.confirmPasswordIconImageView)

        // Set initial transformation method to PasswordTransformationMethod (invisible)
        confirmPasswordEditText?.transformationMethod = PasswordTransformationMethod.getInstance()
        confirmPasswordIconImageView?.setImageResource(R.drawable.baseline_key_off_24)

        confirmPasswordIconImageView?.setOnClickListener {
            toggleConfirmPasswordVisibility()
        }

        val relativeLayout = findViewById<RelativeLayout>(R.id.main)
        val animationDrawable = relativeLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(3500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            confirmPasswordEditText?.transformationMethod = PasswordTransformationMethod.getInstance()
            confirmPasswordIconImageView?.setImageResource(R.drawable.baseline_key_off_24)
        } else {
            confirmPasswordEditText?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            confirmPasswordIconImageView?.setImageResource(R.drawable.key_icon)
        }
        isConfirmPasswordVisible = !isConfirmPasswordVisible
        confirmPasswordEditText?.setSelection(confirmPasswordEditText?.text?.length ?: 0)
    }
}

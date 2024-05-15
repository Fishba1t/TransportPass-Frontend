package com.example.mypublictransportlogin

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ConnectToServerViewModel

class SignUpActivity : AppCompatActivity() {
    private var passwordEditText: EditText? = null
    private var firstName: EditText? = null
    private var lastName: EditText? = null
    private var cnp: EditText? = null
    private var email: EditText? = null
    private var confirmPasswordEditText: EditText? = null
    private var passwordIconImageView: ImageView? = null
    private var confirmPasswordIconImageView: ImageView? = null
    private var isPasswordVisible = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Initialize ViewModel
        val connectToServerViewModel = ConnectToServerViewModel.getInstance()

        // Find views
        firstName = findViewById(R.id.firstNameEditText)
        lastName = findViewById(R.id.lastNameEditText)
        email = findViewById(R.id.emailEditText)
        cnp = findViewById(R.id.ssnEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        passwordIconImageView = findViewById(R.id.passwordIconImageView)
        confirmPasswordIconImageView = findViewById(R.id.confirmPasswordIconImageView)

        findViewById<com.google.android.material.button.MaterialButton>(R.id.signUpButton).setOnClickListener {
            val firstNameText = firstName?.text.toString()
            val lastNameText = lastName?.text.toString()
            val emailText = email?.text.toString()
            val cnpText = cnp?.text.toString()
            val passwordText = passwordEditText?.text.toString()
            connectToServerViewModel.signup(firstNameText, lastNameText, emailText, cnpText, passwordText)
        }

        // Set OnClickListener for passwordIconImageView to toggle password visibility
        passwordIconImageView?.setOnClickListener {
            togglePasswordVisibility()
        }

        // Set OnClickListener for confirmPasswordIconImageView to toggle confirm password visibility
        confirmPasswordIconImageView?.setOnClickListener {
            toggleConfirmPasswordVisibility()
        }

        // Set OnClickListener for loginTextView to navigate back to the login activity
        findViewById<TextView>(R.id.loginTextView).setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }

        val relativeLayout = findViewById<RelativeLayout>(R.id.main)
        val animationDrawable = relativeLayout.background as AnimationDrawable

        animationDrawable.setEnterFadeDuration(3500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()
    }

    private fun togglePasswordVisibility() {
        val transformationMethod = if (!isPasswordVisible) {
            // Show password
            HideReturnsTransformationMethod.getInstance()
        } else {
            // Hide password
            PasswordTransformationMethod.getInstance()
        }
        passwordEditText?.transformationMethod = transformationMethod
        passwordIconImageView?.setImageResource(if (!isPasswordVisible) R.drawable.key_icon else R.drawable.baseline_key_off_24)
        isPasswordVisible = !isPasswordVisible
        passwordEditText?.setSelection(passwordEditText?.text?.length ?: 0)
    }

    private fun toggleConfirmPasswordVisibility() {
        val transformationMethod = if (!isPasswordVisible) {
            // Show password
            HideReturnsTransformationMethod.getInstance()
        } else {
            // Hide password
            PasswordTransformationMethod.getInstance()
        }
        confirmPasswordEditText?.transformationMethod = transformationMethod
        confirmPasswordIconImageView?.setImageResource(if (!isPasswordVisible) R.drawable.key_icon else R.drawable.baseline_key_off_24)
        isPasswordVisible = !isPasswordVisible
        confirmPasswordEditText?.setSelection(confirmPasswordEditText?.text?.length ?: 0)
    }
}

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
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast

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
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var adapterItems: ArrayAdapter<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val backButton = findViewById<ImageButton>(R.id.backButtonsignup)

        // Set OnClickListener for the back button to navigate to the MainActivity
        backButton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish() // Optional: finish SignUpActivity to remove it from the back stack
        }

        // Define the list of items
        val items = arrayOf("Pupil","Student", "Pensioner", "-")

        // Initialize the AutoCompleteTextView
        autoCompleteTextView = findViewById(R.id.auto_complete_txt)

        // Initialize the ArrayAdapter with the list of items
        adapterItems = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)

        // Set the adapter to the AutoCompleteTextView
        autoCompleteTextView.setAdapter(adapterItems)

        // Set item click listener
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            // Get the selected item
            val selectedItem = parent.getItemAtPosition(position).toString()

            // Set the selected item as the text of AutoCompleteTextView
            autoCompleteTextView.setText(selectedItem)

            // Showing a Toast message with the selected item
            Toast.makeText(this@SignUpActivity, "Item $selectedItem selected", Toast.LENGTH_SHORT).show()
        }

        // Set focus change listener to enable editing
        autoCompleteTextView.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                // Clear the text to enable editing
                autoCompleteTextView.text = null
                // Enable the AutoCompleteTextView
                autoCompleteTextView.isEnabled = true
            }
        }
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

package com.example.mypublictransportlogin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

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

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val backButton = findViewById<ImageButton>(R.id.backButtonsignup)
        backButton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }

        val items = arrayOf("Pupil", "Student", "Pensioner", "-")
        autoCompleteTextView = findViewById(R.id.auto_complete_txt)
        adapterItems = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        autoCompleteTextView.setAdapter(adapterItems)

        // Set input type to null to prevent keyboard from showing
        autoCompleteTextView.inputType = 0

        // Always show the dropdown when touching the AutoCompleteTextView
        autoCompleteTextView.setOnTouchListener { _, _ ->
            autoCompleteTextView.showDropDown()
            hideKeyboard(autoCompleteTextView)
            false
        }

        // Always show the dropdown when the AutoCompleteTextView gains focus
        autoCompleteTextView.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                autoCompleteTextView.showDropDown()
                hideKeyboard(view)
            }
        }

        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            autoCompleteTextView.setText(selectedItem, false)
            Toast.makeText(this@SignUpActivity, "Item $selectedItem selected", Toast.LENGTH_SHORT).show()
        }

        val connectToServerViewModel = ConnectToServerViewModel.getInstance()

        firstName = findViewById(R.id.firstNameEditText)
        lastName = findViewById(R.id.lastNameEditText)
        email = findViewById(R.id.emailEditText)
        cnp = findViewById(R.id.ssnEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        passwordIconImageView = findViewById(R.id.passwordIconImageView)
        confirmPasswordIconImageView = findViewById(R.id.confirmPasswordIconImageView)

        findViewById<MaterialButton>(R.id.signUpButton).setOnClickListener {
            val firstNameText = firstName?.text.toString()
            val lastNameText = lastName?.text.toString()
            val emailText = email?.text.toString()
            val cnpText = cnp?.text.toString()
            val passwordText = passwordEditText?.text.toString()
            connectToServerViewModel.signup(firstNameText, lastNameText, emailText, cnpText, passwordText)
        }

        passwordIconImageView?.setOnClickListener {
            togglePasswordVisibility()
        }

        confirmPasswordIconImageView?.setOnClickListener {
            toggleConfirmPasswordVisibility()
        }

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

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun togglePasswordVisibility() {
        val transformationMethod = if (!isPasswordVisible) {
            HideReturnsTransformationMethod.getInstance()
        } else {
            PasswordTransformationMethod.getInstance()
        }
        passwordEditText?.transformationMethod = transformationMethod
        passwordIconImageView?.setImageResource(if (!isPasswordVisible) R.drawable.key_icon else R.drawable.baseline_key_off_24)
        isPasswordVisible = !isPasswordVisible
        passwordEditText?.setSelection(passwordEditText?.text?.length ?: 0)
    }

    private fun toggleConfirmPasswordVisibility() {
        val transformationMethod = if (!isPasswordVisible) {
            HideReturnsTransformationMethod.getInstance()
        } else {
            PasswordTransformationMethod.getInstance()
        }
        confirmPasswordEditText?.transformationMethod = transformationMethod
        confirmPasswordIconImageView?.setImageResource(if (!isPasswordVisible) R.drawable.key_icon else R.drawable.baseline_key_off_24)
        isPasswordVisible = !isPasswordVisible
        confirmPasswordEditText?.setSelection(confirmPasswordEditText?.text?.length ?: 0)
    }
}

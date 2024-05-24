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
import com.google.android.material.button.MaterialButton
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ConnectToServerViewModel
import android.app.Dialog
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

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
    private var isValidItemSelected = false
    private lateinit var statutAutoComplete: AutoCompleteTextView
    private lateinit var adapterItems: ArrayAdapter<String>
    private lateinit var dialog: Dialog

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
        statutAutoComplete = findViewById(R.id.auto_complete_txt)
        adapterItems = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        statutAutoComplete.setAdapter(adapterItems)

        // Set input type to null to prevent keyboard from showing
        statutAutoComplete.inputType = 0

        // Always show the dropdown when touching the AutoCompleteTextView
        statutAutoComplete.setOnTouchListener { _, _ ->
            statutAutoComplete.showDropDown()
            hideKeyboard(statutAutoComplete)
            false
        }

        // Always show the dropdown when the AutoCompleteTextView gains focus
        statutAutoComplete.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                statutAutoComplete.showDropDown()
                hideKeyboard(view)
            }
        }

        statutAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            statutAutoComplete.setText(selectedItem, false)
            // Define the list of items
            val items = arrayOf("Pupil", "Student", "Pensioner", "-")

            // Initialize the AutoCompleteTextView
            statutAutoComplete = findViewById(R.id.auto_complete_txt)

            // Initialize the ArrayAdapter with the list of items
            adapterItems = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)

            // Set the adapter to the AutoCompleteTextView
            statutAutoComplete.setAdapter(adapterItems)

            // Set item click listener
            statutAutoComplete.setOnItemClickListener { parent, view, position, id ->
                // Get the selected item
                val selectedItem = parent.getItemAtPosition(position).toString()

                // Set the selected item as the text of AutoCompleteTextView
                statutAutoComplete.setText(selectedItem)

                // Showing a Toast message with the selected item
                Toast.makeText(
                    this@SignUpActivity,
                    "Item $selectedItem selected",
                    Toast.LENGTH_SHORT
                ).show()
                isValidItemSelected = true
            }

            // Set focus change listener to enable editing
            statutAutoComplete.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    // Clear the text to enable editing
                    statutAutoComplete.text = null
                    // Enable the AutoCompleteTextView
                    statutAutoComplete.isEnabled = true

                    isValidItemSelected = true
                }
            }
            // Initialize ViewModel
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
                val statut = statutAutoComplete.text.toString()
                if (firstNameText == "" || lastNameText == "" || emailText == "" || cnpText == "" || passwordText == ""  || statut == "") {
                    EMPTY_FIELDS()
                } else {
                    connectToServerViewModel.signup(
                        firstNameText,
                        lastNameText,
                        emailText,
                        cnpText,
                        passwordText,
                        statut
                    )
                    lifecycleScope.launch {
                        var response = connectToServerViewModel.getSignupResponse()
                        Log.d("SERVER", "SIGNUP RESPONSE ESTE : $response")
                        if (response == "ErrorResponse") {
                            ALREADY_HAVE_AN_ACCOUNT()
                        }
                        else{
                            CREATED_ACCOUNT()
                        }
                        connectToServerViewModel.setSignupResponse()
                    }
                }
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


        private fun EMPTY_FIELDS() {
            // Create a dialog instance
            dialog = Dialog(this)

            // Set the content view to the disclaimer layout
            dialog.setContentView(R.layout.fill_all_fields_disclaimer)

            // Find the OK button in the dialog layout
            val okButton = dialog.findViewById<Button>(R.id.okButton)

            val close = dialog.findViewById<ImageButton>(R.id.closeButtonPass)

            // Set a click listener for the OK button to dismiss the dialog
            okButton.setOnClickListener {
                dialog.dismiss() // Dismiss the dialog when OK is clicked
            }

            close.setOnClickListener {
                dialog.dismiss() // Dismiss the dialog when OK is clicked
            }


            // Show the dialog
            dialog.show()
        }


        private fun ALREADY_HAVE_AN_ACCOUNT() {
            // Create a dialog instance
            dialog = Dialog(this)

            // Set the content view to the disclaimer layout
            dialog.setContentView(R.layout.already_have_an_account)

            // Find the OK button in the dialog layout
            val okButton = dialog.findViewById<Button>(R.id.okButton)

            val close = dialog.findViewById<ImageButton>(R.id.closeButton2)

            // Set a click listener for the OK button to dismiss the dialog
            okButton.setOnClickListener {
                dialog.dismiss() // Dismiss the dialog when OK is clicked
            }

            close.setOnClickListener {
                dialog.dismiss() // Dismiss the dialog when OK is clicked
            }


            // Show the dialog
            dialog.show()
        }

        private fun CREATED_ACCOUNT(){
            dialog = Dialog(this)

            // Set the content view to the disclaimer layout
            dialog.setContentView(R.layout.created_account_disclaimer)

            // Find the OK button in the dialog layout
            val loginButtonAccountCreated = dialog.findViewById<Button>(R.id.loginButtonAccountCreated)

            val close = dialog.findViewById<ImageButton>(R.id.closeButtonAccountCreated)

            // Set a click listener for the OK button to dismiss the dialog
            loginButtonAccountCreated.setOnClickListener {
                val intent = Intent(this, LogIn::class.java)
                startActivity(intent)
            }

            close.setOnClickListener {
                dialog.dismiss() // Dismiss the dialog when OK is clicked
            }


            // Show the dialog
            dialog.show()
        }
}



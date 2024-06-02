package com.example.mypublictransportlogin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

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
    private lateinit var chooseFilesButton: Button


    companion object {
        private const val REQUEST_CHOOSE_PHOTO = 2
    }

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

            chooseFilesButton.isEnabled = selectedItem == "Pupil" || selectedItem == "Student"

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
        chooseFilesButton = findViewById(R.id.ChooseYourFiles)

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

        chooseFilesButton.setOnClickListener {
            openGallery()
        }
        chooseFilesButton.isEnabled = false // Initially disable the button
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
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

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CHOOSE_PHOTO -> {
                    data?.data?.also { uri ->
                        // Handle the image selected from the gallery here
                        Toast.makeText(this, "Photo selected successfully", Toast.LENGTH_SHORT).show()
                        saveImageToAppStorage(uri)
                    }
                }
            }
        }
    }

    private fun saveImageToAppStorage(imageUri: Uri) {
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val file = File(getExternalFilesDir(null), "IMG_${timeStamp}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            Toast.makeText(this, "Photo saved successfully: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save photo", Toast.LENGTH_SHORT).show()
        }
    }
}

package com.example.mypublictransportlogin

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class LogIn : AppCompatActivity() {
    private lateinit var passwordEditText: EditText
    private lateinit var keyIconImageView: ImageView
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find views
        passwordEditText = findViewById(R.id.passwordEditText)
        keyIconImageView = findViewById(R.id.keyIconImageView)

        // Initially hide the password
        passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        keyIconImageView.setImageResource(R.drawable.baseline_key_off_24)

        // Set OnClickListener for keyIconImageView to toggle password visibility
        keyIconImageView.setOnClickListener {
            togglePasswordVisibility()
        }

        // Set OnClickListener for createAccount TextView
        findViewById<TextView>(R.id.createAccount).setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.forgotPasswordTextView).setOnClickListener {
            val intent4 = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent4)
        }

        findViewById<MaterialButton>(R.id.LOGIN).setOnClickListener {
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val password = passwordEditText.text.toString()
            val connectToServerViewModel = ConnectToServerViewModel.getInstance()
            connectToServerViewModel.connectToServer()
            connectToServerViewModel.login(email, password)
            val intent5 = Intent(this, ClientMain::class.java)
            startActivity(intent5)
        }

        // Apply window insets to handle system UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Start animation
        val relativeLayout = findViewById<RelativeLayout>(R.id.main)
        val animationDrawable = relativeLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(3500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            keyIconImageView.setImageResource(R.drawable.baseline_key_off_24)
        } else {
            passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            keyIconImageView.setImageResource(R.drawable.key_icon)
        }
        isPasswordVisible = !isPasswordVisible
        passwordEditText.setSelection(passwordEditText.text.length)
    }
}

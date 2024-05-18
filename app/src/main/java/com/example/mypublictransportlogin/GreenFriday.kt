package com.example.mypublictransportlogin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GreenFriday : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.green_friday)

        val closeButton: ImageButton = findViewById(R.id.closeButtonGreenFriday)
        closeButton.setOnClickListener {
            finish()
        }

        val GotIt: Button = findViewById(R.id.gotitGreen)
        GotIt.setOnClickListener {
            finish()
        }

    }
}
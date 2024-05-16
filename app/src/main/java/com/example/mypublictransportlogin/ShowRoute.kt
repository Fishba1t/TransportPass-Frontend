package com.example.mypublictransportlogin


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class ShowRoute : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_route)

        val backButton = findViewById<ImageButton>(R.id.backButtonSelectedRoute)

        backButton.setOnClickListener {
            val intent = Intent(this, BussLines::class.java)
            startActivity(intent)
            finish()
        }

    }
}
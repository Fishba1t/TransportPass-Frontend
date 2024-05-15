package com.example.mypublictransportlogin

import AbonamentDetails
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BusPassQRCode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buspass_qrcode_display)

        val backButton = findViewById<ImageButton>(R.id.backButton)

        // Set OnClickListener for the back button to navigate to the MainActivity
        backButton.setOnClickListener {
            val intent = Intent(this, ClientMain::class.java)
            startActivity(intent)
            finish() // Optional: finish SignUpActivity to remove it from the back stack

            Log.d("SERVER", "IN BUSPASSQRCODE")
            val connectToServerViewModel = ConnectToServerViewModel.getInstance()
            GlobalScope.launch {
                val abonament: AbonamentDetails = connectToServerViewModel.getAbonamentDetails()
                Log.d("SERVER", "ABONAMENTUL ESTE : $abonament")

                val textTipTextView = findViewById<TextView>(R.id.TextTip)
                val passTypeString = getString(R.string.pass_type, abonament.tip)
                textTipTextView.text = passTypeString

                val textDataIncepere = findViewById<TextView>(R.id.DataIncepere)
                val validSinceString = getString(R.string.valid_since, abonament.dataIncepere)
                textDataIncepere.text = validSinceString

                val textDataExpirare = findViewById<TextView>(R.id.DataExpirare)
                val validUntilString = getString(R.string.valid_until, abonament.dataExpirare)
                textDataExpirare.text = validUntilString
            }
        }
    }
}
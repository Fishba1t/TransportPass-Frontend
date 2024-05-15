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
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ClientMain : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val connectToServerViewModel = ConnectToServerViewModel.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.client_main)
        findViewById<androidx.cardview.widget.CardView>(R.id.Route).setOnClickListener {
            val intent1 = Intent(this, BussLines::class.java)
            startActivity(intent1)
        }

        findViewById<androidx.cardview.widget.CardView>(R.id.myPassCard).setOnClickListener {
            connectToServerViewModel.show_pass()
            //val context = it.context
            //GlobalScope.launch {
                //val abonament: AbonamentDetails = connectToServerViewModel.getAbonamentDetails()
                // Use the abonament here
                //Log.d("SERVER","CLIENT MAIN ABONAMENTUL ESTE : $abonament")
                val intent = Intent(this,BusPassQRCode::class.java)
                startActivity(intent)
            //}

        }

        findViewById<Button>(R.id.BuyPassCard).setOnClickListener {
            val intent11 = Intent(this, SelectBox::class.java)
            startActivity(intent11)
        }

        findViewById<Button>(R.id.BuyTicketButton).setOnClickListener {
            val intent11 = Intent(this, TicketsActivity::class.java)
            startActivity(intent11)
        }
    }
}
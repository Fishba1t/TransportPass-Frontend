package com.example.mypublictransportlogin

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity

class ClientMain : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.client_main)

        findViewById<androidx.cardview.widget.CardView>(R.id.Route).setOnClickListener {
            val intent1 = Intent(this, BussLines::class.java)
            startActivity(intent1)
        }

        findViewById<Button>(R.id.BuyPassCard).setOnClickListener {
            val intent11 = Intent(this, SelectBox::class.java)
            startActivity(intent11)
        }

        findViewById<Button>(R.id.BuyTicketButton).setOnClickListener {
            val intent11 = Intent(this, TicketsActivity::class.java)
            startActivity(intent11)
        }

        findViewById<androidx.cardview.widget.CardView>(R.id.myPassCard).setOnClickListener {
            val intent12 = Intent(this, BusPassQRCode::class.java)
            startActivity(intent12)
        }

        findViewById<androidx.cardview.widget.CardView>(R.id.ticketsCard).setOnClickListener {
            val intent12 = Intent(this, BusTicketQRCode::class.java)
            startActivity(intent12)
        }

        findViewById<androidx.cardview.widget.CardView>(R.id.logoutCard).setOnClickListener {
            val intent12 = Intent(this, LogIn::class.java)
            startActivity(intent12)
        }

    }
}
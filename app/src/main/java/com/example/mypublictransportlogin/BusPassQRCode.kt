package com.example.mypublictransportlogin

import AbonamentDetails
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BusPassQRCode : AppCompatActivity() {
    private lateinit var abonament: AbonamentDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buspass_qrcode_display)
        Log.d("SERVER", "IN BUSPASSQRCODE")

        val textTipTextView = findViewById<TextView>(R.id.TextTip)
        val textDataIncepere = findViewById<TextView>(R.id.DataIncepere)
        val textDataExpirare = findViewById<TextView>(R.id.DataExpirare)
        val imageview = findViewById<ImageView>(R.id.qrCodeImageView)

        GlobalScope.launch(Dispatchers.Main) {
            abonament = ConnectToServerViewModel.getInstance().getAbonamentDetails()
            Log.d("SERVER", "ABONAMENTUL ESTE : $abonament")

            val passTypeString = getString(R.string.pass_type, abonament.tip)
            textTipTextView.text = passTypeString

            val validSinceString = getString(R.string.valid_since, abonament.dataIncepere)
            textDataIncepere.text = validSinceString

            val validUntilString = getString(R.string.valid_until, abonament.dataExpirare)
            textDataExpirare.text = validUntilString
            val qrBitmap = abonament.qr?.let { byteArrayToBitmap(it) }

            // Set the Bitmap as the source of the ImageView
            qrBitmap?.let { imageview.setImageBitmap(it) }
        }
    }

    private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return try {
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } catch (e: Exception) {
            Log.e("BusPassQRCode", "Error converting byte array to bitmap: ${e.message}")
            null
        }
    }
}


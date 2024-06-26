package com.example.mypublictransportlogin

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SelectBox : AppCompatActivity() {

    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var adapterItems: ArrayAdapter<String>
    private lateinit var dialog: Dialog
    private var selectedPassLayout: LinearLayout? = null
    private lateinit var pass_type : String
    private var price : Double = 0.0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bus_pass)
        

        val connectToServerViewModel = ConnectToServerViewModel.getInstance()
        findViewById<LinearLayout>(R.id.firstpass)?.setOnClickListener {
            toggleBusPassSelection(findViewById(R.id.firstpass) as LinearLayout, "URBAN PASS", "150.00 RON")
            pass_type = "URBAN PASS"
            price=150.0
        }

        findViewById<LinearLayout>(R.id.secondpass)?.setOnClickListener {
            toggleBusPassSelection(findViewById(R.id.secondpass) as LinearLayout, "METROPOLITAN PASS", "165.00 RON")
            pass_type = "METROPOLITAN PASS"
            price = 165.0
        }

        findViewById<LinearLayout>(R.id.thirdpass)?.setOnClickListener {
            toggleBusPassSelection(findViewById(R.id.thirdpass) as LinearLayout, "INTRA-COMMUNAL PASS", "135.00 RON")
            pass_type="INTRA-COMMUNAL PASS"
            price = 135.0
        }

        findViewById<LinearLayout>(R.id.forthpass)?.setOnClickListener {
            toggleBusPassSelection(findViewById(R.id.forthpass) as LinearLayout, "INTER-COMMUNAL PASS", "135.00 RON")
            pass_type="INTER-COMMUNAL PASS"
            price = 135.0
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)

        // Set OnClickListener for the back button to navigate to the MainActivity
        backButton.setOnClickListener {
            val intent = Intent(this, ClientMain::class.java)
            startActivity(intent)
            finish() // Optional: finish SignUpActivity to remove it from the back stack
        }

        findViewById<TextView>(R.id.clickHere).setOnClickListener {
            val intent8 = Intent(this, ChoosePass::class.java)
            startActivity(intent8)
        }


        findViewById<Button>(R.id.paymentButton).setOnClickListener {
            val intent19 = Intent(this, Payment::class.java)
            startActivity(intent19)
            connectToServerViewModel.buyPass(pass_type,price)
        }

        //showDisclaimerDialog()
    }

//    private fun showDisclaimerDialog() {
//        // Create a dialog instance
//        dialog = Dialog(this)
//
//        // Set the content view to the disclaimer layout
//        dialog.setContentView(R.layout.disclaimer)
//
//        // Find the OK button in the dialog layout
//        val okButton = dialog.findViewById<Button>(R.id.okButton)
//
//        // Set a click listener for the OK button to dismiss the dialog
//        okButton.setOnClickListener {
//            dialog.dismiss() // Dismiss the dialog when OK is clicked
//        }
//
//        // Show the dialog
//        dialog.show()
//    }

    private fun onBusPassSelected(layout: LinearLayout, passType: String, price: String) {
        // Change background color of the selected bus pass layout
        layout.setBackgroundResource(R.drawable.bus_pass_background_change) // Set to gold color

        // Optionally, you can show a Toast message indicating the selected pass
        Toast.makeText(this, "$passType selected", Toast.LENGTH_SHORT).show()
        selectedPassLayout = layout // Update selected layout
    }

    private fun deselectBusPass(layout: LinearLayout) {
        // Change background color of the selected bus pass layout
        layout.setBackgroundResource(R.drawable.white_background) // Set to original color

        // Optionally, you can show a Toast message indicating the deselected pass
        val passType = when (layout.id) {
            R.id.firstpass -> "URBAN PASS"
            R.id.secondpass -> "METROPOLITAN PASS"
            R.id.thirdpass -> "INTRA-COMMUNAL PASS"
            R.id.forthpass -> "INTER-COMMUNAL PASS"
            else -> "Unknown Pass"
        }
//        Toast.makeText(this, "$passType unselected", Toast.LENGTH_SHORT).show()
        selectedPassLayout = null // Clear selected layout
    }

    private fun toggleBusPassSelection(layout: LinearLayout, passType: String, price: String) {
        if (layout == selectedPassLayout) {
            // If the clicked layout is already selected, deselect it
            deselectBusPass(layout)
        } else {
            // Deselect the currently selected pass (if any)
            selectedPassLayout?.let { deselectBusPass(it) }

            // Select the new pass
            onBusPassSelected(layout, passType, price)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Dismiss the dialog if it's showing when the activity is destroyed
        if (::dialog.isInitialized && dialog.isShowing) {
            dialog.dismiss()
        }
    }
}

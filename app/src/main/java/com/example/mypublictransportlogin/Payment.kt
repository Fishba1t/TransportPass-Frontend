package com.example.mypublictransportlogin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Payment : AppCompatActivity() {

    private lateinit var expiryDateEditText: EditText
    private lateinit var cardNumberEditText: EditText
    private lateinit var cvvEditText: EditText
    private lateinit var NameOnCard: EditText
    private lateinit var payButton: Button
    private var isNameOnCardValid = false
    private var isCardNumberValid = false
    private var isExpiryDateValid = false
    private var isCvvValid = false
    private lateinit var confirmationTextView: TextView
    private lateinit var confirmationNumberTextView: TextView
    private lateinit var confirmationDateTextView: TextView
    private lateinit var confirmationCvvTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_activity)

        val closeButton: ImageButton = findViewById(R.id.closeButtonPass)
        closeButton.setOnClickListener {
            finish()
        }


        cardNumberEditText = findViewById(R.id.NumberOnCard)
        expiryDateEditText = findViewById(R.id.DateOnCard)
        cvvEditText = findViewById(R.id.CvvOnCard)
        NameOnCard = findViewById(R.id.NameOnCard) // Initialize NameOnCard EditText
        payButton = findViewById(R.id.PayButtonPayment)

        setupNameOnCardInput() // Call setup method for NameOnCard EditText
        setupExpiryDateInput()
        setupCvvInput()
        setupCardNumberInput()

        payButton.setOnClickListener {
            if (validateForm()) {
                showConfirmationDialog()
            } else {
                Toast.makeText(this, "Please complete all the inputs!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupNameOnCardInput() {
        // Find the TextView by its id
        confirmationTextView = findViewById(R.id.confirmationNameTextView)

        NameOnCard.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                val nameOnCard = s.toString().trim()
                if (nameOnCard.isEmpty()) {
                    // Clear any existing confirmation message
                    confirmationTextView.visibility = View.GONE
                    // Set error message
                    NameOnCard.error = "Name on Card cannot be empty"
                    isNameOnCardValid = false
                } else {
                    // Clear error message
                    NameOnCard.error = null
                    // Set confirmation message
                    confirmationTextView.text = "Correct Format"
                    confirmationTextView.visibility = View.VISIBLE
                    isNameOnCardValid = true
                }
            }
        })
    }



    private fun setupCardNumberInput() {
        confirmationNumberTextView = findViewById(R.id.confirmationNumberTextView)
        cardNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                val cardNumber = s.toString().trim()
                if (cardNumber.length < 16) {
                    confirmationNumberTextView.visibility = View.GONE
                    cardNumberEditText.error = "Card number must be 16 digits!"
                    isCardNumberValid = false
                } else if (cardNumber.length > 16) {
                    // Remove extra characters
                    cardNumberEditText.setText(cardNumber.substring(0, 16))
                    cardNumberEditText.setSelection(16) // Move cursor to the end
                } else {
                    // Clear error message
                    cardNumberEditText.error = null
                    confirmationNumberTextView.text = "Correct Format"
                    confirmationNumberTextView.visibility = View.VISIBLE
                    // Set confirmation message
                    isCardNumberValid = true
                }
            }
        })

        // Intercept key events to prevent insertion of additional characters
        cardNumberEditText.setOnKeyListener { _, keyCode, _ ->
            if (cardNumberEditText.text.toString().length >= 16 &&
                (keyCode in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 || keyCode in KeyEvent.KEYCODE_NUMPAD_0..KeyEvent.KEYCODE_NUMPAD_9)
            ) {
                // Return true to consume the key event and prevent further insertion
                return@setOnKeyListener true
            }
            // Return false to allow the insertion of characters
            false
        }
    }




    private fun setupExpiryDateInput() {
        confirmationDateTextView = findViewById(R.id.confirmationDateTextView)
        expiryDateEditText.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    var expiryDate = it.toString().trim().replace("/", "") // Remove any existing "/"
                    if (expiryDate.length > 7) {
                        expiryDate = expiryDate.substring(0, 7) // Truncate to 7 characters if longer
                    }

                    if (expiryDate.length >= 2 && expiryDate[1] != '/') {
                        // Automatically add "/" after the second character
                        expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2)
                    }

                    val firstTwoDigits = if (expiryDate.length >= 2) expiryDate.substring(0, 2) else ""
                    val lastFourDigits = expiryDate.takeLast(4)

                    if (firstTwoDigits.isNotEmpty() && lastFourDigits.isNotEmpty()) {
                        val isMonthValid = firstTwoDigits.toIntOrNull() in 1..12
                        val isYearValid = lastFourDigits.toIntOrNull() ?: 0 >= 2024

                        if (!isMonthValid) {
                            confirmationDateTextView.visibility=View.GONE
                            expiryDateEditText.error = "Invalid Month"
                            isExpiryDateValid=false
                        } else if (!isYearValid) {
                            confirmationDateTextView.visibility=View.GONE
                            expiryDateEditText.error = "Invalid Year"
                            isExpiryDateValid=false
                        } else {
                            confirmationDateTextView.text="Correct Format"
                            confirmationDateTextView.visibility = View.VISIBLE
                            isExpiryDateValid=true
                        }
                    }

                    current = expiryDate
                    expiryDateEditText.removeTextChangedListener(this)
                    expiryDateEditText.setText(current)
                    expiryDateEditText.setSelection(current.length)
                    expiryDateEditText.addTextChangedListener(this)
                }
            }

        })
    }


    private fun setupCvvInput() {
        confirmationCvvTextView = findViewById(R.id.confirmationCvvTextView)
        cvvEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                val cvv = s.toString().trim()
                if (cvv.length < 3) {
                    confirmationCvvTextView.visibility=View.GONE
                    cvvEditText.error = "CVV must be 3 digits"
                    isCvvValid=false
                } else if (cvv.length > 3) {
                    // Remove extra characters
                    cvvEditText.setText(cvv.substring(0, 3))
                    cvvEditText.setSelection(3) // Move cursor to the end
                } else {
                    confirmationCvvTextView.text = "Correct format" // Clear any existing error message
                    confirmationCvvTextView.visibility=View.VISIBLE
                    isCvvValid=true
                }
            }
        })

        // Intercept key events to prevent insertion of additional characters
        cvvEditText.setOnKeyListener { _, keyCode, _ ->
            if (cvvEditText.text.toString().length >= 3 &&
                (keyCode in KeyEvent.KEYCODE_0..KeyEvent.KEYCODE_9 || keyCode in KeyEvent.KEYCODE_NUMPAD_0..KeyEvent.KEYCODE_NUMPAD_9)
            ) {
                // Return true to consume the key event and prevent further insertion
                return@setOnKeyListener true
            }
            // Return false to allow the insertion of characters
            false
        }
    }

    private fun validateForm(): Boolean {
        // Check if all input fields are valid
        return isNameOnCardValid && isCardNumberValid && isExpiryDateValid && isCvvValid
    }

    // Other methods...

    private fun showConfirmationDialog() {
        // Show confirmation dialog
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Payment Confirmation")
        dialogBuilder.setMessage("Payment was successful!")
        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }


}

package com.example.mypublictransportlogin

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mypublictransportlogin.databinding.QrCodeReaderBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class QRCodeReader : AppCompatActivity() {

    private val requestPermissionLauncher=
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted: Boolean ->
            if(isGranted){
                showCamera()
            }
            else{

            }
        }
    private val scanLauncher=
        registerForActivityResult(ScanContract()) {
                result: ScanIntentResult ->
            run {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    setResult(result.contents)
                }
            }
        }
    private lateinit var binding: QrCodeReaderBinding

    private fun setResult(string:String){
        binding.textResult.text=string
        binding.textResult1.text=string
        binding.textResult2.text=string
        binding.textResult3.text=string
    }

    private fun showCamera() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan QR CODE")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(false)

        scanLauncher.launch(options)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initViews()

        val backButton = findViewById<ImageButton>(R.id.backButtonqr_scanner)

        // Set OnClickListener for the back button to navigate to the MainActivity
        backButton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish() // Optional: finish SignUpActivity to remove it from the back stack
        }
    }

    private fun initViews() {
        binding.fab.setOnClickListener{
            checkPermissionCamera(this)
        }
    }

    private fun checkPermissionCamera(context:Context) {
        if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            showCamera()
        }
        else if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
            Toast.makeText(context,"CAMERA permission requested", Toast.LENGTH_SHORT).show()
        }
        else{
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun initBinding() {
        binding = QrCodeReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket

class ConnectToServerViewModel private constructor() : ViewModel() {
    private var port = 55556
    private var serverAddress = "172.23.64.1"
    private val socket = Socket()
    private lateinit var outputStreamDeferred: OutputStream
    private lateinit var writer: PrintWriter
    private lateinit var inputStreamDeferred :InputStream
    private lateinit var reader: BufferedReader
    private val lock = Any()
    private var isConnected = false
    private lateinit var abonament : AbonamentDetails

    init {
        //connectToServer()
    }

    companion object {
        @Volatile
        private var instance: ConnectToServerViewModel? = null

        fun getInstance(): ConnectToServerViewModel {
            return instance ?: synchronized(this) {
                instance ?: ConnectToServerViewModel().also { instance = it }
            }
        }
    }

    private suspend fun ensureConnected() {
        while (!isConnected) {
            delay(100)
        }
    }

    fun connectToServer() {
        GlobalScope.launch {
            try {
                val socketAddress = InetSocketAddress(serverAddress, port)
                socket.connect(socketAddress)
                Log.d("SERVER", "Socket connected to $serverAddress:$port")
                isConnected = true
                outputStreamDeferred = socket.getOutputStream()
                inputStreamDeferred = socket.getInputStream()
                writer = PrintWriter(BufferedWriter(OutputStreamWriter(outputStreamDeferred)))
                reader = BufferedReader(InputStreamReader(inputStreamDeferred))
                Log.d("SERVER", "Socket connection completed successfully")
                Log.d("SERVER", "Socket connection completed successfully $isConnected")
            } catch (e: Exception) {
                Log.e("SERVER", "Socket connection failed: ${e.message}")
                e.printStackTrace()
            }
        }
    }


    suspend fun loginAsync(email: String, password: String): String {
        ensureConnected()
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SERVER", "Sending login request")
                // Crearea unui JSONObject pentru cererea de login
                val loginRequest = JSONObject().apply {
                    put("type", "Login")
                    put("email", email)
                    put("parola", password)
                }

                // Scrierea șirului JSON în fluxul de ieșire
                writer.println(loginRequest)
                writer.flush()
                Log.d("SERVER", "Sent login request")

                // Citirea răspunsului de la server
                val response = reader.readLine()
                Log.d("SERVER", "Received login response: $response")

                response
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error : ${e.message}")
                // Returnarea unui șir gol sau a unui răspuns de eroare implicit
                ""
            }
        }
    }

    fun login(email: String, password: String) {
        GlobalScope.launch {
            val response = loginAsync(email, password)
        }
    }

    suspend fun buyTicketAsync(tip: String, pret: Double): String {
        ensureConnected()
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SERVER", "Sending BuyTicket request")
                // Crearea unui JSONObject pentru cererea de înregistrare
                val buyTicketRequest = JSONObject().apply {
                    put("type", "BuyTicket")
                    put("pret", pret)
                    put("tip", tip)
                }

                // Scrierea șirului JSON în fluxul de ieșire
                writer.println(buyTicketRequest)
                writer.flush()
                Log.d("SERVER", "Sent BuyTicket request")

                // Citirea răspunsului de la server
                val response = reader.readLine()
                Log.d("SERVER", "Received buyTicket response: $response")

                response
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error: ${e.message}")
                // Returnarea unui șir gol sau a unui răspuns de eroare implicit
                ""
            }
        }
    }

    fun buyTicket(tip: String, pret: Double) {
        GlobalScope.launch {
            val response = buyTicketAsync(tip, pret)
        }
    }




    suspend fun buyPassAsync(tip: String, pret: Double): String {
        ensureConnected()
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SERVER", "Sending BuyTicket request")
                // Crearea unui JSONObject pentru cererea de înregistrare
                val buyTicketRequest = JSONObject().apply {
                    put("type", "BuyPass")
                    put("pret", pret)
                    put("tip", tip)
                }

                // Scrierea șirului JSON în fluxul de ieșire
                writer.println(buyTicketRequest)
                writer.flush()
                Log.d("SERVER", "Sent BuyPass request")

                // Citirea răspunsului de la server
                val response = reader.readLine()
                Log.d("SERVER", "Received buyPass response: $response")

                response
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error: ${e.message}")
                // Returnarea unui șir gol sau a unui răspuns de eroare implicit
                ""
            }
        }
    }

    fun buyPass(tip: String, pret: Double) {
        GlobalScope.launch {
            val response = buyPassAsync(tip, pret)
        }
    }


    suspend fun signupAsync(firstName: String, lastName: String, email: String, CNP: String, password: String): String {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SERVER", "Sending signup request")
                // Crearea unui JSONObject pentru cererea de înregistrare
                val signupRequest = JSONObject().apply {
                    put("type", "CreateClient")
                    put("nume", firstName)
                    put("prenume", lastName)
                    put("email", email)
                    put("parola", password)
                    put("CNP", CNP)
                    put("statut", "student")
                }

                // Scrierea șirului JSON în fluxul de ieșire
                writer.println(signupRequest)
                writer.flush()
                Log.d("SERVER", "Sent signup request")

                // Citirea răspunsului de la server
                val response = reader.readLine()
                Log.d("SERVER", "Received signup response: $response")

                response
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error: ${e.message}")
                // Returnarea unui șir gol sau a unui răspuns de eroare implicit
                ""
            }
        }
    }

    fun signup(firstName: String, lastName: String, email: String, CNP: String, password: String) {
        GlobalScope.launch {
            val job = launch {signupAsync(firstName, lastName, email, CNP, password)}
            job.join()
        }
    }


    fun show_pass() {
        GlobalScope.launch {
            val response = showPassAsync()
        }
    }





    suspend fun showPassAsync(): String {
        ensureConnected()
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SERVER", "Sending ShowPass request")
                // Crearea unui JSONObject pentru cererea de înregistrare
                val buyTicketRequest = JSONObject().apply {
                    put("type", "ShowPass")
                }

                // Scrierea șirului JSON în fluxul de ieșire
                writer.println(buyTicketRequest)
                writer.flush()
                Log.d("SERVER", "Sent ShowPass request")

                // Citirea răspunsului de la server
                val response = reader.readLine()
                Log.d("SERVER","RECEIVE SHOWPASS RESPONSE $response")
                val jsonResponse = JSONObject(response)
                val type = jsonResponse.getString("type")
                if(type =="AbonamentResponse"){

                    val dataIncepere = jsonResponse.getString("dataIncepere")
                    val dataExpirare = jsonResponse.getString("dataExpirare")
                    val tip = jsonResponse.getString("tip")
                    val pret = jsonResponse.getDouble("pret")
                    val qrJSONArray = jsonResponse.getJSONArray("qr")
                    val qrByteArray = ByteArray(qrJSONArray.length())
                    for (i in 0 until qrJSONArray.length()) {
                        qrByteArray[i] = qrJSONArray.getInt(i).toByte()
                    }
                    abonament = AbonamentDetails(dataIncepere,dataExpirare,tip,pret,qrByteArray)
                }
                response
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("SERVER", "Error: ${e.message}")
                // Returnarea unui șir gol sau a unui răspuns de eroare implicit
                ""
            }
        }
    }




    suspend fun getAbonamentDetailsSuspend(){
        var nr =1
        while(!::abonament.isInitialized){

            Log.d("SERVER","SUNTEM IN ABONAMENT SUSPEND $nr")
            delay(1000)
            nr+=1
        }
    }

    suspend fun getAbonamentDetails() :AbonamentDetails{
        getAbonamentDetailsSuspend()
        return abonament
    }


}



data class AbonamentDetails(
    val dataIncepere: String,
    val dataExpirare: String,
    val tip: String,
    val pret: Double,
    val qr: ByteArray
)

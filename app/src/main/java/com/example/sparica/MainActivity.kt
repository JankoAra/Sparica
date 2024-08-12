package com.example.sparica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.sparica.navigation.MyNavHost
import com.example.sparica.ui.theme.SparicaTheme
import com.example.sparica.util.scheduleDailyExchangeRateFetch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleDailyExchangeRateFetch(applicationContext)
        val app = application as SparicaApp
        app.registerCreateFileLauncher(this)

        //setContent poziva Composable funkcije
        setContent {
            SparicaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyNavHost()

                }
            }
        }

    }

//    // Modular createFileLauncher
//    private val createFileLauncher = registerForActivityResult(
//        ActivityResultContracts.CreateDocument("*/*") // Accept any file type
//    ) { uri: Uri? ->
//        uri?.let { currentUri ->
//            currentFileContent?.let { content ->
//                writeToFile(currentUri, content)
//            }
//        }
//    }
//
//    // Variables to hold the current file name and content
//    private var currentFileName: String? = null
//    private var currentFileContent: String? = null
//
//    // Call this method to trigger the file creation process
//    fun createFile(fileName: String, contentGenerator: () -> String) {
//        currentFileName = fileName
//        currentFileContent = contentGenerator()
//
//        // Launch the file creation process
//        createFileLauncher.launch(currentFileName!!)
//    }
//    private fun writeToFile(uri: Uri, content: String) {
//        try {
//            contentResolver.openOutputStream(uri)?.use { outputStream ->
//                outputStream.write(content.toByteArray())
//                outputStream.flush()
//                // Notify the user that the file was saved
//                Toast.makeText(this, "File saved successfully", Toast.LENGTH_LONG).show()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Toast.makeText(this, "Failed to save file", Toast.LENGTH_LONG).show()
//        }
//    }


}
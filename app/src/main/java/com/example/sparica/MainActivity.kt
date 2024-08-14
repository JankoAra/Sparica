package com.example.sparica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.sparica.navigation.MyNavHost
import com.example.sparica.reporting.ReportUtils
import com.example.sparica.ui.theme.SparicaTheme
import com.example.sparica.util.scheduleDailyExchangeRateFetch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleDailyExchangeRateFetch(applicationContext)
        val app = application as SparicaApp
        ReportUtils.registerCreateFileLauncher(this)

        //setContent poziva Composable funkcije
        setContent {
            SparicaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyNavHost()
                }
            }
        }
    }
}
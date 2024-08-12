package com.example.sparica

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sparica.data.database.SparicaDatabase
import com.example.sparica.util.scheduleDailyExchangeRateFetch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

//Application moze da sluzi za pocetnu inicijalizaciju i cuvanje globalnih objekata
class SparicaApp : Application() {

    lateinit var db: SparicaDatabase

    override fun onCreate() {
        super.onCreate()

        //init db instance
        db = SparicaDatabase.getDatabase(this, CoroutineScope(Dispatchers.IO + Job()))

        //set up scheduled task
        scheduleDailyExchangeRateFetch(this)

    }


}


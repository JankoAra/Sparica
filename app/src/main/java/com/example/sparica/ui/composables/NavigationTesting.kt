package com.example.sparica.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sparica.data.models.Spending
import com.example.sparica.data.repositories.SpendingRepository
import com.example.sparica.navigation.SecondPath
import com.example.sparica.ui.spendings.composables.SpendingListItem
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondPage(navController: NavHostController, param: String?, iparam: Int?) {
    Scaffold(topBar = { TopAppBar(title = { Text("Details Screen") }) }) {
        Column(modifier = Modifier.padding(it)) {
            Text(text = "Param je: $param")
            Text(text = "Broj je: $iparam")
            Button(onClick = { navController.popBackStack() }) {
                Text("Go Back")
            }
        }
    }
}

@Composable
fun NavigationTesting(navController: NavHostController) {
    Column(modifier = Modifier.padding(16.dp)) {
        var textFieldValue by rememberSaveable { mutableStateOf("") }
        var value2 by rememberSaveable { mutableStateOf<String?>(null) }

        TextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = { Text("Text") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value2 ?: "",
            onValueChange = { upis -> value2 = upis },
            label = { Text("Broj") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // stari nacin rutiranja preko stringova, nadovezivanje parametara u url
                // navController.navigate("second/$textFieldValue")

                // novi nacin rutiranja preko klasa
                navController.navigate(
                    SecondPath(
                        p = textFieldValue, i = value2?.toIntOrNull() ?: 0
                    )
                )
            }, modifier = Modifier.align(Alignment.End)
        ) {
            Text("Go to Details")
        }
    }
}


//@Composable
//fun SpendingList(spendingsFlow: Flow<List<Spending>>, repo: SpendingRepository?) {
//    val spendings by spendingsFlow.collectAsState(initial = emptyList())
//
//    LazyColumn(modifier = Modifier.fillMaxSize()) {
//        items(spendings) { spending ->
//            SpendingListItem(spending = spending, repo){}
//        }
//    }
//}
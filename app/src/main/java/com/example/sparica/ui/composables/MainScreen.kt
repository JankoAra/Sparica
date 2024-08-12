package com.example.sparica.ui.composables

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val key = stringPreferencesKey("name")
    var name by rememberSaveable { mutableStateOf("nepoznato") }

    // Collect the stored name from the DataStore
    val nameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[key] ?: "nema"
    }
    val nameFromStore by nameFlow.collectAsState(initial = "nema")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Sparica")
                }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(Icons.Filled.Check, contentDescription = "Localized description")
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Localized description")
                    }
                    IconButton(onClick = { /* do something */ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Localized description")
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { /*TODO*/ },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Localized description")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Home, contentDescription = "Localized description")
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Tvoje ime:\t\t$nameFromStore")

            OutlinedTextField(
                value = name,
                onValueChange = { newName ->
                    name = newName
                    scope.launch {
                        context.dataStore.edit { settings ->
                            settings[key] = newName
                        }
                    }
                },
                label = { Text("Unesi ime") }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}

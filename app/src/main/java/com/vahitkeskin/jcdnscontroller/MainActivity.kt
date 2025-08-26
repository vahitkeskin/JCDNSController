package com.vahitkeskin.jcdnscontroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vahitkeskin.jcdnscontroller.ui.theme.JCDNSControllerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JCDNSControllerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppScaffold()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Özel DNS Durumu") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = androidx.compose.ui.graphics.Color.Black,   // Toolbar arka planı siyah
                    titleContentColor = androidx.compose.ui.graphics.Color.White // Yazı rengi beyaz
                )
            )
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            PrivateDnsScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JCDNSControllerTheme {
        AppScaffold()
    }
}
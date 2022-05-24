package com.example.jpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.jpack.ui.theme.JpackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JpackTheme {
                // A surface container using the 'background' color from the theme
                Row(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Greeting("Eiva")
                    val text = remember { mutableStateOf("") }
                    TextField(value = text.value, onValueChange = { getPrice(curr = it)})
                }
            }
        }
    }
}

fun getPrice(curr: String){
    val res: String

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JpackTheme {
        Greeting("Android")
    }
}
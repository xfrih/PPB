package com.example.aplikasikalkulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aplikasikalkulator.ui.theme.AplikasiKalkulatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplikasiKalkulatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Calculator(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Calculator(modifier: Modifier = Modifier) {
    var number1 by remember { mutableStateOf("") }
    var number2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            TextField(
                value = number1,
                onValueChange = { number1 = it },
                label = { Text("Angka 1") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            TextField(
                value = number2,
                onValueChange = { number2 = it },
                label = { Text("Angka 2") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            TextField(
                value = result,
                onValueChange = {},
                label = { Text("Hasil") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        val n1 = number1.toDoubleOrNull()
                        val n2 = number2.toDoubleOrNull()
                        if (n1 != null && n2 != null) {
                            result = (n1 + n2).toString()
                        }
                    },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("+")
                }

                Button(
                    onClick = {
                        val n1 = number1.toDoubleOrNull()
                        val n2 = number2.toDoubleOrNull()
                        if (n1 != null && n2 != null) {
                            result = (n1 - n2).toString()
                        }
                    },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("-")
                }

                Button(
                    onClick = {
                        val n1 = number1.toDoubleOrNull()
                        val n2 = number2.toDoubleOrNull()
                        if (n1 != null && n2 != null) {
                            result = (n1 * n2).toString()
                        }
                    },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("*")
                }

                Button(
                    onClick = {
                        val n1 = number1.toDoubleOrNull()
                        val n2 = number2.toDoubleOrNull()
                        if (n1 != null && n2 != null && n2 != 0.0) {
                            result = (n1 / n2).toString()
                        } else if (n2 == 0.0) {
                            result = "Error: Div by 0"
                        } else {
                            result = "Error: Invalid input"
                        }
                    },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("/")
                }
            }

            Button(
                onClick = {
                    number1 = ""
                    number2 = ""
                    result = ""
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Clear")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    AplikasiKalkulatorTheme {
        Calculator()
    }
}

package com.example.composablebutton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composablebutton.ui.theme.ComposableButtonTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposableButtonTheme {
                // Menggunakan warna background ungu sangat muda (f8f0ff) agar senada dengan dadu 3D
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF8F0FF) 
                ) {
                    DiceRollerApp()
                }
            }
        }
    }
}

@Composable
fun DiceRollerApp() {
    DiceWithButtonAndImage(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    var result by remember { mutableIntStateOf(1) }

    val imageResource = when (result) {
        1 -> R.drawable.dice_one
        2 -> R.drawable.dice_two
        3 -> R.drawable.dice_three
        4 -> R.drawable.dice_four
        5 -> R.drawable.dice_five
        else -> R.drawable.dice_six
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gambar Dadu
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = result.toString(),
            modifier = Modifier.size(200.dp) // Mengatur ukuran dadu agar proporsional
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tombol Roll dengan styling yang lebih mirip contoh
        Button(
            onClick = { result = Random.nextInt(1, 7) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4B5E91) // Warna biru/ungu gelap sesuai gambar
            ),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Roll",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiceRollerPreview() {
    ComposableButtonTheme {
        // Surface di preview juga disesuaikan
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF8F0FF)
        ) {
            DiceRollerApp()
        }
    }
}

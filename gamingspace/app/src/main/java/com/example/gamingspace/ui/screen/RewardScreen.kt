package com.example.gamingspace.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamingspace.ui.viewmodel.GamingViewModel

data class Reward(
    val name: String,
    val points: Int,
    val hours: Double,
    val description: String,
    val emoji: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardScreen(
    viewModel: GamingViewModel,
    onBack: () -> Unit
) {
    val member by viewModel.selectedMember.collectAsState()
    val redeemMessage by viewModel.redeemMessage.collectAsState()

    val rewards = listOf(
        Reward("1 Jam Gratis", 50,  1.0, "Main 1 jam tanpa biaya", "⏱️"),
        Reward("3 Jam Gratis", 100, 3.0, "Main 3 jam tanpa biaya", "🕐"),
        Reward("5 Jam Gratis", 150, 5.0, "Main 5 jam tanpa biaya", "🎮")
    )

    LaunchedEffect(redeemMessage) {
        if (redeemMessage != null) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearRedeemMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
    ) {
        TopAppBar(
            title = { Text("Tukar Reward", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A2E))
        )

        Column(modifier = Modifier.padding(20.dp)) {

            // Info poin member
            member?.let { m ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CardGiftcard,
                            contentDescription = null,
                            tint = Color(0xFF00D4FF),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(m.name, color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Poin tersedia", color = Color.Gray, fontSize = 12.sp)
                        }
                        Text(
                            "${m.points} pts",
                            color = Color(0xFF00D4FF),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            }

            // Pesan redeem
            redeemMessage?.let {
                Spacer(modifier = Modifier.height(12.dp))
                val isSuccess = it.startsWith("Berhasil")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSuccess) Color(0xFF1B5E20) else Color(0xFF7F0000)
                    )
                ) {
                    Text(
                        text = it,
                        color = Color.White,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text("Daftar Reward", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(rewards) { reward ->
                    RewardItem(
                        reward = reward,
                        memberPoints = member?.points ?: 0,
                        onRedeem = {
                            viewModel.redeemReward(reward.name, reward.points, reward.hours)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RewardItem(reward: Reward, memberPoints: Int, onRedeem: () -> Unit) {
    val canRedeem = memberPoints >= reward.points
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(reward.emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(reward.name, color = Color.White, fontWeight = FontWeight.Bold)
                Text(reward.description, color = Color.Gray, fontSize = 12.sp)
                Text(
                    "${reward.points} poin  •  +${reward.hours.toInt()} jam",
                    color = Color(0xFF00D4FF),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = onRedeem,
                enabled = canRedeem,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00D4FF),
                    disabledContainerColor = Color(0xFF333333)
                )
            ) {
                Text(
                    "Redeem",
                    color = if (canRedeem) Color.Black else Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
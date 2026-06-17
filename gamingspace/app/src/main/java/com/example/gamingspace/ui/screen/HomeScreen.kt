package com.example.gamingspace.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamingspace.data.model.Member
import com.example.gamingspace.ui.viewmodel.GamingViewModel

@Composable
fun HomeScreen(
    viewModel: GamingViewModel,
    onAddMember: () -> Unit,
    onMemberClick: (Int) -> Unit
) {
    val members by viewModel.members.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E))
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.SportsEsports,
                        contentDescription = null,
                        tint = Color(0xFF00D4FF),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Gaming Space",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Total Member: ${members.size}",
                    color = Color(0xFF00D4FF),
                    fontSize = 14.sp
                )
            }
        }

        // Content
        if (members.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎮", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Belum ada member", color = Color.Gray)
                    Text("Tap + untuk menambah member", color = Color.Gray, fontSize = 12.sp)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(members) { member ->
                    MemberItem(member = member, onClick = { onMemberClick(member.id) })
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        FloatingActionButton(
            onClick = onAddMember,
            modifier = Modifier.padding(24.dp),
            containerColor = Color(0xFF00D4FF)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Tambah Member", tint = Color.Black)
        }
    }
}

@Composable
fun MemberItem(member: Member, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF00D4FF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.name.first().uppercaseChar().toString(),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(member.name, color = Color.White, fontWeight = FontWeight.Bold)
                Text(member.email, color = Color.Gray, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${member.points} pts",
                    color = Color(0xFF00D4FF),
                    fontWeight = FontWeight.Bold
                )
                Text("Member #${member.id}", color = Color.Gray, fontSize = 11.sp)
            }
        }
    }
}
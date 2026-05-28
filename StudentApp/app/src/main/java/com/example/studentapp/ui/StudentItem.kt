package com.example.studentapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.data.Siswa

fun getInitials(nama: String): String {
    val parts = nama.trim().split(" ")
    return if (parts.size >= 2)
        "${parts[0].first().uppercaseChar()}${parts[1].first().uppercaseChar()}"
    else
        nama.take(2).uppercase()
}

fun getBadgeColor(nama: String): Color {
    val colors = listOf(
        Color(0xFF6C63FF),
        Color(0xFFFF6584),
        Color(0xFF43C6AC),
        Color(0xFFFFA07A),
        Color(0xFF36D1DC),
        Color(0xFFCB356B)
    )
    return colors[nama.length % colors.size]
}

@Composable
fun StudentItem(
    siswa: Siswa,
    onEdit: (Siswa) -> Unit,
    onDelete: (Siswa) -> Unit
) {
    val badgeColor = getBadgeColor(siswa.nama)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getInitials(siswa.nama),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = siswa.nama,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1A1A2E)
                )
                Text(
                    text = siswa.email,
                    fontSize = 13.sp,
                    color = Color(0xFF888888)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "ID: #${siswa.id}",
                    fontSize = 11.sp,
                    color = Color(0xFFBBBBBB)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(
                    onClick = { onEdit(siswa) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFF6C63FF),
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { onDelete(siswa) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Hapus",
                        tint = Color(0xFFFF6584),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
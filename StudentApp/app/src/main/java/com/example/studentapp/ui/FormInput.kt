package com.example.studentapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FormInput(onTambah: (String, String) -> Unit) {
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    var namaError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    val purple = Color(0xFF6C63FF)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F0FF)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "➕ Daftar Siswa Baru",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = purple
                )
                TextButton(onClick = { isExpanded = !isExpanded }) {
                    Text(if (isExpanded) "Tutup" else "Buka Form", color = purple)
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nama,
                        onValueChange = {
                            nama = it
                            namaError = false
                        },
                        label = { Text("Nama Lengkap") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = purple)
                        },
                        isError = namaError,
                        supportingText = {
                            if (namaError) Text("Nama tidak boleh kosong", color = Color.Red)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = purple,
                            focusedLabelColor = purple
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = false
                        },
                        label = { Text("Alamat Email") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = purple)
                        },
                        isError = emailError,
                        supportingText = {
                            if (emailError) Text("Email tidak boleh kosong", color = Color.Red)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = purple,
                            focusedLabelColor = purple
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            namaError = nama.isBlank()
                            emailError = email.isBlank()
                            if (!namaError && !emailError) {
                                onTambah(nama.trim(), email.trim())
                                nama = ""
                                email = ""
                                isExpanded = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = purple)
                    ) {
                        Text(
                            "Simpan Data Siswa",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
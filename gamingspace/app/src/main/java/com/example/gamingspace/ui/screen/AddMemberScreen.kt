package com.example.gamingspace.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamingspace.ui.viewmodel.GamingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberScreen(
    viewModel: GamingViewModel,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
    ) {
        TopAppBar(
            title = { Text("Tambah Member", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A2E))
        )

        Column(modifier = Modifier.padding(20.dp)) {
            Text("Registrasi Member Baru", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Isi data lengkap untuk mendaftar", color = Color.Gray, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it; nameError = false },
                label = { Text("Nama Lengkap") },
                isError = nameError,
                supportingText = { if (nameError) Text("Nama wajib diisi") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00D4FF),
                    focusedLabelColor = Color(0xFF00D4FF),
                    cursorColor = Color(0xFF00D4FF),
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; emailError = false },
                label = { Text("Email") },
                isError = emailError,
                supportingText = { if (emailError) Text("Email wajib diisi") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00D4FF),
                    focusedLabelColor = Color(0xFF00D4FF),
                    cursorColor = Color(0xFF00D4FF),
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it; phoneError = false },
                label = { Text("Nomor HP") },
                isError = phoneError,
                supportingText = { if (phoneError) Text("Nomor HP wajib diisi") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00D4FF),
                    focusedLabelColor = Color(0xFF00D4FF),
                    cursorColor = Color(0xFF00D4FF),
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    nameError = name.isBlank()
                    emailError = email.isBlank()
                    phoneError = phone.isBlank()
                    if (!nameError && !emailError && !phoneError) {
                        viewModel.addMember(name, email, phone)
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00D4FF))
            ) {
                Text("Daftarkan Member", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
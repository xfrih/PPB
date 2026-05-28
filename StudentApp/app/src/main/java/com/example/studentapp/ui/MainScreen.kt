package com.example.studentapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studentapp.data.Siswa
import com.example.studentapp.viewmodel.StudentViewModel

@Composable
fun MainScreen(viewModel: StudentViewModel) {
    val siswaList by viewModel.siswaList.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var siswaEdit by remember { mutableStateOf<Siswa?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var siswaHapus by remember { mutableStateOf<Siswa?>(null) }

    val purple = Color(0xFF6C63FF)
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF6C63FF), Color(0xFF9D96FF))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F6FF))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = gradientBrush)
                .padding(horizontal = 20.dp, vertical = 28.dp)
                .statusBarsPadding()
        ) {
            Column {
                Text(
                    text = "📚 Student Registry",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )
                Text(
                    text = "${siswaList.size} siswa terdaftar",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                FormInput(onTambah = { nama, email ->
                    viewModel.tambahSiswa(nama, email)
                })
                Spacer(modifier = Modifier.height(8.dp))

                if (siswaList.isNotEmpty()) {
                    Text(
                        text = "Daftar Siswa",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF444466),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                    )
                }
            }

            if (siswaList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🎓", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Belum ada siswa terdaftar",
                                color = Color(0xFFAAAAAA),
                                fontSize = 15.sp
                            )
                            Text(
                                "Buka form di atas untuk menambahkan",
                                color = Color(0xFFCCCCCC),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            } else {
                items(siswaList) { siswa ->
                    StudentItem(
                        siswa = siswa,
                        onEdit = {
                            siswaEdit = it
                            showEditDialog = true
                        },
                        onDelete = {
                            siswaHapus = it
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }

    // Dialog Edit
    if (showEditDialog && siswaEdit != null) {
        var editNama by remember { mutableStateOf(siswaEdit!!.nama) }
        var editEmail by remember { mutableStateOf(siswaEdit!!.email) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text("✏️ Edit Data Siswa", fontWeight = FontWeight.Bold, color = purple)
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = editNama,
                        onValueChange = { editNama = it },
                        label = { Text("Nama") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, null, tint = purple)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = purple,
                            focusedLabelColor = purple
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, null, tint = purple)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = purple,
                            focusedLabelColor = purple
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editNama.isNotBlank() && editEmail.isNotBlank()) {
                            viewModel.editSiswa(
                                siswaEdit!!.copy(
                                    nama = editNama.trim(),
                                    email = editEmail.trim()
                                )
                            )
                            showEditDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = purple),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showEditDialog = false },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Batal")
                }
            }
        )
    }

    // Dialog Konfirmasi Hapus
    if (showDeleteDialog && siswaHapus != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text("🗑️ Hapus Siswa?", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Data \"${siswaHapus!!.nama}\" akan dihapus secara permanen.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.hapusSiswa(siswaHapus!!)
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6584)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Batal")
                }
            }
        )
    }
}
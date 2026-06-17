package com.example.gamingspace.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamingspace.data.model.Transaction
import com.example.gamingspace.ui.viewmodel.GamingViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    viewModel: GamingViewModel,
    onBack: () -> Unit
) {
    val member by viewModel.selectedMember.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    var amount by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
    ) {
        TopAppBar(
            title = { Text("Transaksi", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A2E))
        )

        Column(modifier = Modifier.padding(20.dp)) {
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(m.name, color = Color.White, fontWeight = FontWeight.Bold)
                            Text("Member #${m.id}", color = Color.Gray, fontSize = 12.sp)
                        }
                        Text("${m.points} pts", color = Color(0xFF00D4FF), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text("Input Transaksi", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it; amountError = false },
                label = { Text("Nominal Pembelian (Rp)") },
                isError = amountError,
                supportingText = {
                    if (amountError) Text("Nominal wajib diisi")
                    else {
                        val pts = amount.toDoubleOrNull()?.let { (it / 1000).toInt() } ?: 0
                        Text("Poin didapat: $pts poin", color = Color(0xFF00D4FF))
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val value = amount.toDoubleOrNull()
                    if (value == null || value <= 0) {
                        amountError = true
                    } else {
                        viewModel.addTransaction(value)
                        amount = ""
                        showSuccess = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00D4FF))
            ) {
                Text("Simpan Transaksi", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            if (showSuccess) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("✅ Transaksi berhasil disimpan!", color = Color.Green, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Riwayat Transaksi", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            if (transactions.isEmpty()) {
                Text("Belum ada transaksi", color = Color.Gray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(transactions) { trx ->
                        TransactionItem(trx)
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val formatted = NumberFormat.getNumberInstance(Locale("id", "ID"))
        .format(transaction.amount.toLong())
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Rp $formatted", color = Color.White, fontWeight = FontWeight.Bold)
                Text(transaction.date, color = Color.Gray, fontSize = 11.sp)
            }
            Text("+${transaction.pointEarned} pts", color = Color(0xFF00D4FF), fontWeight = FontWeight.Bold)
        }
    }
}
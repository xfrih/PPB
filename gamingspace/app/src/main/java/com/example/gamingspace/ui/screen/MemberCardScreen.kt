package com.example.gamingspace.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamingspace.ui.viewmodel.GamingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberCardScreen(
    memberId: Int,
    viewModel: GamingViewModel,
    onBack: () -> Unit,
    onTransaction: () -> Unit,
    onReward: () -> Unit
) {
    val members          by viewModel.members.collectAsState()
    val member           = members.find { it.id == memberId }
    val isSessionActive  by viewModel.isSessionActive.collectAsState()
    val isSessionPaused  by viewModel.isSessionPaused.collectAsState()
    val elapsedSeconds   by viewModel.elapsedSeconds.collectAsState()

    // State untuk dialog konfirmasi hapus
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(member) {
        member?.let { viewModel.selectMember(it) }
    }

    val sessionColor by animateColorAsState(
        targetValue = if (isSessionActive && !isSessionPaused)
            Color(0xFF00FF88) else Color(0xFF00D4FF),
        animationSpec = if (isSessionActive && !isSessionPaused)
            infiniteRepeatable(tween(800), RepeatMode.Reverse)
        else tween(300),
        label = "sessionColor"
    )

    // ── Dialog Konfirmasi Hapus ───────────────────────────────────────────────
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = Color(0xFF1A1A2E),
            icon = {
                Icon(
                    Icons.Default.DeleteForever,
                    contentDescription = null,
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    "Hapus Member?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Data member ${member?.name} akan dihapus permanen.\nAksi ini tidak bisa dibatalkan.",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        member?.let {
                            viewModel.deleteMember(it) { onBack() }
                        }
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Hapus", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Gray
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("Batal")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
    ) {
        TopAppBar(
            title = { Text("Kartu Member", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White
                    )
                }
            },
            // ── Tombol Hapus di kanan TopAppBar ───────────────────────────────
            actions = {
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Hapus Member",
                        tint = Color(0xFFE53935)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1A1A2E))
        )

        member?.let { m ->
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── Digital Card ──────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF0F3460),
                                    Color(0xFF16213E),
                                    Color(0xFF1A1A2E)
                                )
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .then(
                            if (isSessionActive)
                                Modifier.border(2.dp, sessionColor, RoundedCornerShape(20.dp))
                            else Modifier
                        )
                        .padding(24.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.SportsEsports,
                                    contentDescription = null,
                                    tint = Color(0xFF00D4FF),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "Gaming Space",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                getMemberLevel(m.points),
                                color = Color(0xFF00D4FF),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(m.name, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("ID #${String.format("%04d", m.id)}", color = Color.Gray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Bergabung: ${m.joinDate}", color = Color.Gray, fontSize = 11.sp)
                            Text("${m.points} Poin", color = Color(0xFF00D4FF), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // ── Info Cards (3 kolom) ──────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        label = "Total Poin",
                        value = "${m.points}",
                        icon = Icons.Default.Stars
                    )
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        label = "Level",
                        value = getMemberLevel(m.points),
                        icon = Icons.Default.EmojiEvents
                    )
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        label = "Sisa Waktu",
                        value = formatHours(m.remainingHours),
                        icon = Icons.Default.Timer
                    )
                }

                // ── Session Panel ─────────────────────────────────────────────
                SessionPanel(
                    isSessionActive = isSessionActive,
                    isSessionPaused = isSessionPaused,
                    elapsedSeconds  = elapsedSeconds,
                    remainingHours  = m.remainingHours,
                    sessionColor    = sessionColor,
                    onStart       = { viewModel.startSession() },
                    onPauseResume = { viewModel.pauseResumeSession() },
                    onStop        = { viewModel.stopSession() }
                )

                // ── Action Buttons ────────────────────────────────────────────
                Button(
                    onClick = onTransaction,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00D4FF))
                ) {
                    Icon(Icons.Default.AddCard, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tambah Transaksi", color = Color.Black, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onReward,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00D4FF)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00D4FF))
                ) {
                    Icon(Icons.Default.CardGiftcard, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tukar Reward", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ── Session Panel ─────────────────────────────────────────────────────────────
@Composable
fun SessionPanel(
    isSessionActive: Boolean,
    isSessionPaused: Boolean,
    elapsedSeconds: Long,
    remainingHours: Double,
    sessionColor: Color,
    onStart: () -> Unit,
    onPauseResume: () -> Unit,
    onStop: () -> Unit
) {
    val remainingSec = (remainingHours * 3600).toLong()
    val leftSec      = (remainingSec - elapsedSeconds).coerceAtLeast(0L)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
    ) {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sesi Bermain", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(16.dp))

            if (isSessionActive) {
                Text(
                    text = formatSeconds(elapsedSeconds),
                    color = sessionColor,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "Elapsed  •  Sisa: ${formatSeconds(leftSec)}",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                if (isSessionPaused) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("⏸ DIJEDA", color = Color(0xFFFFAA00), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onPauseResume,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFFAA00)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFAA00))
                    ) {
                        Icon(
                            imageVector = if (isSessionPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isSessionPaused) "Lanjut" else "Jeda", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = onStop,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Stop", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                if (remainingHours <= 0.0) {
                    Text(
                        "Saldo waktu habis.\nTambah transaksi atau tukar reward.",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        "Saldo: ${formatHours(remainingHours)}",
                        color = Color(0xFF00D4FF),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Tekan Mulai untuk memulai sesi", color = Color.Gray, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onStart,
                    enabled = remainingHours > 0.0,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00FF88),
                        disabledContainerColor = Color(0xFF333333)
                    )
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = if (remainingHours > 0.0) Color.Black else Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Mulai Sesi",
                        color = if (remainingHours > 0.0) Color.Black else Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

// ── Helper Functions ──────────────────────────────────────────────────────────
fun formatHours(hours: Double): String {
    val totalMinutes = (hours * 60).toInt()
    val h = totalMinutes / 60
    val m = totalMinutes % 60
    return when {
        h == 0 && m == 0 -> "0 Mnt"
        h == 0           -> "$m Mnt"
        m == 0           -> "$h Jam"
        else             -> "$h J $m Mnt"
    }
}

fun formatSeconds(totalSec: Long): String {
    val h = totalSec / 3600
    val m = (totalSec % 3600) / 60
    val s = totalSec % 60
    return if (h > 0)
        String.format("%d:%02d:%02d", h, m, s)
    else
        String.format("%02d:%02d", m, s)
}

@Composable
fun InfoCard(
    modifier: Modifier,
    label: String,
    value: String,
    icon: ImageVector
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF00D4FF), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp, textAlign = TextAlign.Center)
            Text(label, color = Color.Gray, fontSize = 10.sp, textAlign = TextAlign.Center)
        }
    }
}

fun getMemberLevel(points: Int): String = when {
    points >= 5000 -> "LEGENDARY"
    points >= 3000 -> "PLATINUM"
    points >= 1500 -> "GOLD"
    points >= 500  -> "SILVER"
    else          -> "BRONZE"
}
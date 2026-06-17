package com.example.gamingspace.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gamingspace.data.model.Member
import com.example.gamingspace.data.model.Transaction
import com.example.gamingspace.data.repository.GamingRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

class GamingViewModel(private val repository: GamingRepository) : ViewModel() {

    // ── Members ───────────────────────────────────────────────────────────────
    val members: StateFlow<List<Member>> = repository.getAllMembers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedMember = MutableStateFlow<Member?>(null)
    val selectedMember: StateFlow<Member?> = _selectedMember

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private val _redeemMessage = MutableStateFlow<String?>(null)
    val redeemMessage: StateFlow<String?> = _redeemMessage

    // ── Session Timer ─────────────────────────────────────────────────────────
    private val _isSessionActive = MutableStateFlow(false)
    val isSessionActive: StateFlow<Boolean> = _isSessionActive

    private val _isSessionPaused = MutableStateFlow(false)
    val isSessionPaused: StateFlow<Boolean> = _isSessionPaused

    private val _elapsedSeconds = MutableStateFlow(0L)
    val elapsedSeconds: StateFlow<Long> = _elapsedSeconds

    private var timerJob: Job? = null

    // ── Select Member ─────────────────────────────────────────────────────────
    fun selectMember(member: Member) {
        _selectedMember.value = member
        viewModelScope.launch {
            repository.getTransactionsByMember(member.id)
                .collect { _transactions.value = it }
        }
    }

    // ── Add Member ────────────────────────────────────────────────────────────
    fun addMember(name: String, email: String, phone: String) {
        viewModelScope.launch {
            val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            repository.insertMember(
                Member(name = name, email = email, phone = phone, joinDate = date)
            )
        }
    }

    // ── Delete Member ─────────────────────────────────────────────────────────
    fun deleteMember(member: Member, onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.deleteMember(member)
            _selectedMember.value = null
            onDeleted()
        }
    }

    // ── Add Transaction ───────────────────────────────────────────────────────
    fun addTransaction(amount: Double) {
        val member = _selectedMember.value ?: return
        val pointEarned = (amount / 1000).toInt()
        val hoursAdded  = amount / 10000.0
        val date = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date())

        viewModelScope.launch {
            repository.insertTransaction(
                Transaction(
                    memberId    = member.id,
                    amount      = amount,
                    pointEarned = pointEarned,
                    date        = date
                )
            )
            val updatedMember = member.copy(
                points         = member.points + pointEarned,
                remainingHours = member.remainingHours + hoursAdded
            )
            repository.updateMember(updatedMember)
            _selectedMember.value = updatedMember
        }
    }

    // ── Redeem Reward ─────────────────────────────────────────────────────────
    fun redeemReward(rewardName: String, pointCost: Int, hoursAdded: Double) {
        val member = _selectedMember.value ?: return
        if (member.points < pointCost) {
            _redeemMessage.value = "Poin tidak cukup untuk redeem $rewardName"
            return
        }
        viewModelScope.launch {
            val updatedMember = member.copy(
                points         = member.points - pointCost,
                remainingHours = member.remainingHours + hoursAdded
            )
            repository.updateMember(updatedMember)
            _selectedMember.value = updatedMember
            _redeemMessage.value =
                "Berhasil redeem $rewardName! -$pointCost poin, +$hoursAdded jam main"
        }
    }

    fun clearRedeemMessage() {
        _redeemMessage.value = null
    }

    // ── Session : Start ───────────────────────────────────────────────────────
    fun startSession() {
        val member = _selectedMember.value ?: return
        if (member.remainingHours <= 0.0) return

        _isSessionActive.value = false
        _isSessionPaused.value = false
        _elapsedSeconds.value  = 0L
        _isSessionActive.value = true

        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                if (!_isSessionPaused.value) {
                    _elapsedSeconds.value++
                    val remainingSec =
                        (_selectedMember.value?.remainingHours ?: 0.0) * 3600
                    if (_elapsedSeconds.value >= remainingSec.toLong()) {
                        stopSession()
                        break
                    }
                }
            }
        }
    }

    // ── Session : Pause / Resume ──────────────────────────────────────────────
    fun pauseResumeSession() {
        _isSessionPaused.value = !_isSessionPaused.value
    }

    // ── Session : Stop ────────────────────────────────────────────────────────
    fun stopSession() {
        timerJob?.cancel()
        timerJob = null

        val member = _selectedMember.value ?: run {
            _isSessionActive.value = false
            _isSessionPaused.value = false
            return
        }

        val usedHours    = _elapsedSeconds.value / 3600.0
        val newRemaining = (member.remainingHours - usedHours).coerceAtLeast(0.0)

        viewModelScope.launch {
            val updatedMember = member.copy(remainingHours = newRemaining)
            repository.updateMember(updatedMember)
            _selectedMember.value = updatedMember
        }

        _isSessionActive.value = false
        _isSessionPaused.value = false
        _elapsedSeconds.value  = 0L
    }

} // ← pastikan kurung ini ada, ini penutup class GamingViewModel

// ── Factory ───────────────────────────────────────────────────────────────────
class GamingViewModelFactory(private val repository: GamingRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GamingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GamingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
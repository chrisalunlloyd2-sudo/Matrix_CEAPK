package com.matrix.ce.core.viper

/**
 * VIPER Architecture Definitions for KAI 9000 Shell
 */

// --- Entity ---
data class SwarmStatus(
    val agentId: String,
    val status: String, // Online, Offline, Thinking
    val lastPing: String
)

// --- Interactor (Business Logic) ---
interface SwarmInteractorInput {
    fun fetchStatus()
    fun triggerTask(content: String)
}

interface SwarmInteractorOutput {
    fun onStatusFetched(statusList: List<SwarmStatus>)
    fun onError(message: String)
}

// --- Presenter (Logic for View) ---
interface SwarmPresenterInput {
    fun viewDidLoad()
    fun didTapRefresh()
}

// --- View (UI contract) ---
interface SwarmViewInput {
    fun showStatus(statusList: List<SwarmStatus>)
    fun showLoading()
    fun hideLoading()
}

// --- Router (Navigation) ---
interface SwarmRouterInput {
    fun navigateToAppSettings()
    fun navigateToTerminal()
}

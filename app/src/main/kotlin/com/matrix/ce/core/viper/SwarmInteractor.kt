package com.matrix.ce.core.viper

import android.util.Log
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

/**
 * SwarmInteractor: The "Hands" of the VIPER shell.
 * Responsible for fetching swarm status and interacting with MCP servers.
 */
class SwarmInteractor(private val output: SwarmInteractorOutput) : SwarmInteractorInput {

    private val client = OkHttpClient()
    private val TAG = "SwarmInteractor"
    
    // In a real Termux setup, this would be localhost or a specific bridge IP
    private val API_BASE_URL = "http://127.0.0.1:8081/api" 

    override fun fetchStatus() {
        val request = Request.Builder()
            .url("$API_BASE_URL/swarm/status")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Failed to fetch status: ${e.message}")
                output.onError("API Connection Failed")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        output.onError("Server Error: ${response.code}")
                        return
                    }

                    val jsonData = response.body?.string() ?: ""
                    val statusList = parseSwarmStatus(jsonData)
                    output.onStatusFetched(statusList)
                }
            }
        })
    }

    override fun triggerTask(content: String) {
        // Break-out logic to trigger a task via the KAI Orchestrator
        Log.d(TAG, "Triggering Task: $content")
    }

    private fun parseSwarmStatus(json: String): List<SwarmStatus> {
        val list = mutableListOf<SwarmStatus>()
        try {
            val root = JSONObject(json)
            val agents = root.getJSONArray("agents")
            for (i in 0 until agents.length()) {
                val agent = agents.getJSONObject(i)
                list.add(SwarmStatus(
                    agentId = agent.getString("id"),
                    status = agent.getString("status"),
                    lastPing = agent.optString("last_ping", "Never")
                ))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Parsing Error: ${e.message}")
        }
        return list
    }
}

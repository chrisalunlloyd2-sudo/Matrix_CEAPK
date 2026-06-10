package com.matrix.ce.core.viper

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matrix.ce.R

/**
 * MainActivity: The "Face" of the VIPER shell.
 * Renders the Axiomatic Grid and handles user interactions.
 */
class MainActivity : AppCompatActivity(), SwarmViewInput {

    private lateinit var presenter: SwarmPresenterInput
    private lateinit var loadingBar: ProgressBar
    private lateinit var statusList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // VIPER Module Assembly
        val router = SwarmRouter(this)
        val interactor = SwarmInteractor(null!!) // Circular dependency handled in assembly
        val presenter = SwarmPresenter(this, interactor, router)
        // Correcting the circular injection (manual for simplicity in this skeleton)
        val realInteractor = SwarmInteractor(presenter)
        this.presenter = SwarmPresenter(this, realInteractor, router)

        setupUI()
        this.presenter.viewDidLoad()
    }

    private fun setupUI() {
        loadingBar = findViewById(R.id.loading_bar)
        statusList = findViewById(R.id.status_recycler)
        statusList.layoutManager = LinearLayoutManager(this)
    }

    override fun showStatus(statusList: List<SwarmStatus>) {
        runOnUiThread {
            // Update RecyclerView Adapter here
        }
    }

    override fun showLoading() {
        runOnUiThread { loadingBar.visibility = View.VISIBLE }
    }

    override fun hideLoading() {
        runOnUiThread { loadingBar.visibility = View.GONE }
    }
}

/**
 * SwarmRouter: Handles navigation between the shell's components.
 */
class SwarmRouter(private val activity: MainActivity) : SwarmRouterInput {
    override fun navigateToAppSettings() {
        // Navigation logic to Settings Activity
    }

    override fun navigateToTerminal() {
        // Navigation logic to launch Termux or internal Terminal view
    }
}

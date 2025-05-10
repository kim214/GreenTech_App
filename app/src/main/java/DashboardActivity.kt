package com.example.greentechlogin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.greentechlogin.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    // Manual override flags
    private var manualIrrigation = false
    private var manualVentilation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Default section
        showMonitoringSection()
        highlightSelectedTab(binding.btnLiveMonitoring) // Highlight Live Monitoring tab by default

        // Tab clicks
        binding.btnLiveMonitoring.setOnClickListener {
            showMonitoringSection()
            highlightSelectedTab(binding.btnLiveMonitoring)
        }
        binding.btnIrrigation.setOnClickListener {
            showIrrigationSection()
            highlightSelectedTab(binding.btnIrrigation)
        }
        binding.btnVentilation.setOnClickListener {
            showVentilationSection()
            highlightSelectedTab(binding.btnVentilation)
        }

        // Irrigation manual controls
        binding.startIrrigationButton.setOnClickListener {
            manualIrrigation = true
            startIrrigation(auto = false)
        }

        binding.stopIrrigationButton.setOnClickListener {
            manualIrrigation = true
            stopIrrigation(auto = false)
        }

        // Ventilation manual controls
        binding.openVentilationButton.setOnClickListener {
            manualVentilation = true
            openVentilation(auto = false)
        }

        binding.closeVentilationButton.setOnClickListener {
            manualVentilation = true
            closeVentilation(auto = false)
        }

        // Logout button
        binding.btnLogout.setOnClickListener {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    // Section toggles
    private fun showMonitoringSection() {
        binding.liveMonitoringSection.visibility = View.VISIBLE
        binding.irrigationControls.visibility = View.GONE
        binding.ventilationControls.visibility = View.GONE
    }

    private fun showIrrigationSection() {
        binding.liveMonitoringSection.visibility = View.GONE
        binding.irrigationControls.visibility = View.VISIBLE
        binding.ventilationControls.visibility = View.GONE
    }

    private fun showVentilationSection() {
        binding.liveMonitoringSection.visibility = View.GONE
        binding.irrigationControls.visibility = View.GONE
        binding.ventilationControls.visibility = View.VISIBLE
    }

    // Helper function to highlight the selected tab
    private fun highlightSelectedTab(selected: Button) {
        val buttons = listOf(binding.btnLiveMonitoring, binding.btnIrrigation, binding.btnVentilation)
        for (button in buttons) {
            if (button == selected) {
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_700)) // Highlight selected tab
                button.setTextColor(Color.WHITE)
            } else {
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray)) // Neutral color for others
                button.setTextColor(Color.BLACK)
            }
        }
    }

    // Irrigation logic
    private fun startIrrigation(auto: Boolean) {
        binding.irrigationStatus.text = "Irrigation Status: ON"
        binding.startIrrigationButton.visibility = View.GONE
        binding.stopIrrigationButton.visibility = View.VISIBLE
        binding.autoIrrigationStatus.text = if (auto) "Auto-Irrigation: ON" else "Auto-Irrigation: OFF"
        if (!auto) Toast.makeText(this, "Irrigation started", Toast.LENGTH_SHORT).show()
    }

    private fun stopIrrigation(auto: Boolean) {
        binding.irrigationStatus.text = "Irrigation Status: OFF"
        binding.startIrrigationButton.visibility = View.VISIBLE
        binding.stopIrrigationButton.visibility = View.GONE
        binding.autoIrrigationStatus.text = if (auto) "Auto-Irrigation: ON" else "Auto-Irrigation: OFF"
        if (!auto) Toast.makeText(this, "Irrigation stopped", Toast.LENGTH_SHORT).show()
    }

    // Ventilation logic
    private fun openVentilation(auto: Boolean) {
        binding.ventilationStatus.text = "Ventilation Status: OPEN"
        binding.openVentilationButton.visibility = View.GONE
        binding.closeVentilationButton.visibility = View.VISIBLE
        binding.autoVentilationStatus.text = if (auto) "Auto-Ventilation: ON" else "Auto-Ventilation: OFF"
        if (!auto) Toast.makeText(this, "Ventilation opened", Toast.LENGTH_SHORT).show()
    }

    private fun closeVentilation(auto: Boolean) {
        binding.ventilationStatus.text = "Ventilation Status: CLOSED"
        binding.openVentilationButton.visibility = View.VISIBLE
        binding.closeVentilationButton.visibility = View.GONE
        binding.autoVentilationStatus.text = if (auto) "Auto-Ventilation: ON" else "Auto-Ventilation: OFF"
        if (!auto) Toast.makeText(this, "Ventilation closed", Toast.LENGTH_SHORT).show()
    }
}

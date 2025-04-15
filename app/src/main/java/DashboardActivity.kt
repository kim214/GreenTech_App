package com.example.greentechlogin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.greentechlogin.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initially show the monitoring section
        showMonitoringSection()

        // Handle tab switching
        binding.liveMonitoringTab.setOnClickListener {
            showMonitoringSection()
        }

        binding.irrigationTab.setOnClickListener {
            showIrrigationSection()
        }

        binding.ventilationTab.setOnClickListener {
            showVentilationSection()
        }

        // Handle Start/Stop Irrigation button clicks
        binding.startIrrigationButton.setOnClickListener {
            startIrrigation()
        }

        binding.stopIrrigationButton.setOnClickListener {
            stopIrrigation()
        }

        // Handle Open/Close Ventilation button clicks
        binding.openVentilationButton.setOnClickListener {
            openVentilation()
        }

        binding.closeVentilationButton.setOnClickListener {
            closeVentilation()
        }
    }

    // Show monitoring section
    private fun showMonitoringSection() {
        binding.monitoringSection.visibility = View.VISIBLE
        binding.irrigationSection.visibility = View.GONE
        binding.ventilationSection.visibility = View.GONE
    }

    // Show irrigation section
    private fun showIrrigationSection() {
        binding.monitoringSection.visibility = View.GONE
        binding.irrigationSection.visibility = View.VISIBLE
        binding.ventilationSection.visibility = View.GONE
    }

    // Show ventilation section
    private fun showVentilationSection() {
        binding.monitoringSection.visibility = View.GONE
        binding.irrigationSection.visibility = View.GONE
        binding.ventilationSection.visibility = View.VISIBLE
    }

    // Start irrigation functionality
    private fun startIrrigation() {
        binding.irrigationStatus.text = "Irrigation Status: ON"
        binding.startIrrigationButton.visibility = View.GONE
        binding.stopIrrigationButton.visibility = View.VISIBLE

        // Change Auto-Irrigation status
        binding.autoIrrigationStatus.text = "Auto-Irrigation: ON"
        binding.autoIrrigationStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark))

        Toast.makeText(this, "Irrigation Started", Toast.LENGTH_SHORT).show()
    }

    // Stop irrigation functionality
    private fun stopIrrigation() {
        binding.irrigationStatus.text = "Irrigation Status: OFF"
        binding.startIrrigationButton.visibility = View.VISIBLE
        binding.stopIrrigationButton.visibility = View.GONE

        // Change Auto-Irrigation status
        binding.autoIrrigationStatus.text = "Auto-Irrigation: OFF"
        binding.autoIrrigationStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark))

        Toast.makeText(this, "Irrigation Stopped", Toast.LENGTH_SHORT).show()
    }

    // Open ventilation functionality
    private fun openVentilation() {
        binding.ventilationStatus.text = "Ventilation Status: OPEN"
        binding.openVentilationButton.visibility = View.GONE
        binding.closeVentilationButton.visibility = View.VISIBLE

        // Change Auto-Ventilation status
        binding.autoVentilationStatus.text = "Auto-Ventilation: ON"
        binding.autoVentilationStatus.setTextColor(resources.getColor(android.R.color.holo_green_dark))

        Toast.makeText(this, "Fan Opened", Toast.LENGTH_SHORT).show()
    }

    // Close ventilation functionality
    private fun closeVentilation() {
        binding.ventilationStatus.text = "Ventilation Status: CLOSED"
        binding.openVentilationButton.visibility = View.VISIBLE
        binding.closeVentilationButton.visibility = View.GONE

        // Change Auto-Ventilation status
        binding.autoVentilationStatus.text = "Auto-Ventilation: OFF"
        binding.autoVentilationStatus.setTextColor(resources.getColor(android.R.color.holo_red_dark))

        Toast.makeText(this, "Fan Closed", Toast.LENGTH_SHORT).show()
    }

    fun openTab(view: View) {}
    fun closeVentilation(view: View) {}
}

package com.example.greentechlogin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.greentechlogin.databinding.ActivityDashboardBinding
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    private lateinit var mqttClient: MqttClient
    private val mqttBrokerUrl = "tcp://192.168.63.124:1883"
    private val irrigationTopic = "greenhouse/irrigation"
    private val ventilationTopic = "greenhouse/ventilation"
    private var isMqttConnected = false

    private var manualIrrigation = false
    private var manualVentilation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeMqttClient()

        showMonitoringSection()
        highlightSelectedTab(binding.btnLiveMonitoring)

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

        binding.startIrrigationButton.setOnClickListener {
            manualIrrigation = true
            startIrrigation(false)
            sendMessage(irrigationTopic, "ON")
        }

        binding.stopIrrigationButton.setOnClickListener {
            manualIrrigation = true
            stopIrrigation(false)
            sendMessage(irrigationTopic, "OFF")
        }

        binding.openVentilationButton.setOnClickListener {
            manualVentilation = true
            openVentilation(false)
            sendMessage(ventilationTopic, "OPEN")
        }

        binding.closeVentilationButton.setOnClickListener {
            manualVentilation = true
            closeVentilation(false)
            sendMessage(ventilationTopic, "CLOSE")
        }

        binding.btnLogout.setOnClickListener {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun initializeMqttClient() {
        val clientId = "AndroidClient_" + System.currentTimeMillis()

        Thread {
            try {
                mqttClient = MqttClient(
                    mqttBrokerUrl,
                    clientId,
                    MemoryPersistence()
                )
                val options = MqttConnectOptions().apply {
                    isCleanSession = true
                }
                mqttClient.connect(options)
                isMqttConnected = true
                runOnUiThread {
                    Toast.makeText(this, "Connected to MQTT broker", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MQTT", "MQTT Exception: ${e.message}")
                isMqttConnected = false
                runOnUiThread {
                    Toast.makeText(this, "MQTT setup error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    private fun sendMessage(topic: String, message: String) {
        if (isMqttConnected) {
            try {
                val mqttMessage = MqttMessage(message.toByteArray()).apply {
                    qos = 1
                }
                mqttClient.publish(topic, mqttMessage)
                Log.d("MQTT", "Message sent to $topic: $message")
            } catch (e: Exception) {
                Log.e("MQTT", "Error publishing: ${e.message}")
                Toast.makeText(this, "Error sending message: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "MQTT not connected", Toast.LENGTH_SHORT).show()
        }
    }

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

    private fun highlightSelectedTab(selected: Button) {
        val buttons = listOf(binding.btnLiveMonitoring, binding.btnIrrigation, binding.btnVentilation)
        buttons.forEach {
            if (it == selected) {
                it.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_700))
                it.setTextColor(Color.WHITE)
            } else {
                it.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray))
                it.setTextColor(Color.BLACK)
            }
        }
    }

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

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (isMqttConnected) mqttClient.disconnect()
        } catch (e: Exception) {
            Log.e("MQTT", "Error disconnecting: ${e.message}")
        }
    }
}

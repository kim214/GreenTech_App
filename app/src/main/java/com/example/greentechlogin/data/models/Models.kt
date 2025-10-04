package com.example.greentechlogin.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("farm_name")
    val farmName: String? = null,
    @SerialName("farm_location")
    val farmLocation: String? = null,
    @SerialName("phone_number")
    val phoneNumber: String? = null,
    @SerialName("notification_preferences")
    val notificationPreferences: Map<String, Boolean>? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class SensorData(
    val id: String? = null,
    @SerialName("user_id")
    val userId: String,
    val temperature: Double,
    val humidity: Double,
    @SerialName("soil_moisture")
    val soilMoisture: Double,
    @SerialName("light_intensity")
    val lightIntensity: Double? = null,
    val timestamp: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class IrrigationLog(
    val id: String? = null,
    @SerialName("user_id")
    val userId: String,
    val action: String,
    val mode: String,
    @SerialName("duration_minutes")
    val durationMinutes: Int? = null,
    @SerialName("soil_moisture_before")
    val soilMoistureBefore: Double? = null,
    @SerialName("soil_moisture_after")
    val soilMoistureAfter: Double? = null,
    val timestamp: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class VentilationLog(
    val id: String? = null,
    @SerialName("user_id")
    val userId: String,
    val action: String,
    val mode: String,
    @SerialName("temperature_before")
    val temperatureBefore: Double? = null,
    @SerialName("temperature_after")
    val temperatureAfter: Double? = null,
    val timestamp: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class SystemAlert(
    val id: String? = null,
    @SerialName("user_id")
    val userId: String,
    @SerialName("alert_type")
    val alertType: String,
    val severity: String,
    val message: String,
    @SerialName("is_read")
    val isRead: Boolean = false,
    val resolved: Boolean = false,
    val timestamp: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class SystemSettings(
    val id: String? = null,
    @SerialName("user_id")
    val userId: String,
    @SerialName("mqtt_broker_url")
    val mqttBrokerUrl: String = "tcp://192.168.92.124:1883",
    @SerialName("temperature_threshold_min")
    val temperatureThresholdMin: Double = 15.0,
    @SerialName("temperature_threshold_max")
    val temperatureThresholdMax: Double = 35.0,
    @SerialName("humidity_threshold_min")
    val humidityThresholdMin: Double = 40.0,
    @SerialName("humidity_threshold_max")
    val humidityThresholdMax: Double = 80.0,
    @SerialName("soil_moisture_threshold_min")
    val soilMoistureThresholdMin: Double = 30.0,
    @SerialName("soil_moisture_threshold_max")
    val soilMoistureThresholdMax: Double = 70.0,
    @SerialName("auto_mode_enabled")
    val autoModeEnabled: Boolean = true,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

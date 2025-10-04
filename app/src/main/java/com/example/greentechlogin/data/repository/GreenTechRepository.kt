package com.example.greentechlogin.data.repository

import com.example.greentechlogin.data.SupabaseManager
import com.example.greentechlogin.data.models.*
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GreenTechRepository {

    private val supabase = SupabaseManager.client

    suspend fun getUserProfile(userId: String): Result<UserProfile?> = withContext(Dispatchers.IO) {
        try {
            val result = supabase.postgrest["user_profiles"]
                .select(Columns.ALL) {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingleOrNull<UserProfile>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createUserProfile(profile: UserProfile): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabase.postgrest["user_profiles"].insert(profile)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(profile: UserProfile): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabase.postgrest["user_profiles"].update({
                set("full_name", profile.fullName)
                set("farm_name", profile.farmName)
                set("farm_location", profile.farmLocation)
                set("phone_number", profile.phoneNumber)
                set("notification_preferences", profile.notificationPreferences)
            }) {
                filter {
                    eq("id", profile.id)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insertSensorData(data: SensorData): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabase.postgrest["sensor_data"].insert(data)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecentSensorData(userId: String, limit: Int = 50): Result<List<SensorData>> = withContext(Dispatchers.IO) {
        try {
            val result = supabase.postgrest["sensor_data"]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                    }
                    order("timestamp", ascending = false)
                    limit(limit.toLong())
                }
                .decodeList<SensorData>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLatestSensorData(userId: String): Result<SensorData?> = withContext(Dispatchers.IO) {
        try {
            val result = supabase.postgrest["sensor_data"]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                    }
                    order("timestamp", ascending = false)
                    limit(1)
                }
                .decodeSingleOrNull<SensorData>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insertIrrigationLog(log: IrrigationLog): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabase.postgrest["irrigation_logs"].insert(log)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getIrrigationLogs(userId: String, limit: Int = 50): Result<List<IrrigationLog>> = withContext(Dispatchers.IO) {
        try {
            val result = supabase.postgrest["irrigation_logs"]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                    }
                    order("timestamp", ascending = false)
                    limit(limit.toLong())
                }
                .decodeList<IrrigationLog>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insertVentilationLog(log: VentilationLog): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabase.postgrest["ventilation_logs"].insert(log)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVentilationLogs(userId: String, limit: Int = 50): Result<List<VentilationLog>> = withContext(Dispatchers.IO) {
        try {
            val result = supabase.postgrest["ventilation_logs"]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                    }
                    order("timestamp", ascending = false)
                    limit(limit.toLong())
                }
                .decodeList<VentilationLog>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insertAlert(alert: SystemAlert): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabase.postgrest["system_alerts"].insert(alert)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUnreadAlerts(userId: String): Result<List<SystemAlert>> = withContext(Dispatchers.IO) {
        try {
            val result = supabase.postgrest["system_alerts"]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                        eq("is_read", false)
                    }
                    order("timestamp", ascending = false)
                }
                .decodeList<SystemAlert>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllAlerts(userId: String, limit: Int = 100): Result<List<SystemAlert>> = withContext(Dispatchers.IO) {
        try {
            val result = supabase.postgrest["system_alerts"]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                    }
                    order("timestamp", ascending = false)
                    limit(limit.toLong())
                }
                .decodeList<SystemAlert>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun markAlertAsRead(alertId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabase.postgrest["system_alerts"].update({
                set("is_read", true)
            }) {
                filter {
                    eq("id", alertId)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSystemSettings(userId: String): Result<SystemSettings?> = withContext(Dispatchers.IO) {
        try {
            val result = supabase.postgrest["system_settings"]
                .select(Columns.ALL) {
                    filter {
                        eq("user_id", userId)
                    }
                }
                .decodeSingleOrNull<SystemSettings>()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createSystemSettings(settings: SystemSettings): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabase.postgrest["system_settings"].insert(settings)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateSystemSettings(settings: SystemSettings): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabase.postgrest["system_settings"].update({
                set("mqtt_broker_url", settings.mqttBrokerUrl)
                set("temperature_threshold_min", settings.temperatureThresholdMin)
                set("temperature_threshold_max", settings.temperatureThresholdMax)
                set("humidity_threshold_min", settings.humidityThresholdMin)
                set("humidity_threshold_max", settings.humidityThresholdMax)
                set("soil_moisture_threshold_min", settings.soilMoistureThresholdMin)
                set("soil_moisture_threshold_max", settings.soilMoistureThresholdMax)
                set("auto_mode_enabled", settings.autoModeEnabled)
            }) {
                filter {
                    eq("user_id", settings.userId)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

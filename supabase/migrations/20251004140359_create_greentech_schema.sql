/*
  # GreenTech Smart Farming Database Schema

  ## Overview
  This migration creates a comprehensive database schema for the GreenTech smart farming application,
  including user profiles, sensor data tracking, irrigation logs, and notification management.

  ## Tables Created

  ### 1. user_profiles
  - `id` (uuid, primary key) - Links to auth.users
  - `full_name` (text) - User's full name
  - `farm_name` (text, nullable) - Name of the user's farm
  - `farm_location` (text, nullable) - Location of the farm
  - `phone_number` (text, nullable) - Contact phone number
  - `notification_preferences` (jsonb) - User notification settings
  - `created_at` (timestamptz) - Profile creation timestamp
  - `updated_at` (timestamptz) - Last update timestamp

  ### 2. sensor_data
  - `id` (uuid, primary key) - Unique identifier
  - `user_id` (uuid, foreign key) - Links to auth.users
  - `temperature` (numeric) - Temperature reading in Celsius
  - `humidity` (numeric) - Humidity percentage
  - `soil_moisture` (numeric) - Soil moisture percentage
  - `light_intensity` (numeric, nullable) - Light intensity reading
  - `timestamp` (timestamptz) - When the reading was taken
  - `created_at` (timestamptz) - Record creation timestamp

  ### 3. irrigation_logs
  - `id` (uuid, primary key) - Unique identifier
  - `user_id` (uuid, foreign key) - Links to auth.users
  - `action` (text) - Action performed (START, STOP)
  - `mode` (text) - Mode used (AUTO, MANUAL)
  - `duration_minutes` (integer, nullable) - Duration of irrigation
  - `soil_moisture_before` (numeric, nullable) - Soil moisture before irrigation
  - `soil_moisture_after` (numeric, nullable) - Soil moisture after irrigation
  - `timestamp` (timestamptz) - When the action occurred
  - `created_at` (timestamptz) - Record creation timestamp

  ### 4. ventilation_logs
  - `id` (uuid, primary key) - Unique identifier
  - `user_id` (uuid, foreign key) - Links to auth.users
  - `action` (text) - Action performed (OPEN, CLOSE)
  - `mode` (text) - Mode used (AUTO, MANUAL)
  - `temperature_before` (numeric, nullable) - Temperature before ventilation
  - `temperature_after` (numeric, nullable) - Temperature after ventilation
  - `timestamp` (timestamptz) - When the action occurred
  - `created_at` (timestamptz) - Record creation timestamp

  ### 5. system_alerts
  - `id` (uuid, primary key) - Unique identifier
  - `user_id` (uuid, foreign key) - Links to auth.users
  - `alert_type` (text) - Type of alert (TEMPERATURE, MOISTURE, HUMIDITY, SYSTEM)
  - `severity` (text) - Severity level (INFO, WARNING, CRITICAL)
  - `message` (text) - Alert message
  - `is_read` (boolean) - Whether alert has been read
  - `resolved` (boolean) - Whether alert has been resolved
  - `timestamp` (timestamptz) - When the alert was created
  - `created_at` (timestamptz) - Record creation timestamp

  ### 6. system_settings
  - `id` (uuid, primary key) - Unique identifier
  - `user_id` (uuid, foreign key) - Links to auth.users
  - `mqtt_broker_url` (text) - MQTT broker URL
  - `temperature_threshold_min` (numeric) - Minimum temperature threshold
  - `temperature_threshold_max` (numeric) - Maximum temperature threshold
  - `humidity_threshold_min` (numeric) - Minimum humidity threshold
  - `humidity_threshold_max` (numeric) - Maximum humidity threshold
  - `soil_moisture_threshold_min` (numeric) - Minimum soil moisture threshold
  - `soil_moisture_threshold_max` (numeric) - Maximum soil moisture threshold
  - `auto_mode_enabled` (boolean) - Whether auto mode is enabled
  - `created_at` (timestamptz) - Settings creation timestamp
  - `updated_at` (timestamptz) - Last update timestamp

  ## Security
  - Enable Row Level Security (RLS) on all tables
  - Users can only access their own data
  - Policies ensure authenticated users can perform CRUD operations on their own records

  ## Indexes
  - Indexes on user_id columns for faster queries
  - Indexes on timestamp columns for time-based queries
*/

-- Create user_profiles table
CREATE TABLE IF NOT EXISTS user_profiles (
  id uuid PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
  full_name text NOT NULL,
  farm_name text,
  farm_location text,
  phone_number text,
  notification_preferences jsonb DEFAULT '{"temperature": true, "humidity": true, "soilMoisture": true, "system": true}'::jsonb,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

ALTER TABLE user_profiles ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can view own profile"
  ON user_profiles FOR SELECT
  TO authenticated
  USING (auth.uid() = id);

CREATE POLICY "Users can insert own profile"
  ON user_profiles FOR INSERT
  TO authenticated
  WITH CHECK (auth.uid() = id);

CREATE POLICY "Users can update own profile"
  ON user_profiles FOR UPDATE
  TO authenticated
  USING (auth.uid() = id)
  WITH CHECK (auth.uid() = id);

-- Create sensor_data table
CREATE TABLE IF NOT EXISTS sensor_data (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
  temperature numeric(5, 2) NOT NULL,
  humidity numeric(5, 2) NOT NULL,
  soil_moisture numeric(5, 2) NOT NULL,
  light_intensity numeric(8, 2),
  timestamp timestamptz DEFAULT now(),
  created_at timestamptz DEFAULT now()
);

ALTER TABLE sensor_data ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can view own sensor data"
  ON sensor_data FOR SELECT
  TO authenticated
  USING (auth.uid() = user_id);

CREATE POLICY "Users can insert own sensor data"
  ON sensor_data FOR INSERT
  TO authenticated
  WITH CHECK (auth.uid() = user_id);

CREATE INDEX IF NOT EXISTS idx_sensor_data_user_id ON sensor_data(user_id);
CREATE INDEX IF NOT EXISTS idx_sensor_data_timestamp ON sensor_data(timestamp DESC);

-- Create irrigation_logs table
CREATE TABLE IF NOT EXISTS irrigation_logs (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
  action text NOT NULL CHECK (action IN ('START', 'STOP')),
  mode text NOT NULL CHECK (mode IN ('AUTO', 'MANUAL')),
  duration_minutes integer,
  soil_moisture_before numeric(5, 2),
  soil_moisture_after numeric(5, 2),
  timestamp timestamptz DEFAULT now(),
  created_at timestamptz DEFAULT now()
);

ALTER TABLE irrigation_logs ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can view own irrigation logs"
  ON irrigation_logs FOR SELECT
  TO authenticated
  USING (auth.uid() = user_id);

CREATE POLICY "Users can insert own irrigation logs"
  ON irrigation_logs FOR INSERT
  TO authenticated
  WITH CHECK (auth.uid() = user_id);

CREATE INDEX IF NOT EXISTS idx_irrigation_logs_user_id ON irrigation_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_irrigation_logs_timestamp ON irrigation_logs(timestamp DESC);

-- Create ventilation_logs table
CREATE TABLE IF NOT EXISTS ventilation_logs (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
  action text NOT NULL CHECK (action IN ('OPEN', 'CLOSE')),
  mode text NOT NULL CHECK (mode IN ('AUTO', 'MANUAL')),
  temperature_before numeric(5, 2),
  temperature_after numeric(5, 2),
  timestamp timestamptz DEFAULT now(),
  created_at timestamptz DEFAULT now()
);

ALTER TABLE ventilation_logs ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can view own ventilation logs"
  ON ventilation_logs FOR SELECT
  TO authenticated
  USING (auth.uid() = user_id);

CREATE POLICY "Users can insert own ventilation logs"
  ON ventilation_logs FOR INSERT
  TO authenticated
  WITH CHECK (auth.uid() = user_id);

CREATE INDEX IF NOT EXISTS idx_ventilation_logs_user_id ON ventilation_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_ventilation_logs_timestamp ON ventilation_logs(timestamp DESC);

-- Create system_alerts table
CREATE TABLE IF NOT EXISTS system_alerts (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
  alert_type text NOT NULL CHECK (alert_type IN ('TEMPERATURE', 'MOISTURE', 'HUMIDITY', 'SYSTEM')),
  severity text NOT NULL CHECK (severity IN ('INFO', 'WARNING', 'CRITICAL')),
  message text NOT NULL,
  is_read boolean DEFAULT false,
  resolved boolean DEFAULT false,
  timestamp timestamptz DEFAULT now(),
  created_at timestamptz DEFAULT now()
);

ALTER TABLE system_alerts ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can view own alerts"
  ON system_alerts FOR SELECT
  TO authenticated
  USING (auth.uid() = user_id);

CREATE POLICY "Users can insert own alerts"
  ON system_alerts FOR INSERT
  TO authenticated
  WITH CHECK (auth.uid() = user_id);

CREATE POLICY "Users can update own alerts"
  ON system_alerts FOR UPDATE
  TO authenticated
  USING (auth.uid() = user_id)
  WITH CHECK (auth.uid() = user_id);

CREATE INDEX IF NOT EXISTS idx_system_alerts_user_id ON system_alerts(user_id);
CREATE INDEX IF NOT EXISTS idx_system_alerts_timestamp ON system_alerts(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_system_alerts_is_read ON system_alerts(is_read);

-- Create system_settings table
CREATE TABLE IF NOT EXISTS system_settings (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL UNIQUE REFERENCES auth.users(id) ON DELETE CASCADE,
  mqtt_broker_url text DEFAULT 'tcp://192.168.92.124:1883',
  temperature_threshold_min numeric(5, 2) DEFAULT 15.0,
  temperature_threshold_max numeric(5, 2) DEFAULT 35.0,
  humidity_threshold_min numeric(5, 2) DEFAULT 40.0,
  humidity_threshold_max numeric(5, 2) DEFAULT 80.0,
  soil_moisture_threshold_min numeric(5, 2) DEFAULT 30.0,
  soil_moisture_threshold_max numeric(5, 2) DEFAULT 70.0,
  auto_mode_enabled boolean DEFAULT true,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

ALTER TABLE system_settings ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can view own settings"
  ON system_settings FOR SELECT
  TO authenticated
  USING (auth.uid() = user_id);

CREATE POLICY "Users can insert own settings"
  ON system_settings FOR INSERT
  TO authenticated
  WITH CHECK (auth.uid() = user_id);

CREATE POLICY "Users can update own settings"
  ON system_settings FOR UPDATE
  TO authenticated
  USING (auth.uid() = user_id)
  WITH CHECK (auth.uid() = user_id);

CREATE INDEX IF NOT EXISTS idx_system_settings_user_id ON system_settings(user_id);

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Triggers to automatically update updated_at
CREATE TRIGGER update_user_profiles_updated_at
  BEFORE UPDATE ON user_profiles
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_system_settings_updated_at
  BEFORE UPDATE ON system_settings
  FOR EACH ROW
  EXECUTE FUNCTION update_updated_at_column();
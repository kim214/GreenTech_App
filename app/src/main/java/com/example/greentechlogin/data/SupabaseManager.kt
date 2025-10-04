package com.example.greentechlogin.data

import android.content.Context
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime

object SupabaseManager {
    private const val SUPABASE_URL = "https://dkplvcznlgoserigexez.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRrcGx2Y3pubGdvc2VyaWdleGV6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTk1ODMzMDMsImV4cCI6MjA3NTE1OTMwM30.RQ4Ip-EqJRxRVytyro76lHhwiUmg6mK9P_aLVDRGqqc"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Realtime)
    }

    val auth: Auth
        get() = client.auth

    val database
        get() = client.postgrest
}

package com.example.greentechlogin

import SupabaseClient
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button
    private lateinit var signupLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ✅ Check if launched via deep link
        intent?.data?.let { uri ->
            Log.d("DeepLink", "App opened via deep link: $uri")
            // You can also pre-fill email or add custom behavior here if needed
        }

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginBtn = findViewById(R.id.loginBtn)


        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val supabaseService = SupabaseClient.authService
            val call = supabaseService.login(AuthRequest(email, password))

            call.enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Login successful!", Toast.LENGTH_SHORT).show()

                        val token = response.body()?.access_token
                        if (token != null) {
                            val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                            prefs.edit().putString("access_token", token).apply()
                        }

                        startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, "Login failed: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Login error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            })
        }



    }
}

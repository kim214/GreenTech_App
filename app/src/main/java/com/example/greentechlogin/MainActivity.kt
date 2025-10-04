package com.example.greentechlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.greentechlogin.auth.AuthManager
import com.example.greentechlogin.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authManager = AuthManager(this)

        intent?.data?.let { uri ->
            Log.d("DeepLink", "App opened via deep link: $uri")
        }

        lifecycleScope.launch {
            if (authManager.isUserLoggedIn()) {
                startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
                finish()
                return@launch
            }
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Please fill all fields")
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        binding.signupLink.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.forgotPasswordLink.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.loginBtn.isEnabled = false

        lifecycleScope.launch {
            val result = authManager.signIn(email, password)

            binding.progressBar.visibility = View.GONE
            binding.loginBtn.isEnabled = true

            result.onSuccess {
                showToast("Login successful!")
                startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
                finish()
            }.onFailure { error ->
                showToast("Login failed: ${error.message}")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}

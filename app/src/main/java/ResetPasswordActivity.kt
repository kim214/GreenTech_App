package com.example.greentechlogin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.greentechlogin.auth.AuthManager
import kotlinx.coroutines.launch

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var resetPasswordBtn: Button
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        authManager = AuthManager(this)

        emailInput = findViewById(R.id.emailInput)
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn)

        resetPasswordBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email address.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            resetPassword(email)
        }
    }

    private fun resetPassword(email: String) {
        resetPasswordBtn.isEnabled = false

        lifecycleScope.launch {
            val result = authManager.resetPassword(email)

            resetPasswordBtn.isEnabled = true

            result.onSuccess {
                Toast.makeText(
                    this@ResetPasswordActivity,
                    "Password reset email sent! Please check your inbox.",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }.onFailure { error ->
                Toast.makeText(
                    this@ResetPasswordActivity,
                    "Failed to send reset email: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

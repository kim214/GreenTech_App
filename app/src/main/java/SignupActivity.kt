package com.example.greentechlogin

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.example.greentechlogin.auth.AuthManager
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var signupBtn: Button
    private lateinit var loginLink: TextView
    private lateinit var successAnim: LottieAnimationView
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        authManager = AuthManager(this)

        nameInput = findViewById(R.id.nameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput)
        signupBtn = findViewById(R.id.signupBtn)
        loginLink = findViewById(R.id.loginLink)
        successAnim = findViewById(R.id.successAnim)

        signupBtn.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signupUser(name, email, password)
        }

        loginLink.setOnClickListener {
            finish()
        }
    }

    private fun signupUser(name: String, email: String, password: String) {
        signupBtn.isEnabled = false

        lifecycleScope.launch {
            val result = authManager.signUp(email, password, name)

            signupBtn.isEnabled = true

            result.onSuccess {
                successAnim.visibility = View.VISIBLE
                successAnim.playAnimation()

                Toast.makeText(
                    this@SignupActivity,
                    "Signup successful! Welcome to GreenTech!",
                    Toast.LENGTH_LONG
                ).show()

                successAnim.postDelayed({
                    startActivity(Intent(this@SignupActivity, DashboardActivity::class.java))
                    finish()
                }, 2000)
            }.onFailure { error ->
                Toast.makeText(
                    this@SignupActivity,
                    "Signup failed: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
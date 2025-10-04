package com.example.greentechlogin.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.greentechlogin.data.SupabaseManager
import com.example.greentechlogin.data.models.UserProfile
import com.example.greentechlogin.data.repository.GreenTechRepository
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class AuthManager(private val context: Context) {

    private val repository = GreenTechRepository()
    private val auth = SupabaseManager.auth

    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    }

    suspend fun signUp(email: String, password: String, fullName: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            val user = auth.currentUserOrNull()
            if (user != null) {
                val profile = UserProfile(
                    id = user.id,
                    fullName = fullName
                )
                repository.createUserProfile(profile)

                saveUserData(user.id, email)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<UserInfo> = withContext(Dispatchers.IO) {
        try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val user = auth.currentUserOrNull()
            if (user != null) {
                saveUserData(user.id, email)
                Result.success(user)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.signOut()
            clearUserData()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): UserInfo? = withContext(Dispatchers.IO) {
        auth.currentUserOrNull()
    }

    suspend fun isUserLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        auth.currentUserOrNull() != null
    }

    suspend fun getUserId(): String? = withContext(Dispatchers.IO) {
        context.dataStore.data.first()[USER_ID_KEY]
    }

    suspend fun getUserEmail(): String? = withContext(Dispatchers.IO) {
        context.dataStore.data.first()[USER_EMAIL_KEY]
    }

    private suspend fun saveUserData(userId: String, email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
            preferences[USER_EMAIL_KEY] = email
        }
    }

    private suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun resetPassword(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.resetPasswordForEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

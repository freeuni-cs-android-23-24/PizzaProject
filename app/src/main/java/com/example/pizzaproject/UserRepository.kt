package com.example.pizzaproject

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserRepository {

    private val _currentUser = MutableStateFlow<PizzaUser?>(null)
    val currentUser: StateFlow<PizzaUser?> = _currentUser.asStateFlow()

    fun initialize() {
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            auth.currentUser?.let { user ->
                PizzaUser(user.displayName ?: "N/A")
            }
        }
    }

    companion object {
        private var instance: UserRepository? = null

        fun getInstance(): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository().also { instance = it }
            }
        }
    }
}


data class PizzaUser(
    val name: String,
)
package com.example.pizzaproject

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pizzaproject.ui.theme.PizzaProjectTheme
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { _ ->
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return@registerForActivityResult

        Toast.makeText(
            this,
            "Hello ${currentUser.displayName}",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setAlwaysShowSignInMethodScreen(false)
            .setIsSmartLockEnabled(false)
            .build()

        val userRepository = UserRepository.getInstance()
        userRepository.initialize()

        setContent {
            PizzaProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val user by userRepository.currentUser.collectAsState()

                    if (user == null) {
                        Text("Login first!")
                        signInLauncher.launch(signInIntent)
                    }
                    BuyPizzaScreen(
                        modifier = Modifier.padding(innerPadding),
                        onBuyPizzaClicked = {
                            Toast.makeText(
                                this,
                                "Enjoy your pizza!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BuyPizzaScreen(modifier: Modifier = Modifier, onBuyPizzaClicked: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            content = { Text("Buy pizza") },
            onClick = { onBuyPizzaClicked() }
        )
    }
}
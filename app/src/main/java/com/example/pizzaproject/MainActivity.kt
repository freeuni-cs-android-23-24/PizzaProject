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
            .build()

        setContent {
            PizzaProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BuyPizzaScreen(
                        modifier = Modifier.padding(innerPadding),
                        onBuyPizzaClicked = {
                            if (FirebaseAuth.getInstance().currentUser == null) {
                                signInLauncher.launch(signInIntent)
                            } else {
                                Toast.makeText(
                                    this,
                                    "You are already logged in",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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
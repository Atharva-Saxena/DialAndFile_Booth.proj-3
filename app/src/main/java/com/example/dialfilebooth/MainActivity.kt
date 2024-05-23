package com.example.dialfilebooth


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dialfilebooth.ContactListScreen
import com.example.dialfilebooth.MainScreen
import com.example.dialfilebooth.MainViewModel
import com.example.dialfilebooth.SecureContactListScreen

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(
                            viewModel = viewModel,
                            navigateToContactList = { navController.navigate("contact_list") },
                            navigateToSecureContactList = { navController.navigate("secure_contact_list") }
                        )
                    }
                    composable("contact_list") {
                        ContactListScreen(viewModel = viewModel)
                    }
                    composable("secure_contact_list") {
                        SecureContactListScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }

    @Composable
    fun MyTheme(content: @Composable () -> Unit) {
        val colors = lightColorScheme(
            primary = Color(0xFF4CAF50),
            secondary = Color(0xFFFF9800),
            background = Color(0xFFFFFFFF)
        )

        MaterialTheme(
            colorScheme = colors,
            content = content
        )
    }
}
@Preview (showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        viewModel = MainViewModel(),
        navigateToContactList = {},
        navigateToSecureContactList = {},

        )
}
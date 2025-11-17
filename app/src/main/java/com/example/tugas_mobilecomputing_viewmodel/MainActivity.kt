package com.example.tugas_mobilecomputing_viewmodel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tugas_mobilecomputing_viewmodel.ui.theme.Tugas_MobileComputing_ViewModelTheme
import androidx.compose.material3.ButtonDefaults

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tugas_MobileComputing_ViewModelTheme {
                AppNavigator()
            }
        }
    }
}


object AppRoutes {
    const val ITEMLIST_SCREEN = "itemlist"
    const val LOGIN_SCREEN = "login"
    const val ADD_EDIT_ITEM_SCREEN = "add_edititem"
}
@Composable
fun AppNavigator() {

    val navController = rememberNavController()
    val itemViewModel: ItemViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    NavHost(
        navController = navController,
        startDestination = com.example.tugas_mobilecomputing_viewmodel.AppRoutes.LOGIN_SCREEN
    ) {

        composable(route = com.example.tugas_mobilecomputing_viewmodel.AppRoutes.LOGIN_SCREEN) {
            Login(navController = navController)
        }

        composable("itemlist/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            SecondActivity(navController, username, itemViewModel)
        }

        composable("add_edititem/{itemId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("itemId")!!.toInt()
            AddEditItem(navController, id, itemViewModel)
        }
    }
}


@Composable
fun Login (navController: NavHostController){
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    androidx.compose.material3.Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(text = "Login", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username", color = MaterialTheme.colorScheme.onBackground) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = MaterialTheme.colorScheme.onBackground) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate("itemlist/$username")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Login",color = MaterialTheme.colorScheme.onPrimary)
        }
    }
        }
}

@Preview
@Composable
fun LoginPreview() {
    val navController = rememberNavController()
    Login(navController = navController)
}
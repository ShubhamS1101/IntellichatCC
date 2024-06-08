package com.example.afinal


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.afinal.Screens.LoginScreen
import com.example.afinal.ui.theme.FinalTheme
import androidx.navigation.compose.composable
import com.example.afinal.Screens.ChatListScreen
import com.example.afinal.Screens.SignupScreen
import dagger.hilt.android.AndroidEntryPoint


sealed class DestinationScreen(var route: String){
    object SignUp : DestinationScreen("signup")
    object Login : DestinationScreen("login")
    object Profile : DestinationScreen("profile")
    object ChatList : DestinationScreen("chatList")
    object SingleChat : DestinationScreen("singleChat/{chatId}"){
        fun createRoute(id:String) = "singlechat/$id"
    }
    object StatusList : DestinationScreen("statusList")
    object SingleStatus : DestinationScreen("singleStatus/{userId}"){
        fun createRoute(userid:String) = "singleStatus/$userid"
    }

}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigation()
                }

            }
        }
    }


    @Composable
    fun ChatAppNavigation(){

                val navController = rememberNavController()

                var vm= hiltViewModel<LCViewModel>()
                NavHost(navController = navController, startDestination = DestinationScreen.SignUp.route){

                    composable(DestinationScreen.SignUp.route){
                            SignupScreen(navController, vm)
                    }

                    composable(DestinationScreen.Login.route){
                            LoginScreen(navController, vm)
                    }
                    composable(DestinationScreen.ChatList.route){
                            ChatListScreen(navController, vm)
                    }
                }


    }

}

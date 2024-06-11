package screens

import androidx.compose.foundation.text2.input.TextFieldCharSequence
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.afinal.ui.theme.LCViewModel

@Composable
fun SingleChatScreen(navController: NavController,vm : LCViewModel,chatId: String) {
    Text(text = chatId)
}
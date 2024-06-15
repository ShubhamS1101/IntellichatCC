package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.afinal.TitleText
import com.example.afinal.ui.theme.LCViewModel
import data.ChatUser
import data.UserData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(navController: NavController, vm: LCViewModel) {

    val chats = vm.chats.value
    val userData = vm.userData.value
    val showDialog = remember { mutableStateOf(false) }
    val onFabClick: () -> Unit = { showDialog.value = true }
    val onDismiss: () -> Unit = { showDialog.value = false }
    val onAddChat: (String) -> Unit = {
        vm.onAddChat(it)
        showDialog.value = false
    }

    Scaffold(
        floatingActionButton = {
            FAB(
                showDialog = showDialog.value,
                onFabClick = onFabClick,
                onDismiss = onDismiss,
                onAddChat = onAddChat
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.background
                        )
                    ))
            ) {
                TitleText(txt = "Chats", modifier = Modifier.padding(32.dp))
                if (chats.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No Chats Available",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(chats) { chat ->
                            val chatUser = if (chat.user1.userId == userData?.userId) {
                                chat.user2
                            } else {
                                chat.user1
                            }
                            val lastMessagePreview = ""

                            ChatListItem(
                                chatUser = chatUser,
                                lastMessagePreview = lastMessagePreview,
                                onClick = {
                                    chat.chatId.let {
                                        navController.navigate("singleChat/$it")
                                    }
                                }
                            )
                        }
                    }
                BottomNavigationMenu(
                    selectedItem = BottomNavigationItem.CHATLIST,
                    navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
        }
    })
}

        @Composable
        fun ChatListItem(chatUser: ChatUser, lastMessagePreview: String, onClick: () -> Unit) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable(onClick = onClick),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // User Image or Default Image
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    ) {
                        Image(
                            painter = rememberImagePainter(chatUser.imageUrl),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                        if (chatUser.imageUrl == "") {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Default Image",
                                tint = Color.Gray,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = chatUser.name ?: "",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }




@Composable
fun FAB(
    showDialog: Boolean,
    onFabClick: () -> Unit,
    onDismiss: () -> Unit,
    onAddChat: (String) -> Unit
) {
    val addChatMember = remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss.invoke()
                addChatMember.value = ""
            },
            confirmButton = {
                Button(onClick = { onAddChat(addChatMember.value) }) {
                    Text(text = "Add Chat")
                }
            },
            title = { Text(text = "Add Chat") },
            text = {
                OutlinedTextField(
                    value = addChatMember.value,
                    onValueChange = { addChatMember.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text(text = "Enter User ID") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }

    FloatingActionButton(
        onClick = { onFabClick() },
        containerColor = MaterialTheme.colorScheme.primary,
        shape = CircleShape,
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.White)
    }
}

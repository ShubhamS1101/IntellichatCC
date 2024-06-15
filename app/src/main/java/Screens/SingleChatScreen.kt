package screens


import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.afinal.DestinationScreen
import com.example.afinal.commonDivider
import com.example.afinal.commonImage
import com.example.afinal.navigateTo
import com.example.afinal.ui.theme.LCViewModel
import data.Message

@Composable
fun SingleChatScreen(navController: NavController, vm: LCViewModel, chatId: String) {
    Log.d("SingleChatScreen", "Entering SingleChatScreen with chatId: $chatId")

    var reply by rememberSaveable { mutableStateOf("") }
    val onSendReply = {
        vm.onSendReply(chatId, reply)
        reply = ""
    }

    // Retrieve chats and current user
    val chats = vm.chats.value
    val myUser = vm.userData.value

    // Defensive check
    val currentChat = chats.firstOrNull { it.chatId == chatId }
    if (currentChat == null) {
        Log.e("SingleChatScreen", "Error: No chat found with chatId = $chatId")
        // Show some error message or handle the error gracefully
        return
    }

    // Determine the chat user
    val chatUser = if (myUser?.userId == currentChat.user1.userId) {
        currentChat.user2
    } else {
        currentChat.user1
    }

    LaunchedEffect(key1 = Unit) {
        Log.d("SingleChatScreen", "Launching effect to populate messages for chatId: $chatId")
        vm.populateMessages(chatId)
    }

    BackHandler {
        Log.d("SingleChatScreen", "BackHandler invoked, depopulating messages")
        navigateTo(navController,DestinationScreen.ChatList.route)
        vm.depopulateMessage()
    }

    Column {
        ChatHeader(name = chatUser.name ?: "", imageUrl = chatUser.imageUrl ?: "") {
            navController.popBackStack()
            vm.depopulateMessage()
        }

        MessageBox(
            modifier = Modifier.weight(1f),
            chatMessages = vm.chatMessages.value,
            currentUserId = myUser?.userId ?: ""
        )

        ReplyBox(
            reply = reply,
            onReplyChange = { reply = it },
            onSendReply = { onSendReply() },
            smartReplies = vm.smartReplies.value,
            onSmartReplyClick = { smartReply ->
                reply = smartReply
                onSendReply()
            }
        )
    }
}

@Composable
fun ReplyBox(
    reply: String,
    onReplyChange: (String) -> Unit,
    onSendReply: () -> Unit,
    smartReplies: List<String>,
    onSmartReplyClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        commonDivider()

        // Smart Replies
        LazyRow(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(smartReplies) { smartReply ->
                Button(onClick = { onSmartReplyClick(smartReply) }) {
                    Text(text = smartReply)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(value = reply, onValueChange = onReplyChange, maxLines = 3)
            Button(onClick = onSendReply) {
                Text(text = "Send")
            }
        }
    }
}

@Composable
fun MessageBox(modifier: Modifier, chatMessages: List<Message>, currentUserId: String) {
    LazyColumn(modifier = modifier) {
        items(chatMessages) { msg ->
            val alignment = if (msg.sendBy == currentUserId) Alignment.End else Alignment.Start
            val color = if (msg.sendBy == currentUserId) Color(0xFF68C400) else Color(0xFFC0C0C0)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = alignment
            ) {
                Text(
                    text = msg.message ?: "",
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .padding(12.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ChatHeader(
    name: String,
    imageUrl: String,
    onBackClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, modifier = Modifier
            .clickable {
                onBackClicked.invoke()
            }
            .padding(8.dp))
        commonImage(
            data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
        )
        Text(text = name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))
    }
}
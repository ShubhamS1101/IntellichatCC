package screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.afinal.DestinationScreen
import com.example.afinal.commonDivider
import com.example.afinal.commonImage
import com.example.afinal.navigateTo
import com.example.afinal.ui.theme.LCViewModel

@Composable
fun ProfileScreen(navController: NavController, vm: LCViewModel) {

    val userData = vm.userData.value
    var name by rememberSaveable {
        mutableStateOf(userData?.name?:"")
    }
    var number by rememberSaveable {
        mutableStateOf(userData?.number?:"")
    }
    Column {
        ProfileContent(

            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            vm = vm,
            name = name,
            number = number,
            onNameChange = {name = it},
            onNumberChange = {number = it},
            onSave = {
                     vm.createOrUpdateProfile(
                         name = name,
                         number = number
                     )
            },
            onBack = {

                     navigateTo(navController,DestinationScreen.ChatList.route)
            },
            onLogOut = {
                vm.logout()
                navigateTo(navController,DestinationScreen.Login.route)
            }

        )
        BottomNavigationMenu(
            selectedItem = BottomNavigationItem.PROFILE,
            navController = navController
        )

    }


}

@Composable
fun ProfileContent(
    modifier: Modifier,
    vm: LCViewModel,
    name: String,
    number: String,
    onNameChange:(String)->Unit,
    onNumberChange:(String)->Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onLogOut: () -> Unit
) {
    val imageurl = vm.userData.value?.imageurl

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Back",
                Modifier.clickable { onBack.invoke() }
            )
            Text(
                text = "Save",
                Modifier.clickable { onSave.invoke() }
            )
        }

        commonDivider()

        ProfileImage(imageUrl = imageurl, vm = vm)

        commonDivider()

        TextField(
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            label = { Text("Name") },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        TextField(
            value = number,
            onValueChange = onNumberChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            label = { Text("Number") },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        commonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "LogOut",
                modifier = Modifier.clickable { onLogOut.invoke() }
            )
        }
    }
}

@Composable

fun ProfileImage(imageUrl: String?, vm: LCViewModel) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            vm.uploadProfileImage(uri)
        }
    }

    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    launcher.launch("image/*")
                }, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape, modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            ) {
                commonImage(data = imageUrl)
            }
            Text(text = "Change Profile Picture")
        }
    }
}
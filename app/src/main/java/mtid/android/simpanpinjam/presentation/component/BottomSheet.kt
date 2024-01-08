package mtid.android.simpanpinjam.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(username : String, navController: NavController ,onDismiss: () -> Unit) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Text("Username : $username",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, end = 40.dp)
        ) {
            BottomSheetItem(navController = navController, route = "editanggota",
                username = username, text = "Edit Data Anggota", imageVector = Icons.Filled.AccountCircle)
            BottomSheetItem(navController = navController, route = "editsimpanan",
                username = username, text = "Edit Data Simpanan", imageVector = Icons.Filled.Money)
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun BottomSheetItem(navController: NavController, route : String, username : String, text : String, imageVector : ImageVector){
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("${route}/${username}")
            }
    ){
        Icon(imageVector = imageVector, contentDescription = null)
        Spacer(modifier = Modifier.width(15.dp))
        Text(text =  text)
    }
}


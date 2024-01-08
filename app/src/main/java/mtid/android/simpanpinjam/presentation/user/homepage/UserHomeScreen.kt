package mtid.android.simpanpinjam.presentation.user.homepage

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import mtid.android.simpanpinjam.data.remote.Supabase

@Composable
fun UserHomeScreen(
    supabase: Supabase,
    navController: NavController,
    username : String
){
    Text(text = username)
}
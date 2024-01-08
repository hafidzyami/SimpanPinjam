package mtid.android.simpanpinjam.presentation.admin.daftarajuan

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import mtid.android.simpanpinjam.data.remote.Supabase
import mtid.android.simpanpinjam.presentation.admin.navbar.NavbarAdminScreen

@Composable
fun DaftarAjuanScreen(
    supabase: Supabase,
    navController: NavController
){
    Text(text = "tes")
    NavbarAdminScreen(navController = navController)
}
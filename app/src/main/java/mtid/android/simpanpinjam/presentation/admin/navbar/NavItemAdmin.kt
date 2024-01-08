package mtid.android.simpanpinjam.presentation.admin.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItemAdmin(val route: String, val icon: ImageVector, val label: String) {
    companion object {
        val allItems: List<NavItemAdmin> by lazy {
            listOf(Home, ListAjuan, Register)
        }
    }

    object Home : NavItemAdmin("adminhome", Icons.Default.Home, "Home")
    object ListAjuan : NavItemAdmin("listajuan", Icons.Default.FormatListNumbered, "Daftar Ajuan")
    object Register : NavItemAdmin("register", Icons.Default.HowToReg, "Register Anggota")
}
package mtid.android.simpanpinjam.presentation.admin.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatListNumberedRtl
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItemAdmin(val route: String, val icon: ImageVector, val label: String) {
    companion object {
        val allItems: List<NavItemAdmin> by lazy {
            listOf(Home, AjuanSimpanan, AjuanPinjaman, Register)
        }
    }

    object Home : NavItemAdmin("adminhome", Icons.Default.Home, "Home")
    object AjuanSimpanan : NavItemAdmin("listajuansimpanan", Icons.Default.FormatListNumbered, "Ajuan Simpanan")
    object AjuanPinjaman : NavItemAdmin("listajuanpinjaman", Icons.Default.FormatListNumberedRtl, "Ajuan Pinjaman")
    object Register : NavItemAdmin("register", Icons.Default.HowToReg, "Register Anggota")
}
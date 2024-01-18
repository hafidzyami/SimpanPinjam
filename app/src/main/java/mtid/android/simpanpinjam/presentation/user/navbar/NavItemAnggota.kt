package mtid.android.simpanpinjam.presentation.user.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Money
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItemAnggota(val route: String, val icon: ImageVector, val label: String) {

    companion object {
        val itemAnggota : List<NavItemAnggota> by lazy {
            listOf(Simpanan, Pinjaman, Cicilan, Ajuan, Profile)
        }
    }

    object Simpanan : NavItemAnggota("simpanan", Icons.Default.Money, "Simpanan")
    object Pinjaman : NavItemAnggota("pinjaman", Icons.Default.AttachMoney, "Pinjaman")
    object Cicilan : NavItemAnggota("cicilan", Icons.Default.CurrencyExchange, "Cicilan")
    object Ajuan : NavItemAnggota("ajuan", Icons.Default.FormatListNumbered, "Ajuan")
    object Profile : NavItemAnggota("profile", Icons.Default.ManageAccounts, "Profile")
}
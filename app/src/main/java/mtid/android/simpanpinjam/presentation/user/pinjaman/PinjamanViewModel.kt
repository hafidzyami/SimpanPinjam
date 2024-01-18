package mtid.android.simpanpinjam.presentation.user.pinjaman

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import mtid.android.simpanpinjam.data.model.AjuanSimpanan
import mtid.android.simpanpinjam.data.model.Pinjaman
import mtid.android.simpanpinjam.data.remote.Supabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PinjamanViewModel(
    private val supabase: Supabase,
    private val username : String
) : ViewModel() {
    val client  = supabase.client
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    val date = LocalDateTime.now().format(formatter)

    fun insertPinjaman(pokokpinjaman : Long?, jangkawaktu : Int?, kategoripinjaman : String?){
        viewModelScope.launch {
            val pinjaman = Pinjaman(tanggal = date, username = username, pokokpinjaman = pokokpinjaman, saldopinjaman = pokokpinjaman,
                jangkawaktu = jangkawaktu, kategoripinjaman = kategoripinjaman)
            client.from("Pinjaman").insert(pinjaman)
        }
    }
}
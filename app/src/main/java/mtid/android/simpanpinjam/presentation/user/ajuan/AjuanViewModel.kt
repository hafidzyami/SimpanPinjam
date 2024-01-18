package mtid.android.simpanpinjam.presentation.user.ajuan

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import mtid.android.simpanpinjam.data.model.AjuanSimpanan
import mtid.android.simpanpinjam.data.model.Pinjaman
import mtid.android.simpanpinjam.data.model.Simpanan
import mtid.android.simpanpinjam.data.remote.Supabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AjuanViewModel(
    private val supabase: Supabase,
    private val username : String
) : ViewModel() {
    private val _ajuanSimpanan = mutableStateListOf<AjuanSimpanan>()
    val ajuanSimpanan : List<AjuanSimpanan> get() = _ajuanSimpanan

    private val _ajuanPinjaman = mutableStateListOf<Pinjaman>()
    val ajuanPinjaman : List<Pinjaman> get() = _ajuanPinjaman

    val client  = supabase.client

    fun getData(){
        viewModelScope.launch {
            val fetchSimpanan = client.from("AjuanSimpanan")
                .select(){
                    filter { eq("username", username) }
                }
                .decodeList<AjuanSimpanan>()
            _ajuanSimpanan.addAll(fetchSimpanan)

            val fetchPinjaman = client.from("Pinjaman")
                .select(){
                    filter { eq("username", username) }
                }
                .decodeList<Pinjaman>()
            _ajuanPinjaman.addAll(fetchPinjaman)
        }
    }

}
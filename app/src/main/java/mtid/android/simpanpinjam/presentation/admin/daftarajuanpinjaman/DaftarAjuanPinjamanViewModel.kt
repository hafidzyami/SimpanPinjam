package mtid.android.simpanpinjam.presentation.admin.daftarajuanpinjaman

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import mtid.android.simpanpinjam.data.model.Pinjaman
import mtid.android.simpanpinjam.data.remote.Supabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DaftarAjuanPinjamanViewModel(
    private val supabase: Supabase,
) : ViewModel() {
    private val _daftarAjuan = mutableStateListOf<Pinjaman>()
    val daftarAjuan: List<Pinjaman> get() = _daftarAjuan

    val client  = supabase.client
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    val date = LocalDateTime.now().format(formatter)

    fun getData(){
        viewModelScope.launch {
            val fetchAjuan = client.from("Pinjaman").select().decodeList<Pinjaman>()
            val uniqueAjuan = fetchAjuan.distinctBy { it.id}
            _daftarAjuan.clear()
            _daftarAjuan.addAll(uniqueAjuan)
        }
    }
}
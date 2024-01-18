package mtid.android.simpanpinjam.presentation.user.simpanan

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import mtid.android.simpanpinjam.data.model.AjuanSimpanan
import mtid.android.simpanpinjam.data.model.Simpanan
import mtid.android.simpanpinjam.data.remote.Supabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SimpananViewModel(
    private val supabase: Supabase,
    private val username : String
) : ViewModel(){
    private val _simpanan = mutableStateListOf<Simpanan>()
    val simpanan: List<Simpanan> get() = _simpanan
    val client  = supabase.client
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    val date = LocalDateTime.now().format(formatter)

    fun getData(){
        viewModelScope.launch {
            val fetchSimpanan = client.from("Simpanan")
                .select(){
                    filter { eq("username", username) }
                }
                .decodeList<Simpanan>()
            _simpanan.addAll(fetchSimpanan)
        }
    }

    fun ajuanWajib(wajib : Long, oldwajib : Long){
        viewModelScope.launch {
            val ajuan = AjuanSimpanan(username = username, tanggal = date, basewajib = wajib, oldbasewajib = oldwajib)
            client.from("AjuanSimpanan").insert(ajuan)
        }
    }

    fun ajuanSukarela(sukarela : Long, oldsukarela : Long){
        viewModelScope.launch {
            val ajuan = AjuanSimpanan(username = username, tanggal = date, basesukarela = sukarela, oldbasesukarela = oldsukarela)
            client.from("AjuanSimpanan").insert(ajuan)
        }
    }

    fun ajuanAmbilSukarela(sukarela : Long){
        viewModelScope.launch {
            val ajuan = AjuanSimpanan(username = username, tanggal = date, simpanansukarela = sukarela)
            client.from("AjuanSimpanan").insert(ajuan)
        }
    }
}
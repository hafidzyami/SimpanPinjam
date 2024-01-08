package mtid.android.simpanpinjam.presentation.admin.editsimpanan

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import mtid.android.simpanpinjam.data.model.Anggota
import mtid.android.simpanpinjam.data.model.LogSimpanan
import mtid.android.simpanpinjam.data.model.Simpanan
import mtid.android.simpanpinjam.data.remote.Supabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditSimpananViewModel(
    private val supabase: Supabase,
    private val username : String
) : ViewModel() {
    private val _simpanan = mutableStateListOf<Simpanan>()
    val simpanan: List<Simpanan> get() = _simpanan
    val client  = supabase.client

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

    fun updateData(pokok : Long, wajib : Long, sukarela : Long, harkop : Long){
        viewModelScope.launch {
            client.from("Simpanan").update(
                {
                    set("simpananpokok", pokok)
                    set("simpananwajib", wajib)
                    set("simpanansukarela", sukarela)
                    set("simpananharkop", harkop)
                }
            ){
                select()
                filter {
                    eq("username", username)
                }
            }
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
            val date = LocalDateTime.now().format(formatter)
            val bedapokok = pokok - (simpanan[0].simpananpokok ?: 0L)
            val bedawajib = wajib - (simpanan[0].simpananwajib ?: 0L)
            val bedasukarela = sukarela - (simpanan[0].simpanansukarela ?: 0L)
            val bedaharkop = harkop - (simpanan[0].simpananharkop ?: 0L)
            val log = LogSimpanan(username = username, tanggal = date, simpananharkop = bedaharkop,
                simpanansukarela = bedasukarela, simpananpokok = bedapokok, simpananwajib = bedawajib)
            client.from("LogSimpanan").insert(log)
        }
    }
}
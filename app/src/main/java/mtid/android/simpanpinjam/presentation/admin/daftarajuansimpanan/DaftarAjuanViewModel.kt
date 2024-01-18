package mtid.android.simpanpinjam.presentation.admin.daftarajuansimpanan

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import mtid.android.simpanpinjam.data.model.AjuanSimpanan
import mtid.android.simpanpinjam.data.model.LogSimpanan
import mtid.android.simpanpinjam.data.model.Simpanan
import mtid.android.simpanpinjam.data.remote.Supabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DaftarAjuanViewModel(
    private val supabase: Supabase,
) : ViewModel() {
    private val _daftarAjuan = mutableStateListOf<AjuanSimpanan>()
    val daftarAjuan: List<AjuanSimpanan> get() = _daftarAjuan

    val client  = supabase.client
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    val date = LocalDateTime.now().format(formatter)

    fun getData(){
        viewModelScope.launch {
            val fetchAjuan = client.from("AjuanSimpanan").select().decodeList<AjuanSimpanan>()
            val uniqueAjuan = fetchAjuan.distinctBy { it.id}
            _daftarAjuan.clear()
            _daftarAjuan.addAll(uniqueAjuan)
        }
    }
    fun tolak(id : Int){
        viewModelScope.launch {
            client.from("AjuanSimpanan").update(
                {
                    set("status", false)
                }
            ){
                select()
                filter {
                    eq("id", id)
                }
            }
        }
    }

    fun terima(id : Int, tipe : String, angka : Long?, username: String){
        viewModelScope.launch {
            client.from("AjuanSimpanan").update(
                {
                    set("status", true)
                }
            ){
                select()
                filter {
                    eq("id", id)
                }
            }
            val nilai : Long
            if(tipe == "simpanansukarela"){
                val response = client
                   .from("Simpanan").select(columns = Columns.list("simpanansukarela")) {
                       filter {
                           eq("username", username)
                       }
                   }.decodeSingle<Simpanan>()
                nilai = (response.simpanansukarela ?: 0L) - (angka ?: 0L)

                val logSimpanan : LogSimpanan = LogSimpanan(username = username, tanggal = date, simpanansukarela = -angka!!)
                client.from("LogSimpanan").insert(logSimpanan)
            }
            else{
                nilai = angka ?: 0L
            }
            client.from("Simpanan").update(
                {
                    set(tipe, nilai)
                }
            ){
                select()
                filter {
                    eq("username", username)
                }
            }
        }
    }
}
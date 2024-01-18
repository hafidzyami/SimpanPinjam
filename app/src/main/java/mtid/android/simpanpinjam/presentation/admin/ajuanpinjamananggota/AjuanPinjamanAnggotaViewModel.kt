package mtid.android.simpanpinjam.presentation.admin.ajuanpinjamananggota

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import mtid.android.simpanpinjam.data.model.Cicilan
import mtid.android.simpanpinjam.data.model.Pinjaman
import mtid.android.simpanpinjam.data.remote.Supabase

class AjuanPinjamanAnggotaViewModel(
    supabase: Supabase
) : ViewModel() {
    private val _daftarAjuan = mutableStateListOf<Pinjaman>()
    val daftarAjuan: List<Pinjaman> get() = _daftarAjuan

    private val _daftarCicilan = mutableStateListOf<Cicilan>()
    val daftarCicilan: List<Cicilan> get() = _daftarCicilan

    val client  = supabase.client

    fun getData(id : Int){
        viewModelScope.launch {
            val fetchAjuan = client.from("Pinjaman").select(){
                filter {
                    eq("id", id)
                }
            }.decodeList<Pinjaman>()
            _daftarAjuan.addAll(fetchAjuan)

            val fetchCicilan = client.from("Cicilan").select(){
                filter {
                    eq("idpinjaman", id)
                }
            }.decodeList<Cicilan>()
            _daftarCicilan.addAll(fetchCicilan)
        }
    }

    fun updateStatus(status : Boolean, id : Int){
        viewModelScope.launch {
            client.from("Pinjaman").update(
                {
                    set("isdisetujui", status)
                }
            ){
                select()
                filter {
                    eq("id", id)
                }
            }
        }
    }

    fun bayarCicilan(idPinjaman : Int, username : String, tanggal : String, saldopinjaman : Long){
        viewModelScope.launch {
            val cicilan = Cicilan(idpinjaman = idPinjaman, username = username, tanggal = tanggal, status = true)
            client.from("Cicilan").insert(cicilan)

            client.from("Pinjaman").update(
                {
                    set("saldopinjaman", saldopinjaman)
                }
            ){
                select()
                filter {
                    eq("id", idPinjaman)
                }
            }
        }
    }
}
package mtid.android.simpanpinjam.presentation.admin.editanggota

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mtid.android.simpanpinjam.data.model.Anggota
import mtid.android.simpanpinjam.data.remote.Supabase

class EditAnggotaViewModel(
    private val supabase: Supabase,
    private val username : String
) : ViewModel(){

    private val _anggota = mutableStateListOf<Anggota>()
    val anggota: List<Anggota> get() = _anggota
    val client  = supabase.client

    fun getData(){
        viewModelScope.launch {
            _anggota.addAll(client
                .from("Anggota")
                .select(){
                    filter { eq("username", username) }
                }
                .decodeList<Anggota>()
            )
        }
    }

    fun updateData(
        nomor : String,
        nama : String,
        departemen : String,
        kantorcabang : String,
        tanggalmasuk : String,
        tanggalkeluar : String,
        email : String
    ){
        viewModelScope.launch {
            client.from("Anggota").update(
                {
                    set("nomor", nomor)
                    set("nama", nama)
                    set("departemen", departemen)
                    set("kantorcabang", kantorcabang)
                    set("tanggalmasuk", tanggalmasuk)
                    set("tanggalkeluar", tanggalkeluar)
                    set("email", email)
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
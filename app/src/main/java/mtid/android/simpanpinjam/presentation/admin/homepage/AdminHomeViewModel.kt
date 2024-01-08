package mtid.android.simpanpinjam.presentation.admin.homepage

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import mtid.android.simpanpinjam.data.model.Anggota
import mtid.android.simpanpinjam.data.model.UsersLogin
import mtid.android.simpanpinjam.data.remote.Supabase

class AdminHomeViewModel(
    private val supabase: Supabase
) : ViewModel() {

    private val _anggota = mutableStateListOf<Anggota>()
    val anggota: List<Anggota> get() = _anggota

    fun getData(){
        viewModelScope.launch {
            val client  = supabase.client
            val fetchAnggota = client.from("Anggota").select().decodeList<Anggota>()
            val uniqueAnggota = fetchAnggota.distinctBy { it.username }
            _anggota.clear()
            _anggota.addAll(uniqueAnggota)
        }
    }
}
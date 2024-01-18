package mtid.android.simpanpinjam.presentation.user.profile

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import mtid.android.simpanpinjam.data.model.Anggota
import mtid.android.simpanpinjam.data.remote.Supabase

class ProfileViewModel(
    private val supabase: Supabase,
    private val username : String
) : ViewModel() {

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
}
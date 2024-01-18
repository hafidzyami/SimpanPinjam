package mtid.android.simpanpinjam.presentation.user.cicilan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import mtid.android.simpanpinjam.data.model.Pinjaman
import mtid.android.simpanpinjam.data.model.Simpanan
import mtid.android.simpanpinjam.data.remote.Supabase

class CicilanViewModel(
    private val supabase: Supabase
) : ViewModel(){
    private val _pinjaman = mutableListOf<Pinjaman>()
    val pinjaman : List<Pinjaman> get() = _pinjaman
    val client  = supabase.client

    fun getData(username : String){
        viewModelScope.launch {
            val fetch = client.from("Pinjaman")
                .select(){
                    filter {
                        eq("username", username)
                        eq("isdisetujui", true)
                    }
                }
                .decodeList<Pinjaman>()
            val unique = fetch.distinctBy { it.id }
            _pinjaman.clear()
            _pinjaman.addAll(unique)
        }
    }
}
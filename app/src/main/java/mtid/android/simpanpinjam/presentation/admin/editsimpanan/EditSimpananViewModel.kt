package mtid.android.simpanpinjam.presentation.admin.editsimpanan

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import mtid.android.simpanpinjam.data.model.Anggota
import mtid.android.simpanpinjam.data.model.Simpanan
import mtid.android.simpanpinjam.data.remote.Supabase

class EditSimpananViewModel(
    private val supabase: Supabase,
    private val username : String
) : ViewModel() {
    private val _simpanan = mutableStateListOf<Simpanan>()
    val simpanan: List<Simpanan> get() = _simpanan

    fun getData(){
        viewModelScope.launch {
            val client  = supabase.client
            val fetchSimpanan = client.from("Simpanan")
                .select(){
                    filter { eq("username", username) }
                }
                .decodeList<Simpanan>()
            _simpanan.addAll(fetchSimpanan)
        }
    }
}
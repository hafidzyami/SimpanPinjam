package mtid.android.simpanpinjam.presentation.admin.historysimpanan

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import mtid.android.simpanpinjam.data.model.AjuanSimpanan
import mtid.android.simpanpinjam.data.model.LogSimpanan
import mtid.android.simpanpinjam.data.remote.Supabase


class HistorySimpananViewModel(
    private val supabase: Supabase,
    private val username : String
) : ViewModel() {
    private val _logSimpanan = mutableStateListOf<LogSimpanan>()
    val logSimpanan: List<LogSimpanan> get() = _logSimpanan
    val client  = supabase.client

    fun getData(){
        viewModelScope.launch {
            val fetchLog = client.from("LogSimpanan").select(){
                filter {
                    eq("username", username)
                }
            }.decodeList<LogSimpanan>()
            val uniqueLog = fetchLog.distinctBy { it.id}
            _logSimpanan.clear()
            _logSimpanan.addAll(uniqueLog)
        }
    }
}
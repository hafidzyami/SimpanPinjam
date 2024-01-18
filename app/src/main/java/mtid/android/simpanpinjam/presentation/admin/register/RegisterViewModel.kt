package mtid.android.simpanpinjam.presentation.admin.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import mtid.android.simpanpinjam.data.model.Anggota
import mtid.android.simpanpinjam.data.model.Simpanan
import mtid.android.simpanpinjam.data.model.UsersLogin
import mtid.android.simpanpinjam.data.remote.Supabase

class RegisterViewModel(
    private val supabase : Supabase
) : ViewModel() {

    var isUnameFound by mutableStateOf(false)
        private set

    var isPWMatch by mutableStateOf(false)
        private set



    private val _usersLogins = mutableStateListOf<UsersLogin>()
    val usersLogins: List<UsersLogin> get() = _usersLogins

    fun getData(){
        viewModelScope.launch {
            val client  = supabase.client
            _usersLogins.addAll(client.from("UsersLogin").select().decodeList<UsersLogin>())
        }
    }

    fun register(username : String, password : String, cpassword : String,
                 users : List<UsersLogin>, basewajib : Long?, basesukarela : Long?){
        val matchingUser = users.find { it.username == username}
        if(matchingUser != null){
            isUnameFound = true
        }
        else{
            isUnameFound = false
            if(password != cpassword){
                isPWMatch = false
            }
            else{
                isPWMatch = true
                viewModelScope.launch {
                    val data = UsersLogin(username = username, password =  password, isAdmin = false)
                    val dataAnggota = Anggota(username = username)
                    val dataSimpanan = Simpanan(username = username, simpananpokok = 250000,
                        basewajib = basewajib, basesukarela = basesukarela)
                    val client = supabase.client
                    client.from("UsersLogin").insert(data)
                    client.from("Anggota").insert(dataAnggota)
                    client.from("Simpanan").insert(dataSimpanan)
                }
            }
        }
    }
}
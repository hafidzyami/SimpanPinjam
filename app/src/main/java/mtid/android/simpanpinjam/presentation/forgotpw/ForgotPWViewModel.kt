package mtid.android.simpanpinjam.presentation.forgotpw

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import mtid.android.simpanpinjam.data.model.UsersLogin
import mtid.android.simpanpinjam.data.remote.Supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

class ForgotPWViewModel(
    private val supabase : Supabase,
) : ViewModel() {

    var isUnameMatch by mutableStateOf(false)
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

    fun resetPassword(username : String, password : String, cpassword : String, users : List<UsersLogin>){
        val matchingUser = users.find { it.username == username}
        if(matchingUser != null){
            isUnameMatch = true
            if(password == cpassword){
                isPWMatch = true
                viewModelScope.launch {
                    val client  = supabase.client
                    client.from("UsersLogin").update(
                        {
                            set("password", password)
                        }
                    ){
                        select()
                        filter {
                            eq("username", username)
                        }
                    }
                }
            }
            else{
                isPWMatch = false
            }
        }
        else{
            isUnameMatch = false
        }
    }
}
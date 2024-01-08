package mtid.android.simpanpinjam.presentation.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import mtid.android.simpanpinjam.data.model.UsersLogin
import mtid.android.simpanpinjam.data.remote.Supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue

class LoginViewModel(
    private val supabase : Supabase
): ViewModel() {
    private val _usersLogins = mutableStateListOf<UsersLogin>()
    val usersLogins: List<UsersLogin> get() = _usersLogins

    var loginflag by mutableStateOf(false)
        private set

    var isAdmin by mutableStateOf(false)
        private set

    fun getData(){
        viewModelScope.launch {
            val client  = supabase.client
            _usersLogins.addAll(client.from("UsersLogin").select().decodeList<UsersLogin>())
        }
    }

    fun auth(username : String, password : String, users : List<UsersLogin> = _usersLogins){
        val matchingUser = users.find { it.username == username && it.password == password }
        if (matchingUser != null) {
            loginflag = true
            if (matchingUser.isAdmin) {
                isAdmin = true
                // navController.navigate("adminhome")
            } else {
                isAdmin = false
                // navController.navigate("userhome")
            }
        } else {
            loginflag = false
        }
    }
}
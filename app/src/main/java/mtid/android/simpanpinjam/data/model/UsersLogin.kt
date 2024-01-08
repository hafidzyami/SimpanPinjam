package mtid.android.simpanpinjam.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UsersLogin(
    val id : Int = 0,
    val username : String = "",
    val password : String = "",
    val isAdmin : Boolean
)

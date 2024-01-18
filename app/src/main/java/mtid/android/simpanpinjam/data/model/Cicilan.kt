package mtid.android.simpanpinjam.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Cicilan(
    val id : Int = 0,
    val idpinjaman : Int = 0,
    val username : String = "",
    val tanggal : String = "",
    val status : Boolean = false
)

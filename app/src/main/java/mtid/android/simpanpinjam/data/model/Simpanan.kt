package mtid.android.simpanpinjam.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Simpanan(
    val id : Int = 0,
    val username : String = "",
    val simpananpokok : Int? = 0,
    val simpananwajib: Int? = 0,
    val simpanansukarela: Int? = 0,
    val simpananharkop : Int? = 0,
    val basewajib : Int? = 0,
    val basesukarela : Int? = 0,
)
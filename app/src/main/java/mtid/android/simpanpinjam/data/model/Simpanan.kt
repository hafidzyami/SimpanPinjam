package mtid.android.simpanpinjam.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Simpanan(
    val id : Int = 0,
    val username : String = "",
    val simpananpokok : Long? = 0,
    val simpananwajib: Long? = 0,
    val simpanansukarela: Long? = 0,
    val simpananharkop : Long? = 0,
    val simpananqurban : Long? = 0,
    val basewajib : Long? = 0,
    val basesukarela : Long? = 0,
)
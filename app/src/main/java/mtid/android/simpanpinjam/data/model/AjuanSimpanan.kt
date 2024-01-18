package mtid.android.simpanpinjam.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AjuanSimpanan(
    val id : Int = 0,
    val username : String = "",
    val tanggal : String = "",
    val oldbasewajib : Long? = 0,
    val basewajib : Long? = 0,
    val oldbasesukarela : Long? = 0,
    val basesukarela : Long? = 0,
    val status : Boolean? = null,
    val simpanansukarela : Long? = 0
)

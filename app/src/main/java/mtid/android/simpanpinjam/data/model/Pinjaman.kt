package mtid.android.simpanpinjam.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Pinjaman(
    val id : Int = 0,
    val username : String = "",
    val tanggal : String? = "",
    val pokokpinjaman : Long? = 0L,
    val saldopinjaman : Long? = 0L,
    val jangkawaktu : Int? = 0,
    val kategoripinjaman : String? = "",
    val isdisetujui : Boolean? = null
)
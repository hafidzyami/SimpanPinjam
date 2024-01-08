package mtid.android.simpanpinjam.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Anggota(
    val id : Int = 0,
    val nomor : String? = "",
    val nama : String? = "",
    val departemen : String? = "",
    val kantorcabang : String? = "",
    val tanggalmasuk : String? = "",
    val tanggalkeluar : String? = "",
    val email : String? = "",
    val username : String = ""
)

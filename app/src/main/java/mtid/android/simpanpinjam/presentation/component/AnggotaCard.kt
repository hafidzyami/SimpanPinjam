package mtid.android.simpanpinjam.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mtid.android.simpanpinjam.data.model.Anggota

@Composable
fun AnggotaCard(anggota: Anggota, onCardClick : () -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, top = 20.dp, end = 15.dp, bottom = 10.dp)
            .clickable { onCardClick() }
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            AnggotaCardItem("Username", anggota.username)
            AnggotaCardItem("Nomor", anggota.nomor ?: "N/A")
            AnggotaCardItem("Nama", anggota.nama ?: "N/A")
            AnggotaCardItem("Departemen", anggota.departemen ?: "N/A")
            AnggotaCardItem("Kantor Cabang", anggota.kantorcabang ?: "N/A")
            AnggotaCardItem("Tanggal Masuk", anggota.tanggalmasuk ?: "N/A")
            AnggotaCardItem("Tanggal Keluar", anggota.tanggalkeluar ?: "N/A")
            AnggotaCardItem("Email", anggota.email ?: "N/A")
        }
    }
}

@Composable
fun AnggotaCardItem(
    label : String,
    value : String
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        Text("$label", modifier = Modifier.weight(1.5f))
        Text(":", modifier = Modifier.weight(0.1f))
        Text("$value", modifier = Modifier.weight(2f))
    }
}
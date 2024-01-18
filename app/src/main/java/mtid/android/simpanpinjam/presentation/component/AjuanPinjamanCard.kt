package mtid.android.simpanpinjam.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mtid.android.simpanpinjam.data.model.Pinjaman
import mtid.android.simpanpinjam.presentation.functions.formatNumber

@Composable
fun AjuanPinjamanCard(ajuanPinjaman: Pinjaman, onCardClick : (Int) -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, top = 20.dp, end = 15.dp, bottom = 10.dp)
            .clickable { onCardClick(ajuanPinjaman.id) }
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Ajuan Pinjaman")
            Spacer(modifier = Modifier.height(10.dp))
            AjuanPinjamanItem(label = "Username", value = ajuanPinjaman.username)
            AjuanPinjamanItem(label = "Tanggal", value = ajuanPinjaman.tanggal ?: "N/A")
            AjuanPinjamanItem(label = "Kategori", value = ajuanPinjaman.kategoripinjaman ?: "N/A")
            AjuanPinjamanItem(label = "Nilai", value = "Rp " + formatNumber(ajuanPinjaman.pokokpinjaman ?: 0L))
            AjuanPinjamanItem(label = "Saldo", value = "Rp " + formatNumber(ajuanPinjaman.saldopinjaman ?: 0L))
            AjuanPinjamanItem(label = "Jangka Waktu", value = ajuanPinjaman.jangkawaktu.toString() +" Bulan")
            if(ajuanPinjaman.isdisetujui == null){
                AjuanSimpanantem(label = "Status", value = "Menunggu Konfirmasi")
            }
            else if(ajuanPinjaman.isdisetujui == false){
                AjuanSimpanantem(label = "Status", value = "Ditolak")
            }
            else{
                AjuanSimpanantem(label = "Status", value = "Disetujui")
            }
        }
    }
}

@Composable
fun AjuanPinjamanItem(
    label : String,
    value : String
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        Text("$label", modifier = Modifier.weight(1.1f))
        Text(":", modifier = Modifier.weight(0.1f))
        Text("$value", modifier = Modifier.weight(2f))
    }
}
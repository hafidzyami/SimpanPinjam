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
import mtid.android.simpanpinjam.data.model.AjuanSimpanan
import mtid.android.simpanpinjam.presentation.functions.formatNumber

@Composable
fun AjuanSimpananCard(ajuanSimpanan: AjuanSimpanan) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, top = 20.dp, end = 15.dp, bottom = 10.dp)
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            if(ajuanSimpanan.basesukarela != null){
                Text(text = "Ajuan Perubahan Nilai Simpanan Sukarela")
            }
            else if(ajuanSimpanan.basesukarela == null && ajuanSimpanan.basewajib == null){
                Text(text = "Ajuan Pengambilan Simpanan Sukarela")
            }
            else{
                Text(text = "Ajuan Perubahan Nilai Simpanan Wajib")
            }
            Spacer(modifier = Modifier.height(10.dp))
            AjuanSimpanantem("Tanggal", ajuanSimpanan.tanggal)
            if(ajuanSimpanan.basesukarela != null){
                AjuanSimpanantem("Nilai Ajuan","Rp "+  formatNumber( ajuanSimpanan.basesukarela))
            }
            else if(ajuanSimpanan.basesukarela == null && ajuanSimpanan.basewajib == null){
                AjuanSimpanantem("Nilai Ajuan","Rp "+  formatNumber( ajuanSimpanan.simpanansukarela ?: 0L))
            }
            else{
                AjuanSimpanantem("Nilai Ajuan", "Rp "+ formatNumber( ajuanSimpanan.basewajib ?: 0L))
            }
            if(ajuanSimpanan.status == null){
                AjuanSimpanantem("Status", "Menunggu Konfirmasi")
            }
            else if(ajuanSimpanan.status == true){
                AjuanSimpanantem("Status", "Diterima")
            }
            else{
                AjuanSimpanantem("Status", "Ditolak")
            }
        }
    }
}

@Composable
fun AjuanSimpanantem(
    label : String,
    value : String
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        Text("$label", modifier = Modifier.weight(1f))
        Text(":", modifier = Modifier.weight(0.1f))
        Text("$value", modifier = Modifier.weight(2f))
    }
}
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
fun AjuanSimpananCardAdmin(ajuanSimpanan: AjuanSimpanan, onCardClick : () -> Unit) {
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
            AjuanSimpananCardAdminItem("Username", ajuanSimpanan.username)
            if(ajuanSimpanan.basesukarela != null){
                AjuanSimpananCardAdminItem("Tipe", "Perubahan Simpanan Sukarela")
                AjuanSimpananCardAdminItem("Nilai Lama", "Rp " + formatNumber( ajuanSimpanan.oldbasesukarela ?: 0L))
                AjuanSimpananCardAdminItem("Nilai Ajuan", "Rp " + formatNumber( ajuanSimpanan.basesukarela))
            }
            else if (ajuanSimpanan.basewajib != null){
                AjuanSimpananCardAdminItem("Tipe", "Perubahan Simpanan Wajib")
                AjuanSimpananCardAdminItem("Nilai Lama", "Rp " + formatNumber( ajuanSimpanan.oldbasewajib ?: 0L))
                AjuanSimpananCardAdminItem("Nilai Ajuan", "Rp " + formatNumber( ajuanSimpanan.basewajib))
            }
            else{
                AjuanSimpananCardAdminItem("Tipe", "Pengambilan Simpanan Sukarela")
                AjuanSimpananCardAdminItem("Nilai", "Rp " + formatNumber( ajuanSimpanan.simpanansukarela?: 0L))
            }
            AjuanSimpananCardAdminItem("Tanggal", ajuanSimpanan.tanggal)
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
fun AjuanSimpananCardAdminItem(
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
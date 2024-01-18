package mtid.android.simpanpinjam.presentation.component

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
import mtid.android.simpanpinjam.data.model.LogSimpanan
import mtid.android.simpanpinjam.presentation.functions.formatNumber

@Composable
fun HistorySimpananCard(logSimpanan: LogSimpanan) {
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
            Text(text = logSimpanan.tanggal)
            Spacer(modifier = Modifier.height(10.dp))
            LogSimpananItem(label = "Simpanan Wajib", value = "Rp " + formatNumber(logSimpanan.simpananharkop ?: 0L))
            LogSimpananItem(label = "Simpanan Sukarela", value = "Rp " + formatNumber(logSimpanan.simpanansukarela ?: 0L))
            LogSimpananItem(label = "Simpanan Harkop", value = "Rp " + formatNumber(logSimpanan.simpananharkop ?: 0L))
            LogSimpananItem(label = "Simpanan Qurban", value = "Rp " + formatNumber(logSimpanan.simpananqurban ?: 0L))
        }
    }
}

@Composable
fun LogSimpananItem(
    label : String,
    value : String
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        Text("$label", modifier = Modifier.weight(1.3f))
        Text(":", modifier = Modifier.weight(0.1f))
        Text("$value", modifier = Modifier.weight(1f))
    }
}
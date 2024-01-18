package mtid.android.simpanpinjam.presentation.user.pinjaman

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mtid.android.simpanpinjam.data.remote.Supabase
import mtid.android.simpanpinjam.presentation.functions.formatNumber
import mtid.android.simpanpinjam.presentation.user.navbar.NavbarScreenAnggota
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.pow


@Composable
fun PinjamanScreen(
    supabase: Supabase,
    navController: NavController,
    username : String
){

    val viewModel = viewModel<PinjamanViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PinjamanViewModel(supabase, username) as T
            }
        }
    )

    var pokokPinjaman by rememberSaveable { mutableStateOf(0L) }
    var selectedJenis by rememberSaveable { mutableStateOf("Uang") }
    var jangkaWaktu by rememberSaveable { mutableStateOf(0) }
    var buttonClicked by rememberSaveable { mutableStateOf(false) }
    var ajukanClicked by rememberSaveable { mutableStateOf(false) }
    var isZeroBulan by rememberSaveable { mutableStateOf(false) }

    if(ajukanClicked){
        Toast.makeText(LocalContext.current, "Berhasil Ajukan Pinjaman", Toast.LENGTH_SHORT).show()
        navController.navigate("ajuan/${username}" )
    }

    Column(
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )
        {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Pinjaman Anggota $username",
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(20.dp))
                DropdownPinjaman(jenis = selectedJenis, onSelectedTextChange = {selectedJenis = it})
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    label = { Text(text = "Pokok Pinjaman") },
                    value = pokokPinjaman.toString(),
                    onValueChange = { if(it.isNullOrBlank()) pokokPinjaman = 0L else pokokPinjaman = it.toLong() },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    label = { Text(text = "Jangka Waktu (Bulan)") },
                    value = jangkaWaktu.toString(),
                    onValueChange = { if(it.isNullOrBlank()) jangkaWaktu = 0 else jangkaWaktu = it.toInt() },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    isError = isZeroBulan,
                    supportingText = {if(isZeroBulan){
                        Text(text = "Jangka waktu tidak boleh bernilai 0 ")
                    } }
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Row{
                        Button(onClick = {
                            if(jangkaWaktu == 0){isZeroBulan = true}
                            else {isZeroBulan = false
                            buttonClicked = !buttonClicked } },
                            modifier = Modifier.padding(end = 10.dp)) {
                            Text(text = "Simulasi Pinjaman")
                        }
                        Button(onClick = {
                            if(jangkaWaktu == 0){isZeroBulan = true}
                            else {isZeroBulan = false
                            viewModel.insertPinjaman(pokokPinjaman, jangkaWaktu, selectedJenis).also{
                            ajukanClicked = true}
                        }},
                            modifier = Modifier.padding(start = 10.dp)){
                            Text(text = "Ajukan Pinjaman")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                if(buttonClicked){
                    Row(
                        Modifier
                            .background(MaterialTheme.colorScheme.onBackground)
                            .padding(top = 5.dp, bottom = 5.dp)) {
                        Text(text = "Bulan", modifier = Modifier
                            .weight(0.13f)
                            .padding(start = 5.dp), fontSize = 10.sp, color = MaterialTheme.colorScheme.inverseOnSurface)
                        Text(text = "Pinjaman", modifier = Modifier.weight(0.18f), fontSize = 10.sp, color = MaterialTheme.colorScheme.inverseOnSurface)
                        Text(text = "Cicilan", modifier = Modifier.weight(0.15f), fontSize = 10.sp, color = MaterialTheme.colorScheme.inverseOnSurface)
                        Text(text = "Bunga", modifier = Modifier.weight(0.15f), fontSize = 10.sp, color = MaterialTheme.colorScheme.inverseOnSurface)
                        Text(text = "Angsuran", modifier = Modifier.weight(0.15f), fontSize = 10.sp, color = MaterialTheme.colorScheme.inverseOnSurface)
                        Text(text = "Saldo", modifier = Modifier
                            .weight(0.18f)
                            .padding(start = 5.dp), fontSize = 10.sp, color = MaterialTheme.colorScheme.inverseOnSurface)
                    }
                    LazyColumn(modifier = Modifier.weight(1f)){
                        var list = simulasiPinjaman(jangkaWaktu, pokokPinjaman, if(selectedJenis == "Uang") 1 else 2)
                        itemsIndexed(list){index, item ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)){
                                Text(text = item.tanggal, modifier = Modifier.weight(0.13f), fontSize = 10.sp)
                                Text(text = formatNumber(item.pokokpinjaman), modifier = Modifier.weight(0.18f), fontSize = 10.sp)
                                Text(text = formatNumber(item.cicilan), modifier = Modifier.weight(0.15f), fontSize = 10.sp)
                                Text(text = formatNumber(item.bunga), modifier = Modifier.weight(0.15f), fontSize = 10.sp)
                                Text(text = formatNumber(item.angsuran), modifier = Modifier.weight(0.15f), fontSize = 10.sp)
                                Text(text = formatNumber(item.saldo), modifier = Modifier
                                    .weight(0.18f)
                                    .padding(start = 5.dp), fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
        NavbarScreenAnggota(navController, username)
    }
}

data class simulasiPinjamanData(
    val tanggal : String,
    val pokokpinjaman : Long,
    val cicilan : Long,
    val bunga : Long,
    val angsuran : Long,
    val saldo : Long
)


fun simulasiPinjaman(jangkawaktu : Int, pokokpinjaman: Long, kategori : Int) : List<simulasiPinjamanData>{
    val simPinList : MutableList<simulasiPinjamanData> = mutableListOf()
    val formatter = DateTimeFormatter.ofPattern("MM yyyy")
    val today = LocalDate.now()
    val baseMonth = LocalDate.of(today.year, today.month, today.dayOfMonth)
    var nextMonth = LocalDate.from(baseMonth).plusMonths(1)

    val bungaBulan : Float  = 2.1f / 100

    if(kategori == 1){
        var pokokpinjamanTemp = pokokpinjaman
        val cicilan = pokokpinjaman / jangkawaktu
        val bunga = pokokpinjaman * bungaBulan
        val angsuran = cicilan + bunga
        for (i in 1..jangkawaktu){
            var saldo = pokokpinjamanTemp - cicilan
            simPinList.add(simulasiPinjamanData(
                nextMonth.format(formatter),
                pokokpinjamanTemp,
                cicilan,
                bunga.toLong(),
                angsuran.toLong(),
                saldo
            ))
            pokokpinjamanTemp = saldo
            nextMonth = nextMonth.plusMonths(1)
        }
    }
    else{

    }
    return simPinList
}

fun ppmt(rate: Double, per: Int, nper: Int, pv: Double, fv: Double): Double {
    val pmt: Double = -pv * rate / (1 - rate.pow(-nper.toDouble()))
    val type = 1
    val interest: Double = if (type == 1) {
        pv * rate * (1 - (1 + rate).pow(per - 1)) / (1 - (1 + rate).pow(-nper.toDouble()))
    } else {
        pv * rate * (1 - (1 + rate).pow(per - 1)) / (1 - (1 + rate).pow(-nper.toDouble())) - pmt
    }
    return interest
}

@Composable
fun DropdownPinjaman(jenis : String, onSelectedTextChange: (String) -> Unit){
    var mExpanded by remember { mutableStateOf(false) }
    val mStatus = listOf("Uang", "Barang", "Kendaraan", "Pendidikan")
    var mSelectedText by rememberSaveable { mutableStateOf(jenis) }

    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    Column{
        OutlinedTextField(
            value = mSelectedText,
            onValueChange = { mSelectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    mTextFieldSize = coordinates.size.toSize()
                },
            label = { Text("Kategori Pinjaman") },
            readOnly = true,
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { mExpanded = !mExpanded })
            }
        )
        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
        ) {
            mStatus.forEach { label ->
                DropdownMenuItem(
                    { Text(text = label) },
                    onClick = {
                        onSelectedTextChange(label)
                        mSelectedText = label
                        mExpanded = false
                    })
            }
        }
    }
}
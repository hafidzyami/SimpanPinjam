package mtid.android.simpanpinjam.presentation.admin.ajuanpinjamananggota
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mtid.android.simpanpinjam.data.remote.Supabase
import mtid.android.simpanpinjam.presentation.admin.navbar.NavbarAdminScreen
import mtid.android.simpanpinjam.presentation.component.AjuanPinjamanCard
import mtid.android.simpanpinjam.presentation.functions.formatNumber
import mtid.android.simpanpinjam.presentation.user.navbar.NavbarScreenAnggota
import mtid.android.simpanpinjam.presentation.user.pinjaman.simulasiPinjaman
import mtid.android.simpanpinjam.presentation.user.pinjaman.simulasiPinjamanData

@Composable
fun AjuanPinjamanAnggotaScreen(
    supabase: Supabase,
    navController: NavController,
    id : Int,
    isAdmin : String,
    username : String
){
    val viewModel = viewModel<AjuanPinjamanAnggotaViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AjuanPinjamanAnggotaViewModel(supabase) as T
            }
        }
    )

    val daftarAjuan by rememberUpdatedState(viewModel.daftarAjuan)
    val daftarCicilan by rememberUpdatedState(viewModel.daftarCicilan)
    var isClick by rememberSaveable { mutableStateOf(false) }
    var tanggal by rememberSaveable { mutableStateOf("") }
    var saldo by rememberSaveable { mutableStateOf(0L) }
    var isBayar by rememberSaveable { mutableStateOf(false) }

    if(isBayar){
        isBayar = false
        Toast.makeText(LocalContext.current, "Berhasil bayar cicilan!", Toast.LENGTH_SHORT).show()
        navController.navigate("adminhome")
    }

    LaunchedEffect(true){
        viewModel.getData(id)
    }

    if(isClick){
        Toast.makeText(LocalContext.current, "Ajuan berhasil diupdate!", Toast.LENGTH_SHORT).show()
        navController.navigate("listajuanpinjaman")
    }

    if(daftarAjuan.isEmpty() && daftarCicilan.isEmpty()){
        Column {
            Box(modifier = Modifier.weight(1f)){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "Loading...")
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = { viewModel.getData(id)} ) {
                        Text(text = "Refresh")
                    }
                }
            }
            if(isAdmin == "True"){
                NavbarAdminScreen(navController)
            }
            else{
                NavbarScreenAnggota(navController, username)
            }
        }
    }
    else{
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
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    AjuanPinjamanCard(ajuanPinjaman = daftarAjuan[0], onCardClick = {})
                    if(daftarAjuan[0].isdisetujui == null && isAdmin == "True"){
                        Row(modifier = Modifier.padding(start = 20.dp, top = 20.dp)) {
                            Button(onClick = {viewModel.updateStatus(false, id).also { isClick = true }}, modifier = Modifier.padding(end=10.dp)){
                                Text(text = "Tolak")
                            }
                            Button(onClick = { viewModel.updateStatus(true, id).also { isClick = true }}, modifier = Modifier.padding(start=10.dp)) {
                                Text(text = "Terima")
                            }
                        }   
                    }
                    else{
                        if(tanggal != "" && isAdmin == "True"){
                            Button(onClick = { viewModel.bayarCicilan(
                                idPinjaman = daftarAjuan[0].id,
                                username = daftarAjuan[0].username,
                                tanggal = tanggal,
                                saldopinjaman = saldo
                            ).also { isBayar = true } }, modifier = Modifier.padding(start = 20.dp, top = 20.dp)) {
                                Text(text = "Bayar Cicilan Bulan ${tanggal}")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        Modifier
                            .background(MaterialTheme.colorScheme.onBackground)
                            .padding(top = 7.dp, bottom = 7.dp, start = 16.dp, end = 16.dp)) {
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
                    LazyColumn(modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, end = 16.dp)){
                        var list = simulasiPinjaman(daftarAjuan[0].jangkawaktu ?: 1,
                            daftarAjuan[0].pokokpinjaman ?: 0, if(daftarAjuan[0].kategoripinjaman == "Uang") 1 else 2)
                        itemsIndexed(list){index, item ->
                            val flag = daftarCicilan.find { it.tanggal == item.tanggal } != null
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
                                    .background(
                                        if (tanggal == item.tanggal) MaterialTheme.colorScheme.inversePrimary
                                        else if (flag) Color.Green
                                        else Color.Transparent
                                    )
                                    .clickable {
                                        tanggal = item.tanggal
                                        saldo = item.saldo
                                    }){
                                Text(text = item.tanggal, modifier = Modifier.weight(0.13f), fontSize = 10.sp,
                                    color = if(isSystemInDarkTheme() && flag) Color.Black else MaterialTheme.colorScheme.onBackground)
                                Text(text = formatNumber(item.pokokpinjaman), modifier = Modifier.weight(0.18f), fontSize = 10.sp,
                                    color = if(isSystemInDarkTheme() && flag) Color.Black else MaterialTheme.colorScheme.onBackground)
                                Text(text = formatNumber(item.cicilan), modifier = Modifier.weight(0.15f), fontSize = 10.sp,
                                    color = if(isSystemInDarkTheme() && flag) Color.Black else MaterialTheme.colorScheme.onBackground)
                                Text(text = formatNumber(item.bunga), modifier = Modifier.weight(0.15f), fontSize = 10.sp,
                                    color = if(isSystemInDarkTheme() && flag) Color.Black else MaterialTheme.colorScheme.onBackground)
                                Text(text = formatNumber(item.angsuran), modifier = Modifier.weight(0.15f), fontSize = 10.sp,
                                    color = if(isSystemInDarkTheme() && flag) Color.Black else MaterialTheme.colorScheme.onBackground)
                                Text(text = formatNumber(item.saldo), modifier = Modifier
                                    .weight(0.18f)
                                    .padding(start = 5.dp), fontSize = 10.sp,
                                    color = if(isSystemInDarkTheme() && flag) Color.Black else MaterialTheme.colorScheme.onBackground)
                            }
                        }
                    }
                }
            }
            if(isAdmin == "True"){
                NavbarAdminScreen(navController)
            }
            else{
                NavbarScreenAnggota(navController, username)
            }
        }
    }
}
package mtid.android.simpanpinjam.presentation.user.simpanan

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mtid.android.simpanpinjam.data.remote.Supabase
import mtid.android.simpanpinjam.presentation.admin.editsimpanan.EditSimpananViewModel
import mtid.android.simpanpinjam.presentation.admin.navbar.NavbarAdminScreen
import mtid.android.simpanpinjam.presentation.functions.formatNumber
import mtid.android.simpanpinjam.presentation.user.navbar.NavbarScreenAnggota
import java.text.DecimalFormat

@Composable
fun SimpananScreen(
    supabase: Supabase,
    navController: NavController,
    username : String
){
    val viewModel = viewModel<SimpananViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SimpananViewModel(supabase, username) as T
            }
        }
    )
    LaunchedEffect(true){
        viewModel.getData()
    }
    val simpanan by rememberUpdatedState(viewModel.simpanan.firstOrNull())

    if(simpanan?.simpananpokok == null){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Loading...")
            Button(onClick = { viewModel.getData()} ) {
                Text(text = "Refresh")
            }
        }
    }
    else{
        var pokok by rememberSaveable{ mutableStateOf(simpanan?.simpananpokok ?: 0L) }
        var wajib by rememberSaveable{ mutableStateOf(simpanan?.simpananwajib ?: 0L) }
        var sukarela by rememberSaveable { mutableStateOf(simpanan?.simpanansukarela ?: 0L) }
        var harkop by rememberSaveable { mutableStateOf(simpanan?.simpananharkop ?: 0L) }
        var qurban by rememberSaveable { mutableStateOf(simpanan?.simpananqurban ?: 0L) }
        var ajuanWajib by rememberSaveable { mutableStateOf(0L) }
        var ajuanSukarela by rememberSaveable { mutableStateOf(0L) }
        var ambilSukarela by rememberSaveable { mutableStateOf(0L) }

        var isClickedWajib by rememberSaveable { mutableStateOf(false) }
        var flagWajib by rememberSaveable { mutableStateOf(false) }
        var isClickedSukarela by rememberSaveable { mutableStateOf(false) }
        var flagAmbilSukarela by rememberSaveable { mutableStateOf(false) }
        var isClickedAmbilSukarela by rememberSaveable { mutableStateOf(false) }

        if(isClickedWajib){
            isClickedWajib = false
            Toast.makeText(LocalContext.current, "Berhasil Ajukan Perubahan Nilai Simpanan Wajib!", Toast.LENGTH_SHORT).show()
            navController.navigate("ajuan/${username}" )
        }
        if(isClickedSukarela){
            Toast.makeText(LocalContext.current, "Berhasil Ajukan Perubahan Nilai Simpanan Sukarela!", Toast.LENGTH_SHORT).show()
            isClickedSukarela = false
            navController.navigate("ajuan/${username}" )
        }
        if(isClickedAmbilSukarela){
            Toast.makeText(LocalContext.current, "Berhasil Ajukan Pengambilan Simpanan Sukarela!", Toast.LENGTH_SHORT).show()
            isClickedAmbilSukarela = false
            navController.navigate("ajuan/${username}" )
        }

        Column{
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxSize()){
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(modifier = Modifier
                        .padding(top = 10.dp, bottom = 20.dp)
                        .fillMaxWidth()) {
                        Text(
                            text = "Simpanan Anggota \nUsername : $username",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(imageVector = Icons.Filled.History,
                            contentDescription = null,
                            modifier = Modifier.clickable { navController.navigate("historysimpanan/${username}/False")}
                                .padding(top=10.dp, end = 10.dp)
                                .size(30.dp))
                    }
                    OutlinedTextField(
                        label = { Text(text = "Total Simpanan Pokok") },
                        value = "Rp " + formatNumber(pokok),
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        label = { Text(text = "Total Simpanan Wajib") },
                        value = "Rp " + formatNumber(wajib),
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        supportingText = {Text(text = "Nilai per Bulan: Rp " +  formatNumber(simpanan?.basewajib ?: 0L))},
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        label = { Text(text = "Total Simpanan Sukarela") },
                        value = "Rp " + formatNumber(sukarela),
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        supportingText = {Text(text = "Nilai per Bulan: Rp " +  formatNumber(simpanan?.basesukarela ?: 0L))},
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                    ){

                        OutlinedTextField(
                            label = { Text(text = "Pengambilan Simpanan Sukarela") },
                            value = ambilSukarela.toString(),
                            onValueChange = { if(it.isNullOrBlank()) ambilSukarela = 0L else ambilSukarela = it.toLong() },
                            modifier = Modifier.weight(2f),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            ),
                            isError = flagAmbilSukarela,
                            supportingText = {
                                if(flagAmbilSukarela){
                                    Text(text = "Nilai pengambilan tidak dapat melebihi nilai total simpanan")
                                }
                            }
                        )
                        Button(onClick = {
                            if(ambilSukarela > sukarela) flagAmbilSukarela = true else
                            viewModel.ajuanAmbilSukarela(ambilSukarela).also {
                                isClickedAmbilSukarela = true
                            }},
                            modifier = Modifier.padding(16.dp)) {
                            Text(text = "Ajukan")
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        label = { Text(text = "Total Simpanan Harkop") },
                        value = "Rp " + formatNumber(harkop),
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        label = { Text(text = "Total Simpanan Qurban") },
                        value = "Rp " + formatNumber(qurban),
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                    )
                    Spacer(modifier = Modifier.height(30.dp))

                    // Ajuan Perubahan Nilai Simpanan
                    Text(
                        text = "Ajuan Perubahan Nilai Simpanan",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        label = { Text(text = "Simpanan Wajib per Bulan") },
                        value ="Rp " + formatNumber(simpanan?.basewajib ?: 0L),
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                    ){
                        OutlinedTextField(
                            label = { Text(text = "Perubahan Nilai Simpanan Wajib") },
                            value = ajuanWajib.toString(),
                            onValueChange = { if(it.isNullOrBlank()) ajuanWajib = 0L else ajuanWajib = it.toLong() },
                            modifier = Modifier.weight(2f),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            ),
                            supportingText = {Text(text = "Minimum : Rp 50.000")},
                            isError = flagWajib
                        )
                        Button(onClick = { if(ajuanWajib < 50000){flagWajib = true} else {
                            flagWajib = false
                            viewModel.ajuanWajib(ajuanWajib, simpanan?.basewajib ?: 0L).also {
                            isClickedWajib = true
                        }}},
                            modifier = Modifier.padding(16.dp)) {
                            Text(text = "Ajukan")
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        label = { Text(text = "Simpanan Sukarela per Bulan") },
                        value ="Rp " + formatNumber(simpanan?.basesukarela ?: 0L),
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                    ){
                        OutlinedTextField(
                            label = { Text(text = "Perubahan Nilai Simpanan Sukarela") },
                            value = ajuanSukarela.toString(),
                            onValueChange = { if(it.isNullOrBlank()) ajuanSukarela = 0L else ajuanSukarela = it.toLong() },
                            modifier = Modifier.weight(2f),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            ),
                        )
                        Button(onClick = {
                            viewModel.ajuanSukarela(ajuanSukarela, simpanan?.basesukarela ?: 0L).also {
                            isClickedSukarela = true
                        }},
                            modifier = Modifier.padding(16.dp)) {
                            Text(text = "Ajukan")
                        }
                    }
                }
            }
            NavbarScreenAnggota(navController = navController, username = username)
        }
    }
}
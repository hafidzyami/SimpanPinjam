package mtid.android.simpanpinjam.presentation.admin.editsimpanan

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mtid.android.simpanpinjam.data.remote.Supabase
import mtid.android.simpanpinjam.presentation.admin.editanggota.EditAnggotaViewModel
import mtid.android.simpanpinjam.presentation.admin.navbar.NavbarAdminScreen

@Composable
fun EditSimpananScreen(
    supabase: Supabase,
    navController: NavController,
    username : String
){
    val viewModel = viewModel<EditSimpananViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EditSimpananViewModel(supabase, username) as T
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
        var tambahHarkop by rememberSaveable { mutableStateOf(0L) }
        var isEdit by rememberSaveable { mutableStateOf(true) }
        var isClicked by remember{ mutableStateOf(false) }

        if(isClicked){
            isClicked = false
            Toast.makeText(LocalContext.current, "Update Successful!", Toast.LENGTH_SHORT)
                .show()
            navController.navigate("adminhome")
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
                    Text(
                        text = "Simpanan Anggota Username : $username",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
                    )
                    OutlinedTextField(
                        label = { Text(text = "Total Simpanan Pokok") },
                        value = pokok.toString(),
                        onValueChange = { if(it.isNullOrBlank()) pokok = 0L else pokok = it.toLong()},
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = isEdit,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        label = { Text(text = "Total Simpanan Wajib") },
                        value = wajib.toString(),
                        onValueChange = { if(it.isNullOrBlank()) wajib = 0L else wajib = it.toLong() },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = isEdit,
                        supportingText = {Text(text = "Nilai per Bulan: Rp " +  simpanan?.basewajib.toString())},
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = {wajib =  (wajib ?: 0L) + (simpanan?.basewajib ?: 0L)}) {
                        Text(text = "Tambah")
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        label = { Text(text = "Total Simpanan Sukarela") },
                        value = sukarela.toString(),
                        onValueChange = { if(it.isNullOrBlank()) sukarela = 0L else sukarela = it.toLong() },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = isEdit,
                        supportingText = {Text(text = "Nilai per Bulan: Rp " +  simpanan?.basesukarela.toString())},
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = {sukarela =  (sukarela ?: 0L) + (simpanan?.basesukarela ?: 0L)}) {
                        Text(text = "Tambah")
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        label = { Text(text = "Total Simpanan Harkop") },
                        value = harkop.toString(),
                        onValueChange = { if(it.isNullOrBlank()) harkop = 0L else harkop = it.toLong() },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = isEdit,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                    ){
                        OutlinedTextField(
                            label = { Text(text = "Tambah Total Harkop") },
                            value = tambahHarkop.toString(),
                            onValueChange = { if(it.isNullOrBlank()) tambahHarkop= 0L else tambahHarkop = it.toLong() },
                            modifier = Modifier.weight(2f),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            ),
                        )
                        Button(onClick = {harkop = (harkop?: 0L) + (tambahHarkop)},
                            modifier = Modifier.padding(16.dp)) {
                            Text(text = "Tambah")
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    ){
                        Button(
                            onClick = {isEdit = !isEdit},
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),

                        ) {
                            Text(text = if(isEdit) "Edit" else "Cancel Edit")
                        }
                        Button(
                            onClick = {viewModel.updateData(pokok, wajib, sukarela, harkop).also { isClicked = true }},
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                        ) {
                            Text(text = "Update")
                        }
                    }
                }
            }
            NavbarAdminScreen(navController = navController)
        }
    }
}

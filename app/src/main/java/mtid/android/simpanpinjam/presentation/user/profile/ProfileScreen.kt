package mtid.android.simpanpinjam.presentation.user.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mtid.android.simpanpinjam.data.remote.Supabase
import mtid.android.simpanpinjam.presentation.admin.editanggota.DrowdownDept
import mtid.android.simpanpinjam.presentation.admin.editanggota.DrowdownKantor
import mtid.android.simpanpinjam.presentation.admin.editanggota.EditAnggotaViewModel
import mtid.android.simpanpinjam.presentation.user.navbar.NavbarScreenAnggota

@Composable
fun ProfileScreen(
    supabase: Supabase,
    navController: NavController,
    username : String
){
    val viewModel = viewModel<EditAnggotaViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EditAnggotaViewModel(supabase, username) as T
            }
        }
    )
    LaunchedEffect(true){
        viewModel.getData()
    }
    val anggota by rememberUpdatedState(viewModel.anggota.firstOrNull())

    if(anggota?.username == null){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Loading...")

            Button(onClick = { viewModel.getData()}, ) {
                Text(text = "Refresh")
            }
        }
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
                    .padding(20.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Data Anggota Username : $username",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
                )
                OutlinedTextField(
                    label = { Text(text = "Nomor") },
                    value = anggota?.nomor ?: "N/A",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    label = { Text(text = "Nama") },
                    value = anggota?.nama ?: "N/A",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    label = { Text(text = "Kantor Cabang") },
                    value = anggota?.kantorcabang ?: "N/A",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    label = { Text(text = "Departemen") },
                    value = anggota?.departemen ?: "N/A",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    label = { Text(text = "Email") },
                    value = anggota?.email ?: "N/A",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    label = { Text(text = "Tanggal Masuk") },
                    value = anggota?.tanggalmasuk?: "N/A",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    label = { Text(text = "Tanggal Keluar") },
                    value = anggota?.tanggalkeluar ?: "N/A",
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true
                )
            }
        }
        NavbarScreenAnggota(navController, username)
    }
}
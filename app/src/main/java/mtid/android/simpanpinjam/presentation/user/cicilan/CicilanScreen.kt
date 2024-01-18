package mtid.android.simpanpinjam.presentation.user.cicilan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mtid.android.simpanpinjam.data.remote.Supabase
import mtid.android.simpanpinjam.presentation.admin.daftarajuanpinjaman.DaftarAjuanPinjamanViewModel
import mtid.android.simpanpinjam.presentation.admin.navbar.NavbarAdminScreen
import mtid.android.simpanpinjam.presentation.component.AjuanPinjamanCard
import mtid.android.simpanpinjam.presentation.user.navbar.NavbarScreenAnggota

@Composable
fun CicilanScreen(
    supabase: Supabase,
    navController: NavController,
    username : String,
    isAdmin : String
){
    val viewModel = viewModel<CicilanViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CicilanViewModel(supabase) as T
            }
        }
    )

    LaunchedEffect(true){
        viewModel.getData(username)
    }
    val daftarPinjaman by rememberUpdatedState(viewModel.pinjaman)

    if(daftarPinjaman.isEmpty()){
        Column {
            Box(modifier = Modifier.weight(1f)){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "Belum ada pinjaman!")
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = { viewModel.getData(username)} ) {
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
                    Text(
                        text = "Daftar Pinjaman ${username}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
                    )
                    LazyColumn( modifier = Modifier.weight(1f)){
                        itemsIndexed(daftarPinjaman){index, pinjaman ->
                            AjuanPinjamanCard(pinjaman, onCardClick = {navController.navigate("ajuanpinjamananggota/${it}/${isAdmin}/${username}")})
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
package mtid.android.simpanpinjam.presentation.user.ajuan
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mtid.android.simpanpinjam.data.remote.Supabase
import mtid.android.simpanpinjam.presentation.component.AjuanPinjamanCard
import mtid.android.simpanpinjam.presentation.component.AjuanSimpananCard
import mtid.android.simpanpinjam.presentation.user.navbar.NavbarScreenAnggota


@Composable
fun AjuanScreen(
    supabase: Supabase,
    navController: NavController,
    username : String
){

    val viewModel = viewModel<AjuanViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AjuanViewModel(supabase, username) as T
            }
        }
    )
    LaunchedEffect(true){
        viewModel.getData()
    }
    val daftarAjuanSimpanan by rememberUpdatedState(viewModel.ajuanSimpanan)
    val daftarAjuanPinjaman by rememberUpdatedState(viewModel.ajuanPinjaman)
    var selectedStatus by rememberSaveable { mutableStateOf("Semua") }

    if(daftarAjuanSimpanan.isEmpty() && daftarAjuanPinjaman.isEmpty()){
        Column {
            Box(modifier = Modifier.weight(1f)){
                Text(
                    text = "Daftar Ajuan : $username",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(20.dp),
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "Tidak ada ajuan, Silahkan mengajukan!")
                    Text(text = "Jika telah mengajukan, tekan tombol refresh!")
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = { viewModel.getData()} ) {
                        Text(text = "Refresh")
                    }
                }
            }
            NavbarScreenAnggota(navController, username)
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
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, end = 20.dp, bottom = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = "Daftar Ajuan:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
                        )
                        DropdownAjuan(status = selectedStatus){ selectedValue ->
                            selectedStatus = selectedValue
                        }
                    }
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        if(selectedStatus == "Simpanan"){
                            itemsIndexed(daftarAjuanSimpanan){index, ajuan ->
                                AjuanSimpananCard(ajuan)
                            }   
                        }
                        else if(selectedStatus == "Pinjaman"){
                            itemsIndexed(daftarAjuanPinjaman){index, pinjaman ->
                                AjuanPinjamanCard(ajuanPinjaman = pinjaman){}
                            }
                        }
                        else{
                            itemsIndexed(daftarAjuanSimpanan){index, ajuan ->
                                AjuanSimpananCard(ajuan)
                            }
                            itemsIndexed(daftarAjuanPinjaman){index, pinjaman ->
                                AjuanPinjamanCard(ajuanPinjaman = pinjaman){}
                            }
                        }
                    }
                }
            }
            NavbarScreenAnggota(navController, username)
        }
    }
}

@Composable
fun DropdownAjuan(status : String, onSelectedTextChange: (String) -> Unit){
    var mExpanded by remember { mutableStateOf(false) }
    val mStatus = listOf("Simpanan", "Pinjaman", "Semua")
    var mSelectedText by rememberSaveable { mutableStateOf(status) }

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
            label = {Text("Filter by Kategori")},
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
                    {Text(text = label)},
                    onClick = {
                        onSelectedTextChange(label)
                        mSelectedText = label
                        mExpanded = false
                    })
            }
        }
    }
}
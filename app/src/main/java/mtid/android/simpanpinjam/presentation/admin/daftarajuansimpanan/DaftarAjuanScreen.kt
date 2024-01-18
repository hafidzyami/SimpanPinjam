package mtid.android.simpanpinjam.presentation.admin.daftarajuansimpanan

import android.content.Context
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mtid.android.simpanpinjam.data.model.AjuanSimpanan
import mtid.android.simpanpinjam.data.remote.Supabase
import mtid.android.simpanpinjam.presentation.admin.navbar.NavbarAdminScreen
import mtid.android.simpanpinjam.presentation.component.AjuanSimpananCardAdmin

@Composable
fun DaftarAjuanScreen(
    supabase: Supabase,
    navController: NavController
){
    val viewModel = viewModel<DaftarAjuanViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DaftarAjuanViewModel(supabase) as T
            }
        }
    )

    var clicked by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(true, clicked){
        viewModel.getData()
    }
    val daftarAjuan by rememberUpdatedState(viewModel.daftarAjuan)
    var selectedStatus by rememberSaveable { mutableStateOf("Menunggu Konfirmasi") }
    var ajuanSimpananClicked by remember{ mutableStateOf(AjuanSimpanan()) }


    if(clicked){
        AjuanDialog(onDismissRequest = {clicked = false}, ajuanSimpanan = ajuanSimpananClicked,
            onTerima = {id, tipe, angka, username -> viewModel.terima(id, tipe, angka, username)},
            onTolak = {id, -> viewModel.tolak(id)},
            context = context)
    }

    if(daftarAjuan.isEmpty()){
        Column {
            Box(modifier = Modifier.weight(1f)){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "Tidak ada ajuan!")
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = { viewModel.getData()} ) {
                        Text(text = "Refresh")
                    }
                }
            }
            NavbarAdminScreen(navController)
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
                        .padding(top = 20.dp, end = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = "Daftar Ajuan:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
                        )
                        DropdownFilter(status = selectedStatus){ selectedValue ->
                            selectedStatus = selectedValue
                        }
                    }
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        var filteredAjuanList = when (selectedStatus){
                            "Menunggu Konfirmasi" -> daftarAjuan.filter { it.status == null }
                            "Diterima" -> daftarAjuan.filter {it.status == true}
                            "Ditolak" -> daftarAjuan.filter { it.status == false}
                            else -> daftarAjuan
                        }
                        itemsIndexed(filteredAjuanList){index, ajuan ->
                            AjuanSimpananCardAdmin(ajuanSimpanan = ajuan) {
                                ajuanSimpananClicked = ajuan
                                clicked = true
                            }
                        }
                    }

                }
            }
            NavbarAdminScreen(navController)
        }
    }
}

@Composable
fun DropdownFilter(status : String, onSelectedTextChange: (String) -> Unit){
    var mExpanded by remember { mutableStateOf(false) }
    val mStatus = listOf("Menunggu Konfirmasi", "Diterima", "Ditolak", "Semua")
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
            label = {Text("Filter by Status")},
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

@Composable
fun AjuanDialog(onDismissRequest: () -> Unit, ajuanSimpanan: AjuanSimpanan,
                onTolak : (Int) -> Unit,
                onTerima : (Int, String, Long?, String) -> Unit,
                context : Context){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            AjuanSimpananCardAdmin(ajuanSimpanan = ajuanSimpanan) {}
            var tipe : String = if (ajuanSimpanan.basesukarela != null){"basesukarela"}
                                else if (ajuanSimpanan.basewajib != null){"basewajib"}
                                else{"simpanansukarela"}

            var angka : Long? = if (ajuanSimpanan.basesukarela != null){ajuanSimpanan.basesukarela}
                                else if (ajuanSimpanan.basewajib != null){ajuanSimpanan.basewajib}
                                else{ajuanSimpanan.simpanansukarela}
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text("Tutup")
                }
                TextButton(
                    onClick = { onTolak(ajuanSimpanan.id).also {
                        Toast.makeText(context, "Ajuan berhasil ditolak!", Toast.LENGTH_SHORT).show()
                        onDismissRequest()
                    }},
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text("Tolak")
                }
                TextButton(
                    onClick = { onTerima(ajuanSimpanan.id, tipe, angka, ajuanSimpanan.username).also {
                        Toast.makeText(context, "Ajuan berhasil dikonfirmasi!", Toast.LENGTH_SHORT).show()
                        onDismissRequest()
                    } },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text("Konfirmasi")
                }
            }
        }
    }
}
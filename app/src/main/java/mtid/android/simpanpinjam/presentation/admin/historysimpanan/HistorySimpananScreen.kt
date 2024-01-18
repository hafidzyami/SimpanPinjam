package mtid.android.simpanpinjam.presentation.admin.historysimpanan

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
import mtid.android.simpanpinjam.presentation.admin.navbar.NavbarAdminScreen
import mtid.android.simpanpinjam.presentation.component.HistorySimpananCard
import mtid.android.simpanpinjam.presentation.user.navbar.NavbarScreenAnggota

@Composable
fun HistorySimpananScreen(
    supabase: Supabase,
    navController: NavController,
    username : String,
    isAdmin : String
){
    val viewModel = viewModel<HistorySimpananViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HistorySimpananViewModel(supabase, username) as T
            }
        }
    )

    LaunchedEffect(true){
        viewModel.getData()
    }
    val logSimpanan by rememberUpdatedState(viewModel.logSimpanan)
    var selectedStatus by rememberSaveable { mutableStateOf("Semua") }

    if(logSimpanan.isEmpty()){
        Column {
            Box(modifier = Modifier.weight(1f)){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "Tidak ada history simpanan!")
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(onClick = { viewModel.getData()} ) {
                        Text(text = "Refresh")
                    }
                }
            }
            if(isAdmin == "True"){
                NavbarAdminScreen(navController)
            }
            else{
                NavbarScreenAnggota(navController = navController, username = username)
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
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, end = 20.dp, bottom = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = "History Simpanan\n${username}",
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
                        var filteredLogSimpanan = when (selectedStatus){
                            "Sukarela" -> logSimpanan.filter { it.simpanansukarela != null }
                            "Wajib" -> logSimpanan.filter {it.simpananwajib != null}
                            "Harkop" -> logSimpanan.filter {it.simpananharkop != null}
                            "Qurban" -> logSimpanan.filter {it.simpananqurban != null}
                            else -> logSimpanan
                        }
                        itemsIndexed(filteredLogSimpanan){index, log ->
                            HistorySimpananCard(logSimpanan = log)
                        }
                    }

                }
            }
            if(isAdmin == "True"){
                NavbarAdminScreen(navController)
            }
            else{
                NavbarScreenAnggota(navController = navController, username = username)
            }
        }
    }
}

@Composable
fun DropdownFilter(status : String, onSelectedTextChange: (String) -> Unit){
    var mExpanded by remember { mutableStateOf(false) }
    val mStatus = listOf("Semua", "Wajib", "Sukarela", "Harkop", "Qurban")
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
            label = { Text("Filter by Status") },
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
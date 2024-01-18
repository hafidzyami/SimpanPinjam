package mtid.android.simpanpinjam.presentation.admin.editanggota

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mtid.android.simpanpinjam.data.remote.Supabase
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import mtid.android.simpanpinjam.R
import mtid.android.simpanpinjam.presentation.admin.navbar.NavbarAdminScreen

@Composable
fun EditAnggotaScreen(
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
    else{
        val nomor = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(anggota?.nomor.toString())) }
        val nama = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(anggota?.nama.toString())) }
        val email = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(anggota?.email.toString())) }
        val tglMasuk = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(anggota?.tanggalmasuk.toString())) }
        val tglKeluar = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(anggota?.tanggalkeluar.toString())) }
        var kantorCabang by rememberSaveable { mutableStateOf(anggota?.kantorcabang.toString()) }
        var departemen by rememberSaveable { mutableStateOf(anggota?.departemen.toString()) }
        var isUpdate by rememberSaveable { mutableStateOf(false) }
        if(isUpdate){
            Toast.makeText(LocalContext.current, "Update Data Successful!", Toast.LENGTH_SHORT).show()
            isUpdate = false
            navController.navigate("adminhome")
        }
        Column(
        ) {
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxSize())
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
                        value = nomor.value,
                        onValueChange = { nomor.value = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        label = { Text(text = "Nama") },
                        value = nama.value,
                        onValueChange = { nama.value = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    DrowdownKantor(kantorCabang) { selectedValue ->
                        kantorCabang = selectedValue
                    }


                    Spacer(modifier = Modifier.height(20.dp))

                    DrowdownDept(departemen) { selectedValue ->
                        departemen = selectedValue
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        label = { Text(text = "Email") },
                        value = email.value,
                        onValueChange = { email.value = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        label = { Text(text = "Tanggal Masuk") },
                        value = tglMasuk.value,
                        onValueChange = { tglMasuk.value = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        label = { Text(text = "Tanggal Keluar") },
                        value = tglKeluar.value,
                        onValueChange = { tglKeluar.value = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Box(modifier = Modifier.padding(20.dp, 40.dp, 20.dp, 20.dp)) {
                        Button(
                            onClick = {
                                viewModel.updateData(
                                    nomor.value.text,
                                    nama.value.text,
                                    departemen,
                                    kantorCabang,
                                    tglMasuk.value.text,
                                    tglKeluar.value.text,
                                    email.value.text
                                ).also {
                                    isUpdate = true
                                }
                            },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Update Data")
                        }
                    }
                }
            }
            NavbarAdminScreen(navController = navController)
        }

    }
}


@Composable
fun DrowdownKantor(kantor : String, onSelectedTextChange: (String) -> Unit){
    var mExpanded by remember { mutableStateOf(false) }
    val mCities = listOf("Jakarta", "Bandung")
    var mSelectedText by rememberSaveable { mutableStateOf(kantor) }

    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
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
            label = {Text("Kantor Cabang")},
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
            mCities.forEach { label ->
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
fun DrowdownDept(dept : String, onSelectedTextChange: (String) -> Unit){
    var mExpanded by remember { mutableStateOf(false) }
    val mDept = listOf(
        "Regulatory Affairs & Pharmacovigilance",
        "Strategy & Alliance",
        "Compliance and Risk Management",
        "Legal",
        "Financial",
        "General Affairs",
        "Information Technology",
        "Maintenance & Utility",
        "Planning & Supply Chain Management",
        "Production",
        "Technical",
        "Quality Assurance",
        "Quality Control",
        "Business Support",
        "National Sales",
        "Product Management"
    )
    var mSelectedText by rememberSaveable { mutableStateOf(dept) }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
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
            label = {Text("Departemen")},
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
            mDept.forEach { label ->
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
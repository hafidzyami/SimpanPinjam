package mtid.android.simpanpinjam.presentation.admin.editsimpanan

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
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
    val pokok = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(simpanan?.simpananpokok.toString())) }
    val wajib = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(simpanan?.simpananwajib.toString())) }
    val sukarela = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(simpanan?.simpanansukarela.toString())) }
    val harkop = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(simpanan?.simpananharkop.toString())) }
    val basewajib = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(simpanan?.basewajib.toString())) }
    val basesukarela = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(simpanan?.basesukarela.toString())) }

    Column{
        Box(modifier = Modifier
            .weight(1f)
            .fillMaxSize()){
            Column {

            }
        }
        NavbarAdminScreen(navController = navController)
    }
}
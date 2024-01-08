package mtid.android.simpanpinjam.presentation.admin.homepage

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mtid.android.simpanpinjam.data.remote.Supabase
import mtid.android.simpanpinjam.presentation.component.AnggotaCard
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Scaffold
import mtid.android.simpanpinjam.data.model.Anggota
import mtid.android.simpanpinjam.presentation.admin.navbar.NavbarAdminScreen
import mtid.android.simpanpinjam.presentation.component.BottomSheet

@Composable
fun AdminHomeScreen(
    supabase: Supabase,
    navController: NavController
) {
    val viewModel = viewModel<AdminHomeViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AdminHomeViewModel(supabase) as T
            }
        }
    )
    var dataSheet by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(true) {
        viewModel.getData()
    }

    val anggotaList by rememberUpdatedState(viewModel.anggota)
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search") },
                modifier = Modifier
                    .weight(1f),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                    )
                },
                trailingIcon = {
                    if (searchText != ""){
                        IconButton(onClick = {searchText = ""}){
                            Icon(imageVector  = Icons.Default.Close, contentDescription = null)
                        }
                    }
                }
            )
        }
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(anggotaList.filter {
                    it.username.contains(searchText, ignoreCase = true)||
                            (it.nama?.contains(searchText, ignoreCase = true) == true)
                }) { index, anggota ->
                    AnggotaCard(
                        anggota = anggota,
                        onCardClick = { dataSheet = anggota.username }
                    )
                }
            }
            NavbarAdminScreen(navController = navController)
        }


        if (dataSheet != "") {
            BottomSheet(dataSheet, navController) {
                dataSheet = ""
            }
        }
    }

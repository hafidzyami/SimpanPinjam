package mtid.android.simpanpinjam.presentation.admin.register
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import mtid.android.simpanpinjam.data.remote.Supabase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import kotlinx.coroutines.delay
import mtid.android.simpanpinjam.presentation.admin.navbar.NavbarAdminScreen
import mtid.android.simpanpinjam.presentation.forgotpw.ForgotPWViewModel

@Composable
fun RegisterScreen(
    supabase: Supabase,
    navController: NavController
) {
    val viewModel = viewModel<RegisterViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RegisterViewModel(supabase) as T
            }
        }
    )
    var hasDataLoaded by rememberSaveable{ mutableStateOf(false) }
    if(!hasDataLoaded){
        viewModel.getData()
        hasDataLoaded = true
    }
    val username = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val confirmpassword = remember { mutableStateOf(TextFieldValue()) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val simpananPokok = remember { mutableStateOf(TextFieldValue()) }
    val basewajib = remember { mutableStateOf(TextFieldValue()) }
    val basesukarela= remember { mutableStateOf(TextFieldValue()) }

    fun validate() : Boolean{
        return username.value.text.isNotEmpty() && password.value.text.isNotEmpty() && confirmpassword.value.text.isNotEmpty()
                && simpananPokok.value.text.isNotEmpty() && basewajib.value.text.isNotEmpty() && basesukarela.value.text.isNotEmpty()
    }

    // ViewModel
    var clicked by rememberSaveable { mutableStateOf(false) }
    var emptyField by rememberSaveable { mutableStateOf(false) }
    var isUnameFound = viewModel.isUnameFound
    var isPWMatch = viewModel.isPWMatch
    val usersLogins by rememberUpdatedState(viewModel.usersLogins)


    if (clicked) {
        clicked = false
        if(emptyField){
            Toast.makeText(LocalContext.current, "Please fill all the fields!", Toast.LENGTH_SHORT)
                .show()
            emptyField = false
        }
        else if (!isUnameFound) {
            if (isPWMatch) {
                Toast.makeText(LocalContext.current, "Register Successful!", Toast.LENGTH_SHORT)
                    .show()
                navController.navigate("editanggota/${username.value.text}")
            } else {
                Toast.makeText(LocalContext.current, "Password Unmatch!", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(LocalContext.current, "Username Has Already Registered!", Toast.LENGTH_SHORT)
                .show()
            username.value = TextFieldValue("")
            password.value = TextFieldValue("")
            confirmpassword.value = TextFieldValue("")
        }
    }

    if(username.value.text.isNullOrEmpty()){
        Log.d("tai", "memek")
    }

    Box(modifier = Modifier.fillMaxSize() ){
        Column(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                label = { Text(text = "Username") },
                value = username.value,
                onValueChange = { username.value = it },
            )

            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                label = { Text(text = "Password") },
                value = password.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password.value = it },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                label = { Text(text = "Confirm Password") },
                value = confirmpassword.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { confirmpassword.value = it },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                label = { Text(text = "Nilai Simpanan Pokok") },
                value = simpananPokok.value,
                onValueChange = { simpananPokok.value = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                label = { Text(text = "Nilai Simpanan Wajib") },
                value = basewajib.value,
                onValueChange = { basewajib.value = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                label = { Text(text = "Nilai Simpanan Sukarela") },
                value = basesukarela.value,
                onValueChange = { basesukarela.value = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
            )

            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.padding(40.dp, 20.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        clicked = true
                        if(validate()){
                            viewModel.register(
                                username.value.text,
                                password.value.text, confirmpassword.value.text, simpananPokok.value.text.toLongOrNull(), usersLogins,
                                basewajib.value.text.toLongOrNull(), basesukarela.value.text.toLongOrNull()
                            )
                        }
                        else{
                            emptyField = true
                        }
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Register")
                }
            }
        }
        Box(modifier = Modifier.align(Alignment.BottomCenter)){
            NavbarAdminScreen(navController = navController)
        }
    }
}

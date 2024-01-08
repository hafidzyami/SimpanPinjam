package mtid.android.simpanpinjam.presentation.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation


@Composable
fun LoginScreen(
    supabase: Supabase,
    navController : NavController
){

    Log.d("test", "test3")
    val viewModel = viewModel<LoginViewModel>(
        factory = object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(supabase) as T
            }
        }
    )
    LaunchedEffect(true){
        viewModel.getData()
    }

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val username = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }
        val password = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        val openAlertDialog = viewModel.loginflag
        val isAdmin = viewModel.isAdmin
        var clicked by rememberSaveable { mutableStateOf(false) }

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            label = { Text(text = "Username") },
            value = username.value,
            onValueChange = { username.value = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            label = { Text(text = "Password") },
            value = password.value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password.value = it },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(imageVector  = image, description)
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {viewModel.auth(username.value.text, password.value.text).also {
                    clicked = true
                } },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Login")
            }
        }

        if(clicked){
            clicked = false
            if(openAlertDialog){
                if(isAdmin){
                    navController.navigate("adminhome")
                }
                else{
                    navController.navigate("userhome/${username.value.text}")
                }
                Toast.makeText(LocalContext.current, "Login Successful!", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(LocalContext.current, "Invalid Credentials!", Toast.LENGTH_SHORT).show()
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("Forgot password?"),
            onClick = { navController.navigate("forgotpw")},
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                color = MaterialTheme.colorScheme.onBackground
            )
        )


    }
}
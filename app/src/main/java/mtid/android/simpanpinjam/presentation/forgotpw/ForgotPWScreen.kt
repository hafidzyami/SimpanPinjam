package mtid.android.simpanpinjam.presentation.forgotpw


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


@Composable
fun ForgotPWScreen(
    supabase: Supabase,
    navController: NavController
){
    val viewModel = viewModel<ForgotPWViewModel>(
        factory = object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ForgotPWViewModel(supabase) as T
            }
        }
    )

    var hasDataLoaded by rememberSaveable{ mutableStateOf(false) }

    if(!hasDataLoaded){
        viewModel.getData()
        hasDataLoaded = true
    }

    val usersLogins by rememberUpdatedState(viewModel.usersLogins)

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val username = remember { mutableStateOf(TextFieldValue()) }
        val password = remember{ mutableStateOf(TextFieldValue()) }
        val confirmpassword = remember{ mutableStateOf(TextFieldValue()) }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        // ViewModel
        var clicked by rememberSaveable { mutableStateOf(false) }
        var isUnameMatch = viewModel.isUnameMatch
        var isPWMatch = viewModel.isPWMatch

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            label = { Text(text = "Username") },
            value = username.value,
            onValueChange = { username.value = it }
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            label = { Text(text = "New Password") },
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

        OutlinedTextField(
            label = { Text(text = "Confirm Password") },
            value = confirmpassword.value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { confirmpassword.value = it },
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
                onClick = { viewModel.resetPassword(username.value.text,
                    password.value.text, confirmpassword.value.text, usersLogins).also {
                        clicked = true
                } },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Reset Password")
            }
        }

        if(clicked){
            clicked = false
            if(isUnameMatch){
                if(isPWMatch){
                    Toast.makeText(LocalContext.current, "Reset Password Successful!", Toast.LENGTH_SHORT).show()
                    navController.navigate("login")
                }
                else{
                    Toast.makeText(LocalContext.current, "Password Unmatch!", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(LocalContext.current, "Username Not Found!", Toast.LENGTH_SHORT).show()
                username.value = TextFieldValue("")
                password.value = TextFieldValue("")
                confirmpassword.value = TextFieldValue("")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("Already have an account?"),
            onClick = { navController.navigate("login") },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}
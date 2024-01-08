package mtid.android.simpanpinjam

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mtid.android.simpanpinjam.data.remote.Supabase
import mtid.android.simpanpinjam.presentation.admin.daftarajuan.DaftarAjuanScreen
import mtid.android.simpanpinjam.presentation.admin.editanggota.EditAnggotaScreen
import mtid.android.simpanpinjam.presentation.admin.editsimpanan.EditSimpananScreen
import mtid.android.simpanpinjam.presentation.admin.homepage.AdminHomeScreen
import mtid.android.simpanpinjam.presentation.forgotpw.ForgotPWScreen
import mtid.android.simpanpinjam.presentation.login.LoginScreen
import mtid.android.simpanpinjam.presentation.admin.register.RegisterScreen
import mtid.android.simpanpinjam.presentation.user.homepage.UserHomeScreen
import mtid.android.simpanpinjam.ui.theme.SimpanPinjamTheme

class MainActivity : ComponentActivity() {
    private lateinit var supabase : Supabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supabase = Supabase()
        setContent {
            SimpanPinjamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login" ){
                        composable("login"){
                            LoginScreen(supabase = supabase, navController)
                        }
                        composable("register"){
                            RegisterScreen(supabase = supabase, navController)
                        }
                        composable("forgotpw"){
                            ForgotPWScreen(supabase = supabase, navController)
                        }
                        composable("adminhome"){
                            AdminHomeScreen(supabase, navController)
                        }
                        composable("userhome/{username}"){navBackStackEntry->
                            val username = navBackStackEntry.arguments?.getString("username")
                            username?.let{uname->
                                UserHomeScreen(supabase, navController, uname)
                            }
                        }
                        composable("editanggota/{username}"){navBackStackEntry->
                            val username = navBackStackEntry.arguments?.getString("username")
                            username?.let{uname->
                                EditAnggotaScreen(supabase, navController, uname)
                            }
                        }
                        composable("editsimpanan/{username}"){navBackStackEntry->
                            val username = navBackStackEntry.arguments?.getString("username")
                            username?.let{uname->
                                EditSimpananScreen(supabase, navController, uname)
                            }
                        }
                        composable("listajuan"){
                            DaftarAjuanScreen(supabase, navController)
                        }
                    }
                }
            }
        }
    }
}


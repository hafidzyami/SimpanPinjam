package mtid.android.simpanpinjam

import android.os.Bundle
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
import mtid.android.simpanpinjam.presentation.admin.ajuanpinjamananggota.AjuanPinjamanAnggotaScreen
import mtid.android.simpanpinjam.presentation.admin.daftarajuanpinjaman.DaftarAjuanPinjamanScreen
import mtid.android.simpanpinjam.presentation.admin.daftarajuansimpanan.DaftarAjuanScreen
import mtid.android.simpanpinjam.presentation.admin.editanggota.EditAnggotaScreen
import mtid.android.simpanpinjam.presentation.admin.editsimpanan.EditSimpananScreen
import mtid.android.simpanpinjam.presentation.admin.historysimpanan.HistorySimpananScreen
import mtid.android.simpanpinjam.presentation.admin.homepage.AdminHomeScreen
import mtid.android.simpanpinjam.presentation.forgotpw.ForgotPWScreen
import mtid.android.simpanpinjam.presentation.login.LoginScreen
import mtid.android.simpanpinjam.presentation.admin.register.RegisterScreen
import mtid.android.simpanpinjam.presentation.user.ajuan.AjuanScreen
import mtid.android.simpanpinjam.presentation.user.cicilan.CicilanScreen
import mtid.android.simpanpinjam.presentation.user.pinjaman.PinjamanScreen
import mtid.android.simpanpinjam.presentation.user.profile.ProfileScreen
import mtid.android.simpanpinjam.presentation.user.simpanan.SimpananScreen
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
                        composable("historysimpanan/{username}/{isAdmin}"){navBackStackEntry->
                            val username = navBackStackEntry.arguments?.getString("username")
                            val isAdmin = navBackStackEntry.arguments?.getString("isAdmin")
                            username?.let{uname->
                                isAdmin?.let{isAdmin ->
                                    HistorySimpananScreen(supabase, navController, uname, isAdmin)
                                }
                            }
                        }
                        composable("listajuansimpanan"){
                            DaftarAjuanScreen(supabase, navController)
                        }
                        composable("listajuanpinjaman"){
                            DaftarAjuanPinjamanScreen(supabase, navController)
                        }
                        composable("ajuanpinjamananggota/{id}/{isAdmin}/{username}"){navBackStackEntry->
                            val id = navBackStackEntry.arguments?.getString("id")
                            val isAdmin = navBackStackEntry.arguments?.getString("isAdmin")
                            val username = navBackStackEntry.arguments?.getString("username")
                            AjuanPinjamanAnggotaScreen(supabase = supabase, navController = navController, id = id!!.toInt(),
                                isAdmin = isAdmin ?: "True", username = username ?: "")
                        }
                        // Anggota
                        composable("simpanan/{username}"){navBackStackEntry->
                            val username = navBackStackEntry.arguments?.getString("username")
                            username?.let{uname->
                                SimpananScreen(supabase, navController, uname)
                            }
                        }
                        composable("pinjaman/{username}"){navBackStackEntry->
                            val username = navBackStackEntry.arguments?.getString("username")
                            username?.let{uname->
                                PinjamanScreen(supabase, navController, uname)
                            }
                        }
                        composable("cicilan/{username}/{isadmin}"){navBackStackEntry->
                            val username = navBackStackEntry.arguments?.getString("username")
                            val isAdmin = navBackStackEntry.arguments?.getString("isadmin")
                            username?.let{uname->
                                isAdmin?.let{
                                    CicilanScreen(supabase, navController, uname, it)
                                }
                            }
                        }
                        composable("ajuan/{username}"){navBackStackEntry->
                            val username = navBackStackEntry.arguments?.getString("username")
                            username?.let{uname->
                                AjuanScreen(supabase, navController, uname)
                            }
                        }
                        composable("profile/{username}"){navBackStackEntry->
                            val username = navBackStackEntry.arguments?.getString("username")
                            username?.let{uname->
                                ProfileScreen(supabase, navController, uname)
                            }
                        }
                    }
                }
            }
        }
    }
}


package mtid.android.simpanpinjam.data.remote

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

class Supabase {
    val client = createSupabaseClient(
        supabaseUrl = "https://jyjmwklszvoihlpdanxa.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imp5am13a2xzenZvaWhscGRhbnhhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDM4MjI3ODQsImV4cCI6MjAxOTM5ODc4NH0.frAvbwNpbmcyQl0i2JfpGL7RX5GyksF30puo4TajlRQ"
    ){
        install(Postgrest)
        install(Realtime)
    }
}
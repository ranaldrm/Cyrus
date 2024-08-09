package com.example.cyrusflashcards.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

class CyrusSupaBase {

    val supabase = createSupabaseClient(
        supabaseUrl = "https://kosrnnsxhlmqjtiiywmr.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imtvc3JubnN4aGxtcWp0aWl5d21yIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjMwMjE1NDUsImV4cCI6MjAzODU5NzU0NX0.QKhNA5tbDAtiA1JPmWrE_ENCgZBMA74pmca5iKy77cE"
    ) {
        install(Postgrest)
    }
}
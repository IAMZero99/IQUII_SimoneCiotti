package com.example.iquii

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.inputmethod.InputBinding
import androidx.fragment.app.Fragment
import com.example.iquii.databinding.ActivityMainBinding
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        super.onCreate(savedInstanceState)
        // Inizializzazione MainActivity, creazione del menu di navigazione e collegamento ai fragment principali.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadfragment(Pokelist())

        binding.navigation.setOnItemSelectedListener {

            when(it.itemId){

                R.id.pokedek -> loadfragment(Pokelist())
                R.id.pokemon -> loadfragment(favourite())
            }
            true
        }
    }

    private fun loadfragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_1, fragment)
        fragmentTransaction.commit()

    }

}
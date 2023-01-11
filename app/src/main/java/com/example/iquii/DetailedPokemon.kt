package com.example.iquii

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.*

class DetailedPokemon : AppCompatActivity() {


    private lateinit var statsData: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_pokemon)

        //creazione delle variabili e inizializzazione degli item nel layout di dettaglio

        val name: TextView = findViewById(R.id.name)
        val id: TextView = findViewById(R.id.id)
        val image: ImageView = findViewById(R.id.image)

        //richiamo dei dati passati tramite intent per reinserirli nel layout
        val bundle : Bundle? = intent.extras
        val namePort = bundle!!.getString("name")
        val idPort = bundle.getString("code")
        val imgPort = bundle.getString("img")
        //inserimento valore nelle variabili
        name.setText(namePort)
        id.setText(idPort)
        //utilizzo della libreria glide per il caricamento dell'immagine tramite Link
        Glide.with(this).load(imgPort).into(image)
        //inizializzazione riferimento al database, nel ramo dell'item di riferimento, eseguito tramite nome.
        statsData = FirebaseDatabase.getInstance("https://iquii-9b747-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Pokemon/"+namePort+"/stats")
        //richiamo funzione loadBars()
        loadBars()

    }

    private fun loadBars() {
        //creazioine delle ProgressBar
        val hp : ProgressBar
        val attack : ProgressBar
        val defense : ProgressBar
        val specialAttack : ProgressBar
        val specialDefense : ProgressBar
        val speed : ProgressBar
        //inizializzazione progressBar con riferimento al layout del dettaglio
        hp = findViewById(R.id.hp_bar)
        attack = findViewById(R.id.attack_bar)
        defense = findViewById(R.id.defense_bar)
        specialAttack = findViewById(R.id.special_attack_bar)
        specialDefense = findViewById(R.id.special_defense_bar)
        speed = findViewById(R.id.speed_bar)
        //inizializzazione valori massimo di 100
        hp.max = 100
        attack.max = 100
        defense.max = 100
        specialAttack.max = 100
        specialDefense.max = 100
        speed.max = 100


        statsData.get().addOnSuccessListener {
            if (it.exists()){
                //se il collegamento al database riesce le barre vengono riempite con i valori impostati nel database
                val hpValFire = it.child("00/hp").value
                val hpVal = Integer.parseInt(hpValFire.toString())
                hp.setProgress(hpVal)
                val attackValFire = it.child("01/attack").value
                val attackVal = Integer.parseInt(attackValFire.toString())
                attack.setProgress(attackVal)
                val defenseValFire = it.child("02/defense").value
                val defenseVal = Integer.parseInt(defenseValFire.toString())
                defense.setProgress(defenseVal)
                val specialAttackValFire = it.child("03/special-attack").value
                val specialAttackVal = Integer.parseInt(specialAttackValFire.toString())
                specialAttack.setProgress(specialAttackVal)
                val specialDefenseValFire = it.child("04/special-defense").value
                val specialDefenseVal = Integer.parseInt(specialDefenseValFire.toString())
                specialDefense.setProgress(specialDefenseVal)
                val speedValFire = it.child("05/speed").value
                val speedVal = Integer.parseInt(speedValFire.toString())
                speed.setProgress(speedVal)
            }
        }




    }
}
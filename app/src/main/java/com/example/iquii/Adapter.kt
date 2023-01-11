package com.example.iquii

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.*

//creazione classe adapter con inizializzazione della lista array e del collegamento al Fragment Pokelist tramite Context
class Adapter(private val List: ArrayList<Item>,  private val context: Pokelist): RecyclerView.Adapter<Adapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        //Istanza della visualizzazione nel layout "item_layout"
        val Viewitem = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return Holder(Viewitem)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        //inizializzazione lista con riferimento alla lista Item, inizializzazione di un collegamento al database per controllare il valore dei preferiti
        val thisItem = List[position]
        val togg : DatabaseReference
        togg = FirebaseDatabase.getInstance("https://iquii-9b747-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Pokemon/"+thisItem.name)

        //se il collegamento esiste viene controllato il valore nel ramo /favourite, quale se corrispondente a 0 setta l'immagini di una pokeball vuota, altrimenti verra' inserita una pokemon piena

        togg.get().addOnSuccessListener {
            if(it.exists()){
                val tog = it.child("/favourite").value.toString()
                val toggInt = Integer.parseInt(tog)
                if (toggInt == 0){ holder.togg.setImageResource(R.drawable.pokeball_icon_136305) }
                else { holder.togg.setImageResource(R.drawable.poke_full) }
            }
        }
        //inizializzazioine delle variabili tramite holder, con riferimento nella lista Item

        holder.code.text = thisItem.code
        holder.name.text = thisItem.name
        //utilizzo della libreria Glide per convertire il link dell'immagine, in un int per ImageView
        Glide.with(context).load(thisItem.image).into(holder.imgPok)
        //Click Listener sull'item con riferimento al Card View
        holder.card.setOnClickListener{
            //quando cliccato si aprira' l'activity del dettaglio, inviando il nome, l'id e la stringa di immagine alla classe, utilizzando transizione in shared element
            val intent =  Intent(context.requireContext(), DetailedPokemon::class.java)
            intent.putExtra("name", thisItem.name)
            intent.putExtra("img", thisItem.image)
            intent.putExtra("code", thisItem.code)
            val option = ActivityOptionsCompat.makeSceneTransitionAnimation(context.requireActivity(),Pair.create(holder.imgPok, "imageTransition"),Pair.create(holder.name, "nameTransition"),Pair.create(holder.code, "codeTransition")  ).toBundle()
            context.startActivity(intent, option)
        }

        //Click Listener sul tasto preferiti
        holder.togg.setOnClickListener{
            //creazione del collegamento ad un nuovo ramo Favourite,  dove saranno salvati i pokemon con valore 1
            val refer : DatabaseReference

            refer = FirebaseDatabase.getInstance("https://iquii-9b747-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Favourite")

            togg.get().addOnSuccessListener {
                if (it.exists()){
                    //controllo per il salvataggio nel ramo Favourite e settaggio del valore.
                    val tog = it.child("/favourite").value.toString()
                    val toggInt = Integer.parseInt(tog)
                    if (toggInt == 0){
                        togg.child("/favourite").setValue(1)
                        holder.togg.setImageResource(R.drawable.poke_full)
                        val poke = Item(thisItem.name, thisItem.code, thisItem.image )
                        refer.child(thisItem.name.toString()).setValue(poke)
                    }
                    else{
                        holder.togg.setImageResource(R.drawable.pokeball_icon_136305)
                        togg.child("/favourite").setValue(0)
                        refer.child(thisItem.name.toString()).removeValue()
                    }
                }
            }



        }


    }

    override fun getItemCount(): Int {
        //Conteggio degli item nella lista tramite .size
        return List.size
    }
//creazione e inizializzazione del ViewHolder dove si fara riferimento agli item nel layout item.layout

    inner class Holder(Viewitem : View) : RecyclerView.ViewHolder(Viewitem) {


        val name: TextView = Viewitem.findViewById(R.id.name)
        val code: TextView = Viewitem.findViewById(R.id.id)
        val imgPok: ShapeableImageView = Viewitem.findViewById(R.id.image)
        val card: CardView = Viewitem.findViewById(R.id.pokecard)
        val togg : ImageView = Viewitem.findViewById(R.id.fav_pok)



    }

}


package com.example.iquii

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair.create
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
//creazione classe FavAdapter, creazione valori Lista ( con riferimento alla lista favPok ), e creazione del context di riferimento al fragment favourite
class FavAdapter(private val FavList: ArrayList<favPoke>, private val cont: favourite):
    RecyclerView.Adapter<FavAdapter.FavHolder>() {


//creazione viewholder con riferimento al layout fav_item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavAdapter.FavHolder {
        val Viewfav = LayoutInflater.from(parent.context).inflate(R.layout.fav_item, parent, false)
        return FavHolder(Viewfav)
    }

    override fun onBindViewHolder(holder: FavAdapter.FavHolder, position: Int) {
        //inizializzazione delle variabili create nel FavHolder e riferimenti alla lista FavPok
        val favItem = FavList[position]
        holder.favName.text = favItem.name
        Glide.with(cont).load(favItem.image).into(holder.favImage)
        //onClick listener sul  CardView dell'item, con apertura dell'attivita' del dettaglio, inizializzazione sharedElement
        holder.pokCard.setOnClickListener{
            val intent =  Intent(cont.requireContext(), DetailedPokemon::class.java)
            intent.putExtra("name", favItem.name)
            intent.putExtra("img", favItem.image)
            val option = ActivityOptionsCompat.makeSceneTransitionAnimation(cont.requireActivity(), create(holder.favImage, "imageTransition"),
                create(holder.favName, "nameTransition")).toBundle()
            cont.startActivity(intent, option)
        }

    }

    override fun getItemCount(): Int {
       return FavList.size
        //numerazione item tramite .size
    }

    //inizializzazione item nel layout fav_item
    inner class FavHolder(Viewfav : View): RecyclerView.ViewHolder(Viewfav){
        val favName: TextView = Viewfav.findViewById(R.id.name)
        val favImage: ImageView = Viewfav.findViewById(R.id.image)
        val pokCard : CardView = Viewfav.findViewById(R.id.Favpokecard)
    }
}
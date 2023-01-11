package com.example.iquii

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iquii.R
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [favourite.newInstance] factory method to
 * create an instance of this fragment.
 */
class favourite : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
//creazione delle variabili con riferimento al database, ad una recyclerview e alla lista item FavPok
    private lateinit var FavRec: RecyclerView
    private lateinit var FavArray: ArrayList<favPoke>
    private lateinit var firedataFav : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment favourite.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            favourite().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //impostazione del layout manager in grid layout
        val Manager = GridLayoutManager(context, 2)

        //inizializzazione delle variabili per la recycler view
        FavRec = view.findViewById(R.id.favlist)
        FavRec.layoutManager = Manager
        FavRec.setHasFixedSize(true)
        FavArray = arrayListOf<favPoke>()

        //richiamo funzione GetFav()
        GetFav()

    }



    private fun GetFav() {
        //inizializzazione al collegamento firebase ramo Favourite, dove sono salvati i pokemon preferiti.
        firedataFav = FirebaseDatabase.getInstance("https://iquii-9b747-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Favourite")
        firedataFav.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    //se vengono aggiornati i dati la lista viene ricaricata
                    FavArray.clear()
                    for (PokeSnapshot in snapshot.children){
                        val Pok = PokeSnapshot.getValue(favPoke::class.java)
                        FavArray.add(Pok!!)
                    }
                    //inizializzazione FavAdapter
                    val adapt = FavAdapter(FavArray, this@favourite)
                    FavRec.adapter = adapt
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}
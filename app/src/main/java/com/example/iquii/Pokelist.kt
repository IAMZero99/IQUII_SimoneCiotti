package com.example.iquii

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Pokelist.newInstance] factory method to
 * create an instance of this fragment.
 */
class Pokelist : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
//Creazione delle variabili della RecyclerView, per la lista dei Pokemon
    private lateinit var recyclerView: RecyclerView
    private lateinit var ArrayList: ArrayList<Item>
    private lateinit var firedata : DatabaseReference


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
        return inflater.inflate(R.layout.fragment_pokelist, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Pokelist.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Pokelist().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager= LinearLayoutManager(context)
//Inizializzazione RecyclerView e dell'Array di caricamento
        recyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = manager
        recyclerView.setHasFixedSize(true)
        //Riferimento alla lista Item
        ArrayList = arrayListOf<Item>()
        //Richiamo funzione caricamento dati nella lista
        getData()

    }



    private fun getData(){
//Inizializzazione dell'istanza per il collegamento al Database, nel ramo Pokemon
        firedata = FirebaseDatabase.getInstance("https://iquii-9b747-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Pokemon")
//EventListener usato per il caricamento
        firedata.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if ( snapshot.exists()){
                    //In caso di aggiornamento dei dati la lista si resetta
                    ArrayList.clear()
                    //caricamento dei dati in for con riferimento alle variabili nella classe Item
                    for (PokSnapshot in snapshot.children){
                        val po = PokSnapshot.getValue(Item::class.java)
                        ArrayList.add(po!!)
                    }
                    //ordinamento item per codice
                    ArrayList.sortBy { it.code }
                    //caricamento e inizializzazione della classe Adapter
                    val adapter = Adapter(ArrayList, this@Pokelist)
                    recyclerView.adapter = adapter


                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }




}
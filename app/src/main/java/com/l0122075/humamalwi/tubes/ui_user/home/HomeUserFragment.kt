package com.l0122075.humamalwi.tubes.ui_user.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.l0122075.humamalwi.tubes.DataFilm
import com.l0122075.humamalwi.tubes.DetailFilmActivity
import com.l0122075.humamalwi.tubes.MainAdapterAdmin
import com.l0122075.humamalwi.tubes.MainAdapterUser
import com.l0122075.humamalwi.tubes.databinding.FragmentHomeUserBinding

class HomeUserFragment : Fragment() {

    private var _binding: FragmentHomeUserBinding? = null
    private lateinit var filmList: ArrayList<DataFilm>
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var adapter: MainAdapterUser

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        firebaseRef = FirebaseDatabase.getInstance().getReference("film")
        filmList = arrayListOf()

        fetchData()

        binding.homeUser.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
        }



        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchList(newText.toString())
                return true
            }

        })

        return root
    }

    private fun fetchData() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                filmList.clear()
                if (snapshot.exists()){
                    for (Snap in snapshot.children){
                        val list = Snap.getValue(DataFilm::class.java)
                        filmList.add(list!!)
                    }
                    adapter = MainAdapterUser(filmList)
                    binding.homeUser.adapter = adapter

                }

            }

            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this, "error: $error", Toast.LENGTH_SHORT).show()
            }
        })


    }
    fun searchList(text: String) {
        val searchList = ArrayList<DataFilm>()
        for (dataFilm in filmList) {
            if (dataFilm.Judul?.toLowerCase()?.contains(text.toLowerCase()) == true) {
                searchList.add(dataFilm)
            }
        }
        if (::adapter.isInitialized) {
            adapter.searchDataList(searchList)
        } else {
            // Jika adapter belum diinisialisasi, tampilkan pesan log
            Log.e("HomeUserFragment", "Adapter not initialized")
        }
    }





}
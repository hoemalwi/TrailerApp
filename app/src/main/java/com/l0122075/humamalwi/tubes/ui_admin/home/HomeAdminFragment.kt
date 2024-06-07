package com.l0122075.humamalwi.tubes.ui_admin.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.l0122075.humamalwi.tubes.DataFilm
import com.l0122075.humamalwi.tubes.MainAdapterAdmin
import com.l0122075.humamalwi.tubes.databinding.FragmentHomeAdminBinding

class HomeAdminFragment : Fragment() {

    private var _binding: FragmentHomeAdminBinding? = null
    private lateinit var filmList: ArrayList<DataFilm>
    private lateinit var firebaseRef: DatabaseReference
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

        firebaseRef = FirebaseDatabase.getInstance().getReference("film")
        filmList = arrayListOf()

        fetchData()

        binding.homeAdmin.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
        }


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
                }
                val adapter = MainAdapterAdmin(filmList)
                binding.homeAdmin.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this, "error: $error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
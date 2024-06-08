package com.l0122075.humamalwi.tubes.ui_admin.gallery

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
import com.l0122075.humamalwi.tubes.AccountAdapter
import com.l0122075.humamalwi.tubes.DataFilm
import com.l0122075.humamalwi.tubes.DataUser
import com.l0122075.humamalwi.tubes.MainAdapterAdmin
import com.l0122075.humamalwi.tubes.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private lateinit var userList: ArrayList<DataUser>
    private lateinit var firebaseRef: DatabaseReference


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.accountAdmin.layoutManager = LinearLayoutManager(context)
        binding.accountAdmin.setHasFixedSize(true)

        firebaseRef = FirebaseDatabase.getInstance().getReference("users")
        userList = arrayListOf()

        fetchDataUser()

        return root
    }

    private fun fetchDataUser() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){
                userList.clear()
                if (snapshot.exists()){
                    for (Snap in snapshot.children){
                        val list = Snap.getValue(DataUser::class.java)
                        userList.add(list!!)
                    }
                }
                val adapter = AccountAdapter(userList)
                binding.accountAdmin.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this, "error: $error", Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
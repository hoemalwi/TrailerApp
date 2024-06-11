package com.l0122075.humamalwi.tubes.ui_user.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.l0122075.humamalwi.tubes.DataUser
import com.l0122075.humamalwi.tubes.EditProfileActivity
import com.l0122075.humamalwi.tubes.LoginActivity
import com.l0122075.humamalwi.tubes.preferences
import com.l0122075.humamalwi.tubes.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var pref: preferences

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()
        pref = preferences(requireContext())
        firebaseRef = FirebaseDatabase.getInstance().getReference("users")

        fetchUserData()

        binding.edit.setOnClickListener { view ->
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }
        binding.logout.setOnClickListener {
            logout()
        }

        return root
    }

    private fun fetchUserData() {
        val userEmail = pref.prefEmail

        if (userEmail != null) {
            firebaseRef.orderByChild("email").equalTo(userEmail)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (Snap in snapshot.children) {
                                val user = Snap.getValue(DataUser::class.java)
                                if (user != null) {
                                    displayUserData(user)
                                }
                            }
                        } else {

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayUserData(user: DataUser) {
        binding.usrname.text = user.username
        binding.email.text = user.email
        binding.umur.text = user.umur.toString()
        binding.telp.text = user.notelp
        Picasso.get().load(user.img).into(binding.img)
    }
    fun logout(){
        pref.prefClear()
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }

}

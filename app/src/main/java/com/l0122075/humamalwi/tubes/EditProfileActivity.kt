package com.l0122075.humamalwi.tubes


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.l0122075.humamalwi.tubes.DataUser
import com.l0122075.humamalwi.tubes.databinding.ActivityEditProfileBinding
import com.l0122075.humamalwi.tubes.preferences
import com.l0122075.humamalwi.tubes.ui_user.notifications.ProfileFragment
import com.squareup.picasso.Picasso
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var pref: preferences
    private var selectedImageUri: Uri? = null
    private lateinit var pickImage: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        pref = preferences(this)
        firebaseRef = FirebaseDatabase.getInstance().getReference("users")
        pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                binding.image.setImageURI(it)
            }
        }

        loadUserData()

        binding.addButton.setOnClickListener {
            saveUserData()
        }

        binding.image.setOnClickListener {
            pickImage.launch("image/*")
        }
    }

    private fun loadUserData() {
        val userEmail = pref.prefEmail

        if (userEmail != null) {
            firebaseRef.orderByChild("email").equalTo(userEmail)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (snap in snapshot.children) {
                                val user = snap.getValue(DataUser::class.java)
                                if (user != null) {
                                    displayUserData(user)
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "No user data found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Error: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayUserData(user: DataUser) {
        binding.usrname.setText(user.username)
        binding.Telp.setText(user.notelp)
        binding.umur.setText(user.umur?.toString())
        if (!user.img.isNullOrEmpty()) {
            Picasso.get().load(user.img).into(binding.image)
        } else {
            // Set a placeholder or default image if the image URL is empty or null
            binding.image.setImageResource(R.drawable.ic_launcher_background) // Use your own default image resource
        }
    }

    private fun saveUserData() {
        val username = binding.usrname.text.toString()
        val notelp = binding.Telp.text.toString()
        val umur = binding.umur.text.toString().toIntOrNull()

        if (username.isEmpty() || notelp.isEmpty() || umur == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userEmail = pref.prefEmail

        if (userEmail != null) {
            firebaseRef.orderByChild("email").equalTo(userEmail)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (snap in snapshot.children) {
                                val userRef = snap.ref
                                userRef.child("username").setValue(username)
                                userRef.child("notelp").setValue(notelp)
                                userRef.child("umur").setValue(umur)

                                selectedImageUri?.let {
                                    val storageRef = FirebaseStorage.getInstance()
                                        .getReference("user_images/${UUID.randomUUID()}")
                                    storageRef.putFile(it)
                                        .addOnSuccessListener { task ->
                                            task.metadata!!.reference!!.downloadUrl
                                                .addOnSuccessListener { url ->
                                                    userRef.child("img").setValue(url.toString())
                                                    Toast.makeText(
                                                        this@EditProfileActivity,
                                                        "Data and photo saved",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    // Auto-refresh fragment setelah data disimpan
                                                    navigateToProfileFragment()
                                                }
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                this@EditProfileActivity,
                                                "Photo upload failed",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                } ?: run {
                                    Toast.makeText(
                                        this@EditProfileActivity,
                                        "Data saved",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // Auto-refresh fragment setelah data disimpan
                                    navigateToProfileFragment()
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "No user data found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Error: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToProfileFragment() {
        onBackPressed()

    }
}

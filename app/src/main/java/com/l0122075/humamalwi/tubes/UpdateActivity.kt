package com.l0122075.humamalwi.tubes

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.l0122075.humamalwi.tubes.databinding.ActivityUpdateBinding
import com.squareup.picasso.Picasso

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private lateinit var film: DataFilm
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        film = intent.getParcelableExtra("film")!!
        firebaseRef = FirebaseDatabase.getInstance().getReference("film")
        storageRef = FirebaseStorage.getInstance().getReference("images")

        binding.judul.setText(film.Judul)
        binding.sinopsis.setText(film.Sinopsis)
        binding.tahun.setText(film.TahunRilis.toString())
        binding.sutradara.setText(film.Sutradara)
        binding.durasi.setText(film.Durasi.toString())
        binding.genre.setText(film.Genre)
        Picasso.get().load(film.imgfilm).into(binding.image)

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.image.setImageURI(it)
            if (it != null) {
                imageUri = it
            }
        }

        binding.image.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.buttonUpdate.setOnClickListener {
            updateFilm()
        }
    }

    private fun updateFilm() {
        val updatedTitle = binding.judul.text.toString()
        val updatedSinopsis = binding.sinopsis.text.toString()
        val tahun = binding.tahun.text.toString()
        val sutradara = binding.sutradara.text.toString()
        val durasi = binding.durasi.text.toString()
        val genre = binding.genre.text.toString()

        film.Judul = updatedTitle
        film.Sinopsis = updatedSinopsis
        film.TahunRilis = tahun.toInt()
        film.Sutradara = sutradara
        film.Durasi = durasi.toInt()
        film.Genre = genre

        if (imageUri != null) {
            val ref = storageRef.child(film.id.toString())
            ref.putFile(imageUri!!).addOnSuccessListener { task ->
                task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    film.imgfilm = uri.toString()
                    saveFilmData()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        } else {
            saveFilmData()
        }
    }

    private fun saveFilmData() {
        film.id?.let {
            firebaseRef.child(it).setValue(film).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Data successfully updated", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity after update
                } else {
                    Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
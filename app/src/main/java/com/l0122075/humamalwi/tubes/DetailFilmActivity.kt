package com.l0122075.humamalwi.tubes

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.l0122075.humamalwi.tubes.databinding.ActivityDetailFilmBinding
import com.l0122075.humamalwi.tubes.databinding.ActivityUpdateBinding
import com.squareup.picasso.Picasso

class DetailFilmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFilmBinding
    private lateinit var film: DataFilm
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        film = intent.getParcelableExtra("film")!!
        firebaseRef = FirebaseDatabase.getInstance().getReference("film")
        storageRef = FirebaseStorage.getInstance().getReference("images")

        binding.detailJudul.setText(film.Judul)
        binding.detailSinopsis.setText(film.Sinopsis)
        binding.detailTahunRilis.setText(film.TahunRilis.toString())
        binding.detailSutradara.setText(film.Sutradara)
        binding.detailDurasi.setText(film.Durasi.toString())
        binding.detailGenre.setText(film.Genre)
        Picasso.get().load(film.imgfilm).into(binding.detailImage)
    }
}

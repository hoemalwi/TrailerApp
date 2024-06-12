package com.l0122075.humamalwi.tubes

import android.content.Intent
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
import com.l0122075.humamalwi.tubes.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddBinding
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseRef  = FirebaseDatabase.getInstance().getReference("film")
        storageRef = FirebaseStorage.getInstance().getReference("images")

        binding.addButton.setOnClickListener(){
            savedata()
        }


        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()){
            binding.image.setImageURI(it)
            if (it != null){
                uri = it
            }

        }

        binding.image.setOnClickListener {
            pickImage.launch("image/*")
        }

    }

    private fun savedata() {
        val Judul = binding.editText0.text.toString()
        val Sinopsis = binding.editText1.text.toString()
        val TahunRilisText = binding.editText2.text.toString()
        val Sutradara = binding.editText3.text.toString()
        val DurasiText = binding.editText4.text.toString()
        val Genre = binding.editText5.text.toString()
        val LinkYT = binding.editText6.text.toString()


        val TahunRilis = TahunRilisText.toInt()
        val Durasi =  DurasiText.toInt()


        val FilmID = firebaseRef.push().key!!
        var Film: DataFilm

        uri?.let {
            storageRef.child(FilmID).putFile(it)
                .addOnSuccessListener {task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { url ->
                            Toast.makeText(this, "Foto Berhasil Ditambah", Toast.LENGTH_SHORT).show()
                            val img =  url.toString()

                            Film = DataFilm(FilmID, LinkYT, img, Judul, Sinopsis, TahunRilis, Sutradara, Durasi, Genre)
                            firebaseRef.child(FilmID).setValue(Film)
                                .addOnCompleteListener{
                                    Toast.makeText(this, "Data Sukses Ditambahkan", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, AdminActivity::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Data Tidak Dapat Ditambahkan", Toast.LENGTH_SHORT).show()
                                }
                        }
                }
        }


    }
}
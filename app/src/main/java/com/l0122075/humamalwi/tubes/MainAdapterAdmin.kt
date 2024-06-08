package com.l0122075.humamalwi.tubes

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.l0122075.humamalwi.tubes.databinding.ListAdminBinding
import com.squareup.picasso.Picasso

class MainAdapterAdmin(private val list: ArrayList<DataFilm>): RecyclerView.Adapter<MainAdapterAdmin.ViewHolder>() {
    class ViewHolder(val binding: ListAdminBinding):
        RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return MainAdapterAdmin.ViewHolder(
            ListAdminBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem  =list[position]
        holder.apply {
            binding.apply {
                title.text = currentItem.Judul
                description.text = currentItem.Sinopsis
                Picasso.get().load(currentItem.imgfilm).into(image)

                root.setOnClickListener {
                    val context = root.context
                    val intent = Intent(context, UpdateActivity::class.java)
                    intent.putExtra("film", currentItem)
                    context.startActivity(intent)
                }

                root.setOnLongClickListener{
                    MaterialAlertDialogBuilder(holder.itemView.context)
                        .setTitle("Hapus Item")
                        .setMessage("Yakin Untuk Menghapus Item Secara Permanen?")
                        .setPositiveButton("Ya"){_,_ ->
                            val firebaseRef = FirebaseDatabase.getInstance().getReference("film")
                            firebaseRef.child(currentItem.id.toString()).removeValue()
                                .addOnSuccessListener {
                                    deletePhotoFromStorage(holder, currentItem.imgfilm.toString())
                                    Toast.makeText(holder.itemView.context, "Item Berhasil Di Hapus", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener{error ->
                                    Toast.makeText(holder.itemView.context, "Item Gagal Di Hapus| Error: ${error.message}", Toast.LENGTH_SHORT).show()
                                }

                        }
                        .setNegativeButton("Tidak"){_,_ ->


                        }
                        .show()
                    return@setOnLongClickListener true
                }

            }
        }
    }

    private fun deletePhotoFromStorage(holder: ViewHolder, photoUrl: String) {
        val firebaseStorage = FirebaseStorage.getInstance()
        val storageRef = firebaseStorage.getReferenceFromUrl(photoUrl)
        storageRef.delete().addOnSuccessListener {

        }.addOnFailureListener { exception ->
            Toast.makeText(holder.itemView.context, "Gagal Menghapus Foto: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
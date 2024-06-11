package com.l0122075.humamalwi.tubes

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.l0122075.humamalwi.tubes.databinding.ListAdminBinding
import com.l0122075.humamalwi.tubes.databinding.ListUserBinding
import com.squareup.picasso.Picasso

class MainAdapterUser(private var list: ArrayList<DataFilm>): RecyclerView.Adapter<MainAdapterUser.ViewHolder>() {
    class ViewHolder(val binding: ListUserBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return MainAdapterUser.ViewHolder(
            ListUserBinding.inflate(
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
                Picasso.get().load(currentItem.imgfilm).into(image)

                root.setOnClickListener {
                    val context = root.context
                    val intent = Intent(context, DetailFilmActivity::class.java)
                    intent.putExtra("film", currentItem)
                    context.startActivity(intent)
                }
            }

        }
    }

    fun searchDataList(searchList: ArrayList<DataFilm>){
        list = searchList
        notifyDataSetChanged()

    }
}
package com.l0122075.humamalwi.tubes

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.l0122075.humamalwi.tubes.databinding.ListAdminBinding
import com.l0122075.humamalwi.tubes.databinding.ListUserBinding
import com.squareup.picasso.Picasso

class MainAdapterUser(
    private var list: ArrayList<DataFilm>,
    private val showLatestTen: Boolean,
    private val showLatestFive: Boolean,
    private val viewType: Int):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {

        if (showLatestTen) {
            list = getLatestTenItems(list)
            list.reverse()
        }

        if (showLatestFive){
            list = getLatestFiveItems(list)
            list.reverse()
        }
        list.reverse()
    }
    companion object {
        const val VIEW_TYPE_USER = 0
        const val VIEW_TYPE_ADMIN = 1
    }

    class UserViewHolder(val binding: ListUserBinding): RecyclerView.ViewHolder(binding.root) {

    }
    class AdminViewHolder(val binding: ListAdminBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val binding = ListUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                UserViewHolder(binding)
            }
            VIEW_TYPE_ADMIN -> {
                val binding = ListAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AdminViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = list[position]
        when (holder) {
            is UserViewHolder -> {
                holder.binding.apply {
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
            is AdminViewHolder -> {
                holder.binding.apply {
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
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }


    private fun getLatestTenItems(dataList: ArrayList<DataFilm>): ArrayList<DataFilm> {
        return if (dataList.size > 10) {
            ArrayList(dataList.takeLast(10).reversed())
        } else {
            dataList.reversed() as ArrayList<DataFilm>
        }
    }

    private fun getLatestFiveItems(dataList: ArrayList<DataFilm>): ArrayList<DataFilm> {
        return if (dataList.size > 5) {
            ArrayList(dataList.takeLast(5).reversed())
        } else {
            dataList.reversed() as ArrayList<DataFilm>
        }
    }

    fun searchDataList(searchList: ArrayList<DataFilm>){
        list = searchList
        notifyDataSetChanged()

    }
}
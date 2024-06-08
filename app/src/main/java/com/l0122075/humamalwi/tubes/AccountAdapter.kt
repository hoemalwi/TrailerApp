package com.l0122075.humamalwi.tubes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.l0122075.humamalwi.tubes.databinding.ListAccountBinding


class AccountAdapter(private val list: ArrayList<DataUser>): RecyclerView.Adapter<AccountAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListAccountBinding):
        RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return AccountAdapter.ViewHolder(
            ListAccountBinding.inflate(
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
                userId.text = currentItem.id
                userEmail.text = currentItem.email
            }
        }
    }
}
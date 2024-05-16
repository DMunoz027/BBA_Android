package com.doris.bba_android.ui.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doris.bba_android.R
import com.doris.bba_android.model.BabyModel
import com.doris.bba_android.ui.home.ui.adapters.holders.BabyViewHolder

class BabyAdapter(private val babiesList: List<BabyModel>) :
    RecyclerView.Adapter<BabyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyViewHolder =
        BabyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.baby_item, parent, false)
        )

    override fun getItemCount(): Int = babiesList.size

    override fun onBindViewHolder(holder: BabyViewHolder, position: Int) =
        holder.render(babiesList[position])

}
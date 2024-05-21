package com.doris.bba_android.ui.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.doris.bba_android.R
import com.doris.bba_android.model.BabyCareModel
import com.doris.bba_android.ui.home.ui.adapters.holders.BabyCareViewHolder

class BabyCareAdapter(private val babyCareList: List<BabyCareModel>) :
    RecyclerView.Adapter<BabyCareViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BabyCareViewHolder =
        BabyCareViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_baby_care, parent, false)
        )

    override fun getItemCount(): Int = babyCareList.size

    override fun onBindViewHolder(holder: BabyCareViewHolder, position: Int) =
        holder.render(babyCareList[position])



}
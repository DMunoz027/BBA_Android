package com.doris.bba_android.ui.home.ui.adapters.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doris.bba_android.databinding.BabyItemBinding
import com.doris.bba_android.model.BabyModel

class BabyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = BabyItemBinding.bind(view)
    fun render(babyModel: BabyModel) {
        binding.itemBaby.setOnClickListener { }
        binding.nameBaby.text = babyModel.babyName

        Glide.with(binding.photoBaby)
            .load(babyModel.babyPhoto)
            .into(binding.photoBaby)

    }
}
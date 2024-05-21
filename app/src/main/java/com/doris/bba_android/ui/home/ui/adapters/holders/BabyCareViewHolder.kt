package com.doris.bba_android.ui.home.ui.adapters.holders

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.doris.bba_android.databinding.ItemBabyCareBinding
import com.doris.bba_android.model.BabyCareModel

class BabyCareViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemBabyCareBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun render(babyCareModel: BabyCareModel) {
        binding.tvStage.text = babyCareModel.stage
        binding.tvStageResume.text = babyCareModel.resume
        binding.tvActivities.text = " * ${babyCareModel.activities?.joinToString("\n\n* ")}"
        binding.tvCareTips.text =
            " * ${babyCareModel.careTips?.joinToString("\n\n * ")}"
        binding.tvFeedingTips.text =
            " * ${babyCareModel.feedingTips?.joinToString("\n\n * ")}"
        binding.tvSleepTips.text =
            " * ${babyCareModel.sleepTips?.joinToString("\n\n * ")}"
    }
}
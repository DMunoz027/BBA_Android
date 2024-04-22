package com.doris.bba_android.ui.common

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.doris.bba_android.R
import com.doris.bba_android.databinding.LayoutAlertLoadingBinding

class DialogUi(
    context: Context,
    private val anim: Int,
    private val title: Int,
    private val message: Int,
    private val actionCode: Int = 1,
    private val handleNextAction: () -> Unit
) : Dialog(context, R.style.TransparentDialog) {
    private lateinit var binding: LayoutAlertLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutAlertLoadingBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        binding.animLottie.setAnimation(anim)
        binding.titleAlert.text = context.getString(title)
        binding.messageAlert.text = context.getString(message)

        when (actionCode) {
            2 -> {
                binding.btnAction.visibility = View.VISIBLE
                binding.btnAction.text =
                    context.getString(R.string.continue_action)
            }

            3 -> {
                binding.btnAction.visibility = View.VISIBLE
                binding.btnAction.text = context.getString(R.string.try_again)
            }

            else -> {
                binding.btnAction.visibility = View.GONE
            }
        }

        binding.btnAction.setOnClickListener {
            dismiss()
            handleNextAction.invoke()
        }

    }
}
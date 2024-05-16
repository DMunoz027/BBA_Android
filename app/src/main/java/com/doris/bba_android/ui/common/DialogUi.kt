package com.doris.bba_android.ui.common

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.doris.bba_android.R
import com.doris.bba_android.databinding.LayoutAlertLoadingBinding

class DialogUi(
    context: Context
) : Dialog(context, R.style.TransparentDialog) {
    private lateinit var _binding: LayoutAlertLoadingBinding
    private lateinit var binding2: LayoutAlertLoadingBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = LayoutAlertLoadingBinding.inflate(LayoutInflater.from(context))
        setContentView(_binding.root)
    }

    fun update(anim: Int, title: Int, message: Int, actionCode: Int) {
        binding2 = LayoutAlertLoadingBinding.inflate(LayoutInflater.from(context))
        binding2.animLottie.setAnimation(anim)
        binding2.titleAlert.text = context.getString(title)
        binding2.messageAlert.text = context.getString(message)

        when (actionCode) {
            2 -> {
                binding2.btnAction.visibility = View.VISIBLE
                binding2.btnAction.text =
                    context.getString(R.string.continue_action)
            }

            3 -> {
                binding2.btnAction.visibility = View.VISIBLE
                binding2.btnAction.text = context.getString(R.string.try_again)
            }

            else -> {
                binding2.btnAction.visibility = View.GONE
            }
        }
    }

    fun show(action: () -> Unit) {
        binding2.btnAction.setOnClickListener {
            dismiss()
            action.invoke()
        }
        show()
    }

    fun dismissAlert() {
        dismiss()
        cancel()
    }
}

package com.example.hokenbox.resource.customView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.hokenbox.application.base.BaseDialogFragment
import com.example.hokenbox.databinding.CustomDialogFragmentBinding
import javax.inject.Inject

class ProgressView @Inject constructor() : BaseDialogFragment<CustomDialogFragmentBinding>() {

    override fun setupView() {
        super.setupView()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): CustomDialogFragmentBinding {
        return CustomDialogFragmentBinding.inflate(inflater, container, false)
    }
}
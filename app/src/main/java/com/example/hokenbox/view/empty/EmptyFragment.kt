package com.example.hokenbox.view.empty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.hokenbox.R
import com.example.hokenbox.application.base.BaseVMFragment
import com.example.hokenbox.application.base.BaseViewModel
import com.example.hokenbox.application.base.popTo
import com.example.hokenbox.application.base.pushTo
import com.example.hokenbox.databinding.FragmentEmptyBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class EmptyFragment : BaseVMFragment<EmptyFragmentViewModel, FragmentEmptyBinding>() {

    override val viewModel: EmptyFragmentViewModel by viewModels()

    override fun makeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        super.makeViewBinding(inflater, container, savedInstanceState)
        binding = FragmentEmptyBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()
        binding.root.setOnClickListener {
            pushTo(R.id.actionToEmptyFragment)
        }

        binding.header.setOnBackButtonClick { popTo() }
    }
}

@HiltViewModel
class EmptyFragmentViewModel @Inject constructor() : BaseViewModel() {

}
package com.example.hokenbox.application.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hokenbox.databinding.CollectionDemoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PagerFragment : BaseFragment<CollectionDemoBinding>() {

    private val fragments: List<Fragment> = emptyList()

    private lateinit var collectionAdapter: CollectionPagerAdapter

    val currentSelectedPosition: Int
        get() = binding.pager.currentItem

    override fun makeViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        super.makeViewBinding(inflater, container, savedInstanceState)
        binding = CollectionDemoBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()
        collectionAdapter = CollectionPagerAdapter(this)
        binding.pager.isSaveEnabled = false
        binding.pager.isUserInputEnabled = false
        binding.pager.offscreenPageLimit = fragments.size
        binding.pager.adapter = collectionAdapter
        collectionAdapter.setUpFragment(fragments = fragments)
        binding.pager.setCurrentItem(0, false)
    }

    fun setCurrentItem(index: Int, scrollEnable: Boolean = false) {
        binding.pager.setCurrentItem(index, scrollEnable)
    }

    fun getFragment(index: Int): Fragment {
        return fragments[index]
    }
}
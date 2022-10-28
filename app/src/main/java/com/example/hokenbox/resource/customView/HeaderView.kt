package com.example.hokenbox.resource.customView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import com.example.hokenbox.R
import com.example.hokenbox.databinding.ViewHeaderBinding

class HeaderView : FrameLayout {

    var visibleBack: Boolean = false
        set(value) {
            binding.groupBack.visibility = if (value) View.VISIBLE else View.GONE
            field = value
        }

    var title: String = ""
        set(value) {
            binding.textView.text = value
            field = value
        }

    constructor(context: Context) : super(context) {
        initView(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(attrs, 0)
    }

    constructor(
        context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initView(attrs, defStyleAttr)
    }

    lateinit var binding: ViewHeaderBinding

    private fun initView(attrs: AttributeSet?, @AttrRes defStyleAttr: Int) {
        binding = ViewHeaderBinding.inflate(LayoutInflater.from(context), this, true)
        val customAttributesStyle =
            context.obtainStyledAttributes(attrs, R.styleable.HeaderView, defStyleAttr, 0)

        try {
            title = customAttributesStyle.getText(R.styleable.HeaderView_title).toString()
            visibleBack = customAttributesStyle.getBoolean(
                R.styleable.HeaderView_visible_back_button,
                false
            )
        } finally {
            customAttributesStyle.recycle()
        }
    }

    fun setOnBackButtonClick(onclick: () -> Unit) {
        binding.button.setOnClickListener { onclick.invoke() }
    }

}
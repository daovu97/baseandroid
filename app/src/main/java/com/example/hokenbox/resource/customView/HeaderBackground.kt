package com.example.hokenbox.resource.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.hokenbox.R
import com.example.hokenbox.resource.utils.asPixels


class HeaderBackground(
    context: Context,
    attrs: AttributeSet
) : View(context, attrs) {

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.primary_color)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        canvas?.drawCustomPath(
            RectF(canvas.clipBounds),
            context.resources.getDimension(R.dimen.header_radius) * 2,
            paint
        )
    }

}

fun Canvas.drawCustomPath(
    rectF: RectF,
    radius: Float,
    paint: Paint
) {
    val path = Path()
    path.moveTo(rectF.left, rectF.bottom)
    path.lineTo(rectF.left, rectF.top)
    path.lineTo(rectF.right, rectF.top)
    path.lineTo(rectF.right, rectF.bottom)
    path.quadTo(rectF.right / 2, rectF.bottom - radius, rectF.left, rectF.bottom)
    path.lineTo(rectF.left, rectF.bottom)
    path.close()
    drawPath(path, paint)
}
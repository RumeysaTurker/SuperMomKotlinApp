package com.rumeysaturker.supermomkotlinapp.utils
import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

class GridImageView : AppCompatImageView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

//resmin genişliği neyse yüksekliği de o olsun.(Resmi ölçeklendirme)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}
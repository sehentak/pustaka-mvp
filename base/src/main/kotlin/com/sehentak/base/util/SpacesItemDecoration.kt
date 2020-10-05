package com.sehentak.base.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(private val space: Int, private val spanCount: Int?): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (spanCount == 2) outRect.set(space * 2, space, space * 2, space)
        else outRect.set(space, space, space, space)
    }
}
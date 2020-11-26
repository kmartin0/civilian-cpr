package com.km.civilian.cpr.ui.messagesList.adapter

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.km.civilian.cpr.R

class MessageItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    /**
     * Set the spacing between message items.
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spacingHorizontal = context.resources.getDimension(R.dimen.key_line_3).toInt()
        val spacingVertical = context.resources.getDimension(R.dimen.key_line_3).toInt()

        outRect.set(spacingHorizontal, spacingVertical, spacingHorizontal, spacingVertical)
    }

}
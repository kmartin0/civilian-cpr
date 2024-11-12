package com.km.civilian.cpr.ui.messagesList.adapter

import androidx.recyclerview.widget.DiffUtil
import com.km.civilian.cpr.model.Message

class MessageDiffCallback(private val oldList: List<Message>, private val newList: List<Message>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
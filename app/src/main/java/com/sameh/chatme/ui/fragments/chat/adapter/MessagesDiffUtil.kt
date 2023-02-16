package com.sameh.chatme.ui.fragments.chat.adapter

import androidx.recyclerview.widget.DiffUtil
import com.sameh.chatme.data.model.Message

class MessagesDiffUtil(
    private val oldList: ArrayList<Message?>,
    private val newList: ArrayList<Message?>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]?.message == newList[newItemPosition]?.message
                && oldList[oldItemPosition]?.senderId == newList[newItemPosition]?.senderId
                && oldList[oldItemPosition]?.messageTimeDate == newList[newItemPosition]?.messageTimeDate
    }

}
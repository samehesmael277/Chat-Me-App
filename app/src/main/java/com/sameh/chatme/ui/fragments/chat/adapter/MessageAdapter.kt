package com.sameh.chatme.ui.fragments.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sameh.chatme.R
import com.sameh.chatme.constants.Constants
import com.sameh.chatme.data.model.Message

class MessageAdapter(
    val context: Context,
    private val senderId: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messagesList = ArrayList<Message?>()

    private var messagesDiffUtil: MessagesDiffUtil? = null

    fun setMessagesListToAdapter(newMessagesList: ArrayList<Message?>) {
        messagesDiffUtil = MessagesDiffUtil(
            messagesList,
            newMessagesList
        )
        val diffResult = DiffUtil.calculateDiff(messagesDiffUtil!!)
        this.messagesList = newMessagesList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.SEND_MESSAGE) {
            val view: View = LayoutInflater.from(context).inflate(
                R.layout.send_message,
                parent,
                false
            )
            SendViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(
                R.layout.recieve_message,
                parent,
                false
            )
            ReceiveViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messagesList[position]

        if (holder.javaClass == SendViewHolder::class.java) {
            val viewHolder = holder as SendViewHolder
            holder.sendMessage.text = currentMessage?.message
        } else {
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage?.message
        }
    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = messagesList[position]
        return if (senderId == currentMessage!!.senderId) {
            Constants.RECEIVE_MESSAGE
        } else {
            Constants.SEND_MESSAGE
        }

        //return super.getItemViewType(position)
    }


    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sendMessage: TextView = itemView.findViewById(R.id.tv_send_message)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage: TextView = itemView.findViewById(R.id.tv_receive_message)
    }

}
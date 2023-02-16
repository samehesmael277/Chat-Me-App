package com.sameh.chatme.ui.fragments.newmessage.adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sameh.chatme.data.model.User
import com.sameh.chatme.databinding.UserInRecInNewMessageFragmentBinding
import javax.inject.Inject

class NewMessageAdapter @Inject constructor(
    private val context: Application
) : ListAdapter<User, NewMessageAdapter.ViewHolder>(UsersDiffCallBack()) {

    var onClickListener: OnUserClickListener? = null

    fun setData(usersList: ArrayList<User>) {
        this.submitList(usersList)
    }

    inner class ViewHolder(private val binding: UserInRecInNewMessageFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            if (user.profileImgUrl != null) {
                binding.circleImgUser.load(user.profileImgUrl)
            }
            binding.tvUsername.text = user.username

            binding.constraintRoot.setOnClickListener {
                onClickListener!!.onUserClickListener(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserInRecInNewMessageFragmentBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}

// DifUtil
class UsersDiffCallBack : DiffUtil.ItemCallback<User>() {

    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.email == newItem.email
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

}
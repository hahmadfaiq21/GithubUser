package com.github.hahmadfaiq21.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.hahmadfaiq21.githubuser.data.User
import com.github.hahmadfaiq21.githubuser.databinding.ItemUserBinding

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val list = ArrayList<User>()

    fun setList(users: ArrayList<User>) {
        val oldSize = list.size
        val newSize = users.size

        // Add new users
        if (newSize > oldSize) {
            list.addAll(users.subList(oldSize, newSize))
            notifyItemRangeInserted(oldSize, newSize - oldSize)
        }
        // Remove users
        else if (newSize < oldSize) {
            list.subList(newSize, oldSize).clear()
            notifyItemRangeRemoved(newSize, oldSize - newSize)
        }
        // Update users if sizes are the same
        else {
            list.clear()
            list.addAll(users)
            notifyItemRangeChanged(0, newSize)
        }
    }

    inner class UserViewHolder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                tvUsername.text = user.login
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivUser)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }
}
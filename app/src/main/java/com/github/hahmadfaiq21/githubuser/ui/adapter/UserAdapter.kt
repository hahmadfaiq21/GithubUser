package com.github.hahmadfaiq21.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.hahmadfaiq21.githubuser.data.response.UserResponse
import com.github.hahmadfaiq21.githubuser.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val list = ArrayList<UserResponse>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setList(userResponses: ArrayList<UserResponse>) {
        val oldSize = list.size
        val newSize = userResponses.size

        if (newSize > oldSize) {
            list.addAll(userResponses.subList(oldSize, newSize))
            notifyItemRangeInserted(oldSize, newSize - oldSize)
        } else if (newSize < oldSize) {
            list.subList(newSize, oldSize).clear()
            notifyItemRangeRemoved(newSize, oldSize - newSize)
        } else {
            list.clear()
            list.addAll(userResponses)
            notifyItemRangeChanged(0, newSize)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserResponse)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userResponse: UserResponse) {

            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(userResponse)
            }

            binding.apply {
                tvUsername.text = userResponse.login
                Glide.with(itemView)
                    .load(userResponse.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
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
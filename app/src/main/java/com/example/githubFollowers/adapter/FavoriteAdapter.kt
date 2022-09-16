package com.example.githubFollowers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.githubFollowers.R
import com.example.githubFollowers.models.UserData
import com.example.githubFollowers.viewmodels.MainViewModel
import com.example.githubFollowers.views.ProfileFragment
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
class FavoriteAdapter() :
    ListAdapter<UserData, FavoriteAdapter.ViewHolder>(NetworkDataCallBack2()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val avatar: ImageView = view.findViewById(R.id.ivAvatar)
        private val username: TextView = view.findViewById(R.id.tvUsername)
        fun bind(avatarData: String?, usernameData: String?) {
            avatar.load(avatarData) {
                crossfade(600)
                error(R.drawable.ic_person)
            }
            username.text = usernameData

        }

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.user_layout_vertical, parent, false)
                return ViewHolder(view)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position).avatar_url, getItem(position).login)
    }
}

class NetworkDataCallBack2 : DiffUtil.ItemCallback<UserData>() {

    override fun areItemsTheSame(oldItem: UserData, newItem: UserData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: UserData, newItem: UserData): Boolean {
        return oldItem.name == newItem.name
    }
}
package com.example.githubuser.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.githubuser.R
import com.example.githubuser.service.response.UserGithub

class ListUsersAdapter(
    private val listUsers: List<UserGithub>,
    private val onClick: (UserGithub, View) -> Unit
) : RecyclerView.Adapter<ListUsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_github_users, viewGroup, false)
        )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val username: String? = listUsers[position].login
        val avatar: String? = listUsers[position].avatarUrl

        viewHolder.tvUsername.text = username
        Glide.with(viewHolder.itemView.context)
            .load(avatar)
            .transform(RoundedCorners(20))
            .into(viewHolder.imgGithubUser)

        viewHolder.itemView.setOnClickListener { view ->
            onClick(listUsers[position], view)
        }

    }

    override fun getItemCount() = listUsers.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgGithubUser: ImageView = view.findViewById(R.id.img_github_user)
        val tvUsername: TextView = view.findViewById(R.id.tv_username)
    }

}
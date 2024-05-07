package com.example.skillcinema.ui.profile.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class ProfileDiff : DiffUtil.ItemCallback<ProfileAdapterTypes>() {
    override fun areItemsTheSame(oldItem: ProfileAdapterTypes, newItem: ProfileAdapterTypes) =
        oldItem == newItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: ProfileAdapterTypes,
        newItem: ProfileAdapterTypes
    ) = oldItem == newItem
}
package com.example.githubuser.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String = ""

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = ListUsersFragment()
        fragment.arguments = Bundle().apply {
            putInt(ListUsersFragment.ARG_SECTION_NUMBER, position + 1)
            putString(ListUsersFragment.ARG_USERNAME, username)
        }
        return fragment
    }

}
package com.example.githubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.databinding.FragmentFavoriteBinding
import com.example.githubuser.helper.FavoriteViewModel
import com.example.githubuser.helper.UsersViewModelFactory
import com.example.githubuser.service.response.UserGithub

class FavoriteFragment : Fragment() {
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        UsersViewModelFactory.getInstance(requireActivity().application)
    }
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.apply {
            rvFavoriteFragment.layoutManager = layoutManager
            rvFavoriteFragment.addItemDecoration(itemDecoration)
        }
        favoriteViewModel.getAllFavoriteUsers().observe(viewLifecycleOwner) { listFavoriteUsers ->
            val items = arrayListOf<UserGithub>()
            listFavoriteUsers.map {
                val item = UserGithub(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            setListUsersData(items)
        }

    }

    private fun setListUsersData(listItemUsers: List<UserGithub>) {
        val adapter = ListUsersAdapter(listItemUsers) { user, view ->
            val toDetailFragment =
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment()
            if (user.login != null) toDetailFragment.username = user.login
            view.findNavController().navigate(toDetailFragment)
        }
        binding.rvFavoriteFragment.adapter = adapter
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
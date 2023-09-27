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
import com.example.githubuser.databinding.FragmentListUsersBinding
import com.example.githubuser.helper.ListUsersViewModel
import com.example.githubuser.service.response.UserGithub


class ListUsersFragment : Fragment() {

    private val listUsersViewModel: ListUsersViewModel by viewModels()

    private var _binding: FragmentListUsersBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(ARG_SECTION_NUMBER)
        val username = arguments?.getString(ARG_USERNAME)

        val layoutManager = LinearLayoutManager(activity)
        binding.rvGithubUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.rvGithubUsers.addItemDecoration(itemDecoration)

        if (username != null && !listUsersViewModel.listUsers.isInitialized) {
            if (position == 1) {
                listUsersViewModel.getListFollowers(username)
            } else {
                listUsersViewModel.getListFollowing(username)
            }
        }

        listUsersViewModel.listUsers.observe(viewLifecycleOwner) { listUser ->
            if (listUser != null) {
                setListUsersData(listUser)
            }
        }

        listUsersViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

    }

    private fun setListUsersData(listItemUsers: List<UserGithub>) {

        val adapter = ListUsersAdapter(listItemUsers) { user, view ->
            val toSelfFragment = DetailFragmentDirections.actionDetailFragmentSelf()
            if (user.login != null) toSelfFragment.username = user.login
            view.findNavController().navigate(toSelfFragment)

        }
        binding.rvGithubUsers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"

    }
}
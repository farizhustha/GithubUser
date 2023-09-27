package com.example.githubuser.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.databinding.FragmentHomeBinding
import com.example.githubuser.helper.ListUsersViewModel
import com.example.githubuser.service.response.UserGithub

class HomeFragment : Fragment() {
    private val listUsersViewModel: ListUsersViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!listUsersViewModel.listUsers.isInitialized) listUsersViewModel.getListUsers()

        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.option_menu, menu)

                val searchManager =
                    activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
                val searchView = menu.findItem(R.id.search).actionView as SearchView

                searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
                searchView.queryHint = resources.getString(R.string.search_hint)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String): Boolean {
                        listUsersViewModel.getSearchListUsers(query)
                        searchView.clearFocus()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.favorite_navigation -> {
                        val toFavoriteFragment =
                            HomeFragmentDirections.actionHomeFragmentToFavoriteFragment()
                        findNavController().navigate(toFavoriteFragment)
                        true
                    }
                    R.id.setting_navigation -> {
                        val toSettingActivity =
                            HomeFragmentDirections.actionHomeFragmentToSettingActivity()
                        findNavController().navigate(toSettingActivity)
                        true
                    }
                    else -> true
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val layoutManager = LinearLayoutManager(activity)
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.apply {
            rvListUsersHome.layoutManager = layoutManager
            rvListUsersHome.addItemDecoration(itemDecoration)
        }

        listUsersViewModel.listUsers.observe(viewLifecycleOwner) { listUsers ->
            if (listUsers != null) {
                setListUsersData(listUsers)
            }
        }
        listUsersViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)

        }
    }

    private fun setListUsersData(listItemUsers: List<UserGithub>) {

        val adapter = ListUsersAdapter(listItemUsers) { user, view ->
            val toDetailFragment = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
            if (user.login != null) toDetailFragment.username = user.login
            view.findNavController().navigate(toDetailFragment)
        }
        binding.rvListUsersHome.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                progressBarHome.visibility = View.VISIBLE
            } else {
                progressBarHome.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.example.githubuser.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.database.FavoriteUsers
import com.example.githubuser.databinding.FragmentDetailBinding
import com.example.githubuser.helper.DetailViewModel
import com.example.githubuser.helper.UsersViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator


class DetailFragment : Fragment() {
    private val detailViewModel by viewModels<DetailViewModel> {
        UsersViewModelFactory.getInstance(requireActivity().application)
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()

    private var favoriteUsers: FavoriteUsers? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!detailViewModel.user.isInitialized) detailViewModel.getDetailUser(args.username)

        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.option_menu, menu)

                menu.findItem(R.id.search).isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.favorite_navigation -> {
                        val toFavoriteFragment =
                            DetailFragmentDirections.actionDetailFragmentToFavoriteFragment()
                        findNavController().navigate(toFavoriteFragment)
                        true
                    }
                    R.id.setting_navigation -> {
                        val toSettingActivity =
                            DetailFragmentDirections.actionDetailFragmentToSettingActivity()
                        findNavController().navigate(toSettingActivity)
                        true
                    }
                    else -> true
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity() as AppCompatActivity)
        sectionsPagerAdapter.username = args.username
        binding.apply {
            contentDetail.viewPagerDetail.adapter = sectionsPagerAdapter
            fabDetail.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red
                )
            )
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        detailViewModel.user.observe(viewLifecycleOwner) { user ->
            val follow = listOf(user?.followers, user?.following)

            if (user?.login != null) {
                favoriteUsers = FavoriteUsers(user.login, user.avatarUrl)
                detailViewModel.getFavoriteUserByUsername(user.login)
                    .observe(viewLifecycleOwner) { favorite ->
                        binding.fabDetail.apply {
                            if (favorite == null) {
                                setImageResource(R.drawable.ic_baseline_favorite_border_24)
                                setOnClickListener {
                                    detailViewModel.insert(favoriteUsers as FavoriteUsers)
                                }
                            } else {
                                setImageResource(R.drawable.ic_baseline_favorite_24)
                                setOnClickListener {
                                    detailViewModel.delete(favoriteUsers as FavoriteUsers)
                                }
                            }
                        }

                    }
            }

            binding.contentDetail.apply {
                Glide.with(view).load(user?.avatarUrl)
                    .circleCrop()
                    .into(imgProfileDetail)
                if (user?.name == null) {
                    tvNameDetail.text =
                        user?.login
                } else {
                    tvNameDetail.text = user.name
                }
                tvUsernameDetail.text = user?.login

                TabLayoutMediator(tabsDetail, viewPagerDetail) { tab, position ->
                    tab.text = "${follow[position]} ${resources.getString(TAB_TITLES[position])}"
                }.attach()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.contentDetail.apply {
            if (isLoading) {
                progressBarDetail.visibility = View.VISIBLE
            } else {
                progressBarDetail.visibility = View.GONE
            }
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
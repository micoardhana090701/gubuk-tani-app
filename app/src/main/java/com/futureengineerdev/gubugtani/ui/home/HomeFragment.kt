package com.futureengineerdev.gubugtani.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.ui.profile.ProfileActivity
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.article.ViewModelArticleFactory
import com.futureengineerdev.gubugtani.databinding.FragmentHomeBinding
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.ui.camera.CameraFragment
import com.futureengineerdev.gubugtani.ui.profile.ProfileViewModel
import com.futureengineerdev.gubugtani.viewmodel.AuthViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val homeAdapter = HomeAdapter()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var authViewModel: AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupViewModel()
        setupRecyclerView()
        viewLifecycleOwner.lifecycleScope.launch{
            setupProfileView(view)
        }
        binding.swRefresh.setOnRefreshListener {
            refresh()
        }
    }

    private fun setupViewModel() {

        homeViewModel = ViewModelProvider(this, ViewModelArticleFactory(requireContext()))[HomeViewModel::class.java]
        val searchQuery = "%%"
        homeViewModel.getArticle(searchQuery).observe(viewLifecycleOwner){
            CoroutineScope(Dispatchers.IO).launch {
                homeAdapter.submitData(it)
            }
        }
    }

    private fun refresh(){
        var swipeRefreshLayout = binding.swRefresh
        swipeRefreshLayout.isRefreshing = false
        homeAdapter.refresh()
    }

    override fun onResume() {
        super.onResume()
        homeAdapter.refresh()
        setupViewModel()
    }

    private suspend fun setupProfileView(view: View){
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(requireActivity().application)

        profileViewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]

        val accessToken = pref.getUserKey().first()
        profileViewModel.getProfile(access_token = "Bearer $accessToken")
        profileViewModel.profileUser.observe(viewLifecycleOwner){
            when (it) {
                is Resource.Success -> {
                    showLoading(false)
                    if (it != null){
                        binding.tvNamaUserHome.setText(it.data?.result?.user?.name)
                        val ivProfileHome = binding.ivProfileHome
                        if (it.data?.result?.user?.avatar == null){
                            Glide.with(view.context)
                                .load(R.drawable.baseline_account_circle_24)
                                .centerCrop()
                                .into(ivProfileHome)
                        }
                        else{
                            Glide.with(view.context)
                                .load("https://app.gubuktani.com/storage/" + it.data.result.user.avatar)
                                .centerCrop()
                                .into(ivProfileHome)
                        }
                    }
                    else{
                        val ivProfileHome = binding.ivProfileHome
                        binding.tvNamaUserHome.setText(it?.data?.result?.user?.name)
                        Glide.with(view.context)
                            .load(R.drawable.baseline_account_circle_24)
                            .centerCrop()
                            .into(ivProfileHome)
                    }
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }

                else -> {}
            }

        }
        binding.ivProfileHome.setOnClickListener{
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setupRecyclerView() {

        with(binding.rvArticleHome){
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 3)
            addItemDecoration(ItemSpacingDecoration(2))
            adapter = homeAdapter
        }
    }

    class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.top = spacing
            outRect.bottom = spacing
            outRect.left = spacing
            outRect.right = spacing
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.isLoadingHome.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
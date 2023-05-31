package com.futureengineerdev.gubugtani.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.ui.profile.ProfileActivity
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.article.ViewModelArticleFactory
import com.futureengineerdev.gubugtani.databinding.FragmentHomeBinding
import com.futureengineerdev.gubugtani.etc.UserPreferences
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
    }

    private fun setupViewModel() {

        homeViewModel = ViewModelProvider(this, ViewModelArticleFactory(requireContext()))[HomeViewModel::class.java]

        homeViewModel.getArticle().observe(viewLifecycleOwner){
            CoroutineScope(Dispatchers.IO).launch {
                homeAdapter.submitData(it)
            }
        }
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
        binding.ivProfileHome.setOnClickListener{
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setupRecyclerView() {
        with(binding.rvArticleHome){
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = homeAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
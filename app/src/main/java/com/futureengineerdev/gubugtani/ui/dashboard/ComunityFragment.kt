package com.futureengineerdev.gubugtani.ui.dashboard

import android.content.Context
import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.util.query
import com.futureengineerdev.gubugtani.HomeActivity
import com.futureengineerdev.gubugtani.WriteArticleActivity
import com.futureengineerdev.gubugtani.api.ApiConfig
import com.futureengineerdev.gubugtani.article.ArticleAdapter
import com.futureengineerdev.gubugtani.article.ViewModelArticleFactory
import com.futureengineerdev.gubugtani.databinding.FragmentComunityBinding
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.viewmodel.AuthViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComunityFragment() : Fragment() {

    private var _binding: FragmentComunityBinding? = null
    private val binding get() = _binding!!
    private val articleAdapter = ArticleAdapter()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var comunityViewModel: ComunityViewModel
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComunityBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        setupView()
        binding.swRefreshComunity.setOnRefreshListener {
            refresh()
        }
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(requireActivity().application)

    }

    private fun setupRecyclerView() {
        with(binding.rvArticle){
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
        }
    }

    private fun refresh(){
        var swipeRefreshLayout = binding.swRefreshComunity
        swipeRefreshLayout.isRefreshing = false
        articleAdapter.refresh()
    }

    override fun onResume() {
        super.onResume()
        articleAdapter.refresh()
        setupViewModel()
    }

    private fun setupView() {
        binding.btnWriteArticle.setOnClickListener {
            startActivity(Intent(requireContext(), WriteArticleActivity::class.java))
        }
    }


    private fun setupViewModel() {
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(requireActivity().application)

        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
        comunityViewModel = ViewModelProvider(
            this,
            ViewModelArticleFactory(requireContext())
        )[ComunityViewModel::class.java]

        val searchQuery = binding.searchView.text.toString()
        comunityViewModel.getArticle(searchQuery).observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.IO).launch {
                articleAdapter.submitData(it)
            }
        }

        binding.btnSearch.setOnClickListener {
            val searchQuery = binding.searchView.text.toString().trim()

            comunityViewModel.getArticle(searchQuery).observe(viewLifecycleOwner) {
                CoroutineScope(Dispatchers.IO).launch {
                    articleAdapter.submitData(it)
                }
            }
        }
    }

    private fun showLoading(isLoadingLogin: Boolean){
        binding.isLoadingSearch.visibility = if (isLoadingLogin) View.VISIBLE else View.GONE
        binding.btnSearch.isEnabled = !isLoadingLogin
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

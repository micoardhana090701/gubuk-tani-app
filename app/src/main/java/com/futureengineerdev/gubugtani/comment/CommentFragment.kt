package com.futureengineerdev.gubugtani.comment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.futureengineerdev.gubugtani.databinding.FragmentCommentBinding
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.response.DataItemComment
import com.futureengineerdev.gubugtani.viewmodel.AddCommentViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class CommentFragment : Fragment() {

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!
    private lateinit var commentAdapter: CommentAdapter
    private var article_id: Int? = null
    private lateinit var addCommentViewModel: AddCommentViewModel
    private lateinit var getCommentViewModel: GetCommentViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            article_id = it?.getInt(EXTRA_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        viewLifecycleOwner.lifecycleScope.launch {
            getCommentItem()
        }
    }

    private fun setupView(){
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(requireActivity().application)
        addCommentViewModel = ViewModelProvider(this, viewModelFactory)[AddCommentViewModel::class.java]
        binding.btnSendComment.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                uploadComment()
            }
        }
    }

    private suspend fun uploadComment(){
        try {
            val nullComment = binding.etAddComment.text.toString()
            if (nullComment.isNotEmpty()){
                val comment = nullComment.toRequestBody("text/plain".toMediaType())
                CoroutineScope(Dispatchers.IO).launch {
                    article_id?.let {
                        addCommentViewModel.addComment(article_id = it, comment = comment)
                    }
                }
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Komentar berhasil diupload", Toast.LENGTH_SHORT).show()
                    hideKeyboard()
                    binding.etAddComment.text?.clear()
                }

            }else{
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Komentar tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("CommentFragment", "uploadComment: ${e.message}", )
        }

    }

    private suspend fun getCommentItem(){
        val pref = UserPreferences.getInstance(requireContext().dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(requireActivity().application)
        val accessToken = pref.getUserKey().first()
        getCommentViewModel = ViewModelProvider(this, viewModelFactory)[GetCommentViewModel::class.java]
        article_id?.let {
            getCommentViewModel.getComment(accessToken = "Bearer $accessToken", articleId = it)
            getCommentViewModel.comment.observe(viewLifecycleOwner) {
                commentAdapterInitialize(comment = it.result?.data as List<DataItemComment>)
            }
        }
        binding.swipeRefreshComment.setOnRefreshListener {
            refresh()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun commentAdapterInitialize(comment: List<DataItemComment>){
        commentAdapter = CommentAdapter(comment)
        binding.rvComment.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = commentAdapter
        }
        commentAdapter.notifyDataSetChanged()
        var swipeRefreshLayout = binding.swipeRefreshComment
        swipeRefreshLayout.isRefreshing = false
    }
    private fun refresh(){
        viewLifecycleOwner.lifecycleScope.launch {
            getCommentItem()
        }
    }
    override fun onResume(){
        super.onResume()
        refresh()
    }

    @SuppressLint("ServiceCast")
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etAddComment.windowToken, 0)
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }

}
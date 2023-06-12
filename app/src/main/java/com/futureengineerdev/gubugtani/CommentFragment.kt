package com.futureengineerdev.gubugtani

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.futureengineerdev.gubugtani.databinding.FragmentCommentBinding
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.viewmodel.AddCommentViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class CommentFragment : Fragment() {

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!
    private var article_id: Int? = null
    private lateinit var addCommentViewModel: AddCommentViewModel
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
        val nullComment = binding.etAddComment.text.toString()
        if (nullComment.isEmpty()){
            binding.etAddComment.error = "Komentar tidak boleh kosong"
            binding.etAddComment.requestFocus()
            return
        }else{
            val comment = nullComment.toRequestBody("text/plain".toMediaType())
            CoroutineScope(Dispatchers.IO).launch {
                article_id?.let {
                    addCommentViewModel.addComment(article_id = it, comment = comment)
                }
            }
        }
    }
    companion object {
        const val EXTRA_ID = "extra_id"
    }

}
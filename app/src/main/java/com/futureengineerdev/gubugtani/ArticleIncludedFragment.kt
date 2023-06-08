package com.futureengineerdev.gubugtani

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.futureengineerdev.gubugtani.databinding.FragmentArticleIncludedBinding
import com.futureengineerdev.gubugtani.databinding.FragmentHomeBinding
import com.futureengineerdev.gubugtani.response.DataItem

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ArticleIncludedFragment : Fragment() {

    private var _binding: FragmentArticleIncludedBinding? = null
    private val binding get() = _binding!!
    private var disease: DataItem? = null
    private var frameLayoutVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            disease = it.getParcelable(EXTRA_ARTICLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleIncludedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disease?.let {
            binding.tvJudulPenanganan.text = it.article.title
            binding.tvDeskripsiPenanganan.text = it.article.content
        }
    }



    companion object {
        const val EXTRA_ARTICLE = "extra_article"
    }
}
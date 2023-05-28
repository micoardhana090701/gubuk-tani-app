package com.futureengineerdev.gubugtani.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.futureengineerdev.gubugtani.databinding.FragmentPestisideBinding

class PestisideFragment : Fragment() {

    private var _binding: FragmentPestisideBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val pestisideViewModel =
            ViewModelProvider(this).get(PestisideViewModel::class.java)

        _binding = FragmentPestisideBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        pestisideViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
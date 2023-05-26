package com.futureengineerdev.gubugtani.ui.camera

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.databinding.FragmentCameraBinding
import com.futureengineerdev.gubugtani.databinding.FragmentComunityBinding
import com.futureengineerdev.gubugtani.etc.createCustomTempFile
import com.futureengineerdev.gubugtani.etc.fixImageRotation
import com.futureengineerdev.gubugtani.etc.uriToFile
import com.futureengineerdev.gubugtani.ui.dashboard.ComunityViewModel
import java.io.File

class CameraFragment : Fragment(), View.OnClickListener{

    private var _binding: FragmentCameraBinding? = null
    private lateinit var currentPhotoPath: String
    private val binding get() = _binding!!
    private var getFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cameraViewModel =
            ViewModelProvider(this).get(CameraViewModel::class.java)

        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        with(binding){
            btnTambahFoto.setOnClickListener(this@CameraFragment)
            btnUploadFoto.setOnClickListener(this@CameraFragment)
            btnGalery.setOnClickListener(this@CameraFragment)
        }
    }


    override fun onClick(v: View?) {
        when(v){
            binding.btnTambahFoto -> startCamera()
            binding.btnUploadFoto -> uploadFoto()
            binding.btnGalery -> openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih Foto")
        inGallery.launch(chooser)
    }

    private val inGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == RESULT_OK){
            val setImage: Uri = it.data?.data as Uri
            val myFile = uriToFile(setImage, requireContext().applicationContext)
            binding.ivFotoUploadDeteksi.setImageURI(setImage)
            getFile = myFile
        }
    }

    private fun uploadFoto() {
        Toast.makeText(requireContext().applicationContext, "Foto Telah Terupload", Toast.LENGTH_SHORT).show()
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)
        createCustomTempFile(requireContext().applicationContext).also {
            val photoURI : Uri = FileProvider.getUriForFile(
                requireContext().applicationContext,
                "com.futureengineerdev.gubugtani.fileprovider",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            inCamera.launch(intent)
        }
    }

    private val inCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val myFile = File(currentPhotoPath)
            val options = BitmapFactory.Options()
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath, options)
            myFile.let { file ->
                val fix = fixImageRotation(bitmap, file.path)
                getFile = file
                binding.ivFotoUploadDeteksi.setImageBitmap(fix)
            }
        }
    }
}
package com.futureengineerdev.gubugtani.ui.camera

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.databinding.ActivityHomeBinding
import com.futureengineerdev.gubugtani.databinding.FragmentCameraBinding
import com.futureengineerdev.gubugtani.databinding.FragmentComunityBinding
import com.futureengineerdev.gubugtani.etc.createCustomTempFile
import com.futureengineerdev.gubugtani.etc.fixImageRotation
import com.futureengineerdev.gubugtani.etc.uriToFile
import com.futureengineerdev.gubugtani.ui.dashboard.ComunityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

class CameraFragment : AppCompatActivity(), View.OnClickListener{

    private var _binding: FragmentCameraBinding? = null
    private lateinit var currentPhotoPath: String
    private val binding get() = _binding!!
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupView()
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSION
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupView() {
        with(binding){
            btnShowPopupCamera.setOnClickListener(this@CameraFragment)
            btnUploadFoto.setOnClickListener(this@CameraFragment)
            btnBackUnggahFoto.setOnClickListener(this@CameraFragment)
        }
    }


    override fun onClick(v: View?) {
        when(v){
            binding.btnShowPopupCamera -> {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("Pilih Direktori")
                alertDialogBuilder.setIcon(R.drawable.baseline_image_24)

                alertDialogBuilder.setPositiveButton("Camera") { dialogInterface: DialogInterface, i: Int ->
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.resolveActivity(packageManager)
                    createCustomTempFile(application).also {
                        val photoURI : Uri = FileProvider.getUriForFile(
                            this.applicationContext,
                            "com.futureengineerdev.gubugtani.fileprovider",
                            it
                        )
                        currentPhotoPath = it.absolutePath
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        inCamera.launch(intent)
                    }
                    dialogInterface.dismiss()
                }

                alertDialogBuilder.setNegativeButton("Galeri") { dialogInterface: DialogInterface, i: Int ->
                    val intent = Intent()
                    intent.action = Intent.ACTION_GET_CONTENT
                    intent.type = "image/*"
                    val chooser = Intent.createChooser(intent, "Pilih Gambar")
                    inGalery.launch(chooser)
                    dialogInterface.dismiss()
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
            binding.btnUploadFoto -> uploadFoto()
            binding.btnBackUnggahFoto -> finish()
        }
    }

    private val inGalery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode == RESULT_OK){
            val setImage: Uri = result.data?.data as Uri
            val myFile = uriToFile(setImage, this)
            binding.ivFotoUploadDeteksi.setImageURI(setImage)
            getFile = myFile
        }
    }


    private fun uploadFoto() {
        Toast.makeText(this, "Foto Telah Terupload", Toast.LENGTH_SHORT).show()
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
    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
        const val EXTRA_DATA = "extra_data"

    }
}
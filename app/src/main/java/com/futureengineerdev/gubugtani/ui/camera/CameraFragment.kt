package com.futureengineerdev.gubugtani.ui.camera

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.futureengineerdev.gubugtani.ChooseActivity
import com.futureengineerdev.gubugtani.DetailActivity
import com.futureengineerdev.gubugtani.DiseaseResultActivity
import com.futureengineerdev.gubugtani.DonateActivity
import com.futureengineerdev.gubugtani.HomeActivity
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.databinding.ActivityHomeBinding
import com.futureengineerdev.gubugtani.databinding.FragmentCameraBinding
import com.futureengineerdev.gubugtani.databinding.FragmentComunityBinding
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.etc.createCustomTempFile
import com.futureengineerdev.gubugtani.etc.fixImageRotation
import com.futureengineerdev.gubugtani.etc.reduceFileImage
import com.futureengineerdev.gubugtani.etc.uriToFile
import com.futureengineerdev.gubugtani.response.PlantDiseaseResult
import com.futureengineerdev.gubugtani.response.PlantsItem
import com.futureengineerdev.gubugtani.ui.dashboard.ComunityViewModel
import com.futureengineerdev.gubugtani.ui.profile.ProfileActivity
import com.futureengineerdev.gubugtani.viewmodel.AddArticleViewModel
import com.futureengineerdev.gubugtani.viewmodel.AuthViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CameraFragment : AppCompatActivity(), View.OnClickListener{

    private var _binding: FragmentCameraBinding? = null
    private lateinit var currentPhotoPath: String
    private lateinit var cameraViewModel: CameraViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")

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
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)
        cameraViewModel = ViewModelProvider(this, viewModelFactory)[CameraViewModel::class.java]

        with(binding){
            btnShowPopupCamera.setOnClickListener(this@CameraFragment)
            btnUploadFoto.setOnClickListener(this@CameraFragment)
            btnBackUnggahFoto.setOnClickListener(this@CameraFragment)
        }
    }


    override fun onClick(v: View?) {
        val plantDisease = intent.getParcelableExtra<PlantsItem>(EXTRA_DATA)
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
            binding.btnUploadFoto -> {
                showLoading(true)
                uploadFoto(plantDisease!!)
                getResultDisease()
            }
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

    private fun uploadFoto(plantDiseaseUpload: PlantsItem) {
        with(binding){
            val plantId = plantDiseaseUpload.id.toString().toRequestBody("text/plain".toMediaType())
            if(getFile != null){
                val file = reduceFileImage(getFile as File)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestImageFile
                )
                CoroutineScope(Dispatchers.IO).launch {
                    cameraViewModel.upload(image = imageMultiPart, plant_id = plantId)
                }
            }else{
                Toast.makeText(this@CameraFragment, "Gambar tidak boleh kosong", Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        }
    }

    private fun getResultDisease(){
        cameraViewModel.uploadDisease.observe(this){
            try {
                if (it.result != null){
                    val resultIntent = Intent(this, DiseaseResultActivity::class.java)
                    resultIntent.putExtra(DiseaseResultActivity.EXTRA_RESULT, it.result?.detection?.result)
                    resultIntent.putExtra(DiseaseResultActivity.EXTRA_CONFIDENCE, it.result?.detection?.confidence)
                    resultIntent.putExtra(DiseaseResultActivity.EXTRA_IMAGE, it.result?.detection?.image)
                    startActivity(resultIntent)
                } else {
                    val dialogBuilder = AlertDialog.Builder(this)
                    val inflater = layoutInflater
                    val dialogView = inflater.inflate(R.layout.custom_dialog, null)
                    dialogBuilder.setView(dialogView)

                    val alertDialog = dialogBuilder.create()
                    alertDialog.setCancelable(false)
                    val btnDismiss = dialogView.findViewById<Button>(R.id.btnDialog)
                    val errorView = dialogView.findViewById<TextView>(R.id.tvWarning)
                    if (it.meta?.message == "Kuota Deteksi Penyakit Gratis telah Habis. Nikmati Deteksi Tanpa Batas dengan Melakukan Donasi."){
                        btnDismiss.setText("Berlangganan")
                        errorView.setText(it.meta?.message.toString())
                        btnDismiss.setOnClickListener {
                            alertDialog.dismiss()
                            showLoading(false)
                            startActivity(Intent(this, DonateActivity::class.java))
                            finish()
                        }
                    } else{
                        btnDismiss.setText("Kembali")
                        errorView.setText(it.meta?.message.toString())
                        btnDismiss.setOnClickListener {
                            alertDialog.dismiss()
                            showLoading(false)
                            finish()
                        }
                    }
                    alertDialog.show()
                }
                showLoading(false)
            }catch (e: Exception) {
                showLoading(false)
            }

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

    private fun showLoading(isLoadingLogin: Boolean){
        binding.isLoadingUploadDisease.visibility = if (isLoadingLogin) View.VISIBLE else View.GONE
        binding.btnUploadFoto.isEnabled = !isLoadingLogin
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
        const val EXTRA_DATA = "extra_data"
    }
}
package com.futureengineerdev.gubugtani

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.futureengineerdev.gubugtani.databinding.ActivityWriteArticleBinding
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.etc.createCustomTempFile
import com.futureengineerdev.gubugtani.etc.fixImageRotation
import com.futureengineerdev.gubugtani.etc.reduceFileImage
import com.futureengineerdev.gubugtani.etc.uriToFile
import com.futureengineerdev.gubugtani.response.ArticleImagesUpload
import com.futureengineerdev.gubugtani.ui.camera.CameraFragment
import com.futureengineerdev.gubugtani.ui.profile.ProfileViewModel
import com.futureengineerdev.gubugtani.viewmodel.AddArticleViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class WriteArticleActivity : AppCompatActivity() {

    private var _binding: ActivityWriteArticleBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentPhotoPath: String
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var addArticleViewModel: AddArticleViewModel
    private var getFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWriteArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                WriteArticleActivity.REQUIRED_PERMISSIONS,
                WriteArticleActivity.REQUEST_CODE_PERMISSION
            )
        }

        setupView()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
    private fun setupView() {
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)
        addArticleViewModel = ViewModelProvider(this, viewModelFactory)[AddArticleViewModel::class.java]

        binding.btnShowPopup.setOnClickListener{
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
        binding.btnUploadArticle.setOnClickListener{

            if(getFile == null){
                Toast.makeText(this,"Foto artikel tidak boleh kosong", Toast.LENGTH_SHORT).show()

            }
            else{
                CoroutineScope(Dispatchers.IO).launch {
                    uploadArticle()
                }
            }

        }
    }

    private val inCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == RESULT_OK){
            val myFile = File(currentPhotoPath)
            val options = BitmapFactory.Options()
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath, options)
            myFile.let{ file ->
                val fix = fixImageRotation(bitmap, file.path)
                getFile = file
                binding.ivFotoUploadArtikel.setImageBitmap(fix)
            }
        }
    }

    private val inGalery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode == RESULT_OK){
            val setImage: Uri = result.data?.data as Uri
            val myFile = uriToFile(setImage, this)
            binding.ivFotoUploadArtikel.setImageURI(setImage)
            getFile = myFile
        }
    }

    private suspend fun uploadArticle() {
        val nullDeskripsi = binding.etUploadDeskripsiArtikel.text.toString()
        val nullJudul = binding.etUploadJudulArtikel.text.toString()
    if (getFile != null) {
        val file = reduceFileImage(getFile as File)
        val judul = nullJudul.toRequestBody("text/plain".toMediaType())
        val deskripsi = nullDeskripsi.toRequestBody("text/plain".toMediaType())
        val defaultType = "community"
        val type = defaultType.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "images[0]",
            file.name,
            requestImageFile
        )
        CoroutineScope(Dispatchers.IO).launch {
            addArticleViewModel.upload(imageMultipart, type, judul, deskripsi)
        }
    }
}


    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }

}
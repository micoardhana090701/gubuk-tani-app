package com.futureengineerdev.gubugtani

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.futureengineerdev.gubugtani.databinding.ActivityUpdateBinding
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.etc.createCustomTempFile
import com.futureengineerdev.gubugtani.etc.fixImageRotation
import com.futureengineerdev.gubugtani.etc.reduceFileImage
import com.futureengineerdev.gubugtani.etc.uriToFile
import com.futureengineerdev.gubugtani.ui.profile.ProfileViewModel
import com.futureengineerdev.gubugtani.viewmodel.AuthViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UpdateActivity : AppCompatActivity(){

    private lateinit var _binding: ActivityUpdateBinding
    private val binding get() = _binding!!
    private lateinit var currentPhotoPath: String
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var authViewModel : AuthViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private var getFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        lifecycleScope.launch{
            setupViewModel()
        }
    }

    private suspend fun setupViewModel() {
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)

        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
        profileViewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        profileViewModel.updateUser.observe(this){
            if (it != null){
                Toast.makeText(this, "Akun Telah di Update", Toast.LENGTH_SHORT).show()
                showLoadingUpdate(true)
            }
        }

        binding.btnUpateBack.setOnClickListener{
            finish()
        }
        binding.ivFotoUpdate.setOnClickListener{
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
        getUserDefault()
        binding.btnSendUpdate.setOnClickListener{
            showLoadingUpdate(true)
            CoroutineScope(Dispatchers.IO).launch {
                uploadAll()
            }
        }

    }

    private suspend fun getUserDefault(){
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)

        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
        profileViewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        val accessToken = pref.getUserKey().first()

        profileViewModel.getProfile(access_token = "Bearer $accessToken")
        profileViewModel.profileUser.observe(this){
            if (it != null){
                binding.etNamaUpdate.setText(it.data?.result?.user?.name)
                binding.etUsernameUpdate.setText(it.data?.result?.user?.username)
                val ivFotoProfile = binding.ivFotoUpdate
                if (it.data?.result?.user?.avatar == null){
                    Glide.with(this)
                        .load(R.drawable.baseline_account_circle_24)
                        .centerCrop()
                        .into(ivFotoProfile)
                }
                else{
                    Glide.with(this)
                        .load("https://app.gubuktani.com/storage/" + it.data.result.user.avatar)
                        .centerCrop()
                        .into(ivFotoProfile)
                }
            }
        }
    }

    private suspend fun uploadAll() {
        try{
            if (getFile != null) {
                val file = reduceFileImage(getFile as File)
                val username = binding.etUsernameUpdate.text.toString().toRequestBody("text/plain".toMediaType())
                val name = binding.etNamaUpdate.text.toString().toRequestBody("text/plain".toMediaType())
                val city = binding.etCityUpdate.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "avatar",
                    file.name,
                    requestImageFile
                )
                CoroutineScope(Dispatchers.IO).launch {
                    profileViewModel.updateAll(imageMultipart=imageMultipart, username=username, name=name, city=city)
                }
                finish()
            } else{
                val username = binding.etUsernameUpdate.text.toString().toRequestBody("text/plain".toMediaType())
                val name = binding.etNamaUpdate.text.toString().toRequestBody("text/plain".toMediaType())
                val city = binding.etCityUpdate.text.toString().toRequestBody("text/plain".toMediaType())
                CoroutineScope(Dispatchers.IO).launch {
                    profileViewModel.updateData(username=username, name=name, city=city)
                }
                finish()
            }
            showLoadingUpdate(false)
        } catch (e: Exception){
            Log.e("Error", e.toString())
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
                binding.ivFotoUpdate.setImageBitmap(fix)
            }
        }
    }
    private val inGalery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode == RESULT_OK){
            val setImage: Uri = result.data?.data as Uri
            val myFile = uriToFile(setImage, this@UpdateActivity)
            binding.ivFotoUpdate.setImageURI(setImage)
            getFile = myFile
        }
    }
    private fun showLoadingUpdate(isLoadingUpdate: Boolean){
        binding.isLoadingUpdating.visibility = if (isLoadingUpdate) View.VISIBLE else View.GONE
        binding.btnSendUpdate.isEnabled = !isLoadingUpdate
    }
}
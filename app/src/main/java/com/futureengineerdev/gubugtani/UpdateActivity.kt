package com.futureengineerdev.gubugtani

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
import com.futureengineerdev.gubugtani.component.CustomAlertDialog
import com.futureengineerdev.gubugtani.databinding.ActivityUpdateBinding
import com.futureengineerdev.gubugtani.databinding.FragmentProfileBinding
import com.futureengineerdev.gubugtani.etc.Resource
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.etc.createCustomTempFile
import com.futureengineerdev.gubugtani.etc.fixImageRotation
import com.futureengineerdev.gubugtani.etc.uriToFile
import com.futureengineerdev.gubugtani.response.UpdateUser
import com.futureengineerdev.gubugtani.ui.profile.ProfileViewModel
import com.futureengineerdev.gubugtani.viewmodel.AuthViewModel
import com.futureengineerdev.gubugtani.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

        lifecycleScope.launch{
            setupViewModel()
        }
    }

    private suspend fun setupViewModel() {
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        val customAlertDialog = CustomAlertDialog(this)
        viewModelFactory.setApplication(application)

        authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
        profileViewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        profileViewModel.updateUser.observe(this){
            if (it != null){
                Toast.makeText(this, "Akun Telah di Update", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnUpateBack.setOnClickListener{
            finish()
        }

        binding.ivFotoUpdate.setOnClickListener{
            customAlertDialog.showDialog(
                cameraButtonClickListener = {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.resolveActivity(packageManager)

                    createCustomTempFile(application).also{
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this@UpdateActivity,
                            "com.futureengineerdev.gubugtani.filefotoprofile",
                            it
                        )
                        currentPhotoPath = it.absolutePath
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        inCamera.launch(intent)
                    }
                },
                galeryButtonClickListener = {
                    val intent = Intent()
                    intent.action = Intent.ACTION_GET_CONTENT
                    intent.type = "image/*"
                    val chooser = Intent.createChooser(intent, "Pilih Gambar")
                    inGalery.launch(chooser)
                }
            )
        }

        binding.btnSendUpdate.setOnClickListener{
            val city = binding.etCityUpdate.text.toString()
            val name = binding.etNamaUpdate.text.toString()
            val email = binding.etUpdateEmail.text.toString()
            val username = binding.etUsernameUpdate.text.toString()
            if (name.isNotEmpty()&&username.isNotEmpty()){
                lifecycleScope.launch {
                    profileViewModel.updateProfile(city, name, email, username)
                }
            }
            else{
                Toast.makeText(this, "ProfileTerupdate", Toast.LENGTH_SHORT).show()
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
}
package com.futureengineerdev.gubugtani

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.futureengineerdev.gubugtani.databinding.ActivityPaymentBinding
import com.futureengineerdev.gubugtani.etc.UserPreferences
import com.futureengineerdev.gubugtani.etc.createCustomTempFile
import com.futureengineerdev.gubugtani.etc.fixImageRotation
import com.futureengineerdev.gubugtani.etc.reduceFileImage
import com.futureengineerdev.gubugtani.etc.uriToFile
import com.futureengineerdev.gubugtani.payment.PaymentMethodViewModel
import com.futureengineerdev.gubugtani.payment.PaymentResultActivity
import com.futureengineerdev.gubugtani.payment.PaymentViewModel
import com.futureengineerdev.gubugtani.response.PaymentMethodsItem
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

class PaymentActivity : AppCompatActivity() {

    private var _binding: ActivityPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private lateinit var paymentViewModel: PaymentViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)

        val buktiUpload = intent.getParcelableExtra<PaymentMethodsItem>(EXTRA_DATA_PAYMENT)
        paymentViewModel = ViewModelProvider(this, viewModelFactory)[PaymentViewModel::class.java]


        binding.btnUploadBukti.setOnClickListener {
            uploadFotoBukti(buktiUpload!!)
            showLoading(true)
            getResultPayment()
        }

        binding.btnShowPopupBukti.setOnClickListener {
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
    }

    private fun uploadFotoBukti(buktiUpload: PaymentMethodsItem){
        val paymentId = buktiUpload.id.toString().toRequestBody("text/plain".toMediaType())
        val notes = "Upgrade".toRequestBody("text/plain".toMediaType())

        if (getFile != null){
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestImageFile
            )
            CoroutineScope(Dispatchers.IO).launch {
                paymentViewModel.upload(image = imageMultiPart, paymentMethodId = paymentId, notes = notes)
            }
        } else{
            Toast.makeText(this, "Gambar Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getResultPayment(){
        paymentViewModel.uploadPayment.observe(this){
            try {
                if (it.result != null){
                    val resultIntent = Intent(this, PaymentResultActivity::class.java)
                    resultIntent.putExtra(PaymentResultActivity.EXTRA_STATUS_PAYMENT, it.result?.payment?.status)
                    resultIntent.putExtra(PaymentResultActivity.EXTRA_IMAGE_PAYMENT, it.result?.payment?.image)
                    startActivity(resultIntent)
                } else {
                    Toast.makeText(this, it.meta?.message, Toast.LENGTH_SHORT).show()
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
                binding.ivBuktiPembayaran.setImageBitmap(fix)
            }
        }
    }
    private val inGalery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result ->
        if (result.resultCode == RESULT_OK){
            val setImage: Uri = result.data?.data as Uri
            val myFile = uriToFile(setImage, this)
            binding.ivBuktiPembayaran.setImageURI(setImage)
            getFile = myFile
        }
    }

    private fun showLoading(isLoadingLogin: Boolean){
        binding.isLoadingUploadDisease.visibility = if (isLoadingLogin) View.VISIBLE else View.GONE
        binding.btnUploadBukti.isEnabled = !isLoadingLogin
    }

    companion object{
        const val EXTRA_DATA_PAYMENT = "extra_data_payment"
    }
}
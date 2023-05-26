package com.futureengineerdev.gubugtani.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Message
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.futureengineerdev.gubugtani.R
import com.futureengineerdev.gubugtani.databinding.CustomAlertDialogBinding
import com.futureengineerdev.gubugtani.etc.createCustomTempFile
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener

class CustomAlertDialog(private val context: Context) {

    private lateinit var alertDialog: AlertDialog
    private lateinit var dialogView: View
    private lateinit var titleTextView: TextView
    private lateinit var messageTextView: TextView
    private lateinit var cameraButton: ImageView
    private lateinit var galeryButton: ImageView
    private lateinit var currentPhotoPath: String

    fun showDialog(
        cameraButtonClickListener: () -> Unit,
        galeryButtonClickListener: () -> Unit

    ) {
        val builder = AlertDialog.Builder(context)
        dialogView = LayoutInflater.from(context).inflate(R.layout.custom_alert_dialog, null)

        cameraButton = dialogView.findViewById(R.id.btnCameraUpdate)
        galeryButton = dialogView.findViewById(R.id.btnGaleryUpdate)

        cameraButton.setOnClickListener {
            cameraButtonClickListener.invoke()
            alertDialog.dismiss()
        }
        galeryButton.setOnClickListener{
            galeryButtonClickListener.invoke()
            alertDialog.dismiss()
        }
        builder.setCancelable(true)
        val alertDialog = builder.create()
        alertDialog.show()
    }
}
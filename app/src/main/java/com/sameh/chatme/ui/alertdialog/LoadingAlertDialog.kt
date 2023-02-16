package com.sameh.chatme.ui.alertdialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.sameh.chatme.databinding.LoadingAlertDialogBinding

class LoadingAlertDialog(private val context: Context) {

    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var alert: AlertDialog

    fun showLoadingAlertDialog() {
        alertDialog = AlertDialog.Builder(context)
        val bind: LoadingAlertDialogBinding = LoadingAlertDialogBinding.inflate(
            LayoutInflater.from(
                context
            )
        )
        alertDialog.setView(bind.root)
        alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.setCancelable(false)
        alert.show()
    }

    fun hideLoadingAlertDialog() {
        alert.dismiss()
    }

}
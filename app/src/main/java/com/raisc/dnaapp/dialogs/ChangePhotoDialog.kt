package com.raisc.dnaapp.dialogs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.raisac.smartorder.R
import com.raisc.dnaapp.R
import com.theartofdev.edmodo.cropper.CropImage

class ChangePhotoDialog : DialogFragment() {
    interface OnPhotoReceivedListener {
        fun getImagePath(imagePath: Uri)
        fun getImageBitmap(bitmap: Bitmap?)
    }

    var mOnPhotoReceived: OnPhotoReceivedListener? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_change_photo_dialog, container, false)

        //Initialize the textview for choosing an image from memory
        val selectPhoto = view.findViewById<TextView>(R.id.dialogChoosePhoto)
        selectPhoto.setOnClickListener {
            Log.d(TAG, "onClick: accessing phones memory.")
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            this@ChangePhotoDialog.startActivityForResult(intent, PICKLE_REQUEST_CODE)
        }

        //Initialize the textview for choosing an image from memory
        val takePhoto = view.findViewById<TextView>(R.id.dialogOpenCamera)
        takePhoto.setOnClickListener {
            Log.d(TAG, "onClick: starting camera")
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            this@ChangePhotoDialog.startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        /*
        Results when selecting new image from phone memory
         */if (requestCode == PICKLE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data!!.data
            Log.d(TAG, "onActivityResult: image: $selectedImageUri")
            CropImage.activity(selectedImageUri)
                    .start(context!!, this)

            //send the bitmap and fragment to the interface
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: done taking a photo.")
            val bitmap: Bitmap? = data!!.extras!!["data"] as Bitmap?

            mOnPhotoReceived!!.getImageBitmap(bitmap)
            dialog!!.dismiss()


        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUrl = result.uri
                mOnPhotoReceived!!.getImagePath(resultUrl)
                dialog!!.dismiss()
            }
        }
    }

    override fun onAttach(context: Context) {
        try {
            mOnPhotoReceived = activity as OnPhotoReceivedListener?
        } catch (e: ClassCastException) {
            Log.e(TAG, "onAttach: ClassCastException", e.cause)
        }
        super.onAttach(context)
    }

    companion object {
        private const val TAG = "ChangePhotoDialog"
        const val CAMERA_REQUEST_CODE = 5467 //random number
        const val PICKLE_REQUEST_CODE = 8352 //random number
    }
}
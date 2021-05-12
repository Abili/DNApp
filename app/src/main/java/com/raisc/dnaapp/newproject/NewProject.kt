package com.raisc.dnaapp.newproject

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.UploadTask
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.raisc.dnaapp.R
import com.raisc.dnaapp.databinding.ActivityNewProject2Binding
import com.raisc.dnaapp.model.Project
import java.io.ByteArrayOutputStream
import java.io.IOException

class NewProject : AppCompatActivity(), OnPhotoReceivedListener {

    var imageLoader: ImageLoader? = null
    private var mCurrentUser: FirebaseUser? = null
    private var mUid: String? = null
    private var mReference: DatabaseReference? = null
    private val mDialog: BottomSheetDialog? = null

    override fun getImagePath(imagePath: Uri) {
        if (imagePath.toString() != "") {
            mSelectedImageBitmap = null
            mSelectedImageUri = imagePath
            Log.d(TAG, "getImagePath: got the image uri: $mSelectedImageUri")
            ImageLoader.getInstance().displayImage(imagePath.toString(), binding.profileImage)
        }
    }

    override fun getImageBitmap(bitmap: Bitmap?) {
        if (bitmap != null) {
            mSelectedImageUri = null
            mSelectedImageBitmap = bitmap
            Log.d(TAG, "getImageBitmap: got the image bitmap: $mSelectedImageBitmap")
            binding?.profileImage!!.setImageBitmap(bitmap)
        }
    }


    //firebase
    private val mAuthListener: FirebaseAuth.AuthStateListener? = null

    //widgets
    //private var mProfileImage: ImageView? = null
    private var mSave: Button? = null

    //private ProgressBar mProgressBar;
    private val mResetPasswordLink: TextView? = null

    //vars
    private var mStoragePermissions = false
    private var mSelectedImageUri: Uri? = null
    private var mSelectedImageBitmap: Bitmap? = null
    private var mBytes: ByteArray? = null
    private val progress = 0.0
    var mAuth: FirebaseAuth? = null
    var patients = "patients"
    //boolean isButtonEnabled = !Objects.requireNonNull(mSave).isEnabled();

    private lateinit var binding: ActivityNewProject2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewProject2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.profileImage.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        init()
        hideSoftKeyboard()
        mAuth = FirebaseAuth.getInstance()
        mCurrentUser = mAuth!!.currentUser
        mReference = FirebaseDatabase.getInstance().reference

    }


    private fun init() {
        val getProfle = intent
        val photoUrl = getProfle.getStringExtra("user_image")
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this@NewProject))
        //getUserAccountData();
        binding?.profileImage!!.setOnClickListener {
            getPhotos()
        }
        binding.contents.createProjectBtn.setOnClickListener {
            signUp()
        }

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mCurrentUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            mUid = mCurrentUser!!.uid
        }
    }

    fun signUp() {
        val pName = binding.contents.projectName
        val cName = binding.contents.clientName
        val pLocation = binding.contents.projectLocation
        val cPhone = binding.contents.clientPhone
        val cPayments = binding.contents.payments
        val cID = binding.contents.clientsID


        val projectName = pName.text.toString()
        val clientName = cName.text.toString()
        val clientPhone = pName.text.toString()
        val projectLocation = cName.text.toString()
        val payments = pName.text.toString()
        val clientID = cName.text.toString()

        var phone: String? = null
        if (currentUser != null) {
            phone = mAuth.currentUser?.phoneNumber!!
        }

        if (projectLocation.isNotEmpty() && projectName.isNotEmpty()
            && clientName.isNotEmpty() && clientID.isNotEmpty() && clientPhone.isNotEmpty()
            && payments.isNotEmpty()
        ) {
            val project =
                Project(
                    null,
                    projectName,
                    clientName,
                    clientPhone,
                    projectLocation,
                    payments,
                    clientID
                )
            saveToFirebasedatabase(project)

        } else {
            Toast.makeText(this, "Names Required !", Toast.LENGTH_LONG).show()
        }

        /*------ Upload the New Photo -----*/
        if (mSelectedImageUri != null) {
            uploadNewPhoto(mSelectedImageUri)
        } else if (mSelectedImageBitmap != null) {
            uploadNewPhoto(mSelectedImageBitmap)
        }
        binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun saveToFirebasedatabase(user: Users) {

        mReference!!.child("users")
            .child(mUid!!) //.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
            .setValue(user)
            .addOnSuccessListener {
                binding?.progressBar?.visibility = View.GONE

            }
            .addOnFailureListener {
                Toast.makeText(this, "cant upload names", Toast.LENGTH_SHORT).show()
            }
    }


    private fun getPhotos() {
        val dialog = ChangePhotoDialog()
        dialog.show(supportFragmentManager, TAG)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //TODO rsults after selecting image from phone memory
        if (requestCode == PICKFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            val selectedImageUri = data!!.data
            Log.d(TAG, "onActivityResult: image: $selectedImageUri")

            //TODO send the bitmap and frrgment to the interface
            binding?.profileImage!!.setImageURI(selectedImageUri)
            imageUploadDialog!!.dismiss()
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: done taking a photo.")
            val bitmap: Bitmap? = data!!.extras!!["data"] as Bitmap?
            binding?.profileImage!!.setImageBitmap(bitmap)
            imageUploadDialog!!.dismiss()
        }
    }


    /**
     * Uploads a new profile photo to Firebase Storage using a @param ***imageUri***
     *
     * @param imageUri
     */
    fun uploadNewPhoto(imageUri: Uri?) {
        /*
            upload a new profile photo to firebase storage
         */
        Log.d(TAG, "uploadNewPhoto: uploading new profile photo to firebase storage.")

        //Only accept image sizes that are compressed to under 5MB. If thats not possible
        //then do not allow image to be uploaded
        val resize = BackgroundImageResize(null)
        resize.execute(imageUri)
    }

    /**
     * Uploads a new profile photo to Firebase Storage using a @param ***imageBitmap***
     *
     * @param imageBitmap
     */
    fun uploadNewPhoto(imageBitmap: Bitmap?) {
        /*
            upload a new profile photo to firebase storage
         */
        Log.d(TAG, "uploadNewPhoto: uploading new profile photo to firebase storage.")

        //Only accept image sizes that are compressed to under 5MB. If thats not possible
        //then do not allow image to be uploaded
        val resize = BackgroundImageResize(imageBitmap)
        val uri: Uri? = null
        resize.execute(uri)
    }

    /**
     * 1) doinBackground takes an imageUri and returns the byte array after compression
     * 2) onPostExecute will print the % compression to the log once finished
     */
    inner class BackgroundImageResize(bm: Bitmap?) : AsyncTask<Uri?, Int?, ByteArray?>() {
        var mBitmap: Bitmap? = null
        override fun onPreExecute() {
            super.onPreExecute()

            Toast.makeText(this@UserProfile, "please wait...", Toast.LENGTH_SHORT).show()
        }

        override fun doInBackground(vararg params: Uri?): ByteArray? {
            Log.d(TAG, "doInBackground: started.")
            if (mBitmap == null) {
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(
                        this@UserProfile.contentResolver,
                        params[0]
                    )
                    Log.d(
                        TAG,
                        "doInBackground: bitmap size: megabytes: " + mBitmap!!.byteCount / MB + " MB"
                    )
                } catch (e: IOException) {
                    Log.e(TAG, "doInBackground: IOException: ", e.cause)
                }
            }
            var bytes: ByteArray? = null
            for (i in 1..10) {
                if (i == 10) {
                    Toast.makeText(this@UserProfile, "That image is too large.", Toast.LENGTH_SHORT)
                        .show()
                    break
                }
                bytes = getBytesFromBitmap(mBitmap!!, 100 / i)
                Log.d(
                    TAG,
                    "doInBackground: megabytes: (" + (11 - i) + "0%) " + bytes!!.size / MB + " MB"
                )
                if (bytes.size / MB < MB_THRESHHOLD) {
                    return bytes
                }
            }
            return bytes
        }

        override fun onPostExecute(bytes: ByteArray?) {
            super.onPostExecute(bytes)
            hideDialog()
            mBytes = bytes
            //execute the upload
            executeUploadTask()
        }

        init {
            if (bm != null) {
                mBitmap = bm
            }
        }
    }

    // convert from bitmap to byte array
    fun getBytesFromBitmap(bitmap: Bitmap, quality: Int): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }

    private fun executeUploadTask() {
        showDialog()
        //specify where the photo will be stored
        val storageReference = FirebaseStorage.getInstance().reference
            .child(
                FIREBASE_IMAGE_STORAGE + "/" + FirebaseAuth.getInstance().currentUser.uid
                        + "/" + "profile_image"
            ) //just replace the old image with the new one
        if (mBytes!!.size / MB < MB_THRESHHOLD) {

            // Create file metadata including the content type
            val metadata = StorageMetadata.Builder()
                .setContentType("image/jpg")
                .setContentLanguage("en") //see nodes below
                /*
                Make sure to use proper language code ("English" will cause a crash)
                I actually submitted this as a bug to the Firebase github page so it might be
                fixed by the time you watch this video. You can check it out at https://github.com/firebase/quickstart-unity/issues/116
                 */
                .setCustomMetadata("Mitch's special meta data", "JK nothing special here")
                .setCustomMetadata("location", "Iceland")
                .build()
            //if the image size is valid then we can submit to database
            var uploadTask: UploadTask?

            uploadTask = storageReference.putBytes(mBytes!!, metadata)
            //uploadTask = storageReference.putBytes(mBytes); //without metadata
            val uriTask: Task<Uri> = uploadTask.continueWithTask<Uri> { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                //conitnue with task to get download url
                storageReference.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri: Uri? = task.result
                    val mKey = mReference?.child("users")?.push()?.key
                    FirebaseDatabase.getInstance().reference
                        .child("users")
                        .child(mUid!!)
                        .child("profilePics")//.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(downloadUri.toString())
                    Toast.makeText(this@UserProfile, "Account created", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@UserProfile, Location::class.java))
                    finish()

                    hideDialog()

                } else {
                    Toast.makeText(this, "Image is too Large", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    /**
     * Generalized method for asking permission. Can pass any array of permissions
     */
    fun verifyStoragePermissions() {
        Log.d(TAG, "verifyPermissions: asking user for permissions.")
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                permissions[0]
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this.applicationContext,
                permissions[1]
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this.applicationContext,
                permissions[2]
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mStoragePermissions = true
        } else {
            ActivityCompat.requestPermissions(
                this@UserProfile,
                permissions,
                REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionsResult: requestCode: $requestCode")
        when (requestCode) {
            REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(
                    TAG,
                    "onRequestPermissionsResult: User has allowed permission to access: " + permissions[0]
                )
            }
        }
    }


    fun showDialog() {
        binding!!.progressBar.visibility = View.VISIBLE
    }

    fun hideDialog() {
        if (binding!!.progressBar.visibility == View.VISIBLE) {
            binding!!.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun hideSoftKeyboard() {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    /**
     * Return true if the @param is null
     *
     * @param string
     * @return
     */
    private fun isEmpty(string: String): Boolean {
        return string == ""
    }


    companion object {
        private val REQUEST_CODE = 1234
        private val MB_THRESHHOLD = 5.0
        private val MB = 1000000.0

        var imageUploadDialog: Dialog? = null
        val CAMERA_REQUEST_CODE = 5467
        val PICKFILE_REQUEST_CODE = 8352

        private val TAG = "UserProfile"

        const val FIREBASE_IMAGE_STORAGE = "images/users"

        var mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

    }

}






}
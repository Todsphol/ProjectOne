package th.co.todsphol.add.projectone.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import th.co.todsphol.add.projectone.R
import java.io.IOException
import java.util.*

@Suppress("DEPRECATION")
class SelectedActivity : AppCompatActivity(), View.OnClickListener {

    @BindView(R.id.tv_show_success) lateinit var tvSuccess : TextView
    @BindView(R.id.btn_select) lateinit var btnSelected : Button
    @BindView(R.id.imageView) lateinit var ivSelected : ImageView
    @BindView(R.id.btn_confirm_register) lateinit var btnConfirm : Button
    @BindView(R.id.toolbar) lateinit var toolBar : Toolbar
    @BindView(R.id.tv_toolbar_title) lateinit var tvTitle : TextView
    private var filePath : Uri? = null
    var storage : FirebaseStorage? = null
    var storageReference : StorageReference? = null
    val PICK_IMAGE_REQUEST = 1234
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected)
        ButterKnife.bind(this)
        tvSuccess.text = intent.getStringExtra(EXTRA_PHONE)
        storage = FirebaseStorage.getInstance()
        storageReference = storage?.reference
        btnSelected.setOnClickListener(this)
        btnConfirm.setOnClickListener(this)
        setToolBar()

    }

    fun setToolBar() {
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        tvTitle.text = "รูปภาพรถ"
    }

    override fun onClick(p0: View?) {
        if (p0 == btnSelected) {
            isSelected()
        } else if (p0 == btnConfirm) {
            isUploading()
        }
    }
    var EXTRA_URI : Uri? = null
    private fun isUploading() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val imageRef = storageReference!!.child("images/User/0968613128/" + UUID.randomUUID().toString())
            imageRef.putFile(filePath!!)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_SHORT).show()
                        EXTRA_URI = it.downloadUrl
                        val gotoLoginSucIntent = Intent(this, LoginSuccessActivity::class.java)
                        gotoLoginSucIntent.putExtra("EXTRA_PHONE",tvSuccess.text.toString())
                        gotoLoginSucIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(gotoLoginSucIntent)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                    }
                    .addOnProgressListener {taskSnapShot ->
                        val progress = 100.0 * taskSnapShot.bytesTransferred/taskSnapShot.totalByteCount
                        progressDialog.setMessage("Uploaded"+ progress.toInt() + "%...")
                    }
        }
    }

    private fun isSelected() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"SELECT PICTURE"),PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == Activity.RESULT_OK &&
                data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filePath)
                ivSelected.setImageBitmap(bitmap)
            }catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val EXTRA_PHONE = "EXTRA_PHONE"

    }
}

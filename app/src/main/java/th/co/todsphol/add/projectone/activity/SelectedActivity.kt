@file:Suppress("DEPRECATION")

package th.co.todsphol.add.projectone.activity

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import butterknife.ButterKnife
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import th.co.todsphol.add.projectone.R
import java.io.IOException
import java.util.*
import kotlinx.android.synthetic.main.activity_selected.*
import kotlinx.android.synthetic.main.custom_toolbar.*

@Suppress("DEPRECATION")
open class SelectedActivity : AppCompatActivity(), View.OnClickListener {


    private var baseR = FirebaseDatabase.getInstance().reference
    private var dataCar = baseR.child("User").child("user1").child("DATA_CAR")
    private var filePath: Uri? = null
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private val PICK_IMAGE_REQUEST = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected)
        ButterKnife.bind(this)
        tv_show_success.text = intent.getStringExtra(EXTRA_PHONE)
        storage = FirebaseStorage.getInstance()
        storageReference = storage?.reference
        btn_select.setOnClickListener(this)
        btn_confirm_register.setOnClickListener(this)
        setToolBar()

    }

    private fun setToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        tv_toolbar_title.text = "รูปภาพรถ"
    }

    override fun onClick(p0: View?) {
        if (p0 == btn_select) {
            isSelected()
        } else if (p0 == btn_confirm_register) {
            isUploading()
        }
    }

    private var EXTRA_URI = ""
    private fun isUploading() {
        val progressDialog = ProgressDialog(this)
        val alertDialog = AlertDialog.Builder(this).create()
        if (filePath != null) {
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val imageRef = storageReference!!.child("images/User/0968613128/" + UUID.randomUUID().toString())
            imageRef.putFile(filePath!!)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_SHORT).show()
                        EXTRA_URI = it.downloadUrl.toString()
                        dataCar.child("Images").setValue(EXTRA_URI)
                        val gotoLoginSucIntent = Intent(this, LoginSuccessActivity::class.java)
                        gotoLoginSucIntent.putExtra("EXTRA_PHONE", tv_show_success.text.toString())
                        gotoLoginSucIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(gotoLoginSucIntent)
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }
                    .addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                    }
                    .addOnProgressListener { taskSnapShot ->
                        val progress = 100.0 * taskSnapShot.bytesTransferred / taskSnapShot.totalByteCount
                        progressDialog.setMessage("Uploaded" + progress.toInt() + "%...")
                    }

        } else {
            alertDialog.setTitle("แจ้งเตือน")
            alertDialog.setMessage("กรุณาเลือกรูปภาพรถของคุณ")
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", { _, _ ->
                alertDialog.dismiss()
            })
            alertDialog.show()

        }
    }

    private fun isSelected() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == Activity.RESULT_OK &&
                data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    companion object {
        const val EXTRA_PHONE = "EXTRA_PHONE"

    }
}

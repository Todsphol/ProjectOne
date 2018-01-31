package th.co.todsphol.add.projectone.activity

import android.app.Activity
import android.app.Instrumentation
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.jaredrummler.materialspinner.MaterialSpinner
import th.co.todsphol.add.projectone.R
import android.support.design.widget.Snackbar
import android.support.v4.provider.FontsContractCompat
import android.view.View
import android.widget.*
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_new__register.*
import th.co.todsphol.add.projectone.PhoneNumberWatcher
import java.io.IOException
import java.util.*


@Suppress("DEPRECATION")
class NewRegisterActivity : AppCompatActivity(), View.OnClickListener {

    private var baseR = FirebaseDatabase.getInstance().reference
    private var dataName = baseR.child("User").child("user1").child("DATA_PERS")
    private var dataCar = baseR.child("User").child("user1").child("DATA_CAR")
    private var dataStatus = baseR.child("User").child("user1").child("STATUS")
    private var dataREG = baseR.child("User").child("user1").child("DATA_REG")
    private var dataLocation = baseR.child("User").child("user1").child("DATA_LOCATION")
    private var filePath : Uri? = null
    var storage : FirebaseStorage? = null
    var storageReference : StorageReference? = null
    @BindView(R.id.toolbar) lateinit var toolBar: Toolbar
    @BindView(R.id.tv_toolbar_title) lateinit var titleToolbar: TextView
    @BindView(R.id.edt_first_name) lateinit var edtFirstName: EditText
    @BindView(R.id.edt_last_name) lateinit var edtLastName: EditText
    @BindView(R.id.edt_age) lateinit var edtAge: EditText
    @BindView(R.id.spinner_brand) lateinit var spinnerBrand: MaterialSpinner
    @BindView(R.id.edt_color) lateinit var edtColor: EditText
    @BindView(R.id.edt_licence_plate) lateinit var edtLicencePlate: EditText
    @BindView(R.id.edt_phone_number) lateinit var edtPhoneNumber: EditText
    @BindView(R.id.edt_password) lateinit var edtPassword: EditText
    @BindView(R.id.edt_confirm_password) lateinit var edtConfirmPassword: EditText
    @BindView(R.id.btn_select) lateinit var btnSelected : Button
    @BindView(R.id.imageView) lateinit var ivSelected : ImageView
    @BindView(R.id.btn_register) lateinit var btnRegister : Button
    var REQUEST_CODE = 1
    val PICK_IMAGE_REQUEST = 1234
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new__register)
        ButterKnife.bind(this)
        setToolbar()
        spinnerBrand()

        edtPhoneNumber.addTextChangedListener(PhoneNumberWatcher(edtPhoneNumber))
//        btnSelected.setOnClickListener {
//            val permissionCheck = ContextCompat.checkSelfPermission(this,
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
//            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//                startGallery()
//            } else {
//                ActivityCompat.requestPermissions(this,
//                        Array(5, { android.Manifest.permission.READ_EXTERNAL_STORAGE }),
//                        2000)
//            }
//        }
//        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(intent, REQUEST_CODE)
        storage = FirebaseStorage.getInstance()
        storageReference = storage?.reference
        btnSelected.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
    }
    override fun onClick(p0: View?) {
        if (p0 == btnSelected) {
            showFileChooser()
        }else if(p0 == btnRegister) {
            uploadFile()
        }
    }

    private fun uploadFile() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val imageRef = storageReference!!.child("images/" + UUID.randomUUID().toString())
            imageRef.putFile(filePath!!)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_SHORT).show()

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

    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"SELECT PICTURE"),PICK_IMAGE_REQUEST)
    }

    private fun startGallery() {
        val cameraIntent = Intent(Intent.ACTION_GET_CONTENT)
        cameraIntent.type = "image/*"
        if (cameraIntent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(cameraIntent, REQUEST_CODE)
        }
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



    val EXTRA_PHONE = "EXTRA_PHONE"
    @OnClick(R.id.btn_register)
    fun registerButtonClicked() {
        val checkBlankInEdittext = (edtFirstName.text.toString() == "" || edtLastName.text.toString() == ""
                || edtAge.text.toString() == "" || edtColor.text.toString() == ""
                || edtLicencePlate.text.toString() == "" || edtPhoneNumber.text.toString() == ""
                || edtPassword.text.toString() == "" || edtConfirmPassword.text.toString() == "")
        val replacePhone = edtPhoneNumber.text.toString().replace("-".toRegex(), "")
                .replace("\\s+", "")
        val checkLenPhone =  replacePhone.length < 10
        val checkPassword = edtPassword.text.toString() != edtConfirmPassword.text.toString()
        when {
            checkBlankInEdittext -> Toast.makeText(this,"กรอกข้อมูลให้ครบ",Toast.LENGTH_SHORT).show()
            checkLenPhone -> Toast.makeText(this,"กรอกเบอร์โทรให้ครบ",Toast.LENGTH_SHORT).show()
            checkPassword-> Toast.makeText(this,"รหัสผ่านไม่เหมือนกัน",Toast.LENGTH_SHORT).show()
            else -> {

                dataName.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataName.child("name").setValue(edtFirstName.text.toString())
                        dataName.child("surname").setValue(edtLastName.text.toString())
                        dataName.child("age").setValue(edtAge.text.toString())

                    }

                    override fun onCancelled(p0: DatabaseError?) {

                    }

                })
                dataCar.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot?) {
                        spinnerBrand()
                        dataCar.child("color").setValue(edtColor.text.toString())
                        dataCar.child("LP").setValue(edtLicencePlate.text.toString())
                    }

                    override fun onCancelled(p0: DatabaseError?) {

                    }

                })
                dataREG.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot?) {
                        val edt = edtPhoneNumber.text.toString().replace("-".toRegex(), "")
                                .replace("\\s+","")
                        dataREG.child("telephone").setValue(edt)
                        if (edtPassword.text.toString() != edtConfirmPassword.text.toString()) {
                            Toast.makeText(this@NewRegisterActivity,"กรุณากรอก Password ให้เหมือนกัน", Toast.LENGTH_SHORT).show()
                        } else {
                            dataREG.child("password").setValue(edtPassword.text.toString())
                        }
                    }

                    override fun onCancelled(p0: DatabaseError?) {

                    }

                })
                dataLocation.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val vir = dataSnapshot.child("Vir").getValue(Int::class.java)
                        if (vir == 0) {
                            dataStatus.child("Salarm").setValue(0)
                            dataStatus.child("Slogin").setValue(0)
                        } else {
                            dataStatus.child("Salarm").setValue(0)
                            dataStatus.child("Slogin").setValue(0)
                        }

                    }

                    override fun onCancelled(p0: DatabaseError?) {

                    }

                })

                val loginSuccess = Intent(this,LoginSuccessActivity::class.java)
                loginSuccess.putExtra(EXTRA_PHONE,edtPhoneNumber.text.toString())
                loginSuccess.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(loginSuccess)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                dataCar.child("Type").setValue(getItem)

            }
        }

    }

     var getItem : Any? = "Honda"
    fun spinnerBrand() {

        spinnerBrand.setTextColor(resources.getColor(R.color.colorGreen))
        spinnerBrand.setArrowColor(resources.getColor(R.color.colorGreen))
        spinnerBrand.textSize = resources.getDimension(R.dimen.textSizeInSpinner)
        spinnerBrand.setItems("Honda", "YAMAHA", "SUZUKI", "Kawasaki"
                , "DUCATI", "Vespa", "HARLEY", "SHOPPER","BMW")
        spinnerBrand.setOnItemSelectedListener { view, _, _,
                                                 item ->
            Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show()
            getItem = item

        }
    }

    fun setToolbar() {
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        titleToolbar.text = getString(R.string.register)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)

    }

}

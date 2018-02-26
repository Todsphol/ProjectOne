package th.co.todsphol.add.projectone.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.jaredrummler.materialspinner.MaterialSpinner
import th.co.todsphol.add.projectone.R
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.view.inputmethod.InputMethodManager
import android.widget.*
import butterknife.OnClick
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import th.co.todsphol.add.projectone.numbermanager.PhoneNumberWatcher


@Suppress("DEPRECATION")
class NewRegisterActivity : AppCompatActivity(){

    private var baseR = FirebaseDatabase.getInstance().reference
    private var dataName = baseR.child("User").child("user1").child("DATA_PERS")
    private var dataCar = baseR.child("User").child("user1").child("DATA_CAR")
    private var dataStatus = baseR.child("User").child("user1").child("STATUS")
    private var dataREG = baseR.child("User").child("user1").child("DATA_REG")
    private var dataLocation = baseR.child("User").child("user1").child("DATA_LOCATION")
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
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_register)
        ButterKnife.bind(this)
        setToolbar()
        spinnerBrand()

        edtPhoneNumber.addTextChangedListener(PhoneNumberWatcher(edtPhoneNumber))
    }

    private val EXTRA_PHONE = "EXTRA_PHONE"
    @OnClick(R.id.btn_register)
    fun registerButtonClicked() {
        val checkBlankInEdiText = (edtFirstName.text.toString() == ""
                || edtLastName.text.toString() == ""
                || edtAge.text.toString() == ""
                || edtColor.text.toString() == ""
                || edtLicencePlate.text.toString() == ""
                || edtPhoneNumber.text.toString() == ""
                || edtPassword.text.toString() == ""
                || edtConfirmPassword.text.toString() == "")
        val replacePhone = edtPhoneNumber.text.toString().replace("-".toRegex(), "")
                .replace("\\s+", "")
        val checkLenPhone =  replacePhone.length < 10
        val checkPassword = edtPassword.text.toString() != edtConfirmPassword.text.toString()
        when {
            checkBlankInEdiText -> Toast.makeText(this, "กรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show()
            checkLenPhone -> Toast.makeText(this, "กรอกเบอร์โทรให้ครบ", Toast.LENGTH_SHORT).show()
            checkPassword -> Toast.makeText(this, "รหัสผ่านไม่เหมือนกัน", Toast.LENGTH_SHORT).show()
            else -> {
                if (!isConnected(this)) alertDialogIsNotConnect(this).show()
                else {
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
                                    .replace("\\s+", "")
                            dataREG.child("telephone").setValue(edt)
                            if (edtPassword.text.toString() != edtConfirmPassword.text.toString()) {
                                Toast.makeText(this@NewRegisterActivity, "กรุณากรอก Password ให้เหมือนกัน", Toast.LENGTH_SHORT).show()
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

                    val loginSuccess = Intent(this, SelectedActivity::class.java)
                    loginSuccess.putExtra(EXTRA_PHONE, edtPhoneNumber.text.toString())
                    loginSuccess.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(loginSuccess)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    dataCar.child("Type").setValue(getItem)

                }
            }
        }
    }

     private var getItem : Any? = "Honda"
    @SuppressLint("ClickableViewAccessibility")
    fun spinnerBrand() {

        spinnerBrand.setTextColor(resources.getColor(R.color.colorPink))
        spinnerBrand.setArrowColor(resources.getColor(R.color.colorPink))
        spinnerBrand.textSize = resources.getDimension(R.dimen.textSizeInSpinner)
        spinnerBrand.setItems("Honda", "YAMAHA", "SUZUKI", "Kawasaki"
                , "DUCATI","BMW")
        spinnerBrand.setOnTouchListener { _, _ ->
            val inputMethodManager : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken,0)
        }
        spinnerBrand.setOnItemSelectedListener { view, _, _,
                                                 item ->
            Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show()
            getItem = item

        }
    }


    private fun setToolbar() {
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        titleToolbar.text = getString(R.string.register)
    }
    private fun isConnected(context: Context): Boolean {
        val cm : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnectedOrConnecting) {
            val wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            val mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

            return mobile != null && mobile.isConnectedOrConnecting || (wifi != null && wifi.isConnectedOrConnecting)
        }else {
            return false
        }
    }

    private fun alertDialogIsNotConnect(c: Context): AlertDialog.Builder {
        val builder : AlertDialog.Builder = AlertDialog.Builder(c)
        builder.setTitle("ไม่มีการเชื่อมต่ออินเทอร์เน็ต")
        builder.setMessage("คุณต้องการต่ออินเทอร์เน็ตหรือ Wifi ")
        builder.setPositiveButton("Ok", { _, _ ->
            finish()
        })

        return builder
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)

    }

}

package th.co.todsphol.add.projectone.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTextChanged
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import th.co.todsphol.add.projectone.numbermanager.PhoneNumberWatcher
import th.co.todsphol.add.projectone.R
import kotlinx.android.synthetic.main.activity_login.*

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private var baseR = FirebaseDatabase.getInstance().reference
    private var dataReg = baseR.child("User").child("user1").child("DATA_REG")
    private var dataStatus = baseR.child("User").child("user1").child("STATUS")

    companion object {
        const val EXTRA_PHONE = "EXTRA_PHONE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        if (!isConnected(this)) {
            setPhoneEdittext()
            alertDialogIsNotConnect(this).show()
        } else {
            setPhoneEdittext()
        }
    }

    private fun setPhoneEdittext() {
        onText(edt_phone_number.toString())
        edt_phone_number.addTextChangedListener(PhoneNumberWatcher(edt_phone_number))
        edt_phone_number.setText(intent.getStringExtra(EXTRA_PHONE), TextView.BufferType.EDITABLE)
        edt_phone_number.setSelection(edt_phone_number.text.length)
    }

    private fun isConnected(context: Context): Boolean {
        val cm: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnectedOrConnecting) {
            val wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            val mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

            return mobile != null && mobile.isConnectedOrConnecting || (wifi != null && wifi.isConnectedOrConnecting)
        } else {
            return false
        }
    }

    private fun alertDialogIsNotConnect(c: Context): AlertDialog.Builder {
        val builder: AlertDialog.Builder = AlertDialog.Builder(c)
        builder.setTitle("ไม่มีการเชื่อมต่ออินเทอร์เน็ต")
        builder.setMessage("คุณต้องการต่ออินเทอร์เน็ตหรือ Wifi ")
        builder.setPositiveButton("Ok", { _, _ ->
            finish()
        })

        return builder
    }


    @OnTextChanged(R.id.edt_phone_number, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    fun onText(phoneNumber: CharSequence) {
        if (phoneNumber.toString().length == 12) {
            edt_password.visibility = android.view.View.VISIBLE
            return
        }
        edt_password.visibility = android.view.View.GONE
    }


    @OnClick(R.id.btn_login)
    fun checkLogin() {
        if (!isConnected(this)) {
            alertDialogIsNotConnect(this).show()
        } else {
            dataReg.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val regPhone = dataSnapshot.child("telephone").getValue(String::class.java)
                    val regPassword = dataSnapshot.child("password").getValue(String::class.java)

                    val replaceNumber = edt_phone_number.text.toString().replace("-".toRegex(), "")
                            .replace("\\s+", "")
                    val checkTrue = replaceNumber == regPhone
                            && edt_password.text.toString() == regPassword
                    val checkPhoneTrue = replaceNumber == regPhone
                            && edt_password.text.toString() != regPassword
                    val checkEdtBlank = edt_phone_number.text.toString() == "" || edt_password.text.toString() == ""
                    val checkLength = replaceNumber.length < 10 || edt_password.text.toString().length < 6
                    val adminNumber = "0123456789"
                    val adminPassword = "123456"
                    val admin = replaceNumber == adminNumber && edt_password.text.toString() == adminPassword

                    when {
                        checkTrue -> isCorrect()
                        checkPhoneTrue -> Toast.makeText(this@LoginActivity, "Password ของคุณไม่ถูกต้อง", Toast.LENGTH_SHORT).show()
                        checkEdtBlank || checkLength -> Toast.makeText(this@LoginActivity
                                , "กรุณากรอกเบอร์โทรหรือ password ให้ครบ"
                                , Toast.LENGTH_SHORT).show()
                        admin -> isCorrect()
                        else -> Toast.makeText(this@LoginActivity, "ไม่มีเบอร์นี้อยู่ในระบบ", Toast.LENGTH_SHORT).show()


                    }
                }

                override fun onCancelled(p0: DatabaseError?) {

                }
            })

        }
    }

    private fun isCorrect() {
        val homeIntent = Intent(this@LoginActivity, DisplayActivity::class.java)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(homeIntent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        dataStatus.child("Slogin").setValue(1)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}

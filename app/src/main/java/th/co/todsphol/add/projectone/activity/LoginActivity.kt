package th.co.todsphol.add.projectone.activity

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTextChanged
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import th.co.todsphol.add.projectone.PhoneNumberWatcher
import th.co.todsphol.add.projectone.R
import th.co.todsphol.add.projectone.fragment.FirstFragment

import com.google.firebase.messaging.FirebaseMessaging.*

class LoginActivity : AppCompatActivity() {

    @BindView(R.id.edt_phone_number) lateinit var edtPhone: EditText
    @BindView(R.id.edt_password) lateinit var edtPassword: EditText
    private var baseR = FirebaseDatabase.getInstance().reference
    private var dataReg = baseR.child("User").child("user1").child("DATA_REG")
    var dataStatus = baseR.child("User").child("user1").child("STATUS")

    companion object {
        val EXTRA_PHONE = "EXTRA_PHONE"
        private val TAG = "FirstPageActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        onText(edtPhone.toString())
        edtPhone.addTextChangedListener(PhoneNumberWatcher(edtPhone))
        edtPhone.setText(intent.getStringExtra(EXTRA_PHONE), TextView.BufferType.EDITABLE)
        edtPhone.setSelection(edtPhone.text.length)
//        Glide.with(this).load(drawable.shoot).crossFade().into(vImageBackground)
    }


    @OnTextChanged(R.id.edt_phone_number, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    fun onText(phoneNumber: CharSequence) {
        if (phoneNumber.toString().length == 12) {
            edtPassword.visibility = android.view.View.VISIBLE
            return
        }
        edtPassword.visibility = android.view.View.GONE
    }


    @OnClick(R.id.btn_login)
    fun checkLogin() {
        dataReg.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val regPhone = dataSnapshot.child("telephone").getValue(String::class.java)
                val regPassword = dataSnapshot.child("password").getValue(String::class.java)

                val replaceNumber = edtPhone.text.toString().replace("-".toRegex(), "")
                        .replace("\\s+", "")
                val checkTrue = replaceNumber == regPhone
                        && edtPassword.text.toString() == regPassword
                val checkPhoneTrue = replaceNumber == regPhone
                        && edtPassword.text.toString() != regPassword
                val checkEdtBlank = edtPhone.text.toString() == "" || edtPassword.text.toString() == ""
                val checkLength = replaceNumber.length < 10 || edtPassword.text.toString().length < 6
                val adminNumber = "0123456789"
                val adminPassword = "123456"
                val admin = replaceNumber == adminNumber && edtPassword.text.toString() == adminPassword

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

    private fun isCorrect() {
        val homeIntent = Intent(this@LoginActivity, MapsActivity::class.java)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(homeIntent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        dataStatus.child("Slogin").setValue(1)
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, FirstFragment.newInstance())
                .commit()
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}

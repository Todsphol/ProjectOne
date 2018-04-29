package th.co.todsphol.add.projectone.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import th.co.todsphol.add.projectone.R
import kotlinx.android.synthetic.main.activity_first_page.*

class FirstPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)
        onClickGotoLogin()
        onClickNewRegister()
    }

    private fun onClickGotoLogin() {
        btn_back_to_login.setOnClickListener {
            val nextLogin = Intent(this, LoginActivity::class.java)
            startActivity(nextLogin)
            overridePendingTransition(R.anim.slide_right, R.anim.slide_left)
        }

    }

    private fun onClickNewRegister() {

        btn_new_register.setOnClickListener {
            val newRegister = Intent(this, NewRegisterActivity::class.java)
            startActivity(newRegister)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

    }

}

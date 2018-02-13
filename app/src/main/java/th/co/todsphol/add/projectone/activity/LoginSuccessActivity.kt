package th.co.todsphol.add.projectone.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import th.co.todsphol.add.projectone.R


class LoginSuccessActivity : AppCompatActivity() {
    @BindView(R.id.tv_show_success) lateinit var tvSuccess : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_success)
        ButterKnife.bind(this)
        tvSuccess.text = intent.getStringExtra(EXTRA_PHONE)

    }

    @OnClick(R.id.btn_back_to_login)
    fun gotoLogin() {
        val gotoLoginIntent = Intent(this, LoginActivity::class.java)
        gotoLoginIntent.putExtra("EXTRA_PHONE",tvSuccess.text.toString())
        gotoLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(gotoLoginIntent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
    companion object {
        const val EXTRA_PHONE = "EXTRA_PHONE"

    }
}

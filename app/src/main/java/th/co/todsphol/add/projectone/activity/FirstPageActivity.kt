package th.co.todsphol.add.projectone.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import th.co.todsphol.add.projectone.R

class FirstPageActivity : AppCompatActivity() {
    @BindView(R.id.bg_first_page) lateinit var bgImageFirstPage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)
        ButterKnife.bind(this)
        Glide.with(this).load(R.drawable.scout)
                .crossFade()
                .error(R.drawable.scout)
                .into(bgImageFirstPage)
    }

    @OnClick(R.id.btn_back_to_login)
    fun onClickGotoLogin() {
        val nextLogin = Intent(this, LoginActivity::class.java)
        startActivity(nextLogin)
        overridePendingTransition(R.anim.slide_right, R.anim.slide_left)


    }

    @OnClick(R.id.btn_new_register)
    fun onClickNewRegister() {
        val newRegister = Intent(this, NewRegisterActivity::class.java)
        startActivity(newRegister)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

    }

}

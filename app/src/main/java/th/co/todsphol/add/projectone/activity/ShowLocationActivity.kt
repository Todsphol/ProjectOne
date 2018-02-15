package th.co.todsphol.add.projectone.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick
import th.co.todsphol.add.projectone.R

class ShowLocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_location)
        ButterKnife.bind(this)
    }
    @OnClick(R.id.btn_history)
    fun goMap() {
            val intentHistory = Intent(this, MapsActivity::class.java)
            startActivity(intentHistory)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}

package th.co.todsphol.add.projectone.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_splash.*
import th.co.todsphol.add.projectone.R

class SplashActivity : AppCompatActivity() {
    private var baseR = FirebaseDatabase.getInstance().reference
    private var dataStatus = baseR.child("User").child("user1").child("STATUS")

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val firstPageIntent = Intent(this, FirstPageActivity::class.java)
            startActivity(firstPageIntent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkStatusLogin()
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

        Glide.with(this).load(R.drawable.splashlogo)
                .crossFade()
                .error(R.drawable.splashlogo)
                .into(bg_splash)

    }

    private fun checkStatusLogin() {
        dataStatus.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val loginStatus = dataSnapshot.child("Slogin").getValue(Int::class.java)
                when (loginStatus) {
                    1 -> gotoMaps()
                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

    private fun gotoMaps() {
        val mapIntent = Intent(this, DisplayActivity::class.java)
        mapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(mapIntent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }
}

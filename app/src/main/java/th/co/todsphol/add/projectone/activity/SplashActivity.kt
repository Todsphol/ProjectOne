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
import th.co.todsphol.add.projectone.R

class SplashActivity : AppCompatActivity() {
    var baseR = FirebaseDatabase.getInstance().reference
    var dataStatus = baseR.child("User").child("user1").child("STATUS")

    var mDelayHandler: Handler? = null
    val SPLASH_DELAY: Long = 3000

    val mRunnable : Runnable = Runnable {
        if (!isFinishing) {
            val firstPageIntent = Intent(this,FirstPageActivity::class.java)
            startActivity(firstPageIntent)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }
    }

    @BindView(R.id.bg) lateinit var imageBG : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        ButterKnife.bind(this)
        checkStatusLogin()
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

        Glide.with(this).load(R.drawable.splashlogo)
                .crossFade()
                .error(R.drawable.splashlogo)
                .into(imageBG)

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

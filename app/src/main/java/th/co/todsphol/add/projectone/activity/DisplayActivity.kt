package th.co.todsphol.add.projectone.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import butterknife.ButterKnife
import com.google.firebase.database.FirebaseDatabase
import th.co.todsphol.add.projectone.R
import th.co.todsphol.add.projectone.fragment.MapsFragment


class DisplayActivity : AppCompatActivity() {
    private var baseR = FirebaseDatabase.getInstance().reference
    private var dataStatus = baseR.child("User").child("user1").child("STATUS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        ButterKnife.bind(this)
        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, MapsFragment.newInstance())
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
            R.id.action_call -> call()
            R.id.action_history -> history()
            R.id.action_logout -> onClickLogout()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun history() {
        val intentHistory = Intent(this, MapsActivity::class.java)
        startActivity(intentHistory)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun onClickLogout() {
        dataStatus.child("Slogin").setValue(0)
        val logoutIntent = Intent(this, LoginActivity::class.java)
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(logoutIntent)
    }

    @SuppressLint("MissingPermission")
    private fun call() {
        val callPhone = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getString(R.string.phoneNumber)))
        startActivity(callPhone)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}

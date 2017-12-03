package th.co.todsphol.add.projectone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.support.v7.widget.Toolbar

class MainActivity : AppCompatActivity() {
    private lateinit var myToolbar : Toolbar
    private lateinit var tvTitle : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragment()
        setToolbar()

    }
    fun setToolbar() {
        myToolbar = findViewById(R.id.toolbar)
        tvTitle = findViewById(R.id.tv_toolbar_title)
        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        tvTitle.text = "Hellow World"
    }
    fun initFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, FirstFragment.newInstance())
                .commit()
    }
}

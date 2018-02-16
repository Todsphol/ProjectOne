package th.co.todsphol.add.projectone.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import th.co.todsphol.add.projectone.R
import th.co.todsphol.add.projectone.UserModel

class ShowLocationActivity : AppCompatActivity() {
    @BindView(R.id.btn_history) lateinit var btnHistory : Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var result : MutableList<UserModel>
    private lateinit var adapter: UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_location)
        ButterKnife.bind(this)
        result = mutableListOf()
        recyclerView = findViewById(R.id.rc_show_location)
        recyclerView.setHasFixedSize(true)
        createResult()
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        adapter = UserAdapter(result)
        recyclerView.adapter = adapter
    }
    @OnClick(R.id.btn_history)
    fun goMap() {
            val intentHistory = Intent(this, MapsActivity::class.java)
            startActivity(intentHistory)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    fun createResult() {
        for (i in 1..6) {
            result.add(UserModel("333.333","222.222"))
        }
    }

}

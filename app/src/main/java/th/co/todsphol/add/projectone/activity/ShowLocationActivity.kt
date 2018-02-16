package th.co.todsphol.add.projectone.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.firebase.database.*
import th.co.todsphol.add.projectone.R
import th.co.todsphol.add.projectone.UserModel

class ShowLocationActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var result: MutableList<UserModel>
    private lateinit var adapter: UserAdapter

    private var database = FirebaseDatabase.getInstance()
    private var reference = database.reference.child("User").child("user1").child("HISTORY_LOC")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_location)
        ButterKnife.bind(this)
        result = mutableListOf()
        recyclerView = findViewById(R.id.rc_show_location)
        recyclerView.setHasFixedSize(true)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        adapter = UserAdapter(result)
        recyclerView.adapter = adapter
        updateList()
    }


    override fun onContextItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            0 -> Toast.makeText(this, "0", Toast.LENGTH_SHORT).show()
            1 -> Toast.makeText(this, "1", Toast.LENGTH_SHORT).show()
        }

        return super.onContextItemSelected(item)
    }

    fun updateList() {
        try {

            reference.addChildEventListener(object : ChildEventListener {
                override fun onCancelled(databaseError: DatabaseError?) {

                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                    val model: UserModel = dataSnapshot.getValue(UserModel::class.java)!!
                    val posi = dataSnapshot.key.toInt()
                    val index = getItemIndex(model) - 2 + posi
                    result[index] = model
                    adapter.notifyItemChanged(index)

                }

                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    result.add(dataSnapshot.getValue(UserModel::class.java)!!)
                    adapter.notifyDataSetChanged()
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    val model: UserModel = dataSnapshot.getValue(UserModel::class.java)!!
                    val index = getItemIndex(model)

                    result.removeAt(index)
                    adapter.notifyItemRemoved(index)
                }

            })
        } catch (e: NullPointerException) {

        }
    }

    fun getItemIndex(user: UserModel): Int {

        return if ((1..result.size).any { result[it].key == user.key }) 1 else -1
    }


    @OnClick(R.id.btn_history)
    fun goMap() {
        val intentHistory = Intent(this, MapsActivity::class.java)
        startActivity(intentHistory)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

}

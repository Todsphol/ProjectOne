package th.co.todsphol.add.projectone.fragment


import android.app.ActionBar
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import th.co.todsphol.add.projectone.R
import th.co.todsphol.add.projectone.activity.DisplayActivity
import th.co.todsphol.add.projectone.activity.LoginActivity

class DataShowFragment : Fragment() {
    @BindView(R.id.toolbar)
    lateinit var toolBar: Toolbar
    @BindView(R.id.tv_toolbar_title)
    lateinit var title: TextView
    @BindView(R.id.tv_name_client)
    lateinit var nameClient: TextView
    @BindView(R.id.tv_surname_client)
    lateinit var surNameClient: TextView
    @BindView(R.id.tv_color_client)
    lateinit var colorCar: TextView
    @BindView(R.id.tv_brand_client)
    lateinit var brandCar: TextView
    @BindView(R.id.tv_county)
    lateinit var licencePlate: TextView
    @BindView(R.id.tv_status)
    lateinit var alarmStatus: TextView
    @BindView(R.id.imv_status)
    lateinit var imViewStatus: ImageView
    private var baseR = FirebaseDatabase.getInstance().reference
    private var dataName = baseR.child("User").child("user1").child("DATA_PERS")
    private var dataCar = baseR.child("User").child("user1").child("DATA_CAR")
    private var dataStatus = baseR.child("User").child("user1").child("STATUS")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_data_show, container, false)
        ButterKnife.bind(this, view)
        setToolbar()
        getDataCar()
        getDataname()
        getDataStatus()
        return view

    }

    private fun setToolbar() {
        getMainActivity().setSupportActionBar(toolBar)
        getMainActivity().supportActionBar?.setDisplayShowTitleEnabled(false)
        getMainActivity().supportActionBar?.setHomeButtonEnabled(true)
        getMainActivity().supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title.text = "ข้อมูลรถของท่าน"

    }

    fun getDataCar() {
        dataCar.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataColorCar = dataSnapshot.child("color").getValue(String::class.java)
                val dataBrand = dataSnapshot.child("Type").getValue(String::class.java)
                val dataLicencePlate = dataSnapshot.child("LP").getValue(String::class.java)
                brandCar.text = dataBrand.toString()
                colorCar.text = dataColorCar.toString()
                licencePlate.text = dataLicencePlate.toString()
            }

            override fun onCancelled(p0: DatabaseError?) {

            }

        })
    }

    fun getDataname() {
        dataName.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val namePer = dataSnapshot.child("name").getValue(String::class.java)
                val surNamePer = dataSnapshot.child("surname").getValue(String::class.java)
                nameClient.text = namePer.toString()
                surNameClient.text = surNamePer.toString()
            }

            override fun onCancelled(p0: DatabaseError?) {

            }

        })
    }


    fun getDataStatus() {
        dataStatus.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataStatusAlarm = dataSnapshot.child("Salarm").getValue(Int::class.java)
                changeColorStatus(dataStatusAlarm)
            }

            override fun onCancelled(p0: DatabaseError?) {

            }

        })
    }

    private fun changeColorStatus(dataStatusAlarm: Int?) {
        if (dataStatusAlarm == 0) {
            try {
                imViewStatus.setColorFilter(ContextCompat.getColor(context!!, R.color.colorGreen))
                alarmStatus.text = "ปลอดภัย"
            } catch (e: NullPointerException) {

            }

        } else {
            try {
                imViewStatus.setColorFilter(ContextCompat.getColor(context!!, R.color.colorRed))
                alarmStatus.text = "ไม่ปลอดภัย"
            } catch (e: NullPointerException) {

            }
        }
    }


    private fun getMainActivity(): DisplayActivity {
        return activity as DisplayActivity
    }

    companion object {
        fun newInstance(): Fragment {
            val bundle = Bundle()
            val fragment = DataShowFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}
package th.co.todsphol.add.projectone.activity

import android.annotation.SuppressLint
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import butterknife.BindView
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import th.co.todsphol.add.projectone.R
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import butterknife.ButterKnife
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {


    @BindView(R.id.toolbar) lateinit var toolBar: Toolbar
    @BindView(R.id.tv_toolbar_title) lateinit var title: TextView
    @BindView(R.id.tv_name_client) lateinit var nameClient: TextView
    @BindView(R.id.tv_surname_client) lateinit var surNameClient: TextView
    @BindView(R.id.tv_color_client) lateinit var colorCar: TextView
    @BindView(R.id.tv_brand_client) lateinit var brandCar: TextView
    @BindView(R.id.tv_county) lateinit var licencePlate: TextView
    @BindView(R.id.tv_status) lateinit var alarmStatus: TextView
    @BindView(R.id.imv_status) lateinit var imvStatus : ImageView
    lateinit var mMap: GoogleMap
    private var baseR = FirebaseDatabase.getInstance().reference
    private var dataName = baseR.child("User").child("user1").child("DATA_PERS")
    private var dataCar = baseR.child("User").child("user1").child("DATA_CAR")
    private var dataStatus = baseR.child("User").child("user1").child("STATUS")
    private var dataLocation = baseR.child("User").child("user1").child("DATA_LOCATION")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        ButterKnife.bind(this)
        setToolBar()
        getDataname()
        getDataCar()
        getDataStatus()
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
                if (dataStatusAlarm == 0) {
                    alarmStatus.text = "ปลอดภัย"
                    imvStatus.setColorFilter(ContextCompat.getColor(this@MapsActivity, R.color.colorGreenlearm))
                } else {
                    alarmStatus.text = "ไม่ปลอดภัย"
                    imvStatus.setColorFilter(ContextCompat.getColor(this@MapsActivity, R.color.colorRed))
                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }

        })
    }

    fun setToolBar() {
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        title.text = "ตำแหน่งของรถ"
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        dataLocation.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataLatitude = dataSnapshot.child("Latitude").getValue(String::class.java)!!.toDouble()
                val dataLongitude = dataSnapshot.child("Longtitude").getValue(String::class.java)!!.toDouble()
                val testCheck = LatLng(dataLatitude, dataLongitude)
                mMap.addMarker(MarkerOptions().position(testCheck).title("Test"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(testCheck))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f))
            }

            override fun onCancelled(p0: DatabaseError?) {

            }

        })
        mMap = googleMap
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.isMyLocationEnabled = true
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)

    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_call -> call()
            R.id.action_logout -> onClickLogout()
        }
        return super.onOptionsItemSelected(item)
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

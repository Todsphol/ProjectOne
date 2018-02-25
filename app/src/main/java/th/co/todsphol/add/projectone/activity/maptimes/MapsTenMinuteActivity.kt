package th.co.todsphol.add.projectone.activity.maptimes

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import th.co.todsphol.add.projectone.R

class MapsTenMinuteActivity : AppCompatActivity(), OnMapReadyCallback{

    @BindView(R.id.toolbar) lateinit var tenToolBar : Toolbar
    @BindView(R.id.tv_toolbar_title) lateinit var tenTitle : TextView

    private lateinit var mMap: GoogleMap
    private var baseR = FirebaseDatabase.getInstance().reference
    private var locationTenMinute = baseR.child("User").child("user1").child("HISTORY_LOC")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_ten_minute)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        ButterKnife.bind(this)
        setToolbar()
    }

    private fun setToolbar() {
        setSupportActionBar(tenToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        tenTitle.text = "เมื่อ 10 นาทีที่ผ่านมา"
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        locationTenMinute.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataLo10 = dataSnapshot.child("1")
                val dataLatitude10 = dataLo10.child("LAT").getValue(String::class.java)!!.toDouble()
                val dataLongitude10 = dataLo10.child("LON").getValue(String::class.java)!!.toDouble()
                val locCamera = LatLng(dataLatitude10, dataLongitude10)
                mMap.addMarker(MarkerOptions().position(locCamera).title("ตำแหน่งของคุณ").snippet("เมื่อ 10 นาทีที่ผ่านมา"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(locCamera))
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
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}

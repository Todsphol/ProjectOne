package th.co.todsphol.add.projectone.activity.maptimes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

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
import kotlinx.android.synthetic.main.custom_toolbar.*
import th.co.todsphol.add.projectone.R

class MapsThirtyMinuteActivity : AppCompatActivity(), OnMapReadyCallback {

    private var baseR = FirebaseDatabase.getInstance().reference
    private var locationThirtyMinute = baseR.child("User").child("user1").child("HISTORY_LOC")

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_thirty_minute)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setToolbar()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        tv_toolbar_title.text = "เมื่อ 30 นาทีที่ผ่านมา"
    }

    override fun onMapReady(googleMap: GoogleMap) {
        locationThirtyMinute.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataLo30 = dataSnapshot.child("3")
                val dataLatitude30 = dataLo30.child("LAT").getValue(String::class.java)!!.toDouble()
                val dataLongitude30 = dataLo30.child("LON").getValue(String::class.java)!!.toDouble()
                val latLng = LatLng(dataLatitude30, dataLongitude30)
                mMap.addMarker(MarkerOptions().position(latLng).title("ตำแหน่งของคุณ").snippet("เมื่อ 30 นาทีที่ผ่านมา"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f))
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })
        mMap = googleMap
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}

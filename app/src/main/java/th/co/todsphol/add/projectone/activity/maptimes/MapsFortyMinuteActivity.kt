package th.co.todsphol.add.projectone.activity.maptimes

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

class MapsFortyMinuteActivity : AppCompatActivity(), OnMapReadyCallback {

    @BindView(R.id.toolbar) lateinit var fortyToolbar : Toolbar
    @BindView(R.id.tv_toolbar_title) lateinit var fortyTitle : TextView

    private var baseR = FirebaseDatabase.getInstance().reference
    private var locationFortyMinute = baseR.child("User").child("user1").child("HISTORY_LOC")

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_forty_minute)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        ButterKnife.bind(this)
        setToolbar()
    }

    private fun setToolbar() {
        setSupportActionBar(fortyToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        fortyTitle.text = "เมื่อ 40 นาทีที่ผ่านมา"
    }

    override fun onMapReady(googleMap: GoogleMap) {
        locationFortyMinute.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataLo40 = dataSnapshot.child("4")
                val dataLatitude40 = dataLo40.child("LAT").getValue(String::class.java)!!.toDouble()
                val dataLongitude40 = dataLo40.child("LON").getValue(String::class.java)!!.toDouble()
                val latLng = LatLng(dataLatitude40, dataLongitude40)
                mMap.addMarker(MarkerOptions().position(latLng).title("ตำแหน่งของคุณ").snippet("เมื่อ 40 นาทีที่ผ่านมา"))
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

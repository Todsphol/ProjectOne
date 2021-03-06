package th.co.todsphol.add.projectone.activity

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import th.co.todsphol.add.projectone.R
import android.view.MenuItem
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.custom_toolbar.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mMap: GoogleMap
    private var baseR = FirebaseDatabase.getInstance().reference
    private var dataPastPosition = baseR.child("User").child("user1").child("HISTORY_LOC")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setToolBar()
    }

    private fun setToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        tv_toolbar_title.text = "แผนที่แสดงตำแหน่งรถของคุณ"
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        dataPastPosition.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataLo10 = dataSnapshot.child("1")
                val dataLatitude10 = dataLo10.child("LAT").getValue(String::class.java)!!.toDouble()
                val dataLongitude10 = dataLo10.child("LON").getValue(String::class.java)!!.toDouble()
                val dataLo20 = dataSnapshot.child("2")
                val dataLatitude20 = dataLo20.child("LAT").getValue(String::class.java)!!.toDouble()
                val dataLongitude20 = dataLo20.child("LON").getValue(String::class.java)!!.toDouble()
                val dataLo30 = dataSnapshot.child("3")
                val dataLatitude30 = dataLo30.child("LAT").getValue(String::class.java)!!.toDouble()
                val dataLongitude30 = dataLo30.child("LON").getValue(String::class.java)!!.toDouble()
                val dataLo40 = dataSnapshot.child("4")
                val dataLatitude40 = dataLo40.child("LAT").getValue(String::class.java)!!.toDouble()
                val dataLongitude40 = dataLo40.child("LON").getValue(String::class.java)!!.toDouble()
                val dataLo50 = dataSnapshot.child("5")
                val dataLatitude50 = dataLo50.child("LAT").getValue(String::class.java)!!.toDouble()
                val dataLongitude50 = dataLo50.child("LON").getValue(String::class.java)!!.toDouble()
                val dataLo60 = dataSnapshot.child("6")
                val dataLatitude60 = dataLo60.child("LAT").getValue(String::class.java)!!.toDouble()
                val dataLongitude60 = dataLo60.child("LON").getValue(String::class.java)!!.toDouble()
                mMap.addMarker(MarkerOptions().position(LatLng(dataLatitude10, dataLongitude10)).title(getString(R.string.Mylocation)).snippet("10 นาที่ที่ผ่านมา"))
                mMap.addMarker(MarkerOptions().position(LatLng(dataLatitude20, dataLongitude20)).title(getString(R.string.Mylocation)).snippet("20 นาที่ที่ผ่านมา"))
                mMap.addMarker(MarkerOptions().position(LatLng(dataLatitude30, dataLongitude30)).title(getString(R.string.Mylocation)).snippet("30 นาที่ที่ผ่านมา"))
                mMap.addMarker(MarkerOptions().position(LatLng(dataLatitude40, dataLongitude40)).title(getString(R.string.Mylocation)).snippet("40 นาที่ที่ผ่านมา"))
                mMap.addMarker(MarkerOptions().position(LatLng(dataLatitude50, dataLongitude50)).title(getString(R.string.Mylocation)).snippet("50 นาที่ที่ผ่านมา"))
                mMap.addMarker(MarkerOptions().position(LatLng(dataLatitude60, dataLongitude60)).title(getString(R.string.Mylocation)).snippet("60 นาที่ที่ผ่านมา"))
                val testCheck = LatLng(dataLatitude10, dataLongitude10)
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
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}

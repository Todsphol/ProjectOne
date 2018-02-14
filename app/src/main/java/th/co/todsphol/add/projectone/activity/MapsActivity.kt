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
import android.support.v7.widget.Toolbar
import android.view.MenuItem
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
    lateinit var mMap: GoogleMap
    private var baseR = FirebaseDatabase.getInstance().reference
    private var dataLocation = baseR.child("User").child("user1").child("DATA_LOCATION")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        ButterKnife.bind(this)
        setToolBar()
    }

    private fun setToolBar() {
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        title.text = "ประวัติการเดินทางของรถ"
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
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}

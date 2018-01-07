package th.co.todsphol.add.projectone.activity


import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.Nullable
import android.widget.Button
import butterknife.BindView

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import th.co.todsphol.add.projectone.R
import android.R.attr.button
import android.annotation.SuppressLint
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    @Nullable
    @BindView(R.id.btn_call) lateinit var phoneCall : Button

    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isTiltGesturesEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        val chagwattana = LatLng(13.903890, 100.528437)
        mMap.addMarker(MarkerOptions().position(chagwattana).title("Changwattana"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(chagwattana))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))

    }

}
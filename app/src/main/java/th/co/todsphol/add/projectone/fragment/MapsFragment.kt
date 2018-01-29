package th.co.todsphol.add.projectone.fragment


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import th.co.todsphol.add.projectone.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import th.co.todsphol.add.projectone.activity.DisplayActivity


class MapsFragment : Fragment(), OnMapReadyCallback {

    @BindView(R.id.mapView)lateinit var mMapView : MapView
    @BindView(R.id.btn_data) lateinit var nextData : Button
    @BindView(R.id.btn_zoom_in) lateinit var zoomIn : Button
    @BindView(R.id.btn_zoom_out) lateinit var zoomOut : Button
    private var baseR = FirebaseDatabase.getInstance().reference
    private var dataLocation = baseR.child("User").child("user1").child("DATA_LOCATION")
    var mgoogleMap: GoogleMap? = null
    @SuppressLint("MissingPermission")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        ButterKnife.bind(this, view)
        mMapView.onCreate(null)
        mMapView.onResume()
        mMapView.getMapAsync(this)
        nextData.setOnClickListener {
            getMainActivity().changeFragment(DataShowFragment.newInstance())
        }
        setOnClickZoom()
        return view
    }

    private fun setOnClickZoom() {
        zoomIn.setOnClickListener {
            mgoogleMap?.animateCamera(CameraUpdateFactory.zoomIn())
        }
        zoomOut.setOnClickListener {
            mgoogleMap?.animateCamera(CameraUpdateFactory.zoomOut())
        }
    }

    fun getMainActivity() : DisplayActivity {
        return activity as DisplayActivity
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        dataLocation.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataLatitude = dataSnapshot.child("Latitude").getValue(String::class.java)!!.toDouble()
                val dataLongitude = dataSnapshot.child("Longtitude").getValue(String::class.java)!!.toDouble()
                val testCheck = LatLng(dataLatitude, dataLongitude)
                mgoogleMap?.addMarker(MarkerOptions().position(testCheck).title("Test").snippet("My Bicycle"))
                mgoogleMap?.animateCamera(CameraUpdateFactory.newLatLng(testCheck))
            }

            override fun onCancelled(p0: DatabaseError?) {

            }

        })
        mgoogleMap = googleMap
        MapsInitializer.initialize(context)
        mgoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        mgoogleMap?.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
        mgoogleMap?.uiSettings?.isTiltGesturesEnabled = true
        mgoogleMap?.uiSettings?.isRotateGesturesEnabled = true
        mgoogleMap?.uiSettings?.isScrollGesturesEnabled = true
        mgoogleMap?.uiSettings?.isMyLocationButtonEnabled = true
        mgoogleMap?.isMyLocationEnabled = true

    }



    companion object {
        fun newInstance(): Fragment {
            val bundle = Bundle()
            val fragment = MapsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}

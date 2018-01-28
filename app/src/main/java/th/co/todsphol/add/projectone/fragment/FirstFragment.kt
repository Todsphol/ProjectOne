package th.co.todsphol.add.projectone.fragment


import android.annotation.SuppressLint
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
import th.co.todsphol.add.projectone.activity.DisplayActivity


class FirstFragment : Fragment(), OnMapReadyCallback {

    @BindView(R.id.mapView)lateinit var mMapView : MapView
    @BindView(R.id.btn_data) lateinit var nextData : Button
    var mgoogleMap: GoogleMap? = null
    @SuppressLint("MissingPermission")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        ButterKnife.bind(this, view)
        mMapView.onCreate(null)
        mMapView.onResume()
        mMapView.getMapAsync(this)
        nextData.setOnClickListener {
            getMainActivity().changeFragment(SecondFragment.newInstance())
        }
        return view
    }

    fun getMainActivity() : DisplayActivity {
        return activity as DisplayActivity
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        mgoogleMap = googleMap
        MapsInitializer.initialize(context)
        mgoogleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        mgoogleMap?.addMarker(MarkerOptions().position(LatLng(13.903960,100.528231)).title("Central"))?.snippet = "I hope to go"
        mgoogleMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(13.903960,100.528231)))
        mgoogleMap?.animateCamera(CameraUpdateFactory.zoomTo(16.0f))
        mgoogleMap?.uiSettings?.isTiltGesturesEnabled = true
        mgoogleMap?.uiSettings?.isRotateGesturesEnabled = true
        mgoogleMap?.uiSettings?.isScrollGesturesEnabled = true
        mgoogleMap?.uiSettings?.isMyLocationButtonEnabled = true
        mgoogleMap?.isMyLocationEnabled = true
    }


    companion object {
        fun newInstance(): Fragment {
            val bundle = Bundle()
            val fragment = FirstFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}

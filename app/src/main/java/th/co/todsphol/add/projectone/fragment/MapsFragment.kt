package th.co.todsphol.add.projectone.fragment


import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.ActivityCompat
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import th.co.todsphol.add.projectone.activity.DisplayActivity


@Suppress("DEPRECATION")
class MapsFragment : Fragment(), OnMapReadyCallback {

    @BindView(R.id.mapView)
    lateinit var mMapView: MapView
    @BindView(R.id.btn_data)
    lateinit var nextData: Button
    @BindView(R.id.btn_zoom_in)
    lateinit var zoomIn: Button
    @BindView(R.id.btn_zoom_out)
    lateinit var zoomOut: Button
    private var baseR = FirebaseDatabase.getInstance().reference
    private var dataLocation = baseR.child("User").child("user1").child("DATA_LOCATION")
    private var dataCar = baseR.child("User").child("user1").child("DATA_CAR").child("Type")
    var mgoogleMap: GoogleMap? = null
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

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(context!!,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getMainActivity(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
    }

    private fun setOnClickZoom() {
        zoomIn.setOnClickListener {
            mgoogleMap?.animateCamera(CameraUpdateFactory.zoomIn())
        }
        zoomOut.setOnClickListener {
            mgoogleMap?.animateCamera(CameraUpdateFactory.zoomOut())
        }
    }


    private fun getMainActivity(): DisplayActivity {
        return activity as DisplayActivity
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        val height = 96
        val width = 96
        val bitmapYamaha = resources.getDrawable(R.drawable.yamaha1) as BitmapDrawable
        val bitmapKawasaki = resources.getDrawable(R.drawable.kawasaki) as BitmapDrawable
        val bitmapHonda = resources.getDrawable(R.drawable.honda) as BitmapDrawable
        val yamaha = bitmapYamaha.bitmap
        val kawasaki = bitmapKawasaki.bitmap
        val honda = bitmapHonda.bitmap
        val yamahaMarker = Bitmap.createScaledBitmap(yamaha, width, height, false)
        val kawasakiMarker = Bitmap.createScaledBitmap(kawasaki, width, height, false)
        val hondaMarker = Bitmap.createScaledBitmap(honda, width, height, false)
        dataLocation.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataLatitude = dataSnapshot.child("Latitude").getValue(String::class.java)!!.toDouble()
                val dataLongitude = dataSnapshot.child("Longtitude").getValue(String::class.java)!!.toDouble()
                val location = LatLng(dataLatitude, dataLongitude)
                dataCar.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val dataType = dataSnapshot.getValue(String::class.java)
//                        val marker = MarkerOptions().position(testCheck).title("Test").snippet("My Bicycle")
                        if (dataType == "YAMAHA") {
                            mgoogleMap?.addMarker(MarkerOptions().position(location)
                                    .title("Your Motorcycle")
                                    .snippet("Stay Here")
                                    .icon(BitmapDescriptorFactory.fromBitmap(yamahaMarker)))
                        } else if (dataType == "Kawasaki"){
                            mgoogleMap?.addMarker(MarkerOptions().position(location)
                                    .title("Your Motorcycle")
                                    .snippet("Stay Here")
                                    .icon(BitmapDescriptorFactory.fromBitmap(kawasakiMarker)))
                        } else if (dataType == "Honda"){
                            mgoogleMap?.addMarker(MarkerOptions().position(location)
                                    .title("Your Motorcycle")
                                    .snippet("Stay Here")
                                    .icon(BitmapDescriptorFactory.fromBitmap(hondaMarker)))
                        }

                    }

                    override fun onCancelled(p0: DatabaseError?) {

                    }

                })
                mgoogleMap?.animateCamera(CameraUpdateFactory.newLatLng(location))

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
        setUpMap()
    }


    companion object {
        fun newInstance(): Fragment {
            val bundle = Bundle()
            val fragment = MapsFragment()
            fragment.arguments = bundle
            return fragment
        }

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}

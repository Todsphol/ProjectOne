package th.co.todsphol.add.projectone.fragment


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import th.co.todsphol.add.projectone.R
import th.co.todsphol.add.projectone.activity.DisplayActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import me.rishabhkhanna.customtogglebutton.CustomToggleButton


@Suppress("DEPRECATION")
class DataShowFragment : Fragment() {
    @BindView(R.id.toolbar) lateinit var toolBar: Toolbar
    @BindView(R.id.tv_toolbar_title) lateinit var title: TextView
    @BindView(R.id.tv_name_client) lateinit var nameClient: TextView
    @BindView(R.id.tv_surname_client) lateinit var surNameClient: TextView
    @BindView(R.id.tv_color_client) lateinit var colorCar: TextView
    @BindView(R.id.tv_brand_client) lateinit var brandCar: TextView
    @BindView(R.id.tv_county) lateinit var licencePlate: TextView
    @BindView(R.id.tv_status) lateinit var alarmStatus: TextView
    @BindView(R.id.imv_status) lateinit var imViewStatus: ImageView
    @BindView(R.id.imageViewShow) lateinit var ivShowImage : ImageView
    @BindView(R.id.tg_noti) lateinit var tgNotification : CustomToggleButton
    private var baseR = FirebaseDatabase.getInstance().reference
    private var dataName = baseR.child("User").child("user1").child("DATA_PERS")
    private var dataCar = baseR.child("User").child("user1").child("DATA_CAR")
    private var dataStatus = baseR.child("User").child("user1").child("STATUS")
    private val mContext = this
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_data_show, container, false)
        ButterKnife.bind(this, view)
        setToolbar()
        getDataCar()
        getDataname()
        getDataStatus()
        return view

    }


    fun isCheckToggleStatus() {
        tgNotification.setOnClickListener {
            if (tgNotification.isChecked) {
                Toast.makeText(context,"ทำการเปิดการแจ้งเตือนแล้ว",Toast.LENGTH_SHORT).show()
                dataStatus.child("Sowner").setValue(1)
            } else if (!tgNotification.isChecked) {
            Toast.makeText(context,"ทำการปิดการแจ้งเตือนแล้ว",Toast.LENGTH_SHORT).show()
                dataStatus.child("Sowner").setValue(0)
         }
        }
    }

    private fun setToolbar() {
        getMainActivity().setSupportActionBar(toolBar)
        getMainActivity().supportActionBar?.setDisplayShowTitleEnabled(false)
        getMainActivity().supportActionBar?.setHomeButtonEnabled(true)
        getMainActivity().supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title.text = "ข้อมูลรถของท่าน"

    }

    fun getDataCar() {
        dataCar.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataColorCar = dataSnapshot.child("color").getValue(String::class.java)
                val dataBrand = dataSnapshot.child("Type").getValue(String::class.java)
                val dataLicencePlate = dataSnapshot.child("LP").getValue(String::class.java)
                val dataUri = dataSnapshot.child("Images").getValue(String::class.java)
                try {

                    brandCar.text = dataBrand.toString()
                    colorCar.text = dataColorCar.toString()
                    licencePlate.text = dataLicencePlate.toString()
                    generateImage(dataUri)

                }catch (e : IllegalArgumentException) {

                }
            }

            override fun onCancelled(p0: DatabaseError?) {

            }

        })
    }

    private fun generateImage(dataUri: String?) {
        Glide.with(context)
                .load(dataUri)
                .crossFade()
                .error(R.drawable.ic_motorcycle)
                .into(ivShowImage)
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
            @SuppressLint("ResourceAsColor")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataStatusAlarm = dataSnapshot.child("Salarm").getValue(Int::class.java)
                val dataOwnerStatus = dataSnapshot.child("Sowner").getValue(Int::class.java)
                changeColorStatus(dataStatusAlarm)
                if (dataOwnerStatus == 0) {
                    tgNotification.isChecked = false
                } else if (dataOwnerStatus == 1) {
                    tgNotification.isChecked = true
                }
                isCheckToggleStatus()
            }

            override fun onCancelled(p0: DatabaseError?) {

            }

        })
    }

    private fun changeColorStatus(dataStatusAlarm: Int?) {
        if (dataStatusAlarm == 0) {
            try {
                imViewStatus.setColorFilter(ContextCompat.getColor(context!!, R.color.colorGreen))
                alarmStatus.text = "ปลอดภัย"
            } catch (e: NullPointerException) {

            }

        } else {
            try {
                imViewStatus.setColorFilter(ContextCompat.getColor(context!!, R.color.colorRed))
                alarmStatus.text = "ไม่ปลอดภัย"
            } catch (e: NullPointerException) {

            }
        }
    }

//    private fun downloadDataViaUrl() {
//        val progressDialog = ProgressDialog(context)
//        progressDialog.setTitle("Loading...")
//        progressDialog.show()
//        imageRef.downloadUrl.addOnSuccessListener({ uri ->
//            progressDialog.dismiss()
//            Log.d("URI", uri.toString())
//            Glide.with(this)
//                    .load(uri)
//                    .crossFade()
//                    .error(R.drawable.ic_motorcycle)
//                    .into(ivShowImage)
//            Toast.makeText(context,"Downloaded",Toast.LENGTH_SHORT).show()
//        }).addOnFailureListener( {
//            progressDialog.dismiss()
//            Toast.makeText(context,"Download is Fail",Toast.LENGTH_SHORT).show()
//        })
//    }


    private fun getMainActivity(): DisplayActivity {
        return activity as DisplayActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dialog = ProgressDialog.show(activity, "กำลังโหลด", "กรุณารอสักครู่", true)
        dialog.show()
        val handler = Handler()
        handler.postDelayed(Runnable { dialog.dismiss() }, 2000)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (Util.isOnMainThread()) {
            Glide.with(mContext).pauseRequests()
        }

    }

    companion object {
        fun newInstance(): Fragment {
            val bundle = Bundle()
            val fragment = DataShowFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}
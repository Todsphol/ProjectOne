package th.co.todsphol.add.projectone.notification

import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.firebase.messaging.FirebaseMessaging


open class FirebaseMessagingTopic : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {

    }

    val TAG = FirebaseMessagingTopic::class.java.simpleName

    fun subscribe(topic: String?) {
        topic?.let {
            Log.d(TAG, "subscribe: ${topic}")
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
        }
    }

    fun unsubscribe(topic: String?) {
        topic?.let {
            Log.d(TAG, "unsubscribe: ${topic}")
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
        }
    }

}
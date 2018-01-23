package th.co.todsphol.add.projectone.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging


open class FirebaseMessagingTopic {

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
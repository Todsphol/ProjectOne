package th.co.todsphol.add.projectone.notification
import com.google.firebase.iid.FirebaseInstanceIdService



class MessagingInstanceId : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
    }
}
package hv.dev4u.org.clientefirebase.notificaciones;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class MiFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "NOTIFICACION";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("hv.dev4u.org.clientefirebase");
        Log.d(TAG, "onTokenRefresh: "+token);
    }
}

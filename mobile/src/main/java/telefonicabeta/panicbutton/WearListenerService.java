package telefonicabeta.panicbutton;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;


public class WearListenerService extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Intent intent = new Intent("telefonicabeta.panicbutton.RECEIVER");
        intent.putExtra("action", messageEvent.getPath());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        Log.i("Wear", messageEvent.getPath().toString());
    }
}

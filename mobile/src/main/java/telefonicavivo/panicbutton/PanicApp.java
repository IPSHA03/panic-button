package telefonicavivo.panicbutton;

import android.app.Application;
import telefonicavivo.panicbutton.ble.Wearable;


public class PanicApp extends Application {
    private static Wearable kit;

    public void onCreate() {
        super.onCreate();
        this.setKit();
    }

    /**
     * Get/Set Wearable Kit
     */
    private void setKit() {
        kit = new Wearable(getApplicationContext(), "wV3_0B0039AF");
    }
    public static Wearable getKit() {
        return kit;
    }
}

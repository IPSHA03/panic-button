package telefonicabeta.panicbutton;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import telefonicabeta.panicbutton.ble.FindWearableListener;
import telefonicabeta.panicbutton.ble.Wearable;


public class MainActivity extends Activity {
    private PanicButton panicButton;
    private Activity activity;
    private PanicMap panicMap;

    /**
     * Broadcast receiver for the panicButton button
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            Log.i("Wear", Boolean.toString(panicButton.inPanic()));

            if (bundle.getString("action").equals("blink")) {
                panicButton.blink();

            } else {
                if (panicButton.inPanic()) {
                    panicButton.off();

                } else {
                    panicButton.on();
                }
            }
        }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register Broadcast manager
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("telefonicabeta.panicbutton.RECEIVER"));

        // Get the panicButton button in activity
        this.activity = this;
        this.panicMap = new PanicMap(this.activity);
        this.panicButton = new PanicButton(this.activity);

        panicMap.getMap().setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (!panicButton.inPanic) {
                    panicButton.on();
                    panicMap.setMarkerIcon("gray");

                } else {
                    panicButton.off();
                    panicMap.setMarkerIcon("red");
                }
                return true;
            }
        });

        // Instantiate the wearable kit
        final Wearable kit = PanicApp.getKit();
        kit.findWearable();

        // Set the find wearable listeners
        kit.setOnFindWearableListner(new FindWearableListener() {
            @Override
            public void connected(BluetoothDevice device) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(activity, "Conectado ao Kit Wearable", Toast.LENGTH_LONG);
                        toast.show();

                        kit.ledON("GREEN");
                    }
                });
            }

            @Override
            public void disconnected() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(activity, "Desconectado do Kit Wearable", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }

            @Override
            public void notFound() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(activity, "Não foi possível encontrar o Wearable", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        });

        // On panicButton button click
        panicButton.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (panicButton.inPanic()) {
                    panicButton.off();
                    kit.ledOFF();
                    kit.ledON("GREEN");
                    panicMap.setMarkerIcon("red");

                } else {
                    panicButton.on();
                    kit.ledOFF();
                    kit.ledON("RED");
                    panicMap.setMarkerIcon("gray");
                }
            }
        });
        // On panicButton button long click
        panicButton.button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                panicButton.blink();
                return true;
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        Log.i("Panic button", Integer.toString(keyCode));

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case 79:
                if (!panicButton.inPanic()) {
                    panicButton.on();
                    panicMap.setMarkerIcon("gray");
                    //kit.ledOn("RED");
                }

                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                panicButton.off();
                panicMap.setMarkerIcon("red");
                //kit.lefOff();

                return true;

            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

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
import android.widget.Button;
import android.widget.Toast;

import telefonicabeta.panicbutton.ble.FindWearableListener;
import telefonicabeta.panicbutton.ble.Wearable;


public class MainActivity extends Activity {
    PanicButton panic;
    Activity activity;

    /**
     * Broadcast receiver for the panic button
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (bundle != null) {

            Log.i("Wear", Boolean.toString(panic.inPanic()));

            if (bundle.getString("action").equals("blink")) {
                panic.blink();

            } else {
                if (panic.inPanic()) {
                    panic.off();

                } else {
                    panic.on();
                }
            }
        }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Register Broadcast manager
         */
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("telefonicabeta.panicbutton.RECEIVER"));

        final Button button = (Button) findViewById(R.id.panic);
        this.panic = new PanicButton(this, button);
        this.activity = this;

        final Wearable kit = PanicApp.getKit();
        kit.findWearable();

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
                        Toast toast = Toast.makeText(activity, "Disconectado do Kit Wearable", Toast.LENGTH_LONG);
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

        //On panic button click
        panic.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (panic.inPanic()) {
                    panic.off();
                    kit.ledOFF();
                    kit.ledON("GREEN");

                } else {
                    panic.on();
                    kit.ledOFF();
                    kit.ledON("RED");
                }
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        Log.i("Panic button", Integer.toString(keyCode));

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                panic.on();
                //kit.ledOn("RED");

                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                panic.off();
                //kit.lefOff();

                return true;

            case 79:
                if (!panic.inPanic()) {
                    panic.on();
                    //kit.ledOn("RED");
                }

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

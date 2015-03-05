package telefonicavivo.panicbutton;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
    PanicButton panic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.panic);
        panic = new PanicButton(this, button);

        //On panic button click
        panic.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (panic.inPanic()) {
                    panic.off();

                } else {
                    panic.on();
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

                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                panic.off();

                return true;

            case 79:
                if (!panic.inPanic()) {
                    panic.on();
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

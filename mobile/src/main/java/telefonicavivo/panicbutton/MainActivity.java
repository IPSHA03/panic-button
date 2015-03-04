package telefonicavivo.panicbutton;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class MainActivity extends Activity {
    boolean inPanic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button panic = (Button) findViewById(R.id.panic);

        //On panic button click
        panic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (inPanic) {
                    panicButton(panic, false);

                } else {
                    panicButton(panic, true);
                }
            }
        });
    }

    public void panicButton(Button panic, boolean status) {
        final ActionBar actionbar = getActionBar();
        final Window window = this.getWindow();

        if (status) {
            inPanic = true;
            window.setStatusBarColor(this.getResources().getColor(R.color.dark_red));
            window.setNavigationBarColor(this.getResources().getColor(R.color.red));
            actionbar.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.red)));
            panic.setBackgroundColor(this.getResources().getColor(R.color.dark_gray));
            panic.setText("Cancel Panic Button");

        } else {
            inPanic = false;
            window.setStatusBarColor(this.getResources().getColor(R.color.dark_blue));
            window.setNavigationBarColor(this.getResources().getColor(R.color.blue));
            actionbar.setBackgroundDrawable(new ColorDrawable(this.getResources().getColor(R.color.blue)));
            panic.setBackgroundColor(this.getResources().getColor(R.color.red));
            panic.setText("Activate Panic Button");
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        Log.i("Panic button", Integer.toString(keyCode));
        final Button panic = (Button) findViewById(R.id.panic);

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                panicButton(panic, true);

                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                panicButton(panic, false);

                return true;

            case 79:
                if (!inPanic) {
                    panicButton(panic, true);
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

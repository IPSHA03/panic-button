package telefonicavivo.panicbutton;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    boolean inPanic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        final Button panic = (Button) findViewById(R.id.panic);
        final ActionBar actionbar = getActionBar();

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {}
        });

        //On panic button click
        panic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (inPanic) {
                    inPanic = false;
                    actionbar.setBackgroundDrawable(new ColorDrawable(R.color.blue));
                    panic.setText("Activate Panic Button");

                } else {
                    inPanic = true;
                    actionbar.setBackgroundDrawable(new ColorDrawable(R.color.red));
                    panic.setText("Cancel Panic Button");
                }
            }
        });
    }
}

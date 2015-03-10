package telefonicabeta.panicbutton;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity {
    private static final long CONNECTION_TIME_OUT_MS = 100;
    private GoogleApiClient client;
    private String nodeId;
    private Boolean inPanic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initApi();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                setupPanicButton();
            }
        });
    }

    /**
     * Initializes the GoogleApiClient and gets the Node ID of the connected device.
     */
    private void initApi() {
        client = getGoogleApiClient(this);
        retrieveDeviceNode();
    }

    /**
     * Sets up the button for handling click events.
     */
    private void setupPanicButton() {
        final Button panicButton = (Button) findViewById(R.id.panicButton);
        final Resources resources = getResources();

        panicButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (inPanic) {
                    panicButton.setBackgroundColor(resources.getColor(R.color.dark_gray));
                    panicButton.setText(getString(R.string.activate_panic));
                    inPanic = false;

                } else {
                    panicButton.setBackgroundColor(resources.getColor(R.color.red));
                    panicButton.setText(getString(R.string.cancel_panic));
                    inPanic = true;
                }

                panic();
                return true;
            }
        });
    }

    /**
     * Returns a GoogleApiClient that can access the Wear API.
     * @param context
     * @return A GoogleApiClient that can make calls to the Wear API
     */
    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    /**
     * Connects to the GoogleApiClient and retrieves the connected device's Node ID. If there are
     * multiple connected devices, the first Node ID is returned.
     */
    private void retrieveDeviceNode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes(client).await();

                List<Node> nodes = result.getNodes();

                if (nodes.size() > 0) {
                    nodeId = nodes.get(0).getId();
                }
                client.disconnect();
            }
        }).start();
    }

    /**
     * Sends a message turn on or off the panic button.
     */
    private void panic() {
        if (nodeId != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                    String action;

                    if (inPanic) {
                        action = "OFF";

                    } else {
                        action = "ON";
                    }

                    Wearable.MessageApi.sendMessage(client, nodeId, action, null);
                    client.disconnect();
                }
            }).start();
        }
    }
}

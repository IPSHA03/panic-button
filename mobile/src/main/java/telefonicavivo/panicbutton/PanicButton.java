package telefonicavivo.panicbutton;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;


/*
 * Panic Button class
 */
public class PanicButton {
    boolean inPanic = false;
    Activity activity;
    ActionBar actionbar;
    Window window;
    Button button;

    public PanicButton(Activity activity, Button button) {
        inPanic = false;
        this.setActivity(activity);
        this.setActionbar();
        this.setWindow();
        this.setButton(button);
    }

    /*
     * Get/Set Activity
     */
    private void setActivity(Activity activity) {
        this.activity = activity;
    }
    private Activity getActivity() {
        return this.activity;
    }

    /*
     * Get/Set Button
     */
    private void setButton(Button button) {
        this.button = button;
    }
    private Button getButton() {
        return this.button;
    }

    /*
     * Get/Set Actionbar
     */
    private void setActionbar() {
        this.actionbar = this.getActivity().getActionBar();
    }
    private ActionBar getActionbar() {
        return this.actionbar;
    }

    /*
     * Get/Set Window
     */
    private void setWindow() {
        this.window = this.getActivity().getWindow();
    }
    private Window getWindow() {
        return this.window;
    }

    /*
     * Turn on the panic button
     */
    public void on() {
        this.inPanic = true;

        this.getWindow().setStatusBarColor(this.getActivity().getResources().getColor(R.color.dark_red));
        this.getWindow().setNavigationBarColor(this.getActivity().getResources().getColor(R.color.red));
        this.getActionbar().setBackgroundDrawable(new ColorDrawable(this.getActivity().getResources().getColor(R.color.red)));
        this.getButton().setBackgroundColor(this.getActivity().getResources().getColor(R.color.dark_gray));
        this.getButton().setText("Cancel Panic Button");
    }

    /*
     * Turn off the panic button
     */
    public void off() {
        this.inPanic = false;

        this.getWindow().setStatusBarColor(this.getActivity().getResources().getColor(R.color.dark_blue));
        this.getWindow().setNavigationBarColor(this.getActivity().getResources().getColor(R.color.blue));
        this.getActionbar().setBackgroundDrawable(new ColorDrawable(this.getActivity().getResources().getColor(R.color.blue)));
        this.getButton().setBackgroundColor(this.getActivity().getResources().getColor(R.color.red));
        this.getButton().setText("Activate Panic Button");
    }

    /*
     * Check if the panic button is on
     */
    public boolean inPanic() {
        return this.inPanic;
    }
}

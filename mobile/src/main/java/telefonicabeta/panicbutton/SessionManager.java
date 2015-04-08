package telefonicabeta.panicbutton;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SessionManager {
    SharedPreferences preferences;
    Editor editor;
    Context _context;
    private static final String IS_LOGIN = "IsLoggedIn";

    /**
     * Constructor
     * @param context
     */
    public SessionManager(Context context) {
        this._context = context;
        preferences = _context.getSharedPreferences("PanicButtonPref", 0);
        editor = preferences.edit();
    }

    /**
     * Create a login session and store the user email
     * @param email
     */
    public void login(String email) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString("email", email);
        editor.commit();
    }

    /**
     * Redirect to login page is user is not logged
     */
    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    /**
     * Get user stored session data
     */
    public HashMap<String, String> getLoggedUser(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put("email", preferences.getString("email", null));

        return user;
    }

    /**
     * Logout user and session details
     */
    public void logout() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }

    /**
     * Check if the user is logged in
     */
    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }
}


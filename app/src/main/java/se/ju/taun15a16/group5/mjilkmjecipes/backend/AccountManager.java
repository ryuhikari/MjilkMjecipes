package se.ju.taun15a16.group5.mjilkmjecipes.backend;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;

import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTManager;

/**
 * Created by kevin on 27.11.2016.
 */

public class AccountManager {

    // Singleton Variable
    private static AccountManager managerInstance = new AccountManager();

    private static final String PREFERENCES_FILE = "";

    private AccessTokenTracker tracker;

    public static final String PREF_TOKEN_TIMESTAMP = "TOKEN_TIMESTAMP";
    public static final String PREF_TOKEN_EXPIRATION_TIME = "TOKEN_EXPIRATION_TIME";
    public static final String PREF_USER_ID = "USER_ID";
    public static final String PREF_USERNAME = "USERNAME";
    public static final String PREF_PASSWORD = "PASSWORD";
    public static final String PREF_TOKEN = "TOKEN_LOGIN";
    public static final String PREF_IS_FACEBOOK_LOGIN = "LOGIN_FACEBOOK";

    // Private constructor
    private AccountManager(){
    }

    // Get AccountManager instance
    public static AccountManager getInstance() {
        return managerInstance;
    }


    public boolean isFacebookLogin(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREF_IS_FACEBOOK_LOGIN, false);
    }

    public String getLoginToken(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return preferences.getString(PREF_TOKEN,null);
    }

    public String getUserID(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return preferences.getString(PREF_USER_ID,null);
    }

    public String getUserName(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return preferences.getString(PREF_USERNAME,null);
    }

    public String getUserPassword(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return preferences.getString(PREF_PASSWORD,null);
    }

    public void setLoginToken(Context context, String token){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_TOKEN, token);
        editor.apply();
    }

    public void setIsFacebookLogin(Context context, boolean isFBLogin){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_IS_FACEBOOK_LOGIN, isFBLogin);
        editor.apply();
    }

    public void setUserID(Context context, String userID){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_USER_ID, userID);
        editor.apply();
    }

    public void setUserName(Context context, String username){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_USERNAME, username);
        editor.apply();
    }

    public void setUserPassword(Context context, String password){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_PASSWORD, password);
        editor.apply();

    }

    public void setLoginTokenTimestampAndExpirationDate(Context context, String startTimestamp, long expirationTime){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_TOKEN_TIMESTAMP, startTimestamp);
        editor.putLong(PREF_TOKEN_EXPIRATION_TIME, expirationTime);
        editor.apply();
    }

    public boolean login(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        RESTManager restManager = RESTManager.getInstance();
        if(isFacebookLogin(context)){
            AccessToken token = AccessToken.getCurrentAccessToken();
            if(token == null || token.getToken() == null || token.getToken().isEmpty() || token.isExpired()){

                tracker = new AccessTokenTracker(){

                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                        tracker.stopTracking();
                        JSONObject data = restManager.createLoginTokenFacebook(currentAccessToken.toString());
                    }
                };
                AccessToken.refreshCurrentAccessTokenAsync();
            }
            JSONObject result = restManager.createLoginTokenFacebook(token.getToken());
            if(result == null){
                return false;
            }
        }else{
            String username = preferences.getString(PREF_USERNAME,null);
            String pw = preferences.getString(PREF_PASSWORD,null);
            if(username == null || pw == null){
                return false;
            }
            JSONObject data = restManager.createLoginToken(username, pw);
            try {
                String token = data.getString("access_token");
                String timestamp = data.getString("timestamp");
                long expirationTime = data.getLong("expires_in");
                setLoginToken(context, token);
                setLoginTokenTimestampAndExpirationDate(context, timestamp, expirationTime);

                Log.d("Login","RAW JSON Login-Token: " + data.toString());
                String[] tokenPieces = token.split("\\.");
                Log.d("Login","Encoded Token Header: " + tokenPieces[0]);
                Log.d("Login","Encoded Token Payload: " + tokenPieces[1]);
                Log.d("Login","Encoded Token Signature: " + tokenPieces[2]);
                byte[] decodedTokenHeaderRAW = Base64.decode(tokenPieces[0], Base64.DEFAULT);
                byte[] decodedTokenClaimsRAW = Base64.decode(tokenPieces[1], Base64.DEFAULT);

                String decodedTokenHeader = new String(decodedTokenHeaderRAW, Charset.forName("UTF-8"));
                String decodedTokenClaims = new String(decodedTokenClaimsRAW, Charset.forName("UTF-8"));
                Log.d("Login","Decoded Token Header: " + decodedTokenHeader);
                Log.d("Login","Decoded Token Payload: " + decodedTokenClaims);
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean refreshLogin(Context context, String prefFile){
        SharedPreferences preferences = context.getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        return false;
    }
}

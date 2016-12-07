package se.ju.taun15a16.group5.mjilkmjecipes;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.util.Base64;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import se.ju.taun15a16.group5.mjilkmjecipes.backend.AccountManager;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP400Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTManager;

public class LogInActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(AccountManager.getInstance().isTokenValid(getApplicationContext())){
            launchMainMenuActivity(null);
            return;
        }

        setContentView(R.layout.activity_log_in);

        AccountManager accManager = AccountManager.getInstance();

        String usernameSaved = accManager.getUserName(getApplicationContext());
        String passwordSaved = accManager.getUserPassword(getApplicationContext());


        if(BuildConfig.DEBUG && (usernameSaved == null || passwordSaved == null)){
            ((EditText)findViewById(R.id.editText_login_username)).setText("AdminMjilkRecipes");
            ((EditText)findViewById(R.id.editText_login_password)).setText("Admin!1");

        }else{
            ((EditText)findViewById(R.id.editText_login_username)).setText(usernameSaved);
            ((EditText)findViewById(R.id.editText_login_password)).setText(passwordSaved);
        }





        RelativeLayout loginBarLayout = (RelativeLayout) findViewById(R.id.layoutLoginBar);

        Button btn_login =(Button) findViewById(R.id.button_login_login);
        btn_login.setOnClickListener(view -> {

            //TODO: Replace with actual login
            EditText usernameField = (EditText) findViewById(R.id.editText_login_username);
            EditText pwField = (EditText) findViewById(R.id.editText_login_password);
            String username = usernameField.getText().toString();
            String password = pwField.getText().toString();


            accManager.setUserName(getApplicationContext(), username);
            accManager.setUserPassword(getApplicationContext(), password);

            new AsyncTask<Void, Void, Boolean>(){


                @Override
                protected void onPreExecute() {
                    loginBarLayout.setVisibility(View.VISIBLE);
                }

                @Override
                protected Boolean doInBackground(Void... params) {
                    Boolean result = false;
                    try {
                       result = accManager.login(getApplicationContext());
                    } catch (HTTP400Exception e) {
                        Log.getStackTraceString(e);
                    }
                    return result;
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    loginBarLayout.setVisibility(View.GONE);
                    if(result != null && result){
                        launchMainMenuActivity(null);
                    }else{
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Error logging in!", Toast.LENGTH_LONG).show());
                    }
                }

                @Override
                protected void onCancelled() {
                    loginBarLayout.setVisibility(View.GONE);
                }

            }.execute();
        });

        Button btn_signup =(Button) findViewById(R.id.button_login_signup);
        btn_signup.setOnClickListener(view -> launchSignUpActivity(null));

        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d("Login-FB","Facebook-Token: " + loginResult.getAccessToken().getToken());


                new AsyncTask<String, Void, JSONObject>() {

                    @Override
                    protected void onPreExecute() {
                        loginBarLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected JSONObject doInBackground(String... params) {
                        RESTManager restManager = RESTManager.getInstance();
                        return restManager.createLoginTokenFacebook(loginResult.getAccessToken().getToken());
                    }

                    @Override
                    protected void onPostExecute(JSONObject jsonObject) {

                        //TODO: Make working!
                        try {
                            String token = jsonObject.getString("access_token");
                            Log.d("Login-FB", jsonObject.toString());
                            String[] tokenPieces = token.split("\\.");
                            Log.d("Login-FB","Encoded Token Header: " + tokenPieces[0]);
                            Log.d("Login-FB","Encoded Token Payload: " + tokenPieces[1]);
                            Log.d("Login-FB","Encoded Token Signature: " + tokenPieces[2]);
                            byte[] decodedTokenHeaderRAW = Base64.decode(tokenPieces[0], Base64.DEFAULT);
                            byte[] decodedTokenClaimsRAW = Base64.decode(tokenPieces[1], Base64.DEFAULT);

                            String decodedTokenHeader = new String(decodedTokenHeaderRAW, Charset.forName("UTF-8"));
                            String decodedTokenClaims = new String(decodedTokenClaimsRAW, Charset.forName("UTF-8"));
                            Log.d("Login-FB","Decoded Token Header: " + decodedTokenHeader);
                            Log.d("Login-FB","Decoded Token Payload: " + decodedTokenClaims);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Login", "Successfully Login in via Facebook: " + loginResult.getAccessToken().getToken());
                        loginBarLayout.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), R.string.facebook_login_successful_notify, Toast.LENGTH_SHORT).show();
                        launchMainMenuActivity(null);
                    }

                    @Override
                    protected void onCancelled(JSONObject jsonObject) {
                        loginBarLayout.setVisibility(View.GONE);
                    }

                }.execute(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Log.d("Login","Cancelled Facebooklogin");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("Login", Log.getStackTraceString(exception));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void launchMainMenuActivity(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void launchSignUpActivity(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}

package se.ju.taun15a16.group5.mjilkmjecipes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LogInActivity extends AppCompatActivity {

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button btn_login =(Button) findViewById(R.id.button_login_login);
        btn_login.setOnClickListener(view -> {
            //TODO: Replace with actual login
            //Meanwhile it logs in directly
            launchMainMenuActivity(null);
        });

        Button btn_signup =(Button) findViewById(R.id.button_login_signup);
        btn_signup.setOnClickListener(view -> launchSignUpActivity(null));

        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Login", "Successfully Login in via Facebook: " + loginResult.getAccessToken().getToken());
                Toast.makeText(getApplicationContext(), R.string.facebook_login_successful_notify, Toast.LENGTH_SHORT).show();
                launchMainMenuActivity(null);
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
    }

    public void launchSignUpActivity(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}

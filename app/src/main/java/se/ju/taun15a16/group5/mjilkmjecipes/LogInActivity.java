package se.ju.taun15a16.group5.mjilkmjecipes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        Button btn_login =(Button) findViewById(R.id.button_login_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Complete
                //Meanwhile it logs in directly
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Button btn_signup =(Button) findViewById(R.id.button_login_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchSignUpActivity(null);
            }
        });
    }

    public void launchSignUpActivity(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}

package se.ju.taun15a16.group5.mjilkmjecipes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.concurrent.Semaphore;

import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP400Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTErrorCodes;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTManager;

public class SignUpActivity extends AppCompatActivity {

	private String username = null;
	private String password = null;
	private double longitude = 0.0;
	private double latitude = 0.0;
	private boolean useLocation = false;

	private Semaphore signalLocationUpdated = new Semaphore(1);

	private static final int REQUEST_CODE_ASK_PERMISSIONS = 666;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		// Up navigation arrow on the action bar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);


		EditText editTextUsername = (EditText) findViewById(R.id.editText_signup_username);
		EditText editTextPassword = (EditText) findViewById(R.id.editText_signup_password);
		EditText editTextConfirmPassword = (EditText) findViewById(R.id.editText_signup_confirm_password);

		CheckBox chckbxUseLocation = (CheckBox) findViewById(R.id.checkBox_signup_location);

		chckbxUseLocation.setOnClickListener(view -> {

			if (chckbxUseLocation.isChecked() && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
			}else{
				LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Criteria criteria = new Criteria();
				criteria.setAccuracy(Criteria.ACCURACY_FINE);
				String provider = locManager.getBestProvider(criteria, true);
				try {
					Location currentLocation = locManager.getLastKnownLocation(provider);
					if (currentLocation != null) {
						longitude = currentLocation.getLongitude();
						latitude = currentLocation.getLatitude();
					}else{
						locManager.requestLocationUpdates(provider, 60000L, 10.0f, new LocationListener() {
							@Override
							public void onLocationChanged(Location location) {
								longitude = location.getLongitude();
								latitude = location.getLatitude();
								signalLocationUpdated.release();
							}

							@Override
							public void onStatusChanged(String provider, int status, Bundle extras) {

							}

							@Override
							public void onProviderEnabled(String provider) {

							}

							@Override
							public void onProviderDisabled(String provider) {

							}
						});
						signalLocationUpdated.tryAcquire();
					}
				} catch (SecurityException e) {
					Log.getStackTraceString(e);
				}
			}
		});

		RelativeLayout signUpBar = (RelativeLayout) findViewById(R.id.layoutSignupBar);

		Button btnSignup = (Button) findViewById(R.id.button_signup_signup);
		btnSignup.setOnClickListener(view -> {

			username = editTextUsername.getText().toString();
			password = editTextPassword.getText().toString();
			new AsyncTask<Void, Void, RESTErrorCodes[]>() {

				@Override
				protected void onPreExecute() {
					signUpBar.setVisibility(View.VISIBLE);
				}

				@Override
				protected RESTErrorCodes[] doInBackground(Void... params) {

					try {
						signalLocationUpdated.acquire();
					} catch (InterruptedException e) {
						Log.getStackTraceString(e);
					}

					RESTErrorCodes[] result = {};
					try {
						RESTManager.getInstance().createAccount(username, password, longitude, latitude);
					} catch (HTTP400Exception e) {
						Log.getStackTraceString(e);
						result = e.getErrorCodes();
					}
					signalLocationUpdated.release();
					return result;
				}

				@Override
				protected void onPostExecute(RESTErrorCodes[] result) {

					if (result.length == 0) {
						Toast.makeText(getApplicationContext(), "Account successfully created!", Toast.LENGTH_LONG).show();
						Intent returnData = new Intent();
						returnData.putExtra("username", username);
						returnData.putExtra("password", password);
						setResult(Activity.RESULT_OK, returnData);
						signUpBar.setVisibility(View.GONE);
						finish();
					} else {
						for(int i = 0; i < result.length; ++i){
							switch (result[i]){
								case INVALID_USERNAME:
									//TODO: Use textedit.setError("") for marking a textedit as incorrect!
									break;
								//TODO: Add all possible error codes here except for longitude and latitude
							}
						}
						signUpBar.setVisibility(View.GONE);
						Toast.makeText(getApplicationContext(), "Error creating account!", Toast.LENGTH_LONG).show();
					}
				}

				@Override
				protected void onCancelled() {
					signUpBar.setVisibility(View.GONE);
				}

			}.execute();
		});
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE_ASK_PERMISSIONS: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
					Criteria criteria = new Criteria();
					criteria.setAccuracy(Criteria.ACCURACY_FINE);
					String provider = locManager.getBestProvider(criteria, true);
					try {
						Location currentLocation = locManager.getLastKnownLocation(provider);
						if (currentLocation != null) {
							longitude = currentLocation.getLongitude();
							latitude = currentLocation.getLatitude();
						}else{
							locManager.requestLocationUpdates(provider, 60000L, 10.0f, new LocationListener() {
								@Override
								public void onLocationChanged(Location location) {
									longitude = location.getLongitude();
									latitude = location.getLatitude();
									signalLocationUpdated.release();
								}

								@Override
								public void onStatusChanged(String provider, int status, Bundle extras) {

								}

								@Override
								public void onProviderEnabled(String provider) {

								}

								@Override
								public void onProviderDisabled(String provider) {

								}
							});
							signalLocationUpdated.tryAcquire();
						}
					} catch (SecurityException e) {
						Log.getStackTraceString(e);
					}
				}
				break;
			}

		}
	}
}

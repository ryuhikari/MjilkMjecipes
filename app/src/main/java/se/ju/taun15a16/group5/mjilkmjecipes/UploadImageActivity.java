package se.ju.taun15a16.group5.mjilkmjecipes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP401Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP404Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTErrorCodes;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTManager;

public class UploadImageActivity extends AppCompatActivity {

    public final static String EXTRA_TYPE = "destinyType";
    public final static String EXTRA_ID = "destinyId";

    public final static String TYPE_RECIPE = "recipe";
    public final static String TYPE_COMMENT = "comment";

    private Button optionsButton;
    private Button sendButton;
    private ImageView imageContainer;
    private Drawable imageDrawable;

    private String destinyType = "";
    private String destinyId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        optionsButton = (Button) findViewById(R.id.new_recipe_upload_image_button);
        sendButton = (Button) findViewById(R.id.upload_image_send_button);
        imageContainer = (ImageView) findViewById(R.id.upload_image_imageView);

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDrawable = imageContainer.getDrawable();
                sendImage();
            }
        });

        Intent intent = getIntent();

        if ( intent != null) {
            destinyType = intent.getStringExtra(EXTRA_TYPE);
            destinyId = intent.getStringExtra(EXTRA_ID);
        }

    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageContainer.setImageBitmap(imageBitmap);
            sendButton.setEnabled(true);
        }
    }

    private void sendImage(){
        new AsyncTask<Void, Void, RESTErrorCodes[]>() {

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected RESTErrorCodes[] doInBackground(Void... params) {

                RESTErrorCodes[] result = {};
                try {
                    switch (destinyType) {
                        case TYPE_RECIPE:
                            RESTManager.getInstance().addImageToRecipe(getApplicationContext() ,destinyId, imageDrawable);
                            break;
                        case TYPE_COMMENT:
                            RESTManager.getInstance().addImageToComment(getApplicationContext(), destinyId, imageDrawable);
                            break;
                        default:
                            return null;
                    }
                } catch (HTTP404Exception e) {
                    Log.e("REST", Log.getStackTraceString(e));
                    Context context = getApplicationContext();
                    CharSequence text = e.toString();
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } catch (HTTP401Exception e) {
                    Log.e("REST", Log.getStackTraceString(e));
                    Context context = getApplicationContext();
                    CharSequence text = e.toString();
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

                return result;
            }

            @Override
            protected void onPostExecute(RESTErrorCodes[] result) {

                if (result.length == 0) {
                    Toast.makeText(getApplicationContext(), R.string.upload_image_create_successful_message, Toast.LENGTH_LONG).show();
                    finish();
                } else {

                    // TODO: Finish coding all the error messages
                    for(int i = 0; i < result.length; ++i){
                        switch (result[i]){
                            case INVALID_USERNAME:
                                //TODO: Use textedit.setError("") for marking a textedit as incorrect!
                                break;
                            //TODO: Add all possible error codes here except for longitude and latitude
                        }
                    }
                    Toast.makeText(getApplicationContext(), R.string.upload_image_create_error_message, Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }
}

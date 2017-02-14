package se.ju.taun15a16.group5.mjilkmjecipes;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import se.ju.taun15a16.group5.mjilkmjecipes.backend.AccountManager;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.Direction;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.Recipe;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP400Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP401Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP404Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTErrorCodes;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTManager;
import se.ju.taun15a16.group5.mjilkmjecipes.recipelist.ShowRecipeListActivity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class NewRecipeActivity extends AppCompatActivity {

    // Upload images

    private Button uploadImageButton;
    private TextView warningMessageTextView;

    // To edit a recipe

    public Boolean editingRecipe = false;
    public String recipeType = "";
    public int recipePage = 0;

    public final static String EXTRA_ID = "recipeId";
    public final static String EXTRA_NAME = "recipeName";
    public final static String EXTRA_DESCRIPTION = "recipeDescription";
    public final static String EXTRA_DIRECTIONS = "recipeDirections";

    // Elements related to the recipe

    private long recipeId;
    private EditText recipeName;
    private EditText recipeDescription;

    // Parent view for all rows and the add button.
    private LinearLayout mContainerView;
    // The "Add new" button
    private Button mAddButton;

    // There always should be only one empty row, other empty rows will
    // be removed.
    private View mExclusiveEmptyView;

    private Button sendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        recipeName = (EditText) findViewById(R.id.new_recipe_editText_name);
        recipeDescription = (EditText) findViewById(R.id.new_recipe_editText_description);

        mContainerView = (LinearLayout) findViewById(R.id.new_recipe_directions_parent);
        mAddButton = (Button) findViewById(R.id.btnAddNewItem);

        sendButton = (Button) findViewById(R.id.button_new_recipe_send);

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            editingRecipe =  true;

            recipeType = intent.getStringExtra(ShowRecipeListActivity.EXTRA_TYPE);
            recipePage = intent.getIntExtra(ShowRecipeListActivity.EXTRA_PAGE, 0);

            getSupportActionBar().setTitle(R.string.text_new_recipe_update_recipe);
            sendButton.setText(R.string.btn_new_recipe_update);

            recipeId = intent.getLongExtra(EXTRA_ID, 0);
            recipeName.setText(intent.getStringExtra(EXTRA_NAME));
            recipeDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));

            ArrayList<String> directions = new ArrayList<String>();
            directions = intent.getStringArrayListExtra(EXTRA_DIRECTIONS);

            for (int i=0; i < directions.size(); i++) {
                inflateEditRow(directions.get(i));
            }
        }

        uploadImageButton = (Button)findViewById(R.id.new_recipe_upload_image_button);
        warningMessageTextView = (TextView)findViewById(R.id.new_recipe_warning_message_textView);
        if (editingRecipe) {
            warningMessageTextView.setVisibility(View.GONE);
            uploadImageButton.setEnabled(true);
            uploadImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), R.string.upload_image_create_message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), UploadImageActivity.class);
                    intent.putExtra(UploadImageActivity.EXTRA_TYPE, UploadImageActivity.TYPE_RECIPE);
                    intent.putExtra(UploadImageActivity.EXTRA_ID, Long.toString(recipeId));
                    startActivity(intent);
                }
            });
        }

        // Add some examples
        // inflateEditRow("Direction description");
    }

    // Methods related to Directions
    // ------------------------------------------------------------------------

    // onClick handler for the "Add new" button;
    public void onAddNewClicked(View v) {
        // Inflate a new row and hide the button self.
        inflateEditRow(null);
        //v.setVisibility(View.GONE);
    }

    // onClick handler for the "X" button of each row
    public void onDeleteClicked(View v) {
        // remove the row by calling the getParent on button
        mContainerView.removeView((View) v.getParent());
    }

    // Helper for inflating a row
    private void inflateEditRow(String name) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.new_direction_item, null);
        final ImageButton deleteButton = (ImageButton) rowView
                .findViewById(R.id.buttonDelete);
        final EditText editText = (EditText) rowView
                .findViewById(R.id.editText);

        if (name != null && !name.isEmpty()) {
            editText.setText(name);
        } else {
            mExclusiveEmptyView = rowView;
            //deleteButton.setVisibility(View.INVISIBLE);
        }

        // A TextWatcher to control the visibility of the "Add new" button and
        // handle the exclusive empty view.
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().isEmpty()) {
                    //mAddButton.setVisibility(View.GONE);
                    //deleteButton.setVisibility(View.INVISIBLE);

                    if (mExclusiveEmptyView != null
                            && mExclusiveEmptyView != rowView) {
                        mContainerView.removeView(mExclusiveEmptyView);
                    }
                    mExclusiveEmptyView = rowView;
                } else {

                    if (mExclusiveEmptyView == rowView) {
                        mExclusiveEmptyView = null;
                    }

                    //mAddButton.setVisibility(View.VISIBLE);
                    //deleteButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        // Inflate at the end of all rows but before the "Add new" button
        mContainerView.addView(rowView, mContainerView.getChildCount() - 1);
    }

    // Server
    // -------------------------------------------

    public void sendToServer(View v) {

        Recipe newRecipe = new Recipe();
        newRecipe.setName(recipeName.getText().toString());
        newRecipe.setCreatorId(AccountManager.getInstance().getUserID(getApplicationContext()));
        newRecipe.setDescription(recipeDescription.getText().toString());

        ArrayList<Direction> directions = new ArrayList<Direction>();

        int directionOrder = 1;

        for ( int i = 0; i < mContainerView.getChildCount() - 1; i++) {
            LinearLayout row = (LinearLayout) mContainerView.getChildAt(i);
            EditText directionHolder = (EditText) row.getChildAt(0);

            if ( directionHolder.getText().length() > 0 ) {
                directions.add(new Direction(newRecipe.getId(),directionOrder,directionHolder.getText().toString()));
                directionOrder++;
            }
        }
        newRecipe.setDirections(directions);

        if (editingRecipe) {
            new AsyncTask<Void, Void, RESTErrorCodes[]>() {

                @Override
                protected void onPreExecute() {

                }

                @Override
                protected RESTErrorCodes[] doInBackground(Void... params) {

                    RESTErrorCodes[] result = {};
                    try {
                        newRecipe.setId(recipeId);
                        String creatorId = new String();

                        creatorId = AccountManager.getInstance().getUserID(getApplicationContext());

                        newRecipe.setCreatorId(creatorId);

                        Log.e("Recipe", newRecipe.toString());
                        RESTManager.getInstance().updateRecipe(getApplicationContext(), recipeId, newRecipe);
                    } catch (HTTP401Exception e) {
                        Log.e("REST", Log.getStackTraceString(e));
                    } catch (HTTP404Exception e) {
                        Log.e("DEBUG-REST", Log.getStackTraceString(e));
                        Log.e("Important", e.getMessage());
                    } catch (HTTP400Exception e) {
                        Log.e("REST", Log.getStackTraceString(e));
                        result = e.getErrorCodes();
                    }

                    return result;
                }

                @Override
                protected void onPostExecute(RESTErrorCodes[] result) {

                    if (result.length == 0) {

                        if (recipeType != null) {
                            Toast.makeText(getApplicationContext(), R.string.recipe_edit_successful_message, Toast.LENGTH_SHORT).show();
                            //finish();
                            Intent intent = new Intent(getApplicationContext(), ShowRecipeListActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                            intent.putExtra(ShowRecipeListActivity.EXTRA_TYPE, recipeType);
                            intent.putExtra(ShowRecipeListActivity.EXTRA_PAGE, recipePage);
                            intent.putExtra(ShowRecipeListActivity.EXTRA_SHOW, newRecipe.getId());
                            startActivity(intent);
                            finish();
                        } else {
                            finish();
                        }

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
                        Toast.makeText(getApplicationContext(), R.string.recipe_edit_error_message, Toast.LENGTH_LONG).show();
                    }

                }
            }.execute();
        } else {
            new AsyncTask<Void, Void, RESTErrorCodes[]>() {

                @Override
                protected void onPreExecute() {

                }

                @Override
                protected RESTErrorCodes[] doInBackground(Void... params) {

                    RESTErrorCodes[] result = {};
                    try {
                        Boolean create = RESTManager.getInstance().createRecipe(newRecipe, getApplicationContext());

                    } catch (HTTP401Exception e) {
                        Log.e("REST", Log.getStackTraceString(e));
                    } catch (HTTP400Exception e) {
                        Log.e("REST", Log.getStackTraceString(e));
                        result = e.getErrorCodes();
                    }

                    return result;
                }

                @Override
                protected void onPostExecute(RESTErrorCodes[] result) {

                    if (result.length == 0) {
                        Toast.makeText(getApplicationContext(), R.string.recipe_create_successful_message, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), R.string.recipe_create_error_message, Toast.LENGTH_LONG).show();
                    }

                }
            }.execute();
        }
    }
}

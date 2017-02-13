package se.ju.taun15a16.group5.mjilkmjecipes;

import android.app.Activity;
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
import android.support.v7.view.menu.ShowableListMenu;
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

    // To edit a recipe

    public Boolean editingRecipe = false;

    public final static String EXTRA_ID = "recipeId";
    public final static String EXTRA_NAME = "recipeName";
    public final static String EXTRA_DESCRIPTION = "recipeDescription";
    public final static String EXTRA_DIRECTIONS = "recipeDirections";

    // Elements related to the image

    private static String APP_DIRECTORY = "Mjilk/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "RecipePictures";

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    private ImageView mSetImage;
    private Button mOptionButton;
    private RelativeLayout mRlView;

    private String mPath;


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

        mSetImage = (ImageView) findViewById(R.id.set_picture);
        mOptionButton = (Button) findViewById(R.id.show_options_button);
        mRlView = (RelativeLayout) findViewById(R.id.rl_view);

        recipeName = (EditText) findViewById(R.id.new_recipe_editText_name);
        recipeDescription = (EditText) findViewById(R.id.new_recipe_editText_description);

        mContainerView = (LinearLayout) findViewById(R.id.new_recipe_directions_parent);
        mAddButton = (Button) findViewById(R.id.btnAddNewItem);

        sendButton = (Button) findViewById(R.id.button_new_recipe_send);

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            editingRecipe =  true;

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

        // Add some examples
        // inflateEditRow("Fernando");
        // inflateEditRow("Paco");

        if(mayRequestStoragePermission())
            mOptionButton.setEnabled(true);
        else
            mOptionButton.setEnabled(false);

        mOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });

    }

    private void fillTheGaps(long recipeId) {

    }

    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(mRlView, getString(R.string.text_new_recipe_permissions_message),
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            }).show();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    private void showOptions() {
        final CharSequence[] option = {getString(R.string.text_new_recipe_option_take_picture), getString(R.string.text_new_recipe_option_choose_from_galery), getString(R.string.text_new_recipe_option_cancel)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(NewRecipeActivity.this);
        builder.setTitle(getString(R.string.text_new_recipe_option_choose));
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(option[which] == getString(R.string.text_new_recipe_option_take_picture)){
                    openCamera();
                }else if(option[which] == getString(R.string.text_new_recipe_option_choose_from_galery)){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, getString(R.string.text_new_recipe_choose_image_app)), SELECT_PICTURE);
                }else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void openCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                    + File.separator + imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });


                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    mSetImage.setImageBitmap(bitmap);
                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();
                    mSetImage.setImageURI(path);
                    break;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), getString(R.string.text_new_recipe_permissions_granted), Toast.LENGTH_SHORT).show();
                mOptionButton.setEnabled(true);
            } else {
                showExplanation();
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewRecipeActivity.this);
        builder.setTitle(getString(R.string.text_new_recipe_permissions_denied));
        builder.setMessage(getString(R.string.text_new_recipe_permissions_message));
        builder.setPositiveButton(getString(R.string.text_new_recipe_agree), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(getString(R.string.text_new_recipe_option_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
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
                        Toast.makeText(getApplicationContext(), R.string.recipe_edit_successful_message, Toast.LENGTH_SHORT).show();
                        //finish();
                        Intent intent = new Intent(getApplicationContext(), ShowRecipeListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        intent.putExtra(ShowRecipeListActivity.EXTRA_TYPE, ShowRecipeListActivity.EXTRA_MY);
                        intent.putExtra(ShowRecipeListActivity.EXTRA_SHOW, newRecipe.getId());
                        startActivity(intent);
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

package se.ju.taun15a16.group5.mjilkmjecipes;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

import se.ju.taun15a16.group5.mjilkmjecipes.backend.AccountManager;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP400Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP401Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP404Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTErrorCodes;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTManager;
import se.ju.taun15a16.group5.mjilkmjecipes.recipelist.ShowRecipeListActivity;

/**
 * Created by Fernando on 2017-02-13.
 */

public class NewCommentActivity extends AppCompatActivity {

    public final static String EXTRA_ID = "commentId";
    public final static String EXTRA_RATING = "commentRating";
    public final static String EXTRA_COMMENT = "commentContent";
    public final static String EXTRA_IMAGE = "commentImage";
    public final static String EXTRA_EDIT = "editComment";
    public final static String EXTRA_RECIPE_ID = "recipeId";

    Boolean editingComment = false;

    private String recipeId;
    private String commentId;
    private int commentRating = 0;
    private String commentContent = "";
    private String commentImageURL = "";
    private String commentImagePath = null;

    private ImageView commentImageImageView;
    private RatingBar commentRatingRatingBar;
    private TextView commentContentTextView;
    private Button sendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_comment);

        commentImageImageView = (ImageView) findViewById(R.id.set_picture);
        commentRatingRatingBar = (RatingBar) findViewById(R.id.new_comment_ratingBar_comment_rating);
        commentContentTextView = (TextView) findViewById(R.id.new_comment_editText_comment);
        sendButton = (Button) findViewById(R.id.button_new_comment_send);

        if (savedInstanceState != null) {
            commentId = savedInstanceState.getString("commentId");
            commentRating = savedInstanceState.getInt("commentRating");
            commentContent = savedInstanceState.getString("commentContent");
            commentImageURL = savedInstanceState.getString("commentImageURL");
            commentImagePath = savedInstanceState.getString("commentImagePath");
            recipeId = savedInstanceState.getString("recipeId");
        }

        Intent intent = getIntent();
        editingComment =  intent.getBooleanExtra(EXTRA_EDIT, false);

        if (editingComment) {

            getSupportActionBar().setTitle(R.string.activity_name_edit_comment);
            sendButton.setText(R.string.text_new_comment_update_comment);

            commentId = intent.getStringExtra(EXTRA_ID);
            commentRating = intent.getIntExtra(EXTRA_RATING, 0);
            commentContent = intent.getStringExtra(EXTRA_COMMENT);
            //commentImageURL = intent.getStringExtra(EXTRA_IMAGE);
            recipeId = intent.getStringExtra(EXTRA_RECIPE_ID);

            //new DownLoadImageTask(commentImageImageView).execute(commentImageURL);
            commentRatingRatingBar.setRating(commentRating);
            commentContentTextView.setText(commentContent);
        } else {
            recipeId = intent.getStringExtra(EXTRA_RECIPE_ID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("commentId", commentId);
        outState.putInt("commentRating", commentRating);
        outState.putString("commentContent", commentContent);
        outState.putString("commentImageURL", commentImageURL);
        outState.putString("commentImagePath", commentImagePath);
        outState.putString("recipeId", recipeId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        commentId = savedInstanceState.getString("commentId");
        commentRating = savedInstanceState.getInt("commentRating");
        commentContent = savedInstanceState.getString("commentContent");
        commentImageURL = savedInstanceState.getString("commentImageURL");
        commentImagePath = savedInstanceState.getString("commentImagePath");
        recipeId = savedInstanceState.getString("recipeId");
    }

    // Server
    // -------------------------------------------

    public void sendToServer(View v) {
        String commentText = commentContentTextView.getText().toString();
        int grade = Math.round(commentRatingRatingBar.getRating());
        String userId = AccountManager.getInstance().getUserID(getApplicationContext());

        if (editingComment) {
            new AsyncTask<Void, Void, RESTErrorCodes[]>() {

                @Override
                protected void onPreExecute() {

                }

                @Override
                protected RESTErrorCodes[] doInBackground(Void... params) {

                    RESTErrorCodes[] result = {};
                    try {
                       Boolean updateComment = RESTManager.getInstance().updateComment(getApplicationContext(), commentId, commentText, grade);
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
                        Toast.makeText(getApplicationContext(), R.string.comment_edit_error_message, Toast.LENGTH_LONG).show();
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
                        Boolean createComment = RESTManager.getInstance().addCommentToRecipe(getApplicationContext(), recipeId, commentText, grade, userId);

                    } catch (HTTP401Exception e) {
                        Log.e("REST", Log.getStackTraceString(e));
                    } catch (HTTP400Exception e) {
                        Log.e("REST", Log.getStackTraceString(e));
                        result = e.getErrorCodes();
                    } catch (HTTP404Exception e) {
                        e.printStackTrace();
                    }

                    return result;
                }

                @Override
                protected void onPostExecute(RESTErrorCodes[] result) {

                    if (result.length == 0) {
                        Toast.makeText(getApplicationContext(), R.string.comment_create_successful_message, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(), R.string.comment_create_error_message, Toast.LENGTH_LONG).show();
                    }

                }
            }.execute();
        }
    }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap>{
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Log.v("Comment Image URL", urlOfImage);
            if (urlOfImage == null) {
                return null;
            }
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            if (result == null) {
                return;
            }
            imageView.setImageBitmap(result);
        }
    }
}

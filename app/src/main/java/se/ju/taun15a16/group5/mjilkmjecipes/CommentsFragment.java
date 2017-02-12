package se.ju.taun15a16.group5.mjilkmjecipes;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import se.ju.taun15a16.group5.mjilkmjecipes.backend.Direction;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.HTTP404Exception;
import se.ju.taun15a16.group5.mjilkmjecipes.backend.rest.RESTManager;
import se.ju.taun15a16.group5.mjilkmjecipes.recipelist.DirectionAdapter;

public class CommentsFragment extends Fragment {

    private ViewGroup commentsLayout;
    private LayoutInflater inflater;
    private ViewGroup container;

    public CommentsFragment() {

    }

    public static CommentsFragment newInstance(final long id) {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
        args.putLong("recipeID", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;

        View commentsView = inflater.inflate(R.layout.fragment_comments, container, false);
        commentsLayout = (ViewGroup) commentsView.findViewById(R.id.commentsLinearLayout);
        loadCommentData();
        return commentsView;
    }


    protected void loadCommentData(){
        new AsyncTask<Void, Void, JSONArray>(){

            @Override
            protected JSONArray doInBackground(Void... voids) {
                Bundle args = getArguments();;
                long recipeId = args.getLong("recipeID");
                JSONArray rawCommentData = null;
                try {
                    RESTManager restManager = RESTManager.getInstance();
                    String id = Long.toString(recipeId);
                    rawCommentData = restManager.getAllCommentsFromRecipe(id);
                    Log.d("REST", rawCommentData.toString());
                } catch (HTTP404Exception e) {
                    Log.e("REST", Log.getStackTraceString(e));
                }
                return rawCommentData;
            }

            @Override
            protected void onPostExecute(JSONArray rawCommentData) {
                if(rawCommentData == null){
                    return;
                }

                try {
                    for(int i = 0; i < rawCommentData.length(); ++i){
                        JSONObject comment = rawCommentData.getJSONObject(i);
                        String username = comment.getJSONObject("commenter").getString("userName");
                        float rating = (float)comment.getDouble("grade");
                        String commentText = comment.getString("text");
                        long creationTimestamp = comment.getLong("created");

                        createCommentView(username, rating, commentText, null, creationTimestamp);
                    }

                } catch (JSONException e) {
                    Log.e("REST", Log.getStackTraceString(e));
                }
            }
        }.execute();
    }

    /**
     * Adds a comment to the parent layout.
     * @param username Username to display.
     * @param rating Rating to display.
     * @param comment (Optional)Comment-text to display.
     * @param image (Optional)Image to display.
     * @return The created comment view.
     */
    public View createCommentView(String username, float rating, String comment, Bitmap image, long createDate){
        View commentView = inflater.inflate(R.layout.recipes_comment, container, false);
        TextView userLbl = (TextView) commentView.findViewById(R.id.commentUsernameLbl);
        userLbl.setText(username);
        RatingBar commentRating = (RatingBar) commentView.findViewById(R.id.commentRatingBar);
        commentRating.setRating(rating);
        TextView commentLbl = (TextView) commentView.findViewById(R.id.commentLbl);
        if(comment == null || comment.trim().isEmpty()){
            comment = "";
        }
        commentLbl.setText(comment);
        ImageView commentImage = (ImageView) commentView.findViewById(R.id.commentImage);
        // Set only if theres a custom image, otherwise display dummy
        if(image != null){
            commentImage.setImageBitmap(image);
        }
        // Add to the views penultimate index
        commentsLayout.addView(commentView, commentsLayout.getChildCount());
        return commentView;
    }
}

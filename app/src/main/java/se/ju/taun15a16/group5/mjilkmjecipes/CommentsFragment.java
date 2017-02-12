package se.ju.taun15a16.group5.mjilkmjecipes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

                        int commentId = comment.getInt("id");
                        String username = comment.getJSONObject("commenter").getString("userName");
                        float rating = (float)comment.getDouble("grade");
                        String commentText = comment.getString("text");
                        int creationTimestamp = comment.getInt("created");
                        String commentImageURL = comment.getString("image");

                        createCommentView(commentId, username, creationTimestamp, rating, commentText, commentImageURL, creationTimestamp);
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
     * @param imageURL (Optional)Image URL to display.
     * @return The created comment view.
     */
    public View createCommentView(int commentId, String username, int commentCreated, float rating, String comment, String imageURL, long createDate){
        View commentView = inflater.inflate(R.layout.recipes_comment, container, false);

        TextView commentIdTextView = (TextView) commentView.findViewById(R.id.textView_comment_id);
        commentIdTextView.setText(Integer.toString(commentId));

        TextView userLbl = (TextView) commentView.findViewById(R.id.commentUsernameLbl);
        userLbl.setText(username);

        Date tempDate = new Date(commentCreated * 1000L);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(tempDate);

        TextView commentCreatedTextView = (TextView) commentView.findViewById(R.id.textView_comment_created);
        commentCreatedTextView.setText(date);

        RatingBar commentRating = (RatingBar) commentView.findViewById(R.id.commentRatingBar);
        commentRating.setRating(rating);

        TextView commentLbl = (TextView) commentView.findViewById(R.id.commentLbl);
        if(comment == null || comment.trim().isEmpty()){
            comment = "";
        }
        commentLbl.setText(comment);
        ImageView commentImage = (ImageView) commentView.findViewById(R.id.commentImage);
        // Set only if theres a custom image, otherwise display dummy
        if(imageURL != null){
            new DownLoadImageTask(commentImage).execute(imageURL);
        }
        // Add to the views penultimate index
        commentsLayout.addView(commentView, commentsLayout.getChildCount());

        // Set onClickListener
        ImageButton editButton = (ImageButton) commentView.findViewById(R.id.imageButton_comment_edit);
        ImageButton deleteButton = (ImageButton) commentView.findViewById(R.id.imageButton_comment_delete);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentAction(v);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentAction(v);
            }
        });

        return commentView;
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

    private void commentAction(View v) {
        int buttonClicked = v.getId();

        View commentItem = (View) findParentRecursively(v, R.id.commentItem);
        TextView commentAuthorTextView = (TextView) commentItem.findViewById(R.id.commentUsernameLbl);
        String commentAuthor = commentAuthorTextView.getText().toString();

        TextView commentIdTextView = (TextView) commentItem.findViewById(R.id.textView_comment_id);
        String commentId = commentIdTextView.getText().toString();

        String message = new String();
        switch (buttonClicked) {
            case R.id.imageButton_comment_edit:
                message = commentId+" edit";
                break;
            case R.id.imageButton_comment_delete:
                message = commentId+" delete";
                break;
            default:
                break;
        }

        Toast.makeText(getContext(), message+" "+commentAuthor, Toast.LENGTH_SHORT).show();
    }

    public ViewParent findParentRecursively(View view, int targetId) {
        if (view.getId() == targetId) {
            return (ViewParent)view;
        }
        View parent = (View) view.getParent();
        if (parent == null) {
            return null;
        }
        return findParentRecursively(parent, targetId);
    }
}

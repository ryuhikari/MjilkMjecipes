package se.ju.taun15a16.group5.mjilkmjecipes;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class CommentsFragment extends Fragment {

    private ViewGroup commentsLayout;
    private LayoutInflater inflater;
    private ViewGroup container;

    public CommentsFragment() {

    }

    public static CommentsFragment newInstance() {
        CommentsFragment fragment = new CommentsFragment();
        Bundle args = new Bundle();
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
        Button loadMoreCommentsBtn = (Button) commentsView.findViewById(R.id.commentsLoadMoreBtn);
        loadMoreCommentsBtn.setOnClickListener(view -> {
            //TODO: Fire asyncTask to load more comments from the server! Then delete the following line
            createCommentView("TestUser", 4.5f, "Lorem Ipsum", null);
        });
        return commentsView;
    }

    /**
     * Adds a comment to the parent layout.
     * @param username Username to display.
     * @param rating Rating to display.
     * @param comment (Optional)Comment-text to display.
     * @param image (Optional)Image to display.
     * @return The created comment view.
     */
    public View createCommentView(String username, float rating, String comment, Bitmap image){
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
        commentsLayout.addView(commentView, commentsLayout.getChildCount() - 1);
        return commentView;
    }
}

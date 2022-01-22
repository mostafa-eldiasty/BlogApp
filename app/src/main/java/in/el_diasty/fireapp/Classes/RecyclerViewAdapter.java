package in.el_diasty.fireapp.Classes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.el_diasty.fireapp.R;

/**
 * Created by Mostafa El-Diasty on 1/16/2019.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList<Post> posts;
    private Context context;
    private String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private Map<String, String> likesMap;

    public RecyclerViewAdapter(ArrayList<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_post, parent, false);
        RecyclerViewHolder rvh = new RecyclerViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        FirebaseDatabase.getInstance().getReference().child("likes").child(posts.get(position).getPostBlogDate()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        likesMap = (Map<String, String>) dataSnapshot.getValue();

                        try {
                            if (likesMap.containsKey(currentUID)) {
                                holder.likeImage.setImageResource(R.drawable.ic_action_like);
                                holder.likeImage.setTag("like");
                            }
                        } catch (Exception e) {
                        }

                        holder.likesNumbers.setText(String.valueOf(dataSnapshot.getChildrenCount()) + " likes");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

//        Handler uiHandler = new Handler(Looper.getMainLooper());
//        uiHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                //TODO your background code
//

//            }
//        });

        Picasso.with(context).load(posts.get(position).getUserImage()).fit().noFade().placeholder(R.mipmap.ic_user).into(holder.userImage);
        Picasso.with(context).load(posts.get(position).getPostImage()).fit().noFade().placeholder(android.R.color.darker_gray).into(holder.postImage);

        holder.userName.setText(posts.get(position).getUserName());
        holder.postBlogDate.setText(posts.get(position).getPostBlogDate());
        holder.postDesc.setText(posts.get(position).getPostDesc());

        holder.blogPostDate = posts.get(position).getPostBlogDate();
        holder.UID = posts.get(position).getUID();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public long getItemId(int position) {
        setHasStableIds(true);
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView userImage;
        public ImageView postImage, likeImage;
        public TextView userName, postBlogDate, postDesc, likesNumbers;

        public String UID;
        public String blogPostDate = null;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            itemView.findViewById(R.id.likeButton).setOnClickListener(this);

            userImage = (CircleImageView) itemView.findViewById(R.id.user_image);
            postImage = (ImageView) itemView.findViewById(R.id.postImage);
            userName = (TextView) itemView.findViewById(R.id.userName);
            postBlogDate = (TextView) itemView.findViewById(R.id.blogPostDate);
            postDesc = (TextView) itemView.findViewById(R.id.postDescription);
            likeImage = (ImageView) itemView.findViewById(R.id.likeButton);
            likesNumbers = (TextView) itemView.findViewById(R.id.liksText);
        }

        @Override
        public void onClick(View v) {
            if (((ImageView) v).getTag().equals("unlike")) {
                ((ImageView) v).setImageResource(R.drawable.ic_action_like);
                ((ImageView) v).setTag("like");
                FirebaseDatabase.getInstance().getReference().child("likes").child(blogPostDate).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("");
            } else {
                ((ImageView) v).setImageResource(R.drawable.ic_like);
                ((ImageView) v).setTag("unlike");
                FirebaseDatabase.getInstance().getReference().child("likes").child(blogPostDate).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null);
            }
        }
    }

}
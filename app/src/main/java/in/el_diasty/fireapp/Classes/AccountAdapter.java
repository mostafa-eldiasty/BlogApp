package in.el_diasty.fireapp.Classes;

import android.content.Context;
import android.content.res.Resources;
import android.provider.Contacts;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.el_diasty.fireapp.Activites.MainActivity;
import in.el_diasty.fireapp.R;

/**
 * Created by Mostafa El-Diasty on 1/16/2019.
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.RecyclerViewHolder> {

    private ArrayList<Post> posts;
    private Context context;

    public AccountAdapter(ArrayList<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_recycler_view_post, parent, false);
        RecyclerViewHolder rvh = new RecyclerViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Picasso.with(context).load(posts.get(position).getUserImage()).fit().into(holder.userImage);
        Picasso.with(context).load(posts.get(position).getPostImage()).fit().into(holder.postImage);
        holder.userName.setText(posts.get(position).getUserName());
        holder.postBlogDate.setText(posts.get(position).getPostBlogDate());
        holder.postDesc.setText(posts.get(position).getPostDesc());

        holder.UID = posts.get(position).getUID();
//        holder.likesPosition = posts.get(position).getLikesNumber();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView userImage;
        public ImageView postImage, likeImage;
        public TextView userName, postBlogDate, postDesc, likesNumbers;

        public String likesPosition, UID;
        public int likesNum;
        private boolean clicked = false;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

//            itemView.findViewById(R.id.likeButton).setOnClickListener(this);

            userImage = (CircleImageView) itemView.findViewById(R.id.user_image);
            postImage = (ImageView) itemView.findViewById(R.id.postImage);
            userName = (TextView) itemView.findViewById(R.id.userName);
            postBlogDate = (TextView) itemView.findViewById(R.id.blogPostDate);
            postDesc = (TextView) itemView.findViewById(R.id.postDescription);
            likeImage = (ImageView) itemView.findViewById(R.id.likeButton);
            likesNumbers = (TextView) itemView.findViewById(R.id.liksText);
        }

//        @Override
//        public void onClick(View v) {
//            if(((ImageView)v).getDrawable().getConstantState() == v.getContext().getResources().getDrawable(R.drawable.ic_like).getConstantState()){
//                ((ImageView)v).setImageResource(R.drawable.ic_action_like);
//
////                FirebaseDatabase.getInstance().getReference().child("image descriptions").child(UID).child("new post data" + likesPosition).child("likes").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
//            }else{
//                ((ImageView)v).setImageResource(R.drawable.ic_like);
////                FirebaseDatabase.getInstance().getReference().child("image descriptions").child(UID).child("new post data" + likesPosition).child("likes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
//            }
//        }
    }
}
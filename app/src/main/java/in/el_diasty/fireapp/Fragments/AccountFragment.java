package in.el_diasty.fireapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.el_diasty.fireapp.Classes.Post;
import in.el_diasty.fireapp.Classes.RecyclerViewAdapter;
import in.el_diasty.fireapp.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class AccountFragment extends Fragment {

    private ArrayList<Post> posts = new ArrayList<>();
    private RecyclerView postsRecyclerView;
    private RecyclerViewAdapter Adapter;
    private DatabaseReference dataRef;
    private String profileImageUri = null;
    private String blogImageUri = null;
    private String userName;
    private String desc;
    private String currentTime;
    private String UID;
    private String userImageURL;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        Adapter = new RecyclerViewAdapter(posts, getActivity());
        postsRecyclerView = (RecyclerView) view.findViewById(R.id.postsRecylerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postsRecyclerView.setAdapter(Adapter);

        dataRef = FirebaseDatabase.getInstance().getReference("image descriptions");
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (!posts.isEmpty()) {
            return view;
        }

        FirebaseDatabase.getInstance().getReference().child("profile_images urls").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userImageURL = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dataRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0)
                    Toast.makeText(getActivity(), "you do not have any posts", Toast.LENGTH_LONG).show();
                posts.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    try {
                        userName = messageSnapshot.child("username").getValue().toString();
                        profileImageUri = userImageURL;
                        blogImageUri = messageSnapshot.child("blog image").getValue().toString();
                        desc = messageSnapshot.child("description").getValue().toString();
                        currentTime = messageSnapshot.child("date").getValue().toString();
                    } catch (Exception e) {
                        return;
                    }
                    posts.add(new Post(profileImageUri, blogImageUri, userName, currentTime, desc, ""));
                    Adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return view;
    }
}
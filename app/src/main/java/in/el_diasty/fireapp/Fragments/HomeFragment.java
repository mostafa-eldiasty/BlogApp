package in.el_diasty.fireapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import in.el_diasty.fireapp.Classes.Post;
import in.el_diasty.fireapp.Classes.RecyclerViewAdapter;
import in.el_diasty.fireapp.R;


/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment {

    private ArrayList<Post> posts = new ArrayList<>();
    private RecyclerView postsRecyclerView;
    private RecyclerViewAdapter Adapter;
    private DatabaseReference dataRef;
    private String profileImageUri = null;
    private String blogImageUri = null;
    private String desc;
    private String userName;
    private String currentTime;
    private Map<String, String> map = new HashMap<>(); //map for profile images urls
    public static boolean isPostAdded = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        Adapter = new RecyclerViewAdapter(posts, getActivity());
        postsRecyclerView = (RecyclerView) view.findViewById(R.id.postsRecylerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postsRecyclerView.setHasFixedSize(true);
//        postsRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        postsRecyclerView.setNestedScrollingEnabled(false);
        postsRecyclerView.setAdapter(Adapter);

        dataRef = FirebaseDatabase.getInstance().getReference("image descriptions");

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!posts.isEmpty() && isPostAdded == false) {
            return;
        }

        isPostAdded = false;

        FirebaseDatabase.getInstance().getReference().child("profile_images urls").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                map.put(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                posts.clear();

                for (final DataSnapshot userData : dataSnapshot.getChildren()) {
                    dataRef.child(userData.getKey()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            try {
                                userName = dataSnapshot.child("username").getValue().toString();
                                profileImageUri = map.get(userData.getKey());
                                blogImageUri = dataSnapshot.child("blog image").getValue().toString();
                                desc = dataSnapshot.child("description").getValue().toString();
                                currentTime = dataSnapshot.child("date").getValue().toString();
                            } catch (Exception e) {
                                return;
                            }

                            posts.add(new Post(profileImageUri, blogImageUri, userName, currentTime, desc, userData.getKey()));

                            Collections.sort(posts, new Comparator<Post>() {
                                @Override
                                public int compare(Post o1, Post o2) {
                                    return o1.getPostBlogDate().compareTo(o2.getPostBlogDate());
                                }
                            });
                            Adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
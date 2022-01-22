package in.el_diasty.fireapp.Activites;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import in.el_diasty.fireapp.Fragments.AccountFragment;
import in.el_diasty.fireapp.Fragments.HomeFragment;
import in.el_diasty.fireapp.Fragments.NotificationFragment;
import in.el_diasty.fireapp.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private Fragment homeFragment, notificationFragment, accountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        homeFragment = new HomeFragment();
        notificationFragment = new NotificationFragment();
        accountFragment = new AccountFragment();

        //Replace fragment using bottom Nav bar by calling Replace fragmebt func
        ReplaceFragment(homeFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.home1) {
                    ReplaceFragment(homeFragment);
                    return true;
                } else if (item.getItemId() == R.id.notification) {
                    ReplaceFragment(notificationFragment);
                    return true;
                } else if (item.getItemId() == R.id.account) {
                    ReplaceFragment(accountFragment);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //check if the user not logged in then direct him to login page
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Login();
            return;
        }

        //check if the user has not a username then direct him to setting page
        FirebaseDatabase.getInstance().getReference("users_names").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    dataSnapshot.getValue().toString();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "You should have a user name", Toast.LENGTH_LONG).show();
                    Intent settingIntent = new Intent(MainActivity.this, settingsActivity.class);
                    startActivity(settingIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_search_btn) {

        } else if (item.getItemId() == R.id.action_settings_btn) {
            Intent settingsIntent = new Intent(MainActivity.this, settingsActivity.class);
            startActivity(settingsIntent);
        } else if (item.getItemId() == R.id.action_logout_btn) {
            mAuth.signOut();
            Login();
        } else {
            return false;
        }

        return true;
    }

    private void Login() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    public void GoToNewPostActivity(View view) {
        Intent newPostIntent = new Intent(MainActivity.this, NewPostActivity.class);
        startActivity(newPostIntent);
    }

    private void ReplaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFreagment, fragment);
        fragmentTransaction.commit();
    }
}

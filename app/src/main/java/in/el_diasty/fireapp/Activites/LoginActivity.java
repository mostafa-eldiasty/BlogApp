package in.el_diasty.fireapp.Activites;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import in.el_diasty.fireapp.R;

public class LoginActivity extends AppCompatActivity {

    private String email, password;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
    }

    public void Login(View view) {

        progressBar.setVisibility(View.VISIBLE);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() != NetworkInfo.State.CONNECTED &&
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() != NetworkInfo.State.CONNECTED) {

            Toast.makeText(LoginActivity.this, "check your internet connection", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        email = ((TextView) findViewById(R.id.email)).getText().toString();
        password = ((TextView) findViewById(R.id.password)).getText().toString();
        progressBar.setVisibility(View.VISIBLE);

        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        GoToMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                }
            });
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void Regestriation(View view) {
        Intent regIntent = new Intent(LoginActivity.this, RegActivity.class);
        startActivity(regIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            GoToMainActivity();
        }
    }

    private void GoToMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}

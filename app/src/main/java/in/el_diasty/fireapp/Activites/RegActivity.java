package in.el_diasty.fireapp.Activites;

import android.content.Intent;
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

public class RegActivity extends AppCompatActivity {

    private TextView emailText, passText, confirmPassText;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        emailText = (TextView) findViewById(R.id.email);
        passText = (TextView) findViewById(R.id.password);
        confirmPassText = (TextView) findViewById(R.id.confirmPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

    }

    public void AlreadyHaveAccount(View view) {

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            Intent loginIntent = new Intent(RegActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    public void CreateAccount(View view) {

        String email = emailText.getText().toString();
        String password = passText.getText().toString();
        String confirmPass = confirmPassText.getText().toString();

        progressBar.setVisibility(View.VISIBLE);

        if (password.equals(confirmPass)) {

            try {

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent settingsIntent = new Intent(RegActivity.this, settingsActivity.class);
                            startActivity(settingsIntent);
                            finish();
                        } else {
                            Toast.makeText(RegActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                Toast.makeText(RegActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RegActivity.this, "your password doesn't match with confirm password", Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            Intent mainIntent = new Intent(RegActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
}

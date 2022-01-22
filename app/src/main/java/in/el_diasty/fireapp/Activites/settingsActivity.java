package in.el_diasty.fireapp.Activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import in.el_diasty.fireapp.Classes.DownloadImage;
import in.el_diasty.fireapp.R;

public class settingsActivity extends AppCompatActivity {

    private Uri mainImageUri = null;
    private ImageView profileImg;
    private EditText userNameRef;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private StorageReference storageRef;
    private DatabaseReference usersNamesRef;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(in.el_diasty.fireapp.R.layout.activity_settings);

        profileImg = (ImageView) findViewById(R.id.profile_image);
        userNameRef = (EditText) findViewById(R.id.userName);
        mAuth = FirebaseAuth.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersNamesRef = database.getReference("users_names");
        userID = mAuth.getCurrentUser().getUid();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        UpdateUserInfo();

    }

    private void UpdateUserInfo() {

        usersNamesRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.getValue(String.class);
                userNameRef.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.i("Database error", "Failed to read value.", error.toException());
            }
        });

        storageRef.child("profile_images/" + userID + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                DownloadImage downloadImage = new DownloadImage();
                Bitmap image = null;
                try {
                    image = downloadImage.execute(uri.toString()).get();
                    profileImg.setImageBitmap(image);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                profileImg.setImageBitmap(image);
            }
        });

    }


    public void Set_Image(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(settingsActivity.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = result.getUri();
                profileImg.setImageURI(mainImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            }
        }
    }

    public void updateSetting(View view) {

        progressBar.setVisibility(View.VISIBLE);

        String userName = userNameRef.getText().toString();

        usersNamesRef.child(userID).setValue(userName);

        if (mainImageUri == null && profileImg.getDrawable().getConstantState() != settingsActivity.this.getResources()
                .getDrawable(R.mipmap.profile_pic).getConstantState()) {

            Toast.makeText(settingsActivity.this, "user info updated", Toast.LENGTH_SHORT);

            Intent mainIntent = new Intent(settingsActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
            return;
        }

        if (mainImageUri == null) {
            mainImageUri = Uri.parse("android.resource://in.el_diasty.fireapp/" + R.mipmap.profile_pic);
        }

        StorageReference imageRef = storageRef.child("profile_images").child(userID + ".jpg");
        imageRef.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
//                    image is uploaded successfully
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = task.getResult().getDownloadUrl();
                    FirebaseDatabase.getInstance().getReference().child("profile_images urls").child(userID).setValue(downloadUrl.toString());

                    Toast.makeText(settingsActivity.this, "user info updated", Toast.LENGTH_SHORT);

                    Intent mainIntent = new Intent(settingsActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();


                } else {
                    Toast.makeText(settingsActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

package in.el_diasty.fireapp.Activites;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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

import java.text.DateFormat;
import java.util.Date;

import in.el_diasty.fireapp.Fragments.HomeFragment;
import in.el_diasty.fireapp.R;

public class NewPostActivity extends AppCompatActivity {

    private Uri imageUri = null;
    private ImageView image;
    private EditText imageDescriptionText;
    private String imageDescription;
    private long childNum, n;
    private String userID;

    private FirebaseAuth mAuth;
    private DatabaseReference dataRef;
    private StorageReference storageRef;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        image = (ImageView) findViewById(R.id.imageView);
        imageDescriptionText = (EditText) findViewById(R.id.imageDescriptionText);

        mAuth = FirebaseAuth.getInstance();
        dataRef = FirebaseDatabase.getInstance().getReference("image descriptions");

        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public void PostBlog(View view) {
        if (imageUri == null) {
            Toast.makeText(NewPostActivity.this, "Please upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        userID = mAuth.getCurrentUser().getUid();

        dataRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                childNum = dataSnapshot.getChildrenCount() + 1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imageDescription = imageDescriptionText.getText().toString();
        n = childNum;

        //storing username
        FirebaseDatabase.getInstance().getReference("users_names").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataRef.child(userID).child("new post data" + String.valueOf(n)).child("username").setValue(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //storing user image url
        storageRef.child("profile_images").child(userID + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                dataRef.child(userID).child("new post data" + String.valueOf(n)).child("user image").setValue(uri.toString());
            }
        });

        //storing post description
        dataRef.child(userID).child("new post data" + String.valueOf(n)).child("description").setValue(imageDescription);

        //storing post date
        String currentTime = DateFormat.getDateTimeInstance().format(new Date());
        dataRef.child(userID).child("new post data" + String.valueOf(n)).child("date").setValue(currentTime);

        //storing post image
        storageRef.child("blog image").child(userID).child("image" + String.valueOf(n)).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") String downloadImageUri = taskSnapshot.getDownloadUrl().toString();
                dataRef.child(userID).child("new post data" + String.valueOf(n)).child("blog image").setValue(downloadImageUri);
            }
        });

        Intent mainIntent = new Intent(NewPostActivity.this, MainActivity.class);
        startActivity(mainIntent);
        HomeFragment.isPostAdded = true;
        finish();

    }

    public void Add_Image(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(NewPostActivity.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                image.setImageURI(imageUri);
                ((TextView) findViewById(R.id.textView)).setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                childNum = dataSnapshot.getChildrenCount() + 1;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

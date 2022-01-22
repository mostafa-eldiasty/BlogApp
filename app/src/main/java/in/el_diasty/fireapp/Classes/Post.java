package in.el_diasty.fireapp.Classes;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mostafa El-Diasty on 1/16/2019.
 */

public class Post {
    private String userImage, postImage, userName, postBlogDate, postDesc, UID;

    public Post(String userImage, String postImage, String userName, String postBlogDate, String postDesc, String UID) {
        this.userImage = userImage;
        this.postImage = postImage;
        this.userName = userName;
        this.postBlogDate = postBlogDate;
        this.postDesc = postDesc;
        this.UID = UID;
    }

    public String getUserImage() {
        return userImage;
    }

    public String getPostImage() {
        return postImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getPostBlogDate() {
        return postBlogDate;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public String getUID() {
        return UID;
    }
}

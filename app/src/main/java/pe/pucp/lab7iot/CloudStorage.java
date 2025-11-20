package pe.pucp.lab7iot;

import android.net.Uri;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CloudStorage {

    private StorageReference storageRef;

    public CloudStorage() {
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    public UploadTask uploadProfileImage(Uri fileUri, String userId) {
        return storageRef.child("profiles/" + userId + ".jpg").putFile(fileUri);
    }

    public Task<Uri> getProfileImageUrl(String userId) {
        return storageRef.child("profiles/" + userId + ".jpg").getDownloadUrl();
    }
}

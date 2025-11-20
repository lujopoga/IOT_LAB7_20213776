package pe.pucp.lab7iot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;

    private TextView textNombre, textCorreo, textDni;
    private ImageView profileImage;
    private Button uploadButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CloudStorage cloudStorage;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        cloudStorage = new CloudStorage();

        textNombre = findViewById(R.id.textNombre);
        textCorreo = findViewById(R.id.textCorreo);
        textDni = findViewById(R.id.textDni);
        profileImage = findViewById(R.id.profileImage);
        uploadButton = findViewById(R.id.uploadButton);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();


        db.collection("usuarios").document(uid).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        textNombre.setText("Nombre: " + doc.getString("nombre"));
                        textCorreo.setText("Correo: " + doc.getString("correo"));
                        textDni.setText("DNI: " + doc.getString("dni"));
                    }
                });


        uploadButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();


            profileImage.setImageURI(imageUri);

            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null) return;
            String uid = user.getUid();

            cloudStorage.uploadProfileImage(imageUri, uid)
                    .addOnSuccessListener(task -> {
                        cloudStorage.getProfileImageUrl(uid).addOnSuccessListener(url -> {

                            Toast.makeText(ProfileActivity.this,
                                    "Imagen subida: " + url.toString(),
                                    Toast.LENGTH_LONG).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ProfileActivity.this,
                                "Error al subir imagen: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        }
    }
}

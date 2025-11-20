package pe.pucp.lab7iot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseApp;


public class MainActivity extends AppCompatActivity {

    private AuthService authService;


    private EditText editTextNombre;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextDni;


    private Button btnLogin;
    private Button btnRegister;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        authService = new AuthService();


        editTextPassword = findViewById(R.id.editTextPassword);
        editTextDni = findViewById(R.id.editTextDni);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextEmail = findViewById(R.id.editTextEmail);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnReset = findViewById(R.id.btnReset);


        btnLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Ingresa correo y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            authService.signInWithEmail(email, password, new AuthCallback() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    Toast.makeText(MainActivity.this, "Login correcto", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(i);
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


        btnRegister.setOnClickListener(v -> {
            String nombre = editTextNombre.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String pass = editTextPassword.getText().toString().trim();
            String dni = editTextDni.getText().toString().trim();

            authService.registerUser(nombre, email, pass, dni, new AuthCallback() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    Toast.makeText(MainActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });



        btnReset.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Ingresa el correo para recuperar", Toast.LENGTH_SHORT).show();
                return;
            }

            authService.sendPasswordResetEmail(email, new AuthCallback() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    Toast.makeText(MainActivity.this, "Correo de recuperación enviado", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });
    }
}

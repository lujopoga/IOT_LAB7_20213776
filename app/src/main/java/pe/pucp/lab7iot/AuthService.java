package pe.pucp.lab7iot;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Looper;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String REGISTRO_URL = "http://192.168.18.47:8080/registro";

    public AuthService() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public void signInWithEmail(String email, String password, AuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        callback.onSuccess(user);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public void sendPasswordResetEmail(String email, AuthCallback callback) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(null);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
    }

    public void registerUser(String nombre, String email, String password, String dni, AuthCallback callback) {

        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(REGISTRO_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");


                String body = "{"
                        + "\"dni\":\"" + dni + "\","
                        + "\"correo\":\"" + email + "\""
                        + "}";

                OutputStream os = conn.getOutputStream();
                os.write(body.getBytes("UTF-8"));
                os.flush();
                os.close();

                int code = conn.getResponseCode();
                Log.d("AuthService", "HTTP /registro code = " + code);

                if (code == HttpURLConnection.HTTP_OK) {

                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(() -> {

                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();


                                        Map<String, Object> data = new HashMap<>();
                                        data.put("nombre", nombre);
                                        data.put("dni", dni);
                                        data.put("correo", email);

                                        db.collection("usuarios")
                                                .document(user.getUid())
                                                .set(data)
                                                .addOnSuccessListener(aVoid -> {

                                                    callback.onSuccess(user);
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e("FirestoreError", "Error guardando datos: ", e);
                                                    callback.onFailure(e);
                                                });

                                    } else {
                                        callback.onFailure(task.getException());
                                    }
                                });

                    });

                } else {

                    InputStream errorStream = conn.getErrorStream();
                    StringBuilder sb = new StringBuilder();
                    if (errorStream != null) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(errorStream));
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        br.close();
                    }

                    String errorMsg = sb.length() > 0 ? sb.toString()
                            : "Error en validación del registro (código " + code + ")";

                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    mainHandler.post(() -> callback.onFailure(new Exception(errorMsg)));
                }

            } catch (Exception e) {
                Log.e("AuthService", "Error llamando /registro", e);
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(() -> callback.onFailure(e));
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }


}

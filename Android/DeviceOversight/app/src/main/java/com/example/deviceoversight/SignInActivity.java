package com.example.deviceoversight;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.deviceoversight.utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextView forgotText;
    private Button loginButton;
    private EditText loginEmail;
    private EditText loginPassword;
    private TextView loginText;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    public void onStart() {
        registerReceiver(this.networkChangeListener, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        super.onStart();
    }

    @Override
    public void onStop() {
        unregisterReceiver(this.networkChangeListener);
        super.onStop();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // Kiểm tra trạng thái đăng nhập
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Người dùng đã đăng nhập, điều hướng đến MainActivity
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return; // Ngăn không cho tiếp tục thực thi phần còn lại của phương thức
        }

        setContentView(R.layout.activity_sign_in);
        initUI();
        setupListeners();
    }

    private void initUI() {
        this.loginEmail = findViewById(R.id.login_email);
        this.loginPassword = findViewById(R.id.login_password);
        this.loginButton = findViewById(R.id.login_btn_signin);
        this.loginText = findViewById(R.id.login_text_signup);
        this.forgotText = findViewById(R.id.forgot_text_Password);
        this.auth = FirebaseAuth.getInstance();
    }

    private void setupListeners() {
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        this.loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        this.forgotText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });
    }

    private void attemptLogin() {
        String email = this.loginEmail.getText().toString().trim();
        String password = this.loginPassword.getText().toString().trim();

        if (email.isEmpty()) {
            this.loginEmail.setError("Email cannot be empty");
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.loginEmail.setError("Please enter valid email");
            Toast.makeText(this, "Please enter valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            this.loginPassword.setError("Password cannot be empty");
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        this.auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Lưu trạng thái đăng nhập
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();

                        Toast.makeText(SignInActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        loginPassword.setError("Login Failed");
                    }
                });
    }
}

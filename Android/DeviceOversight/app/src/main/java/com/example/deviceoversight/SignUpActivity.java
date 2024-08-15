package com.example.deviceoversight;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.deviceoversight.utility.NetworkChangeListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";

    private FirebaseAuth fAuth;
    private Button mButton;
    private TextInputEditText mConfirmPassword;
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private TextView mView;
    private NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onStart() {
        registerReceiver(this.networkChangeListener, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(this.networkChangeListener);
        super.onStop();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUI();
        setupListeners();
    }

    private void initUI() {
        mView = findViewById(R.id.signup_text_login);
        mEmail = findViewById(R.id.signup_email);
        mPassword = findViewById(R.id.signup_password);
        mConfirmPassword = findViewById(R.id.signup_confirm_password);
        mButton = findViewById(R.id.btn_signup);
        fAuth = FirebaseAuth.getInstance();
    }

    private void setupListeners() {
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });
    }

    private void attemptSignUp() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString();
        String confirmPassword = mConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Email Field can't be empty");
            Toast.makeText(this, "Email Field can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.matches(EMAIL_PATTERN)) {
            mEmail.setError("Enter a valid Email Address");
            Toast.makeText(this, "Enter a valid Email Address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            mPassword.setError("Password must be at least 6 characters long");
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.matches(PASSWORD_PATTERN)) {
            mPassword.setError("Mix of letters (upper and lower case), number, and symbols");
            Toast.makeText(this, "Mix of letters (upper and lower case), number, and symbols", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!confirmPassword.equals(password)) {
            mConfirmPassword.setError("Confirm Password and Password should be same");
            Toast.makeText(this, "Confirm Password and Password should be same", Toast.LENGTH_SHORT).show();
            return;
        }

        signUpUser(email, password);
    }

    private void signUpUser(String email, String password) {
        mButton.setVisibility(View.INVISIBLE);
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(SignUpActivity.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(SignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        mButton.setVisibility(View.VISIBLE);
                    }
                });
    }
}
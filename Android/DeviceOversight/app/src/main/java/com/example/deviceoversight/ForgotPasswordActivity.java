package com.example.deviceoversight;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.deviceoversight.utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    Button btnBack;
    Button btnReset;
    EditText edtEmail;
    FirebaseAuth mAuth;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    String strEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String txtEmail;

    /* access modifiers changed from: protected */
    public void onStart() {
        registerReceiver(this.networkChangeListener, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        unregisterReceiver(this.networkChangeListener);
        super.onStop();
    }

    /* access modifiers changed from: protected */
    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_forgot_password);
        this.btnReset = (Button) findViewById(R.id.btn_rs);
        this.btnBack = (Button) findViewById(R.id.btn_back);
        this.edtEmail = (EditText) findViewById(R.id.email);
        this.mAuth = FirebaseAuth.getInstance();
        this.btnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ForgotPasswordActivity forgotPasswordActivity = ForgotPasswordActivity.this;
                forgotPasswordActivity.txtEmail = forgotPasswordActivity.edtEmail.getText().toString().trim();
                if (TextUtils.isEmpty(ForgotPasswordActivity.this.txtEmail)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Email field can't be empty", Toast.LENGTH_SHORT).show();
                    ForgotPasswordActivity.this.edtEmail.setError("Failed");
                } else if (ForgotPasswordActivity.this.txtEmail.matches(ForgotPasswordActivity.this.strEmail)) {
                    ForgotPasswordActivity.this.ResetPassword();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Enter a valid Email Address", Toast.LENGTH_SHORT).show();
                    ForgotPasswordActivity.this.edtEmail.setError("Failed");
                }
            }
        });
        this.btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ForgotPasswordActivity.this.startActivity(new Intent(ForgotPasswordActivity.this, SignInActivity.class));
                ForgotPasswordActivity.this.finish();
            }
        });
    }

    /* access modifiers changed from: private */
    public void ResetPassword() {
        this.btnReset.setVisibility(View.INVISIBLE);
        this.mAuth.sendPasswordResetEmail(this.txtEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            public void onSuccess(Void voidR) {
                Toast.makeText(ForgotPasswordActivity.this, "Reset Password link has been sent to your registered Email", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception exc) {
                Toast.makeText(ForgotPasswordActivity.this, "Error :- " + exc.getMessage(), Toast.LENGTH_SHORT).show();
                ForgotPasswordActivity.this.btnReset.setVisibility(View.VISIBLE);
            }
        });
    }
}


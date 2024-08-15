package com.example.deviceoversight.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.deviceoversight.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.regex.Pattern;

public class ChangePasswordFragment extends Fragment {
    TextInputEditText CurrentPass;
    TextInputEditText NewPass;
    Button btnPass;
    TextInputLayout textCurrentPass;
    TextInputLayout textNewPass;
    private View view;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_change_password, viewGroup, false);
        this.view = inflate;
        this.textCurrentPass = (TextInputLayout) inflate.findViewById(R.id.text_current_pass);
        this.textNewPass = (TextInputLayout) this.view.findViewById(R.id.text_new_pass);
        this.CurrentPass = (TextInputEditText) this.view.findViewById(R.id.current_password);
        this.NewPass = (TextInputEditText) this.view.findViewById(R.id.new_password);
        this.btnPass = (Button) this.view.findViewById(R.id.btn_pass);
        textCrPass();
        textNPass();
        showChangePassword();
        return this.view;
    }

    private void showChangePassword() {
        this.btnPass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String trim = ChangePasswordFragment.this.CurrentPass.getText().toString().trim();
                String trim2 = ChangePasswordFragment.this.NewPass.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    Toast.makeText(ChangePasswordFragment.this.getActivity(), "Enter your current password...", Toast.LENGTH_SHORT).show();
                } else if (trim2.length() < 6) {
                    Toast.makeText(ChangePasswordFragment.this.getActivity(), "Password length must least 6 characters...", Toast.LENGTH_SHORT).show();
                } else {
                    ChangePasswordFragment.this.updatePassword(trim, trim2);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void updatePassword(final String str, final String str2) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser.reauthenticate(EmailAuthProvider.getCredential(currentUser.getEmail(), str)).addOnSuccessListener(new OnSuccessListener<Void>() {
            public void onSuccess(Void voidR) {
                currentUser.updatePassword(str2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    public void onSuccess(Void voidR) {
                        if (str.equals(str2)) {
                            Toast.makeText(ChangePasswordFragment.this.getActivity(), "The new password must not be the same as the old password", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChangePasswordFragment.this.getActivity(), "Password has changed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    public void onFailure(Exception exc) {
                        Toast.makeText(ChangePasswordFragment.this.getActivity(), "An unknown error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception exc) {
                Toast.makeText(ChangePasswordFragment.this.getActivity(), "Current password is incorrect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void textCrPass() {
        this.CurrentPass.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String obj = charSequence.toString();
                if (obj.length() < 6) {
                    ChangePasswordFragment.this.textCurrentPass.setHelperText("Password must 6 characters long");
                    ChangePasswordFragment.this.textCurrentPass.setError("");
                } else if (Pattern.compile("[a-zA-Z0-9]").matcher(obj).find()) {
                    ChangePasswordFragment.this.textCurrentPass.setHelperText("Your password are strong");
                    ChangePasswordFragment.this.textCurrentPass.setError("");
                } else {
                    ChangePasswordFragment.this.textCurrentPass.setError("Mix of letters(upper and lower case), number and symbols");
                }
            }
        });
    }

    private void textNPass() {
        this.NewPass.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String obj = charSequence.toString();
                if (obj.length() < 6) {
                    ChangePasswordFragment.this.textNewPass.setHelperText("Password must 6 characters long");
                    ChangePasswordFragment.this.textNewPass.setError("");
                } else if (Pattern.compile("[a-zA-Z0-9]").matcher(obj).find()) {
                    ChangePasswordFragment.this.textNewPass.setHelperText("Your password are strong");
                    ChangePasswordFragment.this.textNewPass.setError("");
                } else {
                    ChangePasswordFragment.this.textNewPass.setError("Mix of letters(upper and lower case), number and symbols");
                }
            }
        });
    }
}


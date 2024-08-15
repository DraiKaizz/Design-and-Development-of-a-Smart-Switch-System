package com.example.deviceoversight.Fragment;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.deviceoversight.MainActivity;
import com.example.deviceoversight.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MyProfileFragment extends Fragment {
    private Button btnUpdate;
    private EditText edtEmail;
    private EditText edtFullName;
    private ImageView imgAvatar;
    private Uri mUri;
    private MainActivity mainActivity;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        initUi();
        setUserInformation();
        mainActivity = (MainActivity) requireActivity();
        initListener();
        return view;
    }

    private void initUi() {
        imgAvatar = view.findViewById(R.id.image_mp);
        edtFullName = view.findViewById(R.id.edt_full_name);
        edtEmail = view.findViewById(R.id.edt_email);
        btnUpdate = view.findViewById(R.id.btn_update);
    }

    private void setUserInformation() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            Uri photoUrl = currentUser.getPhotoUrl();

            Log.d("MyProfileFragment", "Display Name: " + displayName);
            Log.d("MyProfileFragment", "Email: " + email);
            Log.d("MyProfileFragment", "Photo URL: " + (photoUrl != null ? photoUrl.toString() : "null"));

            edtFullName.setText(displayName);
            edtEmail.setText(email);

            if (photoUrl != null) {
                Glide.with(getActivity())
                        .load(photoUrl)
                        .error(R.drawable.ic_avatar_default)
                        .into(imgAvatar);
            } else {
                imgAvatar.setImageResource(R.drawable.ic_avatar_default);
            }
        } else {
            Log.d("MyProfileFragment", "Current user is null");
        }
    }

    private void initListener() {
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
            }
        });
    }

    private void onClickRequestPermission() {
        if (requireActivity() != null) {
            if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                mainActivity.openGallery();
            } else {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MainActivity.MY_REQUEST_CODE);
            }
        }
    }

    public void setUri(Uri uri) {
        this.mUri = uri;
    }

    public void setBitmapImage(Bitmap bitmap) {
        imgAvatar.setImageBitmap(bitmap);
    }

    private void onClickUpdateProfile() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(edtFullName.getText().toString().trim())
                    .setPhotoUri(mUri)
                    .build();

            currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(requireActivity(), "Update profile success", Toast.LENGTH_SHORT).show();
                                mainActivity.showUserInformation();
                            } else {
                                Toast.makeText(requireActivity(), "Update profile failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
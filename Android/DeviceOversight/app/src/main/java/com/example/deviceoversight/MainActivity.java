package com.example.deviceoversight;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.deviceoversight.Fragment.ChangePasswordFragment;
import com.example.deviceoversight.Fragment.ControlFragment;
import com.example.deviceoversight.Fragment.HomeFragment;
import com.example.deviceoversight.Fragment.MyProfileFragment;
import com.example.deviceoversight.Fragment.SetTimerFragment;
import com.example.deviceoversight.utility.NetworkChangeListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int FRAGMENT_CHANGE_PASSWORD = 4;
    private static final int FRAGMENT_CONTROL = 1;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_MY_PROFILE = 3;
    private static final int FRAGMENT_SET_TIMER = 2;
    public static final int MY_REQUEST_CODE = 11;
    private int currentFragment = 0;
    public final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleActivityResult);
    private DrawerLayout drawerLayout;
    private ImageView imgAvatar;
    private NavigationView navigationView;
    public final MyProfileFragment myProfileFragment = new MyProfileFragment();
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private TextView tvEmail;
    private TextView tvName;

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

    @Override
    public void onPause() {
        super.onPause();
        // Dừng các tính năng không cần thiết khi Fragment bị ẩn
    }

    private void handleActivityResult(ActivityResult activityResult) {
        if (activityResult.getResultCode() == RESULT_OK) {
            Uri data = activityResult.getData().getData();
            this.myProfileFragment.setUri(data);
            try {
                this.myProfileFragment.setBitmapImage(MediaStore.Images.Media.getBitmap(getContentResolver(), data));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initUi();
        this.drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        showUserInformation();
        this.navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new HomeFragment());
        this.navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    private void initUi() {
        this.navigationView = findViewById(R.id.navigation_view);
        this.imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.img_avatar);
        this.tvName = navigationView.getHeaderView(0).findViewById(R.id.tv_name);
        this.tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_email);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.nav_home) {
            if (this.currentFragment != FRAGMENT_HOME) {
                replaceFragment(new HomeFragment());
                this.currentFragment = FRAGMENT_HOME;
                setTitle("Home");
            }
        } else if (itemId == R.id.nav_control) {
            if (this.currentFragment != FRAGMENT_CONTROL) {
                replaceFragment(new ControlFragment());
                this.currentFragment = FRAGMENT_CONTROL;
                setTitle("Control");
            }
        } else if (itemId == R.id.nav_set_timer) {
            if (this.currentFragment != FRAGMENT_SET_TIMER) {
                replaceFragment(new SetTimerFragment());
                this.currentFragment = FRAGMENT_SET_TIMER;
                setTitle("Set Timer");
            }
        } else if (itemId == R.id.nav_my_profile) {
            if (this.currentFragment != FRAGMENT_MY_PROFILE) {
                replaceFragment(this.myProfileFragment);
                this.currentFragment = FRAGMENT_MY_PROFILE;
                setTitle("My Profile");
            }
        } else if (itemId == R.id.nav_change_password) {
            if (this.currentFragment != FRAGMENT_CHANGE_PASSWORD) {
                replaceFragment(new ChangePasswordFragment());
                this.currentFragment = FRAGMENT_CHANGE_PASSWORD;
                setTitle("Change Password");
            }
        } else if (itemId == R.id.nav_sign_out) {
            logout();
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        // Điều hướng về SignInActivity
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment); // Thay thế Fragment hiện tại bằng Fragment mới
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showUserInformation() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            Uri photoUrl = currentUser.getPhotoUrl();
            this.tvName.setText(displayName);
            this.tvEmail.setText(email);
            if (photoUrl != null) {
                Glide.with(this).load(photoUrl).error(R.drawable.ic_avatar_default).into(this.imgAvatar);
            } else {
                this.imgAvatar.setImageResource(R.drawable.ic_avatar_default);
            }
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 11 && iArr.length > 0 && iArr[0] == 0) {
            openGallery();
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.activityResultLauncher.launch(Intent.createChooser(intent, "Select picture"));
    }
}
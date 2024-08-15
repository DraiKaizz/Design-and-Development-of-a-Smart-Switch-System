package com.example.deviceoversight.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;
import androidx.fragment.app.Fragment;
import com.example.deviceoversight.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ControlFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference myRef_1;
    DatabaseReference my_data;
    ToggleButton switch1;
    ToggleButton switch2;
    ToggleButton toggleButton;
    int tt1 = 0;
    int tt2 = 0;
    int tt_button1 = 0;
    int tt_button2 = 0;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_control, viewGroup, false);
        this.toggleButton =  inflate.findViewById(R.id.toggleButton);
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        this.database = instance;
        this.myRef = instance.getReference("Devices/Device State 1");
        this.myRef_1 = this.database.getReference("Devices/Device State 2");
        this.my_data = this.database.getReference();
        this.switch1 =  inflate.findViewById(R.id.switch1);
        this.switch2 =  inflate.findViewById(R.id.switch2);


        this.myRef.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals("1")) {
                    ControlFragment.this.tt1 = 1;
                } else if (dataSnapshot.getValue().toString().equals("0")) {
                    ControlFragment.this.tt1 = 0;
                }
            }
        });
        this.myRef_1.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().toString().equals("1")) {
                    ControlFragment.this.tt2 = 1;
                } else if (dataSnapshot.getValue().toString().equals("0")) {
                    ControlFragment.this.tt2 = 0;
                }
            }
        });
        this.my_data.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                if (ControlFragment.this.tt1 == 1 && ControlFragment.this.tt2 == 1) {
                    ControlFragment.this.toggleButton.setChecked(true);
                    ControlFragment.this.switch1.setChecked(true);
                    ControlFragment.this.switch2.setChecked(true);
                } else if (ControlFragment.this.tt1 == 0 && ControlFragment.this.tt2 == 0) {
                    ControlFragment.this.toggleButton.setChecked(false);
                    ControlFragment.this.switch1.setChecked(false);
                    ControlFragment.this.switch2.setChecked(false);
                } else if (ControlFragment.this.tt1 == 1 && ControlFragment.this.tt2 == 0) {
                    ControlFragment.this.switch1.setChecked(true);
                    ControlFragment.this.switch2.setChecked(false);
                    ControlFragment.this.toggleButton.setChecked(false);
                } else if (ControlFragment.this.tt1 == 0 && ControlFragment.this.tt2 == 1) {
                    ControlFragment.this.switch1.setChecked(false);
                    ControlFragment.this.switch2.setChecked(true);
                    ControlFragment.this.toggleButton.setChecked(false);
                }
            }
        });


        this.switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateView0(v);
            }
        });
        this.switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateView1(v);
            }
        });
        this.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateView2(v);
            }
        });
        return inflate;
    }

    public void onCreateView0(View view) {
        if (this.switch1.isChecked()) {
            this.tt_button1 = 1;
            this.myRef.setValue(1);
            return;
        }
        this.tt_button1 = 0;
        this.myRef.setValue(0);
    }

    public void onCreateView1(View view) {
        if (this.switch2.isChecked()) {
            this.tt_button2 = 1;
            this.myRef_1.setValue(1);
            return;
        }
        this.tt_button2 = 0;
        this.myRef_1.setValue(0);
    }
    public void onCreateView2(View view) {
        if (this.toggleButton.isChecked()) {
            this.myRef.setValue(1);
            this.myRef_1.setValue(1);
            return;
        }
        this.myRef.setValue(0);
        this.myRef_1.setValue(0);
    }
}

package com.example.deviceoversight.Fragment;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import com.example.deviceoversight.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;

public class SetTimerFragment extends Fragment {
    int Hour;
    int Hour1;
    int Hour1Off;
    int Hour1On;
    int HourOff;
    int HourOn;
    int Minute;
    int Minute1;
    int Minute1Off;
    int Minute1On;
    int MinuteOff;
    int MinuteOn;
    ToggleButton btnCancel;
    ToggleButton btnCancel1;
    Button btnTimer;
    Button btnTimer1;
    Calendar calendar;
    int countdowH;
    int countdowM;
    DatabaseReference currentDataTime;
    DatabaseReference currentDataTime1;
    int currentHour1;
    int currentMinute1;
    DatabaseReference currentTimeHour;
    DatabaseReference currentTimeMinute;
    DatabaseReference dataTime;
    DatabaseReference dataTime1;
    FirebaseDatabase database;
    int getCountdowH;
    int getCountdowM;
    int getSelectHour1Off;
    int getSelectHour1On;
    int getSelectHourOff;
    int getSelectHourOn;
    int getSelectMinute1Off;
    int getSelectMinute1On;
    int getSelectMinuteOff;
    int getSelectMinuteOn;
    final Handler handler = new Handler();
    int modeH = 23;
    int modeM = 59;
    DatabaseReference myRef;
    DatabaseReference myRef_1;


    DatabaseReference myTimeHour1Off;
    DatabaseReference myTimeHour1On;
    DatabaseReference myTimeMinute1Off;
    DatabaseReference myTimeMinute1On;
    DatabaseReference myTimeOffHour;
    DatabaseReference myTimeOffMinute;
    DatabaseReference myTimeOnHour;
    DatabaseReference myTimeOnMinute;
    int nextDayH;
    int nextDayH1;
    int nextDayM;
    int nextDayM1;
    int selectHour1Off;
    int selectHour1On;
    int selectHourOff;
    int selectHourOn;
    int selectMinute1Off;
    int selectMinute1On;
    int selectMinuteOff;
    int selectMinuteOn;
    int state;
    int state1;
    TextView time;
    TextView time1;
    TimePickerDialog timePickerDialog;
    TimePickerDialog timePickerDialog1;
    int val;
    int val1;
    TextView viewTimer;
    TextView viewTimer1;
    TextView wait;
    TextView wait1;

    @SuppressLint("WrongViewCast")
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_set_timer, viewGroup, false);
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        this.database = instance;
        this.myRef = instance.getReference("Devices/Device State 1");
        this.myRef_1 = this.database.getReference("Devices/Device State 2");
        this.myTimeOnHour = this.database.getReference("Time Select For 1/Hour On");
        this.myTimeOnMinute = this.database.getReference("Time Select For 1/Minute On");
        this.myTimeOffHour = this.database.getReference("Time Select For 1/Hour Off");
        this.myTimeOffMinute = this.database.getReference("Time Select For 1/Minute Off");
        this.currentDataTime = this.database.getReference();
        this.currentTimeHour = this.database.getReference("Time Current/Hour");
        this.currentTimeMinute = this.database.getReference("Time Current/Minute");
        this.dataTime = this.database.getReference();
        this.dataTime1 = this.database.getReference();
        this.myTimeHour1On = this.database.getReference("Time Select For 2/Hour On");
        this.myTimeMinute1On = this.database.getReference("Time Select For 2/Minute On");
        this.myTimeHour1Off = this.database.getReference("Time Select For 2/Hour Off");
        this.myTimeMinute1Off = this.database.getReference("Time Select For 2/Minute Off");
        this.currentDataTime1 = this.database.getReference();
        this.wait =  inflate.findViewById(R.id.wait);
        this.wait1 =  inflate.findViewById(R.id.wait1);
        this.btnTimer =  inflate.findViewById(R.id.btn_timer);
        this.btnTimer1 =  inflate.findViewById(R.id.btn_timer1);
        this.btnCancel =  inflate.findViewById(R.id.btn_cancel);
        this.btnCancel1 =  inflate.findViewById(R.id.btn_cancel1);
        this.viewTimer =  inflate.findViewById(R.id.view_timer);
        this.viewTimer1 = inflate.findViewById(R.id.view_timer1);
        this.time =  inflate.findViewById(R.id.time);
        this.time1 =  inflate.findViewById(R.id.time1);
        this.calendar = Calendar.getInstance();


        final Handler handler2 = new Handler();
        handler2.post(new Runnable() {
            public void run() {
                SetTimerFragment.this.calendar = Calendar.getInstance();
                SetTimerFragment setTimerFragment = SetTimerFragment.this;
                setTimerFragment.currentHour1 = setTimerFragment.calendar.get(11);
                SetTimerFragment setTimerFragment2 = SetTimerFragment.this;
                setTimerFragment2.currentMinute1 = setTimerFragment2.calendar.get(12);
                SetTimerFragment.this.currentTimeHour.setValue(Integer.valueOf(SetTimerFragment.this.currentHour1));
                SetTimerFragment.this.currentTimeMinute.setValue(Integer.valueOf(SetTimerFragment.this.currentMinute1));
                handler2.postDelayed(this, 16);
            }
        });


        //// time current
        this.currentTimeHour.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                if (value instanceof Number) {
                    SetTimerFragment.this.countdowH = ((Number) value).intValue();
                }
                if (SetTimerFragment.this.countdowH > SetTimerFragment.this.modeH) {
                    Toast.makeText(SetTimerFragment.this.getContext(), "Someone Intervened", Toast.LENGTH_SHORT).show();
                } else if (SetTimerFragment.this.countdowH < 0 || SetTimerFragment.this.countdowH > 23) {
                    SetTimerFragment.this.getCountdowH = 0;
                } else {
                    SetTimerFragment setTimerFragment = SetTimerFragment.this;
                    setTimerFragment.getCountdowH = setTimerFragment.countdowH;
                }
            }
        });
        this.currentTimeMinute.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                if (value instanceof Number) {
                    SetTimerFragment.this.countdowM = ((Number) value).intValue();
                }
                if (SetTimerFragment.this.countdowM > SetTimerFragment.this.modeM) {
                    Toast.makeText(SetTimerFragment.this.getContext(), "Someone Intervened", Toast.LENGTH_SHORT).show();
                } else if (SetTimerFragment.this.countdowM >= 0 && SetTimerFragment.this.countdowM <= 59) {
                    SetTimerFragment setTimerFragment = SetTimerFragment.this;
                    setTimerFragment.getCountdowM = setTimerFragment.countdowM;
                }
            }
        });


        /// time set of device 1
        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateView(v);
            }
        });
        this.currentDataTime.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.child("Time Select For 1").child("Hour On").getValue();
                Object value2 = dataSnapshot.child("Time Select For 1").child("Minute On").getValue();
                Object value3 = dataSnapshot.child("Time Select For 1").child("State").getValue();
                Object value4 = dataSnapshot.child("Time Select For 1").child("Hour Off").getValue();
                Object value5 = dataSnapshot.child("Time Select For 1").child("Minute Off").getValue();
                Object value6 = dataSnapshot.child("Devices").child("Device State 1").getValue();
                if (value instanceof Number) {
                    SetTimerFragment.this.HourOn = ((Number) value).intValue();
                }
                if (value2 instanceof Number) {
                    SetTimerFragment.this.MinuteOn = ((Number) value2).intValue();
                }
                if (value4 instanceof Number) {
                    SetTimerFragment.this.HourOff = ((Number) value4).intValue();
                }
                if (value5 instanceof Number) {
                    SetTimerFragment.this.MinuteOff = ((Number) value5).intValue();
                }
                if (value3 instanceof Number) {
                    SetTimerFragment.this.state = ((Number) value3).intValue();
                }
                if (value6 instanceof Number) {
                    SetTimerFragment.this.val = ((Number) value6).intValue();
                }
                if (SetTimerFragment.this.val == 1) {
                    if (SetTimerFragment.this.state == 1) {
                        SetTimerFragment.this.time.setText(String.format("Hour: %02d  Minute: %02d", new Object[]{Integer.valueOf(SetTimerFragment.this.HourOff), Integer.valueOf(SetTimerFragment.this.MinuteOff)}));
                        SetTimerFragment.this.btnCancel.setChecked(true);
                    } else if (SetTimerFragment.this.state == 0) {
                        SetTimerFragment.this.time.setText("Hour: 00  Minute: 00");
                        SetTimerFragment.this.btnCancel.setChecked(false);
                    }
                } else if (SetTimerFragment.this.val != 0) {
                } else {
                    if (SetTimerFragment.this.state == 1) {
                        SetTimerFragment.this.time.setText(String.format("Hour: %02d  Minute: %02d", new Object[]{Integer.valueOf(SetTimerFragment.this.HourOn), Integer.valueOf(SetTimerFragment.this.MinuteOn)}));
                        SetTimerFragment.this.btnCancel.setChecked(true);
                    } else if (SetTimerFragment.this.state == 0) {
                        SetTimerFragment.this.time.setText("Hour: 00  Minute: 00");
                        SetTimerFragment.this.btnCancel.setChecked(false);
                    }
                }
            }
        });
        this.myTimeOnHour.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                if (value instanceof Number) {
                    SetTimerFragment.this.selectHourOn = ((Number) value).intValue();
                }
                if (SetTimerFragment.this.selectHourOn > SetTimerFragment.this.modeH) {
                    Toast.makeText(SetTimerFragment.this.getContext(), "Someone Intervened", Toast.LENGTH_SHORT).show();
                } else if (SetTimerFragment.this.selectHourOn >= 0 && SetTimerFragment.this.selectHourOn <= 23) {
                    SetTimerFragment setTimerFragment = SetTimerFragment.this;
                    setTimerFragment.getSelectHourOn = setTimerFragment.selectHourOn;
                }
            }
        });
        this.myTimeOnMinute.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                if (value instanceof Number) {
                    SetTimerFragment.this.selectMinuteOn = ((Number) value).intValue();
                }
                if (SetTimerFragment.this.selectMinuteOn > SetTimerFragment.this.modeM) {
                    Toast.makeText(SetTimerFragment.this.getContext(), "Someone Intervened", Toast.LENGTH_SHORT).show();
                } else if (SetTimerFragment.this.selectMinuteOn >= 0 && SetTimerFragment.this.selectMinuteOn <= 59) {
                    SetTimerFragment setTimerFragment = SetTimerFragment.this;
                    setTimerFragment.getSelectMinuteOn = setTimerFragment.selectMinuteOn;
                }
            }
        });
        this.myTimeOffHour.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                if (value instanceof Number) {
                    SetTimerFragment.this.selectHourOff = ((Number) value).intValue();
                }
                if (SetTimerFragment.this.selectHourOff > SetTimerFragment.this.modeH) {
                    Toast.makeText(SetTimerFragment.this.getContext(), "Someone Intervened", Toast.LENGTH_SHORT).show();
                } else if (SetTimerFragment.this.selectHourOff >= 0 && SetTimerFragment.this.selectHourOff <= 23) {
                    SetTimerFragment setTimerFragment = SetTimerFragment.this;
                    setTimerFragment.getSelectHourOff = setTimerFragment.selectHourOff;
                }
            }
        });
        this.myTimeOffMinute.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                if (value instanceof Number) {
                    SetTimerFragment.this.selectMinuteOff = ((Number) value).intValue();
                }
                if (SetTimerFragment.this.selectMinuteOff > SetTimerFragment.this.modeM) {
                    Toast.makeText(SetTimerFragment.this.getContext(), "Someone Intervened", Toast.LENGTH_SHORT).show();
                } else if (SetTimerFragment.this.selectMinuteOff >= 0 && SetTimerFragment.this.selectMinuteOff <= 59) {
                    SetTimerFragment setTimerFragment = SetTimerFragment.this;
                    setTimerFragment.getSelectMinuteOff = setTimerFragment.selectMinuteOff;
                }
            }
        });
        this.dataTime.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                if (SetTimerFragment.this.getContext() != null) {
                    Object value = dataSnapshot.child("Time Select For 1").child("State").getValue();
                    int intValue = value instanceof Number ? ((Number) value).intValue() : 0;
                    Object value2 = dataSnapshot.child("Time Select For 1").child("Enable").getValue();
                    int intValue2 = value2 instanceof Number ? ((Number) value2).intValue() : 0;
                    Object value3 = dataSnapshot.child("Devices").child("Device State 1").getValue();
                    int intValue3 = value3 instanceof Number ? ((Number) value3).intValue() : 0;
                    if (intValue == 1) {
                        if (intValue2 == 1) {
                            if (intValue3 == 0) {
                                if (SetTimerFragment.this.getSelectHourOn > SetTimerFragment.this.modeH && SetTimerFragment.this.getSelectMinuteOn > SetTimerFragment.this.modeM) {
                                    Toast.makeText(SetTimerFragment.this.getContext(), "Someone Intervened", Toast.LENGTH_SHORT).show();
                                } else if (SetTimerFragment.this.getSelectMinuteOn != SetTimerFragment.this.getSelectMinuteOff && SetTimerFragment.this.getSelectHourOn == SetTimerFragment.this.getCountdowH && SetTimerFragment.this.getSelectMinuteOn == SetTimerFragment.this.getCountdowM) {
                                    SetTimerFragment.this.myRef.setValue(1);
                                    SetTimerFragment.this.wait.setText("Device One is ON");
                                    SetTimerFragment.this.dataTime.child("Time Select For 1").child("Enable").setValue(0);
                                } else if (SetTimerFragment.this.getCountdowH == SetTimerFragment.this.getSelectHourOn && SetTimerFragment.this.getCountdowM < SetTimerFragment.this.getSelectMinuteOn) {
                                    SetTimerFragment.this.nextDayH = 0;
                                    SetTimerFragment setTimerFragment = SetTimerFragment.this;
                                    setTimerFragment.nextDayM = setTimerFragment.getSelectMinuteOn - SetTimerFragment.this.getCountdowM;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.getCountdowH == SetTimerFragment.this.getSelectHourOn && SetTimerFragment.this.getCountdowM > SetTimerFragment.this.getSelectMinuteOn) {
                                    SetTimerFragment.this.nextDayH = 23;
                                    SetTimerFragment setTimerFragment2 = SetTimerFragment.this;
                                    setTimerFragment2.nextDayM = (setTimerFragment2.getSelectMinuteOn - SetTimerFragment.this.getCountdowM) + 59;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.getCountdowH < SetTimerFragment.this.getSelectHourOn && SetTimerFragment.this.getCountdowM == SetTimerFragment.this.getSelectMinuteOn) {
                                    SetTimerFragment setTimerFragment3 = SetTimerFragment.this;
                                    setTimerFragment3.nextDayH = setTimerFragment3.getSelectHourOn - SetTimerFragment.this.getCountdowH;
                                    SetTimerFragment.this.nextDayM = 0;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.getCountdowH > SetTimerFragment.this.getSelectHourOn && SetTimerFragment.this.getCountdowM == SetTimerFragment.this.getSelectMinuteOn) {
                                    SetTimerFragment setTimerFragment4 = SetTimerFragment.this;
                                    setTimerFragment4.nextDayH = (setTimerFragment4.getSelectHourOn - SetTimerFragment.this.getCountdowH) + 23;
                                    SetTimerFragment.this.nextDayM = 59;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.getCountdowH > SetTimerFragment.this.getSelectHourOn && SetTimerFragment.this.getCountdowM > SetTimerFragment.this.getSelectMinuteOn) {
                                    SetTimerFragment setTimerFragment5 = SetTimerFragment.this;
                                    setTimerFragment5.nextDayH = (setTimerFragment5.getSelectHourOn - SetTimerFragment.this.getCountdowH) + 23;
                                    SetTimerFragment setTimerFragment6 = SetTimerFragment.this;
                                    setTimerFragment6.nextDayM = (setTimerFragment6.getSelectMinuteOn - SetTimerFragment.this.getCountdowM) + 59;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.getCountdowH > SetTimerFragment.this.getSelectHourOn && SetTimerFragment.this.getCountdowM < SetTimerFragment.this.getSelectMinuteOn) {
                                    SetTimerFragment setTimerFragment7 = SetTimerFragment.this;
                                    setTimerFragment7.nextDayH = (setTimerFragment7.getSelectHourOn - SetTimerFragment.this.getCountdowH) + 24;
                                    SetTimerFragment setTimerFragment8 = SetTimerFragment.this;
                                    setTimerFragment8.nextDayM = setTimerFragment8.getSelectMinuteOn - SetTimerFragment.this.getCountdowM;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.getCountdowH < SetTimerFragment.this.getSelectHourOn && SetTimerFragment.this.getCountdowM < SetTimerFragment.this.getSelectMinuteOn) {
                                    SetTimerFragment setTimerFragment9 = SetTimerFragment.this;
                                    setTimerFragment9.nextDayH = setTimerFragment9.getSelectHourOn - SetTimerFragment.this.getCountdowH;
                                    SetTimerFragment setTimerFragment10 = SetTimerFragment.this;
                                    setTimerFragment10.nextDayM = setTimerFragment10.getSelectMinuteOn - SetTimerFragment.this.getCountdowM;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.getCountdowH < SetTimerFragment.this.getSelectHourOn && SetTimerFragment.this.getCountdowM > SetTimerFragment.this.getSelectMinuteOn) {
                                    SetTimerFragment setTimerFragment11 = SetTimerFragment.this;
                                    setTimerFragment11.nextDayH = (setTimerFragment11.getSelectHourOn - SetTimerFragment.this.getCountdowH) - 1;
                                    SetTimerFragment setTimerFragment12 = SetTimerFragment.this;
                                    setTimerFragment12.nextDayM = (setTimerFragment12.getSelectMinuteOn - SetTimerFragment.this.getCountdowM) + 59;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.selectMinuteOn == SetTimerFragment.this.selectMinuteOff) {
                                    SetTimerFragment.this.wait.setText("Please set the ON and OFF time to a minimum of 1 minute");
                                }
                            } else if (intValue3 != 1) {
                            } else {
                                if (SetTimerFragment.this.getSelectHourOff > SetTimerFragment.this.modeH && SetTimerFragment.this.getSelectMinuteOff > SetTimerFragment.this.modeM) {
                                    Toast.makeText(SetTimerFragment.this.getContext(), "Someone Intervened", android.widget.Toast.LENGTH_SHORT).show();
                                } else if (SetTimerFragment.this.getSelectMinuteOn != SetTimerFragment.this.getSelectMinuteOff && SetTimerFragment.this.getSelectHourOff == SetTimerFragment.this.getCountdowH && SetTimerFragment.this.getSelectMinuteOff == SetTimerFragment.this.getCountdowM) {
                                    SetTimerFragment.this.myRef.setValue(0);
                                    SetTimerFragment.this.wait.setText("Device One is OFF");
                                    SetTimerFragment.this.dataTime.child("Time Select For 1").child("Enable").setValue(0);
                                } else if (SetTimerFragment.this.getCountdowH == SetTimerFragment.this.getSelectHourOff && SetTimerFragment.this.getCountdowM < SetTimerFragment.this.getSelectMinuteOff) {
                                    SetTimerFragment.this.nextDayH = 0;
                                    SetTimerFragment setTimerFragment13 = SetTimerFragment.this;
                                    setTimerFragment13.nextDayM = setTimerFragment13.getSelectMinuteOff - SetTimerFragment.this.getCountdowM;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.getCountdowH == SetTimerFragment.this.getSelectHourOff && SetTimerFragment.this.getCountdowM > SetTimerFragment.this.getSelectMinuteOff) {
                                    SetTimerFragment.this.nextDayH = 23;
                                    SetTimerFragment setTimerFragment14 = SetTimerFragment.this;
                                    setTimerFragment14.nextDayM = (setTimerFragment14.getSelectMinuteOff - SetTimerFragment.this.getCountdowM) + 59;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.getCountdowH < SetTimerFragment.this.getSelectHourOff && SetTimerFragment.this.getCountdowM == SetTimerFragment.this.getSelectMinuteOff) {
                                    SetTimerFragment setTimerFragment15 = SetTimerFragment.this;
                                    setTimerFragment15.nextDayH = setTimerFragment15.getSelectHourOff - SetTimerFragment.this.getCountdowH;
                                    SetTimerFragment.this.nextDayM = 0;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.getCountdowH > SetTimerFragment.this.getSelectHourOff && SetTimerFragment.this.getCountdowM == SetTimerFragment.this.getSelectMinuteOff) {
                                    SetTimerFragment setTimerFragment16 = SetTimerFragment.this;
                                    setTimerFragment16.nextDayH = (setTimerFragment16.getSelectHourOff - SetTimerFragment.this.getCountdowH) + 23;
                                    SetTimerFragment.this.nextDayM = 59;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.getCountdowH > SetTimerFragment.this.getSelectHourOff && SetTimerFragment.this.getCountdowM > SetTimerFragment.this.getSelectMinuteOff) {
                                    SetTimerFragment setTimerFragment17 = SetTimerFragment.this;
                                    setTimerFragment17.nextDayH = (setTimerFragment17.getSelectHourOff - SetTimerFragment.this.getCountdowH) + 23;
                                    SetTimerFragment setTimerFragment18 = SetTimerFragment.this;
                                    setTimerFragment18.nextDayM = (setTimerFragment18.getSelectMinuteOff - SetTimerFragment.this.getCountdowM) + 59;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.getCountdowH > SetTimerFragment.this.getSelectHourOff && SetTimerFragment.this.getCountdowM < SetTimerFragment.this.getSelectMinuteOff) {
                                    SetTimerFragment setTimerFragment19 = SetTimerFragment.this;
                                    setTimerFragment19.nextDayH = (setTimerFragment19.getSelectHourOff - SetTimerFragment.this.getCountdowH) + 24;
                                    SetTimerFragment setTimerFragment20 = SetTimerFragment.this;
                                    setTimerFragment20.nextDayM = setTimerFragment20.getSelectMinuteOff - SetTimerFragment.this.getCountdowM;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.getCountdowH < SetTimerFragment.this.getSelectHourOff && SetTimerFragment.this.getCountdowM < SetTimerFragment.this.getSelectMinuteOff) {
                                    SetTimerFragment setTimerFragment21 = SetTimerFragment.this;
                                    setTimerFragment21.nextDayH = setTimerFragment21.getSelectHourOff - SetTimerFragment.this.getCountdowH;
                                    SetTimerFragment setTimerFragment22 = SetTimerFragment.this;
                                    setTimerFragment22.nextDayM = setTimerFragment22.getSelectMinuteOff - SetTimerFragment.this.getCountdowM;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.getCountdowH < SetTimerFragment.this.getSelectHourOff && SetTimerFragment.this.getCountdowM > SetTimerFragment.this.getSelectMinuteOff) {
                                    SetTimerFragment setTimerFragment23 = SetTimerFragment.this;
                                    setTimerFragment23.nextDayH = (setTimerFragment23.getSelectHourOff - SetTimerFragment.this.getCountdowH) - 1;
                                    SetTimerFragment setTimerFragment24 = SetTimerFragment.this;
                                    setTimerFragment24.nextDayM = (setTimerFragment24.getSelectMinuteOff - SetTimerFragment.this.getCountdowM) + 59;
                                    SetTimerFragment.this.wait.setText(String.format("Please wait for device 1 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH), Integer.valueOf(SetTimerFragment.this.nextDayM)}));
                                } else if (SetTimerFragment.this.selectMinuteOn == SetTimerFragment.this.selectMinuteOff) {
                                    SetTimerFragment.this.wait.setText("Please set the ON and OFF time to a minimum of 1 minute");
                                }
                            }
                        } else if (intValue2 == 0) {
                            SetTimerFragment.this.wait.setText("Set the timer for device 1 one to turn ON. Please click Select Time to set the device turn-on time");
                        }
                    } else if (intValue == 0) {
                        SetTimerFragment.this.wait.setText("Set the timer for device 1 one to turn OFF. Please press the button below before turning ON the device timer");
                    }
                }
            }
        });
        this.myRef.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                SetTimerFragment.this.btnTimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDataChange(dataSnapshot, v);
                    }
                });
                if (dataSnapshot.getValue().toString().equals("1")) {
                    SetTimerFragment.this.viewTimer.setText("Device state 1: ON");
                } else if (dataSnapshot.getValue().toString().equals("0")) {
                    SetTimerFragment.this.viewTimer.setText("Device state 1: OFF");
                }
            }

            public void onDataChange(DataSnapshot dataSnapshot5, View view) {
                if (dataSnapshot5.getValue().toString().equals("1")) {
                    SetTimerFragment.this.showTimeOffApp();
                } else if (dataSnapshot5.getValue().toString().equals("0")) {
                    SetTimerFragment.this.showTimeOnApp();
                }
                if (SetTimerFragment.this.btnTimer.isClickable()) {
                    SetTimerFragment.this.currentDataTime.child("Time Select For 1").child("Enable").setValue(1);
                } else {
                    SetTimerFragment.this.currentDataTime.child("Time Select For 1").child("Enable").setValue(0);
                }
            }
        });



        /// time set of device 2
        this.btnCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateView1(v);
            }
        });
        this.currentDataTime1.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.child("Time Select For 2").child("Hour On").getValue();
                Object value2 = dataSnapshot.child("Time Select For 2").child("Minute On").getValue();
                Object value3 = dataSnapshot.child("Time Select For 2").child("Hour Off").getValue();
                Object value4 = dataSnapshot.child("Time Select For 2").child("Minute Off").getValue();
                Object value5 = dataSnapshot.child("Time Select For 2").child("State").getValue();
                Object value6 = dataSnapshot.child("Devices").child("Device State 2").getValue();
                if (value instanceof Number) {
                    SetTimerFragment.this.Hour1On = ((Number) value).intValue();
                }
                if (value2 instanceof Number) {
                    SetTimerFragment.this.Minute1On = ((Number) value2).intValue();
                }
                if (value3 instanceof Number) {
                    SetTimerFragment.this.Hour1Off = ((Number) value3).intValue();
                }
                if (value4 instanceof Number) {
                    SetTimerFragment.this.Minute1Off = ((Number) value4).intValue();
                }
                if (value5 instanceof Number) {
                    SetTimerFragment.this.state1 = ((Number) value5).intValue();
                }
                if (value6 instanceof Number) {
                    SetTimerFragment.this.val1 = ((Number) value6).intValue();
                }
                if (SetTimerFragment.this.val1 == 0) {
                    if (SetTimerFragment.this.state1 == 1) {
                        SetTimerFragment.this.time1.setText(String.format("Hour: %02d  Minute: %02d", new Object[]{Integer.valueOf(SetTimerFragment.this.Hour1On), Integer.valueOf(SetTimerFragment.this.Minute1On)}));
                        SetTimerFragment.this.btnCancel1.setChecked(true);
                    } else if (SetTimerFragment.this.state1 == 0) {
                        SetTimerFragment.this.time1.setText("Hour: 00  Minute: 00");
                        SetTimerFragment.this.btnCancel1.setChecked(false);
                    }
                } else if (SetTimerFragment.this.val1 != 1) {
                } else {
                    if (SetTimerFragment.this.state1 == 1) {
                        SetTimerFragment.this.time1.setText(String.format("Hour: %02d  Minute: %02d", new Object[]{Integer.valueOf(SetTimerFragment.this.Hour1Off), Integer.valueOf(SetTimerFragment.this.Minute1Off)}));
                        SetTimerFragment.this.btnCancel1.setChecked(true);
                    } else if (SetTimerFragment.this.state1 == 0) {
                        SetTimerFragment.this.time1.setText("Hour: 00  Minute: 00");
                        SetTimerFragment.this.btnCancel1.setChecked(false);
                    }
                }
            }
        });
        this.myTimeHour1On.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                if (value instanceof Number) {
                    SetTimerFragment.this.selectHour1On = ((Number) value).intValue();
                }
                if (SetTimerFragment.this.selectHour1On > SetTimerFragment.this.modeH) {
                    Toast.makeText(SetTimerFragment.this.getContext(), "Someone Intervened", Toast.LENGTH_SHORT).show();
                } else if (SetTimerFragment.this.selectHour1On >= 0 && SetTimerFragment.this.selectHour1On <= 23) {
                    SetTimerFragment setTimerFragment = SetTimerFragment.this;
                    setTimerFragment.getSelectHour1On = setTimerFragment.selectHour1On;
                }
            }
        });
        this.myTimeMinute1On.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                if (value instanceof Number) {
                    SetTimerFragment.this.selectMinute1On = ((Number) value).intValue();
                }
                if (SetTimerFragment.this.selectMinute1On > SetTimerFragment.this.modeM) {
                    Toast.makeText(SetTimerFragment.this.getContext(), "Someone Intervened", android.widget.Toast.LENGTH_SHORT).show();
                } else if (SetTimerFragment.this.selectMinute1On >= 0 && SetTimerFragment.this.selectMinute1On <= 59) {
                    SetTimerFragment setTimerFragment = SetTimerFragment.this;
                    setTimerFragment.getSelectMinute1On = setTimerFragment.selectMinute1On;
                }
            }
        });
        this.myTimeHour1Off.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                if (value instanceof Number) {
                    SetTimerFragment.this.selectHour1Off = ((Number) value).intValue();
                }
                if (SetTimerFragment.this.selectHour1Off > SetTimerFragment.this.modeH) {
                    Toast.makeText(SetTimerFragment.this.getContext(), "Someone Intervened", Toast.LENGTH_SHORT).show();
                } else if (SetTimerFragment.this.selectHour1Off >= 0 && SetTimerFragment.this.selectHour1Off <= 23) {
                    SetTimerFragment setTimerFragment = SetTimerFragment.this;
                    setTimerFragment.getSelectHour1Off = setTimerFragment.selectHour1Off;
                }
            }
        });
        this.myTimeMinute1Off.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                if (value instanceof Number) {
                    SetTimerFragment.this.selectMinute1Off = ((Number) value).intValue();
                }
                if (SetTimerFragment.this.selectMinute1Off > SetTimerFragment.this.modeM) {
                    Toast.makeText(SetTimerFragment.this.getContext(), "Someone Intervened", Toast.LENGTH_SHORT).show();
                } else if (SetTimerFragment.this.selectMinute1Off >= 0 && SetTimerFragment.this.selectMinute1Off <= 59) {
                    SetTimerFragment setTimerFragment = SetTimerFragment.this;
                    setTimerFragment.getSelectMinute1Off = setTimerFragment.selectMinute1Off;
                }
            }
        });
        this.dataTime1.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                if (SetTimerFragment.this.getContext() != null) {
                    Object value = dataSnapshot.child("Time Select For 2").child("State").getValue();
                    int intValue = value instanceof Number ? ((Number) value).intValue() : 0;
                    Object value2 = dataSnapshot.child("Time Select For 2").child("Enable").getValue();
                    int intValue2 = value2 instanceof Number ? ((Number) value2).intValue() : 0;
                    Object value3 = dataSnapshot.child("Devices").child("Device State 2").getValue();
                    int intValue3 = value3 instanceof Number ? ((Number) value3).intValue() : 0;
                    if (intValue == 1) {
                        if (intValue2 == 1) {
                            if (intValue3 == 0) {
                                if (SetTimerFragment.this.getSelectHour1On > SetTimerFragment.this.modeH && SetTimerFragment.this.getSelectMinute1On > SetTimerFragment.this.modeM) {
                                    Toast.makeText(SetTimerFragment.this.getContext(), "Someone Intervened", Toast.LENGTH_SHORT).show();
                                } else if (SetTimerFragment.this.getSelectMinute1On != SetTimerFragment.this.getSelectMinute1Off && SetTimerFragment.this.getSelectHour1On == SetTimerFragment.this.getCountdowH && SetTimerFragment.this.getSelectMinute1On == SetTimerFragment.this.getCountdowM) {
                                    SetTimerFragment.this.myRef_1.setValue(1);
                                    SetTimerFragment.this.wait1.setText("Device Two is ON");
                                    SetTimerFragment.this.dataTime.child("Time Select For 2").child("Enable").setValue(0);
                                } else if (SetTimerFragment.this.getCountdowH == SetTimerFragment.this.getSelectHour1On && SetTimerFragment.this.getCountdowM < SetTimerFragment.this.getSelectMinute1On) {
                                    SetTimerFragment.this.nextDayH1 = 0;
                                    SetTimerFragment setTimerFragment = SetTimerFragment.this;
                                    setTimerFragment.nextDayM1 = setTimerFragment.getSelectMinute1On - SetTimerFragment.this.getCountdowM;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.getCountdowH == SetTimerFragment.this.getSelectHour1On && SetTimerFragment.this.getCountdowM > SetTimerFragment.this.getSelectMinute1On) {
                                    SetTimerFragment.this.nextDayH1 = 23;
                                    SetTimerFragment setTimerFragment2 = SetTimerFragment.this;
                                    setTimerFragment2.nextDayM1 = (setTimerFragment2.getSelectMinute1On - SetTimerFragment.this.getCountdowM) + 59;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.getCountdowH < SetTimerFragment.this.getSelectHour1On && SetTimerFragment.this.getCountdowM == SetTimerFragment.this.getSelectMinute1On) {
                                    SetTimerFragment setTimerFragment3 = SetTimerFragment.this;
                                    setTimerFragment3.nextDayH1 = setTimerFragment3.getSelectHour1On - SetTimerFragment.this.getCountdowH;
                                    SetTimerFragment.this.nextDayM1 = 0;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.getCountdowH > SetTimerFragment.this.getSelectHour1On && SetTimerFragment.this.getCountdowM == SetTimerFragment.this.getSelectMinute1On) {
                                    SetTimerFragment setTimerFragment4 = SetTimerFragment.this;
                                    setTimerFragment4.nextDayH1 = (setTimerFragment4.getSelectHour1On - SetTimerFragment.this.getCountdowH) + 23;
                                    SetTimerFragment.this.nextDayM1 = 59;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.getCountdowH > SetTimerFragment.this.getSelectHour1On && SetTimerFragment.this.getCountdowM > SetTimerFragment.this.getSelectMinute1On) {
                                    SetTimerFragment setTimerFragment5 = SetTimerFragment.this;
                                    setTimerFragment5.nextDayH1 = (setTimerFragment5.getSelectHour1On - SetTimerFragment.this.getCountdowH) + 23;
                                    SetTimerFragment setTimerFragment6 = SetTimerFragment.this;
                                    setTimerFragment6.nextDayM1 = (setTimerFragment6.getSelectMinute1On - SetTimerFragment.this.getCountdowM) + 59;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.getCountdowH > SetTimerFragment.this.getSelectHour1On && SetTimerFragment.this.getCountdowM < SetTimerFragment.this.getSelectMinute1On) {
                                    SetTimerFragment setTimerFragment7 = SetTimerFragment.this;
                                    setTimerFragment7.nextDayH1 = (setTimerFragment7.getSelectHour1On - SetTimerFragment.this.getCountdowH) + 24;
                                    SetTimerFragment setTimerFragment8 = SetTimerFragment.this;
                                    setTimerFragment8.nextDayM1 = setTimerFragment8.getSelectMinute1On - SetTimerFragment.this.getCountdowM;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.getCountdowH < SetTimerFragment.this.getSelectHour1On && SetTimerFragment.this.getCountdowM < SetTimerFragment.this.getSelectMinute1On) {
                                    SetTimerFragment setTimerFragment9 = SetTimerFragment.this;
                                    setTimerFragment9.nextDayH1 = setTimerFragment9.getSelectHour1On - SetTimerFragment.this.getCountdowH;
                                    SetTimerFragment setTimerFragment10 = SetTimerFragment.this;
                                    setTimerFragment10.nextDayM1 = setTimerFragment10.getSelectMinute1On - SetTimerFragment.this.getCountdowM;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.getCountdowH < SetTimerFragment.this.getSelectHour1On && SetTimerFragment.this.getCountdowM > SetTimerFragment.this.getSelectMinute1On) {
                                    SetTimerFragment setTimerFragment11 = SetTimerFragment.this;
                                    setTimerFragment11.nextDayH1 = (setTimerFragment11.getSelectHour1On - SetTimerFragment.this.getCountdowH) - 1;
                                    SetTimerFragment setTimerFragment12 = SetTimerFragment.this;
                                    setTimerFragment12.nextDayM1 = (setTimerFragment12.getSelectMinute1On - SetTimerFragment.this.getCountdowM) + 59;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.selectMinute1On == SetTimerFragment.this.selectMinute1Off) {
                                    SetTimerFragment.this.wait1.setText("Please set the ON and OFF time to a minimum of 1 minute");
                                }
                            } else if (intValue3 != 1) {
                            } else {
                                if (SetTimerFragment.this.getSelectHour1Off > SetTimerFragment.this.modeH && SetTimerFragment.this.getSelectMinute1Off > SetTimerFragment.this.modeM) {
                                    Toast.makeText(SetTimerFragment.this.getContext(), "Someone Intervened", Toast.LENGTH_SHORT).show();
                                } else if (SetTimerFragment.this.getSelectMinute1On != SetTimerFragment.this.getSelectMinute1Off && SetTimerFragment.this.getSelectHour1Off == SetTimerFragment.this.getCountdowH && SetTimerFragment.this.getSelectMinute1Off == SetTimerFragment.this.getCountdowM) {
                                    SetTimerFragment.this.myRef_1.setValue(0);
                                    SetTimerFragment.this.wait1.setText("Device Two is OFF");
                                    SetTimerFragment.this.dataTime.child("Time Select For 2").child("Enable").setValue(0);
                                } else if (SetTimerFragment.this.getCountdowH == SetTimerFragment.this.getSelectHour1Off && SetTimerFragment.this.getCountdowM < SetTimerFragment.this.getSelectMinute1Off) {
                                    SetTimerFragment.this.nextDayH1 = 0;
                                    SetTimerFragment setTimerFragment13 = SetTimerFragment.this;
                                    setTimerFragment13.nextDayM1 = setTimerFragment13.getSelectMinute1Off - SetTimerFragment.this.getCountdowM;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.getCountdowH == SetTimerFragment.this.getSelectHour1Off && SetTimerFragment.this.getCountdowM > SetTimerFragment.this.getSelectMinute1Off) {
                                    SetTimerFragment.this.nextDayH1 = 23;
                                    SetTimerFragment setTimerFragment14 = SetTimerFragment.this;
                                    setTimerFragment14.nextDayM1 = (setTimerFragment14.getSelectMinute1Off - SetTimerFragment.this.getCountdowM) + 59;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.getCountdowH < SetTimerFragment.this.getSelectHour1Off && SetTimerFragment.this.getCountdowM == SetTimerFragment.this.getSelectMinute1Off) {
                                    SetTimerFragment setTimerFragment15 = SetTimerFragment.this;
                                    setTimerFragment15.nextDayH1 = setTimerFragment15.getSelectHour1On - SetTimerFragment.this.getCountdowH;
                                    SetTimerFragment.this.nextDayM1 = 0;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.getCountdowH > SetTimerFragment.this.getSelectHour1Off && SetTimerFragment.this.getCountdowM == SetTimerFragment.this.getSelectMinute1Off) {
                                    SetTimerFragment setTimerFragment16 = SetTimerFragment.this;
                                    setTimerFragment16.nextDayH1 = (setTimerFragment16.getSelectHour1Off - SetTimerFragment.this.getCountdowH) + 23;
                                    SetTimerFragment.this.nextDayM1 = 59;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.getCountdowH > SetTimerFragment.this.getSelectHour1Off && SetTimerFragment.this.getCountdowM > SetTimerFragment.this.getSelectMinute1Off) {
                                    SetTimerFragment setTimerFragment17 = SetTimerFragment.this;
                                    setTimerFragment17.nextDayH1 = (setTimerFragment17.getSelectHour1Off - SetTimerFragment.this.getCountdowH) + 23;
                                    SetTimerFragment setTimerFragment18 = SetTimerFragment.this;
                                    setTimerFragment18.nextDayM1 = (setTimerFragment18.getSelectMinute1Off - SetTimerFragment.this.getCountdowM) + 59;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.getCountdowH > SetTimerFragment.this.getSelectHour1Off && SetTimerFragment.this.getCountdowM < SetTimerFragment.this.getSelectMinute1Off) {
                                    SetTimerFragment setTimerFragment19 = SetTimerFragment.this;
                                    setTimerFragment19.nextDayH1 = (setTimerFragment19.getSelectHour1Off - SetTimerFragment.this.getCountdowH) + 24;
                                    SetTimerFragment setTimerFragment20 = SetTimerFragment.this;
                                    setTimerFragment20.nextDayM1 = setTimerFragment20.getSelectMinute1Off - SetTimerFragment.this.getCountdowM;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.getCountdowH < SetTimerFragment.this.getSelectHour1Off && SetTimerFragment.this.getCountdowM < SetTimerFragment.this.getSelectMinute1Off) {
                                    SetTimerFragment setTimerFragment21 = SetTimerFragment.this;
                                    setTimerFragment21.nextDayH1 = setTimerFragment21.getSelectHour1Off - SetTimerFragment.this.getCountdowH;
                                    SetTimerFragment setTimerFragment22 = SetTimerFragment.this;
                                    setTimerFragment22.nextDayM1 = setTimerFragment22.getSelectMinute1Off - SetTimerFragment.this.getCountdowM;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.getCountdowH < SetTimerFragment.this.getSelectHour1Off && SetTimerFragment.this.getCountdowM > SetTimerFragment.this.getSelectMinute1Off) {
                                    SetTimerFragment setTimerFragment23 = SetTimerFragment.this;
                                    setTimerFragment23.nextDayH1 = (setTimerFragment23.getSelectHour1Off - SetTimerFragment.this.getCountdowH) - 1;
                                    SetTimerFragment setTimerFragment24 = SetTimerFragment.this;
                                    setTimerFragment24.nextDayM1 = (setTimerFragment24.getSelectMinute1Off - SetTimerFragment.this.getCountdowM) + 59;
                                    SetTimerFragment.this.wait1.setText(String.format("Please wait for device 2 until %02d hour %02d minute", new Object[]{Integer.valueOf(SetTimerFragment.this.nextDayH1), Integer.valueOf(SetTimerFragment.this.nextDayM1)}));
                                } else if (SetTimerFragment.this.selectMinute1On == SetTimerFragment.this.selectMinute1Off) {
                                    SetTimerFragment.this.wait1.setText("Please set the ON and OFF time to a minimum of 1 minute");
                                }
                            }
                        } else if (intValue2 == 0) {
                            SetTimerFragment.this.wait1.setText("Set the timer for device 2 one to turn ON. Please click Select Time to set the device turn-on time");
                        }
                    } else if (intValue == 0) {
                        SetTimerFragment.this.wait1.setText("Set the timer for device 2 one to turn OFF. Please press the button below before turning ON the device timer");
                    }
                }
            }
        });
        this.myRef_1.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
            }

            public void onDataChange(DataSnapshot dataSnapshot1) {
                SetTimerFragment.this.btnTimer1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onDataChange1(dataSnapshot1, view);
                    }
                });
                if (dataSnapshot1.getValue().toString().equals("1")) {
                    SetTimerFragment.this.viewTimer1.setText("Device state 2: ON");
                } else if (dataSnapshot1.getValue().toString().equals("0")) {
                    SetTimerFragment.this.viewTimer1.setText("Device state 2: OFF");
                }
            }

            public void onDataChange1(DataSnapshot dataSnapshot, View view) {
                if (dataSnapshot.getValue().toString().equals("1")) {
                    SetTimerFragment.this.showTime1OffApp();
                } else if (dataSnapshot.getValue().toString().equals("0")) {
                    SetTimerFragment.this.showTime1OnApp();
                }
                if (SetTimerFragment.this.btnTimer1.isClickable()) {
                    SetTimerFragment.this.currentDataTime1.child("Time Select For 2").child("Enable").setValue(1);
                } else {
                    SetTimerFragment.this.currentDataTime1.child("Time Select For 2").child("Enable").setValue(0);
                }
            }
        });
        return inflate;
    }
    //device 1
    public void onCreateView(View view) {
        if (this.btnCancel.isChecked()) {
            this.myTimeOnHour.setValue(0);
            this.myTimeOnMinute.setValue(0);
            this.myTimeOffHour.setValue(0);
            this.myTimeOffMinute.setValue(0);
            this.currentDataTime.child("Time Select For 1").child("State").setValue(1);
        } else {
            this.myTimeOnHour.setValue(0);
            this.myTimeOnMinute.setValue(0);
            this.myTimeOffHour.setValue(0);
            this.myTimeOffMinute.setValue(0);
            this.currentDataTime.child("Time Select For 1").child("State").setValue(0);
            this.currentDataTime.child("Time Select For 1").child("Enable").setValue(0);
        }
    }
    public void showTimeOnApp() {
        TimePickerDialog timePickerDialog2;
        timePickerDialog2 = new TimePickerDialog(
                getContext(), // Context của fragment
                2, // Chế độ 24 giờ (2 là giá trị cho chế độ 24 giờ)
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        showTimeOnData(view, hourOfDay, minute);
                    }
                }, this.Hour, this.Minute, true);
        this.timePickerDialog = timePickerDialog2;
        timePickerDialog2.setTitle("Select Time On");
        this.timePickerDialog.show();
    }
    public void showTimeOnData(TimePicker timePicker, int i, int i2) {
        this.Hour = i;
        this.Minute = i2;
        this.time.setText(String.format("Hour: %02d  Minute: %02d", i, i2));
        this.myTimeOnHour.setValue(i);
        this.myTimeOnMinute.setValue(i2);
    }
    public void showTimeOffApp() {
        TimePickerDialog timePickerDialog2;
        timePickerDialog2 = new TimePickerDialog(
                getContext(), // Context của fragment
                2, // Chế độ 24 giờ (2 là giá trị cho chế độ 24 giờ)
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        showTimeOffData(view, hourOfDay, minute);
                    }
                }, this.Hour, this.Minute, true);
        this.timePickerDialog = timePickerDialog2;
        timePickerDialog2.setTitle("Select Time Off");
        this.timePickerDialog.show();
    }
    public void showTimeOffData(TimePicker timePicker, int i, int i2) {
        this.Hour = i;
        this.Minute = i2;
        this.time.setText(String.format("Hour: %02d  Minute: %02d", i, i2));
        this.myTimeOffHour.setValue(i);
        this.myTimeOffMinute.setValue(i2);
    }


    //device 2
    public void onCreateView1(View view) {
        if (this.btnCancel1.isChecked()) {
            this.myTimeHour1On.setValue(0);
            this.myTimeMinute1On.setValue(0);
            this.myTimeHour1Off.setValue(0);
            this.myTimeMinute1Off.setValue(0);
            this.currentDataTime1.child("Time Select For 2").child("State").setValue(1);
            return;
        } else {
        this.myTimeHour1On.setValue(0);
        this.myTimeMinute1On.setValue(0);
        this.myTimeHour1Off.setValue(0);
        this.myTimeMinute1Off.setValue(0);
        this.currentDataTime1.child("Time Select For 2").child("State").setValue(0);
        this.currentDataTime1.child("Time Select For 2").child("Enable").setValue(0);
        }
    }
    public void showTime1OnApp() {
        TimePickerDialog timePickerDialog3;
        timePickerDialog3 = new TimePickerDialog(
                getContext(), // Context của fragment
                2, // Chế độ 24 giờ (2 là giá trị cho chế độ 24 giờ)
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        showTime1OnData(view, hourOfDay, minute);
                    }
                }, this.Hour1, this.Minute1, true);
        this.timePickerDialog1 = timePickerDialog3;
        timePickerDialog3.setTitle("Select Time On");
        this.timePickerDialog1.show();
    }
    public void showTime1OnData(TimePicker timePicker, int i3, int i4) {
        this.Hour1 = i3;
        this.Minute1 = i4;
        this.time1.setText(String.format("Hour: %02d  Minute: %02d", i3, i4));
        this.myTimeHour1On.setValue(i3);
        this.myTimeMinute1On.setValue(i4);
    }
    public void showTime1OffApp() {
        TimePickerDialog timePickerDialog4;
        timePickerDialog4 = new TimePickerDialog(
                getContext(), // Context của fragment
                2, // Chế độ 24 giờ (2 là giá trị cho chế độ 24 giờ)
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        showTime1OffData(view, hourOfDay, minute);
                    }
                }, this.Hour1, this.Minute1, true);
        this.timePickerDialog1 = timePickerDialog4;
        timePickerDialog4.setTitle("Select Time Off");
        this.timePickerDialog1.show();
    }
    public void showTime1OffData(TimePicker timePicker, int i5, int i6) {
        this.Hour1 = i5;
        this.Minute1 = i6;
        this.time1.setText(String.format("Hour: %02d  Minute: %02d", i5, i6));
        this.myTimeHour1Off.setValue(i5);
        this.myTimeMinute1Off.setValue(i6);
    }
}

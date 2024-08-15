package com.example.deviceoversight.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.deviceoversight.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private final int REQ_CODE = 100;
    FirebaseDatabase database;
    DatabaseReference myRef;
    /* access modifiers changed from: private */
    public TextToSpeech myTTS;
    int save;
    int save1;
    int save2;
    int save21;
    int save3;
    int save31;
    TextView viewDevice;
    TextView viewDevice1;
    TextView viewTime;
    TextView viewTime1;
    View voice;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_home, viewGroup, false);
        this.viewTime = inflate.findViewById(R.id.view_time);
        this.viewTime1 = inflate.findViewById(R.id.view_time1);
        this.viewDevice = inflate.findViewById(R.id.view_device);
        this.viewDevice1 = inflate.findViewById(R.id.view_device1);
        this.voice = inflate.findViewById(R.id.btn_voice);
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        this.database = instance;
        DatabaseReference reference = instance.getReference("");
        this.myRef = reference;
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi khi đọc dữ liệu từ Firebase
                Log.e("HomeFragment", "Failed to read value.", databaseError.toException());
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Cập nhật các giá trị từ DataSnapshot
                updateValues(dataSnapshot);
                // Cập nhật giao diện người dùng dựa trên các giá trị đã lấy
                updateUI();
            }
            private void updateValues(DataSnapshot dataSnapshot) {
                // Lấy giá trị từ các nút và chuyển đổi thành kiểu int
                save = getIntValue(dataSnapshot.child("Devices/Device State 1"));
                save1 = getIntValue(dataSnapshot.child("Devices/Device State 2"));
                save2 = getIntValue(dataSnapshot.child("Time Select For 1/Enable"));
                save21 = getIntValue(dataSnapshot.child("Time Select For 1/State"));
                save3 = getIntValue(dataSnapshot.child("Time Select For 2/Enable"));
                save31 = getIntValue(dataSnapshot.child("Time Select For 2/State"));
            }
            private int getIntValue(DataSnapshot snapshot) {
                // Lấy giá trị từ DataSnapshot và chuyển đổi thành int
                Object value = snapshot.getValue();
                return (value instanceof Number) ? ((Number) value).intValue() : 0;
            }
            private void updateUI() {
                // Cập nhật giao diện người dùng dựa trên các giá trị đã lấy
                viewDevice.setText(getDeviceStatusText(save, 1));
                viewDevice1.setText(getDeviceStatusText(save1, 2));
                viewTime.setText(getTimerStatusText(save21, save2, 1));
                viewTime1.setText(getTimerStatusText(save31, save3, 2));
            }
            private String getDeviceStatusText(int status, int deviceNumber) {
                // Tạo chuỗi trạng thái cho thiết bị
                return String.format("Device %d: %s", deviceNumber, status == 1 ? "ON" : "OFF");
            }
            private String getTimerStatusText(int state, int enable, int deviceNumber) {
                // Tạo chuỗi trạng thái cho bộ hẹn giờ
                if (state == 1) {
                    return enable == 1 ? String.format("Device %d timer: ON", deviceNumber) : String.format("Device %d timer: OFF", deviceNumber);
                } else {
                    return String.format("Device %d timer: OFF", deviceNumber);
                }
            }
        });

        this.voice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (HomeFragment.this.voice.isClickable()) {
                    HomeFragment.this.startSpeechRecognition();
                } else {
                    HomeFragment.this.speak("Voice recognition is now disabled. Say 'enable' to turn it back on.");
                }
            }
        });
        initialTextToSpeech();
        return inflate;
    }


    public void startSpeechRecognition() {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        intent.putExtra("android.speech.extra.LANGUAGE", Locale.getDefault());
        intent.putExtra("android.speech.extra.PROMPT", "Need To Speak");
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(getContext(), "Sorry, Your Device not Supported",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void initialTextToSpeech() {
        this.myTTS = new TextToSpeech(getContext().getApplicationContext(), new TextToSpeech.OnInitListener() {
            public void onInit(int i) {
                if (i == 0 && !HomeFragment.this.myTTS.getLanguage().equals(Locale.ENGLISH)) {
                    HomeFragment.this.myTTS.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Kiểm tra mã yêu cầu và mã kết quả
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            // Truy xuất danh sách chuỗi từ Intent
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                // Lấy chuỗi đầu tiên từ danh sách kết quả
                String command = results.get(0);
                // Gọi phương thức với chuỗi đầu tiên trong danh sách
                processVoiceCommand(command);
            } else {
                // Xử lý trường hợp không có kết quả
                Log.e("VoiceCommand", "No voice results received or results list is empty");
            }
        } else {
            // Xử lý trường hợp requestCode, resultCode không hợp lệ hoặc data là null
            Log.e("VoiceCommand", "Invalid requestCode, resultCode, or null data");
        }
    }

    private void processVoiceCommand(String command) {
        // Chuyển chuỗi lệnh thành chữ thường để so sánh dễ dàng hơn
        String commandLower = command.toLowerCase();

        // Xử lý các lệnh theo chuỗi lệnh nhận được
        if (commandLower.contains("device one turn on") ||
                commandLower.contains("bật thiết bị một") ||
                commandLower.contains("bật thiết bị 1")) {
            this.myRef.child("Devices").child("Device State 1").setValue(1);
            speak("Device one is on");
        } else if (commandLower.contains("device one turn off") ||
                commandLower.contains("tắt thiết bị một") ||
                commandLower.contains("tắt thiết bị 1")) {
            this.myRef.child("Devices").child("Device State 1").setValue(0);
            speak("Device one is off");
        } else if (commandLower.contains("device two turn on") ||
                commandLower.contains("bật thiết bị hai") ||
                commandLower.contains("bật thiết bị 2")) {
            this.myRef.child("Devices").child("Device State 2").setValue(1);
            speak("Device two is on");
        } else if (commandLower.contains("device two turn off") ||
                commandLower.contains("tắt thiết bị hai") ||
                commandLower.contains("tắt thiết bị 2")) {
            this.myRef.child("Devices").child("Device State 2").setValue(0);
            speak("Device two is off");
        } else if (commandLower.contains("all devices on") ||
                commandLower.contains("bật tất cả thiết bị")) {
            this.myRef.child("Devices").child("Device State 1").setValue(1);
            this.myRef.child("Devices").child("Device State 2").setValue(1);
            speak("All devices are turned on");
        } else if (commandLower.contains("all devices off") ||
                commandLower.contains("tắt tất cả thiết bị")) {
            this.myRef.child("Devices").child("Device State 1").setValue(0);
            this.myRef.child("Devices").child("Device State 2").setValue(0);
            speak("All devices are turned off");
        } else {
            // Xử lý trường hợp lệnh không nhận diện được
            speak("I did not understand the command");
        }
    }


    /* access modifiers changed from: private */
    public void speak(String str) {
        if (myTTS != null) {
            // Điều chỉnh tốc độ nói
            myTTS.setSpeechRate(0.8f);
            // Gọi phương thức speak
            myTTS.speak(str, 0, (HashMap) null);
        }
    }
}

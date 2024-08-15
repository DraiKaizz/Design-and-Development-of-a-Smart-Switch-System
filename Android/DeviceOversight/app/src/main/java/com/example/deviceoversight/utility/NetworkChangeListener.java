package com.example.deviceoversight.utility;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatButton;
import com.example.deviceoversight.R;

public class NetworkChangeListener extends BroadcastReceiver {
    public void onReceive(final Context context, final Intent intent) {
        if (!Common.isConnectedToInternet(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View inflate = LayoutInflater.from(context).inflate(R.layout.check_internet_dialog, (ViewGroup) null);
            builder.setView(inflate);
            final AlertDialog create = builder.create();
            create.show();
            create.setCancelable(false);
            create.getWindow().setGravity(17);
            ((android.widget.Button) inflate.findViewById(R.id.btn_retry)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    create.dismiss();
                    NetworkChangeListener.this.onReceive(context, intent);
                }
            });
        }
    }
}


package com.young.paint;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class YoungToast {
    public static void showTextToast(Context context,String msg){
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_young,null);
        TextView tvToast = view.findViewById(R.id.tv_toast);
        tvToast.setText(msg);
        toast.setGravity(Gravity.BOTTOM,0,150);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }
}

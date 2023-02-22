package com.example.listerado;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class ToastManager {

    private static boolean isToastShowing = false; // Variable, um den Status des Toasts zu verfolgen

    private static Toast mToast;

    public static void showToast(Context context, String message, int duration) {



            if (mToast != null) mToast.cancel();
            mToast = Toast.makeText(context, message, duration);
            mToast.show();

    }
}
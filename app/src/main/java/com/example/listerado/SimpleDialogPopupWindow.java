package com.example.listerado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

public class SimpleDialogPopupWindow {

    Context context;
    String newArray;


    public SimpleDialogPopupWindow(Context context) {
        this.context = context;
    }


    public String createSimpleDialogPopup() {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.template_simple_dialog_popup_window, null);
        builder.setView(view);

        EditText editText = view.findViewById(R.id.simplePopup_editTextField);

        builder.setPositiveButton("Senden", (dialogInterface, i) -> {
            newArray = editText.getText().toString();
        });

        builder.setNegativeButton("Abbrechen", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        return newArray;
    }
}

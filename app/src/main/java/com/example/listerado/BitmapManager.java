package com.example.listerado;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapManager {




    // Bitmap in eine Datei speichern
    public void saveBitmapToFile(Bitmap bitmap, File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methode zum Lesen der Bitmap-Datei
    public Bitmap readBitmapFromFile(File file) {
        Bitmap bitmap = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}

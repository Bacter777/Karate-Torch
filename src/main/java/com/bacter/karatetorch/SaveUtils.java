package com.bacter.karatetorch;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Vibrator;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

@SuppressWarnings("ALL")
public class SaveUtils
{
    public boolean saveSettings(String command,Context context)
    {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        CameraManager camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        String cameraId = null;
        try
        {
            if (camManager != null)
            {
                cameraId = camManager.getCameraIdList()[0];
            }
            if (camManager != null)
            {
                camManager.setTorchMode(cameraId,true);
                vibrator.vibrate(500);
                return true;
            }
            else {
                camManager.setTorchMode(cameraId,false);
                vibrator.vibrate(500);
                return false;
            }
        }catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean saveFile(Context context, String fileName, String data) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            Writer out = new OutputStreamWriter(fos);
            out.write(data);
            out.close();
            Log.i("saveFile", "File saved successfully");
            return true;
        } catch (IOException ioEx) {
            Log.e("saveFile", ioEx.getMessage());
            return false;
        }
    }
    public static String loadFile(Context context, String fileName)
    {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line = bufferedReader.readLine();
            bufferedReader.close();
            Log.i("loadFile", "File read successfully");
            return line;
        } catch (IOException ioEx) {
            Log.e("loadFile", ioEx.getMessage());
            return null;
        }
    }
}

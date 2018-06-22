package org.doug.monitor.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by wesine on 2018/6/22.
 */

public class CameraTask implements Runnable {


    private byte[] bytes;
    private CameraCallback callback;

    public CameraTask(byte[] bytes, CameraCallback callback) {
        this.callback = callback;
        this.bytes = bytes;
    }

    @Override
    public void run() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if (bitmap != null) {
            callback.callback(bitmap);
        }
    }

    interface CameraCallback {
        void callback(Bitmap bitmap);
    }
}

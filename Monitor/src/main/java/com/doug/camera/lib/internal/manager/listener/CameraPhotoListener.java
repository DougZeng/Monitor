package com.doug.camera.lib.internal.manager.listener;

import com.doug.camera.lib.listeners.CameraFragmentResultListener;

import java.io.File;

/*
 *
 */
public interface CameraPhotoListener {
    void onPhotoTaken(byte[] bytes, File photoFile, CameraFragmentResultListener callback);

    void onPhotoTakeError();
}

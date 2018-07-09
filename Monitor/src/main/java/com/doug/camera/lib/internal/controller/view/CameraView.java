package com.doug.camera.lib.internal.controller.view;

import android.support.annotation.Nullable;
import android.view.View;

import com.doug.camera.lib.configuration.Configuration;
import com.doug.camera.lib.internal.utils.Size;
import com.doug.camera.lib.listeners.CameraFragmentResultListener;

/*
 *
 */
public interface CameraView {

    void updateCameraPreview(Size size, View cameraPreview);

    void updateUiForMediaAction(@Configuration.MediaAction int mediaAction);

    void updateCameraSwitcher(int numberOfCameras);

    void onPhotoTaken(byte[] bytes, @Nullable CameraFragmentResultListener callback);

    void onVideoRecordStart(int width, int height);

    void onVideoRecordStop(@Nullable CameraFragmentResultListener callback);

    void releaseCameraPreview();

}

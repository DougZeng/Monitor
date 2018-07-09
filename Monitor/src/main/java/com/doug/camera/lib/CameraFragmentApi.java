package com.doug.camera.lib;

import android.support.annotation.Nullable;

import com.doug.camera.lib.internal.ui.model.PhotoQualityOption;
import com.doug.camera.lib.listeners.CameraFragmentControlsListener;
import com.doug.camera.lib.listeners.CameraFragmentResultListener;
import com.doug.camera.lib.listeners.CameraFragmentStateListener;
import com.doug.camera.lib.listeners.CameraFragmentVideoRecordTextListener;

/*
 *
 */

public interface CameraFragmentApi {

    void takePhotoOrCaptureVideo(CameraFragmentResultListener resultListener, @Nullable String directoryPath, @Nullable String fileName);

    void openSettingDialog();

    PhotoQualityOption[] getPhotoQualities();

    void switchCameraTypeFrontBack();

    void switchActionPhotoVideo();

    void toggleFlashMode();

    void setStateListener(CameraFragmentStateListener cameraFragmentStateListener);

    void setTextListener(CameraFragmentVideoRecordTextListener cameraFragmentVideoRecordTextListener);

    void setControlsListener(CameraFragmentControlsListener cameraFragmentControlsListener);

    void setResultListener(CameraFragmentResultListener cameraFragmentResultListener);

}

package com.doug.camera.lib.internal.manager;

import android.content.Context;

import java.io.File;

import com.doug.camera.lib.configuration.Configuration;
import com.doug.camera.lib.configuration.ConfigurationProvider;
import com.doug.camera.lib.internal.manager.listener.CameraCloseListener;
import com.doug.camera.lib.internal.manager.listener.CameraOpenListener;
import com.doug.camera.lib.internal.manager.listener.CameraPhotoListener;
import com.doug.camera.lib.internal.manager.listener.CameraVideoListener;
import com.doug.camera.lib.internal.utils.Size;
import com.doug.camera.lib.listeners.CameraFragmentResultListener;

/*
 *
 */
public interface CameraManager<CameraId, SurfaceListener> {

    void initializeCameraManager(ConfigurationProvider configurationProvider, Context context);

    void openCamera(CameraId cameraId, CameraOpenListener<CameraId, SurfaceListener> cameraOpenListener);

    void closeCamera(CameraCloseListener<CameraId> cameraCloseListener);

    void setFlashMode(@Configuration.FlashMode int flashMode);

    void takePhoto(File photoFile, CameraPhotoListener cameraPhotoListener, CameraFragmentResultListener callback);

    void startVideoRecord(File videoFile, CameraVideoListener cameraVideoListener);

    Size getPhotoSizeForQuality(@Configuration.MediaQuality int mediaQuality);

    void stopVideoRecord(CameraFragmentResultListener callback);

    void releaseCameraManager();

    CameraId getCurrentCameraId();

    CameraId getFaceFrontCameraId();

    CameraId getFaceBackCameraId();

    int getNumberOfCameras();

    int getFaceFrontCameraOrientation();

    int getFaceBackCameraOrientation();

    boolean isVideoRecording();

    CharSequence[] getVideoQualityOptions();

    CharSequence[] getPhotoQualityOptions();

    void setCameraId(CameraId currentCameraId);
}

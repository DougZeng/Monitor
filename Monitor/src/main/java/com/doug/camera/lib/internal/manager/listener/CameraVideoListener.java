package com.doug.camera.lib.internal.manager.listener;

import java.io.File;

import com.doug.camera.lib.internal.utils.Size;
import com.doug.camera.lib.listeners.CameraFragmentResultListener;

/*
 *
 */
public interface CameraVideoListener {
    void onVideoRecordStarted(Size videoSize);

    void onVideoRecordStopped(File videoFile, CameraFragmentResultListener callback);

    void onVideoRecordError();
}

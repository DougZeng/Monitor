package com.doug.camera.lib.internal.manager.listener;

/*
 *
 */
public interface CameraCloseListener<CameraId> {
    void onCameraClosed(CameraId closedCameraId);
}

package com.doug.camera.lib.internal.manager.listener;

import com.doug.camera.lib.internal.utils.Size;

/*
 *
 */
public interface CameraOpenListener<CameraId, SurfaceListener> {
    void onCameraOpened(CameraId openedCameraId, Size previewSize, SurfaceListener surfaceListener);

    void onCameraOpenError();
}

package com.doug.camera.lib.listeners;

/**
 * Convenience implementation of {@link CameraFragmentResultListener}. Derive from this and only override what you need.
 *
 */

public class CameraFragmentResultAdapter implements CameraFragmentResultListener {
    @Override
    public void onVideoRecorded(String filePath) {

    }

    @Override
    public void onPhotoTaken(byte[] bytes, String filePath) {

    }
}

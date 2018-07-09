package com.doug.camera.lib;

import android.Manifest;
import android.annotation.SuppressLint;
import android.support.annotation.RequiresPermission;

import com.doug.camera.lib.configuration.Configuration;
import com.doug.camera.lib.internal.ui.BaseAnncaFragment;

public class CameraFragment extends BaseAnncaFragment {

    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.CAMERA)
    public static CameraFragment newInstance(Configuration configuration) {
        return (CameraFragment) BaseAnncaFragment.newInstance(new CameraFragment(), configuration);
    }
}

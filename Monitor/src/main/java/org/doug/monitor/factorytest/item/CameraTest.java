package org.doug.monitor.factorytest.item;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


import com.orhanobut.logger.Logger;

import org.doug.monitor.R;
import org.doug.monitor.base.util.Toaster;
import org.doug.monitor.factorytest.FactoryAutoTest;
import org.doug.monitor.base.util.DateUtil;

import java.io.IOException;

/**
 * FrontCameraTest : The Test for front Camera.
 */
public class CameraTest {
    private static String TAG = CameraTest.class.getSimpleName();
    private static Camera camera;
    private static SurfaceView mCameraView;

    public static int mCamerapostion = 0;
    public static CameraInfo cameraInfo;
    public static int cameraCount;

    public static long mFrontCameraStartTime = 0;  //front camera
    public static long mFrontCameraOverTime = 0;
    public static long mFrontCameraTestTime = 0;
    public static long mBackCameraStartTime = 0;  //back camera
    public static long mBackCamerOverTime = 0;
    public static long mBackCameraTestTime = 0;

    public static boolean mIsCameraStop = false;

    private final static Handler handler = new Handler();
    static int frontcameratestkey = 1;
    private final static Runnable cameratask = new Runnable() {
        public void run() {
            if (!mIsCameraStop) {
                switch (frontcameratestkey) {
                    case 1:
                        if (FindFrontCameraExist()) {
                            mCameraView.setVisibility(View.VISIBLE);
                            mCamerapostion = 1;
                            StartCameraTest();
                            handler.postDelayed(this, 4000); //4s
                            mFrontCameraStartTime = DateUtil.getCurentSecond();
                            FactoryAutoTest.TextViewColorChange(FactoryAutoTest.mTest1, FactoryAutoTest.mTest2);
                            FactoryAutoTest.mTest2.setText(R.string.camera_fronting);
                            Logger.d("FrontCameraTest testing");
                        } else {
                            CameraNoExitTip();
                            mFrontCameraStartTime = DateUtil.getCurentSecond();
                            FactoryAutoTest.mTest2.setTextColor(Color.RED);
                            handler.postDelayed(this, 10); //0.1s
                        }
                        frontcameratestkey = 2;
                        break;
                    case 2:
                        mFrontCameraOverTime = DateUtil.getCurentSecond();
                        FactoryAutoTest.mTest2.setText(R.string.camera_front);
                        if (camera != null) {
                            camera.stopPreview();
                            camera.setPreviewCallback(null);
                            camera.release();
                            camera = null;
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (FindBackCameraExist()) {
                            mCamerapostion = 0;
                            StartCameraTest();
                            handler.postDelayed(this, 4000);//4s
                            mBackCameraStartTime = DateUtil.getCurentSecond();
                            FactoryAutoTest.TextViewColorChange(FactoryAutoTest.mTest2, FactoryAutoTest.mTest3);
                            FactoryAutoTest.mTest3.setText(R.string.camera_backing);
                            Logger.d("FrontCameraTest test over, backCameraTest testing");
                        } else {
                            CameraNoExitTip();
                            mBackCameraStartTime = DateUtil.getCurentSecond();
                            FactoryAutoTest.mTest2.setTextColor(Color.BLACK);
                            FactoryAutoTest.mTest3.setTextColor(Color.RED);
                            handler.postDelayed(this, 10); //0.01s
                        }
                        frontcameratestkey = 3;
                        break;
                    case 3:
                        mIsCameraStop = true;
                        StopCamera();
                        mBackCamerOverTime = DateUtil.getCurentSecond();
                        FactoryAutoTest.mTest3.setText(R.string.camera_back);
                        mFrontCameraTestTime = mFrontCameraOverTime - mFrontCameraStartTime;
                        mBackCameraTestTime = mBackCamerOverTime - mBackCameraStartTime;
                        Logger.d("backCameraTest over" + " ,FrontCameraTest test time:" + mFrontCameraTestTime + " ,BackCameraTest test time:" + mBackCameraTestTime);
                        FactoryAutoTest.factoryAutoTest.handler.sendEmptyMessage(FactoryAutoTest.MSG_RECEIVER_TEST);
                        break;
                }
            }
        }
    };

    /**
     * Start Camera Test.
     */
    public static void CameraTestCase() {
        frontcameratestkey = 1;
        mIsCameraStop = false;
        Logger.d("CameraTest test start");
        if (checkCameraHardware()) {
            InitCamera();
            handler.post(cameratask);
        } else {
            CameraNoExitTip();
            FactoryAutoTest.factoryAutoTest.handler.sendEmptyMessage(FactoryAutoTest.MSG_RECEIVER_TEST);
        }
    }

    @SuppressWarnings("deprecation")
    public static void InitCamera() {
        cameraInfo = new CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        mCameraView = FactoryAutoTest.mSurfaceView;
        mCameraView.getHolder().setFixedSize(mCameraView.getWidth(), mCameraView.getHeight());
        mCameraView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mCameraView.getHolder().addCallback(new SurfaceCallback());
    }

    public static void StopCamera() {
        mCameraView.setVisibility(View.INVISIBLE);
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();//
            camera.release();//
            camera = null;//Cancel the original camera
        }
    }

    /**
     * @ClassName: SurfaceCallback
     * @Function: 回调接口
     */
    private static final class SurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.stopPreview();
                camera.setPreviewCallback(null);
                camera.release();
                camera = null;
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
        }
    }

    public static void StartCameraTest() {
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//Get information for each camera
            if (mCamerapostion == 1) {
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) { // open front camera
                    if (camera != null) {
                        camera.stopPreview();
                        camera.setPreviewCallback(null);
                        camera.release();
                        camera = null;
                    }
                    camera = Camera.open(i);
                    camera.setDisplayOrientation(90);
                    try {
                        // SurfaceHolder target set for displaying camera images
                        camera.setPreviewDisplay(mCameraView.getHolder());
                        camera.startPreview();
//                        mCamerapostion = 0;
                        break;
                    } catch (IOException e) {
                        Logger.e(e.toString());
                    }
                }
            } else if (mCamerapostion == 0) {
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) { // open back camera
                    if (camera != null) {
                        camera.stopPreview();
                        camera.setPreviewCallback(null);
                        camera.release();
                        camera = null;
                    }
                    camera = Camera.open(i);
                    camera.setDisplayOrientation(90);
                    Camera.Parameters parameters = camera.getParameters();
//                    parameters.setFlashMode("torch");
                    camera.setParameters(parameters);
                    try {
                        camera.setPreviewDisplay(mCameraView.getHolder());
                        camera.startPreview();
//                        mCamerapostion = 1;
                        break;
                    } catch (IOException e) {
                        Logger.e(e.toString());
                    }
                }
            }
        }
    }

    /**
     * @return :boolean
     * @MethodName: checkCameraHardware
     * @Functions:Detect whether the phone supports camera
     */
    public static boolean checkCameraHardware() {
        if (FactoryAutoTest.mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * @return :int
     * @MethodName: FindBackCamera
     * @Functions:Get back-facing camera.
     */
    @SuppressLint("NewApi")
    public static boolean FindBackCameraExist() {
        int cameraCount = 0;
        int mCameraPosition;
        CameraInfo cameraInfo = new CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number
        for (mCameraPosition = 0; mCameraPosition < cameraCount; mCameraPosition++) {
            Camera.getCameraInfo(mCameraPosition, cameraInfo); // get camerainfo
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {  // back camera
                return true; //back camera exited
            }
        }
        return false;
    }

    /**
     * @return :int
     * @MethodName: FindFrontCamera
     * @Functions:Get front-facing camera.
     */
    @SuppressLint("NewApi")
    public static boolean FindFrontCameraExist() {
        int cameraCount = 0;
        int mCameraPosition;
        CameraInfo cameraInfo = new CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number
        for (mCameraPosition = 0; mCameraPosition < cameraCount; mCameraPosition++) {
            Camera.getCameraInfo(mCameraPosition, cameraInfo); // get camerainfo
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) { // front camera
                return true;//front camera exited
            }
        }
        return false;
    }


    private static void CameraNoExitTip() {
        if (!FindFrontCameraExist()) {
            Toaster.showToast(FactoryAutoTest.mContext, "Front Camera not exist!");
        } else if (!FindBackCameraExist()) {
            Toaster.showToast(FactoryAutoTest.mContext, "Back Camera not exist!");
        } else {
            Toaster.showToast(FactoryAutoTest.mContext, "Not Find Camera!");
        }
    }

}

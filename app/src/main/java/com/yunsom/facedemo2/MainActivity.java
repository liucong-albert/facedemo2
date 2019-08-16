package com.yunsom.facedemo2;

import android.Manifest;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.dface.dto.EngineWorkingMode;
import com.dface.dto.FrameRotationType;
import com.dface.exception.DFaceException;
import com.serenegiant.usb.DeviceFilter;
import com.serenegiant.usb.USBMonitor;
import com.serenegiant.usbcameracommon.UVCCameraHandler;
import com.serenegiant.usbcameracommon.UvcCameraDataCallBack;
import com.serenegiant.widget.CameraViewInterface;
import com.serenegiant.widget.UVCCameraTextureView;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DfaceApplicationInterface dfai;
    private UVCCameraTextureView mUVCCameraViewColor;
    private UVCCameraTextureView mUVCCameraViewIR;

    //彩色相机预览的图像旋转的角度类型
    private int mDisplayClockWiseTypeColor = FrameRotationType.CLOCK_WISE_0.getType();
    //红外相机预览的图像旋转的角度类型
    private int mDisplayClockWiseTypeIR = FrameRotationType.CLOCK_WISE_0.getType();
    //相机预览分辨率
    private int previewWidthColor;
    private int previewHeightColor;

    //红外相机预览分辨率
    private int previewWidthIR;
    private int previewHeightIR;
    //画人脸边框，角度，关键点
    private FaceOverlayView mFaceOverlayView;

    //是否为前置摄像头
    private boolean isFrontCamera = false;

    private PropertiesUtil mProp;
    private static final String TAG = "MainActivity.class";
    private USBMonitor mUSBMonitor;
    private UVCCameraHandler mHandlerColor;
    private UVCCameraHandler mHandlerIR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPerMission();
        dfai = (DfaceApplicationInterface)getApplication();
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        //初始化配置文件类
        mProp = PropertiesUtil.getInstance(this).setFile(dfai.getConfigPropFile()).init();
//        //读取配置文件初始化系统配置
        initConfigFromPropFile();
        dfai.setAuthed(true);
        init();
        initCamera();
    }
    private void initPerMission(){
        String[] PERMISSION_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        boolean isPermission =  AndPermission.hasPermissions(this,PERMISSION_STORAGE);
        if (!isPermission){
            AndPermission.with(this)
                    .runtime()
                    .permission(PERMISSION_STORAGE)
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> perms) {
                            Log.e(TAG,"onGranted");
                        }
                    })
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> perms) {
                            Log.e(TAG,"onDenied");
                        }
                    })
                    .start();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        dfai.getDfaceEngine().stop();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(!dfai.getAuthed()){
            return;
        }
        initDfai();
        dfai.getDfaceEngine().start();
    }

    private void initDfai(){
        //多脸抓拍模式
        dfai.getDfaceEngine().setWorkingMode(EngineWorkingMode.WORKING_MUTI_FACE_CAPTURE_MODE.getMode());
        //单脸抓拍模式
        //dfai.getDfaceEngine().setWorkingMode(EngineWorkingMode.WORKING_SINGLE_FACE_CAPTURE_MODE.getMode());

        dfai.getDfaceEngine().setDetectMinSize(dfai.getAppConfig().DETECT_MIN_SIZE);
        dfai.getDfaceEngine().setRecognizeMinSize(dfai.getAppConfig().RECOGNIZE_MIN_SIZE);
        dfai.getDfaceEngine().setRGBAntiLevel(dfai.getAppConfig().RGBANTI_LEVEL);
        dfai.getDfaceEngine().setRGBLiveFilter(dfai.getAppConfig().ENABLE_RECOGNIZE_RGBANTI);
        dfai.getDfaceEngine().setRGBLiveFilter1Count(dfai.getAppConfig().RGBANTI_FILTER1_COUNT);
        dfai.getDfaceEngine().setRGBLiveThreshold(dfai.getAppConfig().RGBANTI_THRESHOLD);
        dfai.getDfaceEngine().setPoseFilter(dfai.getAppConfig().ENABLE_POSE_FILTER);
        dfai.getDfaceEngine().setQualityFilter(dfai.getAppConfig().ENABLE_QUALITY_FILTER);
        dfai.getDfaceEngine().setQualityThreshold(dfai.getAppConfig().QUALITY_THRESHOLD);
        dfai.getDfaceEngine().setPoseThreshold(dfai.getAppConfig().CAPTURE_YAW_THRESHOLD, dfai.getAppConfig().CAPTURE_PITCH_THRESHOLD, dfai.getAppConfig().CAPTURE_ROW_THRESHOLD);
        dfai.getDfaceEngine().setQualityThreshold(dfai.getAppConfig().QUALITY_THRESHOLD);
        dfai.getDfaceEngine().setPoseThreshold(dfai.getAppConfig().YAW_THRESHOLD, dfai.getAppConfig().PITCH_THRESHOLD, dfai.getAppConfig().ROW_THRESHOLD);
        dfai.getDfaceEngine().setIRAntiLevel(dfai.getAppConfig().IRANTI_LEVEL);
        dfai.getDfaceEngine().setRGBLiveFilter1CrossFrameCount(dfai.getAppConfig().RGBANTI_FILTER1_CROSS_FRAME_COUNT);
        dfai.getDfaceEngine().setFeatureReturn(true);
    }

    private void initCamera(){
        mUVCCameraViewColor =findViewById(R.id.single_face_surfaceview);
        mUVCCameraViewIR = findViewById(R.id.single_ir_surfaceview);
        mUVCCameraViewColor.setRotation(mDisplayClockWiseTypeColor*90);
        mUVCCameraViewIR.setRotation(mDisplayClockWiseTypeColor*90);

        previewWidthColor = dfai.getAppConfig().CAMERA_WIDTH;
        previewHeightColor = dfai.getAppConfig().CAMERA_HEIGHT;

        previewWidthIR = dfai.getAppConfig().CAMERA_WIDTH;
        previewHeightIR = dfai.getAppConfig().CAMERA_HEIGHT;

        mFaceOverlayView = findViewById(R.id.overlay_view);
        mFaceOverlayView.setPreviewWidth(dfai.getAppConfig().CAMERA_WIDTH);
        mFaceOverlayView.setPreviewHeight(dfai.getAppConfig().CAMERA_HEIGHT);
        mFaceOverlayView.setDisplayOrientation(0);
        mFaceOverlayView.setFront(isFrontCamera);
        if(mDisplayClockWiseTypeColor == FrameRotationType.CLOCK_WISE_90.getType()
                || mDisplayClockWiseTypeColor == FrameRotationType.CLOCK_WISE_270.getType()) {
            if(mFaceOverlayView != null) {
                mFaceOverlayView.setPreviewSize(dfai.getAppConfig().CAMERA_HEIGHT, dfai.getAppConfig().CAMERA_WIDTH);
            }
        }else {
            if(mFaceOverlayView != null) {
                mFaceOverlayView.setPreviewSize(dfai.getAppConfig().CAMERA_WIDTH, dfai.getAppConfig().CAMERA_HEIGHT);
            }
        }
        mUSBMonitor = new USBMonitor(this, mOnDeviceConnectListener);
        mUSBMonitor.register();

        mHandlerColor = UVCCameraHandler.createHandler(this, mUVCCameraViewColor
                , dfai.getAppConfig().CAMERA_WIDTH, dfai.getAppConfig().CAMERA_HEIGHT
                , BANDWIDTH_FACTORS[0], firstDataCallBack);

        mHandlerIR = UVCCameraHandler.createHandler(this, mUVCCameraViewIR
                , dfai.getAppConfig().CAMERA_WIDTH, dfai.getAppConfig().CAMERA_HEIGHT
                , BANDWIDTH_FACTORS[1], secondDataCallBack);

    }
    private static final float[] BANDWIDTH_FACTORS = {0.5f, 0.5f};
    //红外摄像头数据
    private byte[] irData;
    UvcCameraDataCallBack secondDataCallBack = new UvcCameraDataCallBack() {
        @Override
        public void getData(byte[] data) {
//            Log.v(TAG, "数据回调:" + data.length);
            irData = data;
        }
    };

    UvcCameraDataCallBack firstDataCallBack = new UvcCameraDataCallBack() {
        @Override
        public void getData(byte[] data) {
            Log.e(TAG, "数据回调:" + data.length);
        }
    };

    private void init(){
        /**初始化 dfaceDetect dfacePose dfaceRecognize dfaceCompare
         * 尽量用try-catch 模式，可以根据返回的错误码做相应的跳转
         * 错误码可以参考用户手册和开发文档
         **/
        try {
            dfai.getDfaceDetect().initLoad(dfai.getModelPath());
            dfai.getDfacePose().initLoad(dfai.getModelPath());
            dfai.getDfaceRecognize().initLoad(dfai.getModelPath(), 2);
            dfai.getDfaceCompare().initLoad(dfai.getModelPath(), 2);
            dfai.getDfaceRgbAnti().initLoad(dfai.getModelPath());
            //初始化跟踪器
            dfai.getDfaceTrack().initLoad(dfai.getModelPath(), dfai.getAppConfig().CAMERA_WIDTH, dfai.getAppConfig().CAMERA_HEIGHT, 500, 1, (byte)1);
            dfai.getDfaceEngine().initLoad(dfai.getModelPath(), dfai.getAppConfig().CAMERA_HEIGHT, dfai.getAppConfig().CAMERA_WIDTH, 2);

        }catch (DFaceException dfex){
            int err_code = dfex.getCode();
            String err_msg = dfex.getMessage();
            Log.e(TAG, "DFaceException Error Code:"+err_code);
            Log.e(TAG, "DFaceException Error MSG:"+err_msg);
            //如果错误码是机器未激活类别，则跳转到授权页面
        }
    }
    private void initConfigFromPropFile(){
        // 从配置文件读取配置
        mProp.open();
        //读取主节点ip
        dfai.getAppConfig().SYNC_HOST_ADDRESS = mProp.readString("SYNC_HOST_ADDRESS",null);;
        //读取是否为主节点
        dfai.getAppConfig().IS_HOST_DEVICE = mProp.readBoolean("IS_HOST_DEVICE",false);;

        dfai.getAppConfig().DETECT_MIN_SIZE = mProp.readInt("DETECT_MIN_SIZE", 80);

        dfai.getAppConfig().RECOGNIZE_MIN_SIZE = mProp.readInt("RECOGNIZE_MIN_SIZE", 140);
        dfai.getAppConfig().DETECT_MAX_COUNT = mProp.readInt("DETECT_MAX_COUNT", 4);
        dfai.getAppConfig().CONFIDENCE = (float)mProp.readDouble("CONFIDENCE",0.72f);

        dfai.getAppConfig().RGBANTI_LEVEL = mProp.readInt("RGBANTI_LEVEL",1);
        dfai.getAppConfig().RGBANTI_THRESHOLD = (float)mProp.readDouble("RGBANTI_THRESHOLD",0.85f);

        dfai.getAppConfig().RGBANTI_FILTER1_COUNT = mProp.readInt("RGBANTI_FILTER1_COUNT", 4);
        dfai.getAppConfig().IRANTI_LEVEL = (float)mProp.readDouble("IRANTI_LEVEL", 0.6f);

        dfai.getAppConfig().RGBANTI_FILTER1_CROSS_FRAME_COUNT = mProp.readInt("RGBANTI_FILTER1_CROSS_FRAME_COUNT",1);

        dfai.getAppConfig().YAW_THRESHOLD = (float)mProp.readDouble("YAW_THRESHOLD",8.0f);
        dfai.getAppConfig().PITCH_THRESHOLD = (float)mProp.readDouble("PITCH_THRESHOLD",8.0f);
        dfai.getAppConfig().ROW_THRESHOLD = (float)mProp.readDouble("ROW_THRESHOLD",8.0f);

        dfai.getAppConfig().CAPTURE_PITCH_THRESHOLD = (float)mProp.readDouble("CAPTURE_PITCH_THRESHOLD",20.0f);
        dfai.getAppConfig().CAPTURE_YAW_THRESHOLD = (float)mProp.readDouble("CAPTURE_YAW_THRESHOLD",20.0f);
        dfai.getAppConfig().CAPTURE_ROW_THRESHOLD = (float)mProp.readDouble("CAPTURE_ROW_THRESHOLD",20.0f);

        dfai.getAppConfig().QUALITY_THRESHOLD = (float)mProp.readDouble("QUALITY_THRESHOLD",900.0f);
        dfai.getAppConfig().ENABLE_RECOGNIZE_RGBANTI = mProp.readBoolean("ENABLE_RECOGNIZE_RGBANTI",false);
        dfai.getAppConfig().ENABLE_SHOW_IRVIEW = mProp.readBoolean("ENABLE_SHOW_IRVIEW",false);

        dfai.getAppConfig().ENABLE_QUALITY_FILTER = mProp.readBoolean("ENABLE_QUALITY_FILTER",false);
        dfai.getAppConfig().ENABLE_POSE_FILTER = mProp.readBoolean("ENABLE_POSE_FILTER",false);

        dfai.getAppConfig().CAMERA_ROTATION_TYPE = mProp.readInt("CAMERA_ROTATION_TYPE", 0);

        dfai.getAppConfig().CAMERA_WIDTH = mProp.readInt("CAMERA_WIDTH", 640);
        dfai.getAppConfig().CAMERA_HEIGHT = mProp.readInt("CAMERA_HEIGHT", 480);

        dfai.getAppConfig().SAVE_CROP_FACE_SIZE = mProp.readInt("SAVE_CROP_FACE_SIZE", 224);
        dfai.getAppConfig().WIEGAND = mProp.readInt("WIEGAND", 26);
        dfai.getAppConfig().RERECOGNIZE_LOOP_FRAME = mProp.readInt("RERECOGNIZE_LOOP_FRAME", 4);
        dfai.getAppConfig().CUSTOM_ANDROID_BIND = mProp.readBoolean("CUSTOM_ANDROID_BIND",false);

    }


    private final USBMonitor.OnDeviceConnectListener mOnDeviceConnectListener = new USBMonitor.OnDeviceConnectListener() {
        @Override
        public void onAttach(final UsbDevice device) {
            int productId = device.getProductId();
            if (productId == 6800){
                Log.e(TAG,"打开彩色摄像头");
                mUSBMonitor.requestPermission(device);
            }
        }
        @Override
        public void onConnect(final UsbDevice device, final USBMonitor.UsbControlBlock ctrlBlock, final boolean createNew) {
            synchronized (this) {
                //设备连接成功
                if (!mHandlerColor.isOpened()) {
                    mHandlerColor.open(ctrlBlock);
                    final SurfaceTexture st = mUVCCameraViewColor.getSurfaceTexture();
                    mHandlerColor.startPreview(new Surface(st));
                    final List<DeviceFilter> filter = DeviceFilter.getDeviceFilters(MainActivity.this, com.serenegiant.uvccamera.R.xml.device_filter);
                    List<UsbDevice> devicesList = mUSBMonitor.getDeviceList(filter);
                    for (int i = 0 ; i < devicesList.size();i++){
                        UsbDevice item = devicesList.get(i);
                        if (item.getProductId() == 6688){
                            mUSBMonitor.requestPermission(item);//获取设备信息，并检查打开此设备的权限
                        }
                    }
                } else if (!mHandlerIR.isOpened()) {
                    mHandlerIR.open(ctrlBlock);
                    final SurfaceTexture st = mUVCCameraViewIR.getSurfaceTexture();
                    mHandlerIR.startPreview(new Surface(st));
                }
            }
        }

        @Override
        public void onDisconnect(final UsbDevice device, final USBMonitor.UsbControlBlock ctrlBlock) {
            Log.e(TAG, "onDisconnect:" + device);
        }

        @Override
        public void onDettach(final UsbDevice device) {
            Log.e(TAG, "onDettach:" + device);
        }

        @Override
        public void onCancel(final UsbDevice device) {
            Log.e(TAG, "onCancel:");
        }
    };

}

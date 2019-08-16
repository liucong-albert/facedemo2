package com.yunsom.facedemo2;

public class AppConfig {
    //人脸检测线程数
    public static int DETECT_NUM_THREAD = 1;

    //人脸识别线程数
    public static int RECOGNIZE_NUM_THREAD = 1;

    //单目活体线程数
    public static int RGBANTI_NUM_THREAD = 1;

    //检测人脸最小
    public static int DETECT_MIN_SIZE = 80;

    //识别最小人脸
    public static int RECOGNIZE_MIN_SIZE = 140;

    //检测最多人脸个数
    public static int DETECT_MAX_COUNT = 4;

    //置信度
    public static float CONFIDENCE = 0.72f;

    //单目活体级别
    public static int RGBANTI_LEVEL= 2;

    //单目活体Patch阈值
    public static float RGBANTI_THRESHOLD = 0.85f;

    //单目活体Patch数量
    public static int RGBANTI_FILTER1_COUNT = 4;

    //单目活体Patch滑动平均跨帧数
    public static int RGBANTI_FILTER1_CROSS_FRAME_COUNT = 1;

    //红外活体级别
    public static float IRANTI_LEVEL= 0.6f;

    //注册人脸边框最小分值
    public static float MIN_SCORE_THRESHOLD = 0.99f;

    //注册人脸pitch角度阈值
    public static float PITCH_THRESHOLD = 10.0f;

    //注册人脸yaw角度阈值
    public static float YAW_THRESHOLD = 10.0f;

    //注册人脸row角度阈值
    public static float ROW_THRESHOLD = 10.0f;

    //抓拍人脸pitch角度阈值
    public static float CAPTURE_PITCH_THRESHOLD = 30.0f;

    //抓拍人脸yaw角度阈值
    public static float CAPTURE_YAW_THRESHOLD = 30.0f;

    //抓拍人脸row角度阈值
    public static float CAPTURE_ROW_THRESHOLD = 30.0f;

    //注册人脸相机距离(米)限制阈值
    public static float ZOFFSET_THRESHOLD = 1.0f;

    //人脸清晰度阈值
    public static float QUALITY_THRESHOLD = 1000;

    //识别是否开启单目活体检测
    public static boolean ENABLE_RECOGNIZE_RGBANTI = false;

    //DfaceEngine是否开启姿态过滤
    public static boolean ENABLE_POSE_FILTER = false;

    //DfaceEngine是否清晰度过滤
    public static boolean ENABLE_QUALITY_FILTER = false;


    //是否显示红外预览
    public static boolean ENABLE_SHOW_IRVIEW = true;

    //摄像头做镜像翻转
    public static boolean ENABLE_CAMERA_MIRROW = false;

    //保持显示人脸信息帧数
    public static int KEEP_SHOW_FACEINFO_FRAME = 10;

    //人证比对保持显示人脸信息帧数
    public static int KEEP_SHOW_FACEINFO_FRAME_FCARD = 100;

    //摄像头分辨率宽
    public static int CAMERA_WIDTH = 640;

    //摄像头分辨率高
    public static int CAMERA_HEIGHT = 480;

    //保存的裁剪后头像尺寸
    public static int SAVE_CROP_FACE_SIZE = 224;

    //彩色摄像头
    public static int COLOR_CAMERAID = 1;

    //红外摄像头
    public static int IR_CAMERAID = -1;

    //相机旋转角度类型
    public static int CAMERA_ROTATION_TYPE = 0;

    //韦根协议
    public static int WIEGAND = 26;

    //陌生人重识别帧数间隔
    public static int RERECOGNIZE_LOOP_FRAME = 4;

    //是否为主节点
    public static boolean IS_HOST_DEVICE = false;

    //服务器地址
    //public static String SYNC_HOST_ADDRESS = "tcp://192.168.0.159:1234";
    public static String SYNC_HOST_ADDRESS = "";

    //人脸特征加载定时任务时间(毫秒)
    public static int LOAD_FEATURE_REPEAT_TIME = 600000;

    //自定义绑定android
    public static boolean CUSTOM_ANDROID_BIND= false;

}

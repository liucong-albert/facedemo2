package com.yunsom.facedemo2;

import android.app.Application;
import android.os.Environment;
import android.provider.Settings;

import com.dface.DfaceCompare;
import com.dface.DfaceDetect;
import com.dface.DfaceEngine;
import com.dface.DfaceInfrared;
import com.dface.DfacePose;
import com.dface.DfaceRecognize;
import com.dface.DfaceRgbAnti;
import com.dface.DfaceTool;
import com.dface.DfaceTrack;
import com.dface.activation.Accredit;

import java.io.File;

import io.liteglue.SQLiteConnection;
import io.liteglue.SQLiteConnector;

public class DfaceApplication extends Application implements DfaceApplicationInterface {

    public static final String TAG = DfaceApplication.class.getSimpleName();

    //机器激活状态
    public static boolean authed = false;
    //sd卡路径
    public static String sdPath = null;
    //数据库文件路径
    public static String dbFileName = "";

    //同步数据库文件名
    public static String syncDBFileName = "";
    //同步数据库文件路径
    public static String syncDBUri = "";
    //非同步数据库文件名
    public static String unSyncDBFileName = "";
    //非同步数据库文件路径
    public static String unSyncDBUri = "";
    //保存图片目录
    public static String faceimgPath = "";
    //config文件目录
    public static String configFile = "";
    //log日志目录
    public static String logPath = null;

    public static FeaturesBindIds featuresBindIds = new FeaturesBindIds();

    public static String androidID = "";

    public static AppConfig appConfig = new AppConfig();

    public static Accredit accredit = new Accredit();
    public static DfaceDetect dfaceDetect = new DfaceDetect();
    public static DfacePose dfacePose = new DfacePose();
    public static DfaceRecognize dfaceRecognize = new DfaceRecognize();
    public static DfaceCompare dfaceCompare = new DfaceCompare();
    public static DfaceRgbAnti dfaceRgbAnti = new DfaceRgbAnti();
    public static DfaceTrack dfaceTrack = new DfaceTrack();
    public static DfaceEngine dfaceEngine = new DfaceEngine();
    public static DfaceTool dfaceTool = new DfaceTool();

    SQLiteConnector connector = null;
    SQLiteConnection conn = null;
    SQLiteConnection connUnSync = null;

    @Override
    public void onCreate() {
        super.onCreate();

        //加载jni动态库
        System.loadLibrary("dfacepro");

        //获得模型目录
        File sdDir = Environment.getExternalStorageDirectory();//获取SD卡根目录
        sdPath = sdDir.toString() + "/dface/normal_binary/";

        //保存图片目录
        faceimgPath = sdDir.toString() + "/dface/faceimg/";

        //同步数据库文件目录
//        syncDBFileName = sdPath + "/dface_sync_uvc_v48.db";
//        unSyncDBFileName = sdPath + "/dface_unsync_uvc_v48.db";

        syncDBFileName = sdPath + "/dface_sync_uvc_v49.db";
        unSyncDBFileName = sdPath + "/dface_unsync_uvc_v49.db";
        //配置文件目录
        configFile = sdPath + "/config.properties";
        //保存图片目录
        faceimgPath = sdDir.toString() + "/dface/faceimg/";

        androidID = Settings.System.getString(getApplicationContext().getContentResolver(), Settings.System.ANDROID_ID);

        logPath = sdDir.toString() + "/dface/logs/";
    }


    @Override
    public DfaceEngine getDfaceEngine() {
        return dfaceEngine;
    }

    @Override
    public DfaceDetect getDfaceDetect() {
        return dfaceDetect;
    }

    @Override
    public DfaceRgbAnti getDfaceRgbAnti() {
        return dfaceRgbAnti;
    }

    @Override
    public DfaceCompare getDfaceCompare() {
        return dfaceCompare;
    }

    @Override
    public DfaceInfrared getDfaceInfrared() {
        return null;
    }

    @Override
    public DfaceTool getDfaceTool() {
        return dfaceTool;
    }

    @Override
    public DfacePose getDfacePose() {
        return dfacePose;
    }

    @Override
    public DfaceRecognize getDfaceRecognize() {
        return dfaceRecognize;
    }

    @Override
    public DfaceTrack getDfaceTrack() {
        return dfaceTrack;
    }

    @Override
    public boolean getAuthed() {
        return authed;
    }

    @Override
    public void setAuthed(boolean setter) {
        authed = setter;
    }

    @Override
    public AppConfig getAppConfig() {
        return appConfig;
    }

    @Override
    public String getModelPath() {
        return sdPath;
    }

    @Override
    public Accredit getAccredit() {
        return accredit;
    }

    @Override
    public SQLiteConnection getSyncDBConnect() {
        return conn;
    }

    @Override
    public void setSyncDBConnect(SQLiteConnection syncDBConnect) {
        conn = syncDBConnect;
    }

    @Override
    public SQLiteConnection getUnSyncDBConnect() {
        return connUnSync;
    }

    @Override
    public void setUnSyncDBConnect(SQLiteConnection unSyncDBConnect) {
        connUnSync = unSyncDBConnect;
    }

    @Override
    public String getSyncDBFileName() {
        return syncDBFileName;
    }

    @Override
    public String getSyncDBUri() {
        //判断是否为主节点
        if(appConfig.IS_HOST_DEVICE){
            syncDBUri = "file:" + syncDBFileName + "?node=primary&bind=tcp://0.0.0.0:1234";
        }else{
            //次节点，同步模式
            if(appConfig.SYNC_HOST_ADDRESS != null && !appConfig.SYNC_HOST_ADDRESS.isEmpty()){
                syncDBUri = "file:" + syncDBFileName + "?node=secondary&connect=tcp://" + appConfig.SYNC_HOST_ADDRESS;
            }else{
                //次节点，普通模式
                syncDBUri = "file:" + syncDBFileName;
            }
        }
        return syncDBUri;
    }

    @Override
    public String getUnSyncDBFileName() {
        return unSyncDBFileName;
    }

    @Override
    public String getUnSyncDBUri() {
        unSyncDBUri = "file:" + unSyncDBFileName;
        return unSyncDBUri;
    }

    @Override
    public FeaturesBindIds getFeatures() {
        return featuresBindIds;
    }

    @Override
    public String getCurrentDeviceId() {
        return androidID;
    }

    @Override
    public void setAccredit(Accredit acc) {
        accredit = acc;
    }

    @Override
    public String getConfigPropFile() {
        return configFile;
    }

    @Override
    public String getLogDir() {
        return logPath;
    }
}

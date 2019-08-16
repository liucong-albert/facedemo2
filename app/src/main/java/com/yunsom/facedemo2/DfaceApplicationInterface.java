package com.yunsom.facedemo2;

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

import io.liteglue.SQLiteConnection;

public interface DfaceApplicationInterface {
    DfaceEngine getDfaceEngine();
    DfaceDetect getDfaceDetect();
    DfaceRgbAnti getDfaceRgbAnti();
    DfaceCompare getDfaceCompare();
    DfaceInfrared getDfaceInfrared();
    DfaceTool getDfaceTool();
    DfacePose getDfacePose();
    DfaceRecognize getDfaceRecognize();
    DfaceTrack getDfaceTrack();
    boolean getAuthed();
    void setAuthed(boolean setter);
    AppConfig getAppConfig();
    String getModelPath();
    Accredit getAccredit();
    SQLiteConnection getSyncDBConnect();
    void setSyncDBConnect(SQLiteConnection unSyncDBConnect);
    SQLiteConnection getUnSyncDBConnect();
    void setUnSyncDBConnect(SQLiteConnection unSyncDBConnect);
    String getSyncDBFileName();
    String getSyncDBUri();
    String getUnSyncDBFileName();
    String getUnSyncDBUri();
    FeaturesBindIds getFeatures();
    String getCurrentDeviceId();
    void setAccredit(Accredit acc);
    String getConfigPropFile();
    String getLogDir();
}

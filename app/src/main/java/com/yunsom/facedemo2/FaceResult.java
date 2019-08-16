package com.yunsom.facedemo2;

import java.util.Arrays;


public class FaceResult extends Object {

    private float confidence;
    private int id;
    private long time;

    private int x;
    private int y;
    private int width;
    private int height;
    private float[] ppoint;
    private int live;
    private String name;

    public FaceResult() {
        id = 0;
        x = 0;
        y = 0;
        width = 0;
        height = 0;
        confidence = 0.4f;
        ppoint = new float[10];
        time = System.currentTimeMillis();
        live = 1;
        name = "";
    }


    public void setFace(int id, float confidence, int x, int y, int width, int height, float[] ppoint, long time) {
        this.id = id;
        this.confidence = confidence;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.time = time;
        System.arraycopy(ppoint, 0, this.ppoint, 0, 10);
    }

    public void clear() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.confidence = 0.4f;
        this.name = "";
        Arrays.fill(this.ppoint,0.0f);
    }


    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float[] getPpoint() {
        return ppoint;
    }

    public void setPpoint(float[] ppoint) {
        this.ppoint = ppoint;
    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

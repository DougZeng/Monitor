package org.doug.monitor.assembly;

import org.doug.monitor.base.BaseModel;

/**
 * Created by wesine on 2018/6/15.
 */

public class Assembly extends BaseModel {
    private int visualInspection;
    private int camera;
    private int scanner;
    private int touch;
    private int audio;
    private int ethernet;
    private int wifi;

    public Assembly() {
    }

    public Assembly(int visualInspection, int camera, int scanner, int touch, int audio, int ethernet, int wifi) {
        this.visualInspection = visualInspection;
        this.camera = camera;
        this.scanner = scanner;
        this.touch = touch;
        this.audio = audio;
        this.ethernet = ethernet;
        this.wifi = wifi;
    }

    public int getVisualInspection() {
        return visualInspection;
    }

    public void setVisualInspection(int visualInspection) {
        this.visualInspection = visualInspection;
    }

    public int getCamera() {
        return camera;
    }

    public void setCamera(int camera) {
        this.camera = camera;
    }

    public int getScanner() {
        return scanner;
    }

    public void setScanner(int scanner) {
        this.scanner = scanner;
    }

    public int getTouch() {
        return touch;
    }

    public void setTouch(int touch) {
        this.touch = touch;
    }

    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }

    public int getEthernet() {
        return ethernet;
    }

    public void setEthernet(int ethernet) {
        this.ethernet = ethernet;
    }

    public int getWifi() {
        return wifi;
    }

    public void setWifi(int wifi) {
        this.wifi = wifi;
    }
}

package org.doug.monitor.menu;

/**
 * Created by wesine on 2018/5/23.
 */

public class DeviceItem {
    private String title;
    private String desc;
    private boolean isOn;

    public DeviceItem(String title, String desc, boolean isOn) {
        this.title = title;
        this.desc = desc;
        this.isOn = isOn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    @Override
    public String toString() {
        return "DeviceItem{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", isOn=" + isOn +
                '}';
    }
}

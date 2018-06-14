package org.doug.monitor.menu;

/**
 * Created by wesine on 2018/6/14.
 */

public class MenuItem {

    private String title;
    private Class<?> activity;
    private int imageResource;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class<?> getActivity() {
        return activity;
    }

    public void setActivity(Class<?> activity) {
        this.activity = activity;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}

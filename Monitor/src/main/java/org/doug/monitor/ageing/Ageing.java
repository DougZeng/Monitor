package org.doug.monitor.ageing;

import org.doug.monitor.base.BaseModel;

/**
 * Created by wesine on 2018/6/15.
 */

public class Ageing extends BaseModel {
    private int aging;

    public int getAging() {
        return aging;
    }

    public void setAging(int aging) {
        this.aging = aging;
    }

    public Ageing(int aging) {
        this.aging = aging;
    }
}

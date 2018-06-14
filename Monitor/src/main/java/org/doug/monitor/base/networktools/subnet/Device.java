package org.doug.monitor.base.networktools.subnet;

import java.net.InetAddress;

/**
 * Created by wesine on 2018/6/14.
 */

public class Device {
    public String ip = "";
    public String hostname = "";
    public String mac = "";


    public float time = 0;

    public Device(InetAddress ip) {
        this.ip = ip.getHostAddress();
        this.hostname = ip.getCanonicalHostName();
    }

    @Override
    public String toString() {
        return "Device{" +
                "ip='" + ip + '\'' +
                ", hostname='" + hostname + '\'' +
                ", mac='" + mac + '\'' +
                ", time=" + time +
                '}';
    }
}

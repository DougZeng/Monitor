package org.doug.monitor.monitor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by wesine on 2018/6/1.
 */

public interface Reader {
    void startRecord();

    void stopRecord();

    boolean isRecording();

    void notifyError(IOException e);

    void setIntervals(int intervalRead, int intervalUpdate, int intervalWidth);

    List<Map<String, Object>> getProcesses();

    void addProcess(Map<String, Object> process);

    void removeProcess(Map<String, Object> process);

    int getIntervalRead();

    int getIntervalUpdate();

    int getIntervalWidth();

    List<Float> getCPUTotalP();

    List<Float> getCPUAMP();

    List<Integer> getMemoryAM();

    int getMemTotal();

    List<String> getMemUsed();

    List<String> getMemAvailable();

    List<String> getMemFree();

    List<String> getCached();

    List<String> getThreshold();

}

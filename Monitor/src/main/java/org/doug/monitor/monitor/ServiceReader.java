/* 
 * 2010-2017 (C) Antonio Redondo
 * http://antonioredondo.com
 * http://github.com/AntonioRedondo/AnotherMonitor
 *
 * Code under the terms of the GNU General Public License v3.
 *
 */

package org.doug.monitor.monitor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.*;
import android.os.Debug.MemoryInfo;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.doug.monitor.base.App;
import org.doug.monitor.base.Constans;
import org.doug.monitor.R;
import org.doug.monitor.base.util.SharedPreferencesUtils;


public class ServiceReader extends Service implements Reader {

    private boolean /*threadSuspended, */recording, firstRead = true, topRow = true;
    private int memTotal, pId, intervalRead, intervalUpdate, intervalWidth, maxSamples = 2000;
    private long workT, totalT, workAMT, total, totalBefore, work, workBefore, workAM, workAMBefore;
    private String s;
    private String[] sa;
    private List<Float> cpuTotal, cpuAM;
    private List<Integer> memoryAM;
    private List<Map<String, Object>> mListSelected; // Integer		 Constans.pId
    // String		 Constans.pName
    // Integer	 Constans.work
    // Integer	 Constans.workBefore
    // List<Sring> Constans.finalValue
    private List<String> memUsed, memAvailable, memFree, cached, threshold;
    private ActivityManager am;
    private Debug.MemoryInfo[] amMI;
    private ActivityManager.MemoryInfo mi;
    private NotificationManager mNM;
    private Notification mNotificationRead, mNotificationRecord;
    private BufferedReader reader;
    private BufferedWriter mW;
    private File mFile;
    //    private SharedPreferences mPrefs;
    private Runnable readRunnable = new Runnable() { // http://docs.oracle.com/javase/8/docs/technotes/guides/concurrency/threadPrimitiveDeprecation.html
        @Override
        public void run() {
            // The service makes use of an explicit Thread instead of a Handler because with the Threat the code is executed more synchronously.
            // However the ViewGraphic is drew with a Handler because the drawing code must be executed in the UI thread.
            Thread thisThread = Thread.currentThread();
            while (readThread == thisThread) {
                read();
                try {
                    Thread.sleep(intervalRead);
/*					synchronized (this) {
                        while (readThread == thisThread && threadSuspended)
							wait();
					}*/
                } catch (InterruptedException e) {
                    break;
                }

                // The Runnable can be suspended and resumed with the below code:
//				threadSuspended = !threadSuspended;
//				if (!threadSuspended)
//					notify();
            }
        }

/*		public synchronized void stop() {
            readThread = null;
			notify();
		}*/


    };
    private volatile Thread readThread = new Thread(readRunnable, Constans.readThread);
    private BroadcastReceiver receiverStartRecord = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            startRecord();
            sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        }
    },
            receiverStopRecord = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    stopRecord();
                    sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
                }
            },
            receiverClose = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
                    sendBroadcast(new Intent(Constans.actionFinishActivity));
                    stopSelf();
                }
            };


    class ServiceReaderDataBinder extends Binder {
        ServiceReader getService() {
            return ServiceReader.this;
        }
    }


    @Override
    public void onCreate() {
        cpuTotal = new ArrayList<Float>(maxSamples);
        cpuAM = new ArrayList<Float>(maxSamples);
        memoryAM = new ArrayList<Integer>(maxSamples);
        memUsed = new ArrayList<String>(maxSamples);
        memAvailable = new ArrayList<String>(maxSamples);
        memFree = new ArrayList<String>(maxSamples);
        cached = new ArrayList<String>(maxSamples);
        threshold = new ArrayList<String>(maxSamples);

        pId = Process.myPid();

        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        amMI = am.getProcessMemoryInfo(new int[]{pId});
        mi = new ActivityManager.MemoryInfo();

        intervalRead = (int) SharedPreferencesUtils.getFromSpfs(this, Constans.intervalRead, Constans.defaultIntervalRead);
        intervalUpdate = (int) SharedPreferencesUtils.getFromSpfs(this, Constans.intervalUpdate, Constans.defaultIntervalUpdate);
        intervalWidth = (int) SharedPreferencesUtils.getFromSpfs(this, Constans.intervalWidth, Constans.defaultIntervalWidth);

//        mPrefs = getSharedPreferences(getString(R.string.app_name) + Constans.prefs, MODE_PRIVATE);
//        intervalRead = mPrefs.getInt(Constans.intervalRead, Constans.defaultIntervalRead);
//        intervalUpdate = mPrefs.getInt(Constans.intervalUpdate, Constans.defaultIntervalUpdate);
//        intervalWidth = mPrefs.getInt(Constans.intervalWidth, Constans.defaultIntervalWidth);

        readThread.start();

//		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(Constants.anotherMonitorEvent));
        registerReceiver(receiverStartRecord, new IntentFilter(Constans.actionStartRecord));
        registerReceiver(receiverStopRecord, new IntentFilter(Constans.actionStopRecord));
        registerReceiver(receiverClose, new IntentFilter(Constans.actionClose));

        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        PendingIntent contentIntent = TaskStackBuilder.create(this)
//				.addParentStack(ActivityMonitor.class)
//				.addNextIntent(new Intent(this, ActivityMonitor.class))
                .addNextIntentWithParentStack(new Intent(this, ActivityMonitor.class))
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pIStartRecord = PendingIntent.getBroadcast(this, 0, new Intent(Constans.actionStartRecord), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pIStopRecord = PendingIntent.getBroadcast(this, 0, new Intent(Constans.actionStopRecord), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pIClose = PendingIntent.getBroadcast(this, 0, new Intent(Constans.actionClose), PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationRead = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notify_read2))
//				.setTicker(getString(R.string.notify_read))
                .setSmallIcon(R.drawable.icon_bw)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon, null))
                .setWhen(0) // Removes the time
                .setOngoing(true)
                .setContentIntent(contentIntent) // PendingIntent.getActivity(this, 0, new Intent(this, ActivityMonitor.class), 0)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.notify_read2)))
                .addAction(R.drawable.icon_circle_sb, getString(R.string.menu_record), pIStartRecord)
                .addAction(R.drawable.icon_times_ai, getString(R.string.menu_close), pIClose)
                .build();

        mNotificationRecord = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notify_record2))
                .setTicker(getString(R.string.notify_record))
                .setSmallIcon(R.drawable.icon_recording_bw)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_recording, null))
                .setWhen(0)
                .setOngoing(true)
                .setContentIntent(contentIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.notify_record2)))
                .addAction(R.drawable.icon_stop_sb, getString(R.string.menu_stop_record), pIStopRecord)
                .addAction(R.drawable.icon_times_ai, getString(R.string.menu_close), pIClose)
                .build();

//		mNM.notify(0, mNotificationRead);
        startForeground(10, mNotificationRead); // If not the AM service will be easily killed when a heavy-use memory app (like a browser or Google Maps) goes onto the foreground
    }


    @Override
    public void onDestroy() {
        if (recording)
            stopRecord();
        mNM.cancelAll();

        unregisterReceiver(receiverStartRecord);
        unregisterReceiver(receiverStopRecord);
        unregisterReceiver(receiverClose);

        try {
            readThread.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        synchronized (this) {
            readThread = null;
            notify();
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceReaderDataBinder();
    }


    @SuppressLint("NewApi")
    @SuppressWarnings("unchecked")
    private void read() {
        try {
            reader = new BufferedReader(new FileReader("/proc/meminfo"));
            s = reader.readLine();
            while (s != null) {
                // Memory is limited as far as we know
                while (memFree.size() >= maxSamples) {
                    cpuTotal.remove(cpuTotal.size() - 1);
                    cpuAM.remove(cpuAM.size() - 1);
                    memoryAM.remove(memoryAM.size() - 1);

                    memUsed.remove(memUsed.size() - 1);
                    memAvailable.remove(memAvailable.size() - 1);
                    memFree.remove(memFree.size() - 1);
                    cached.remove(cached.size() - 1);
                    threshold.remove(threshold.size() - 1);
                }
                if (mListSelected != null && !mListSelected.isEmpty()) {
                    List<Integer> l = (List<Integer>) (mListSelected.get(0)).get(Constans.pFinalValue);
                    if (l != null && l.size() >= maxSamples)
                        for (Map<String, Object> m : mListSelected) {
                            ((List<Integer>) m.get(Constans.pFinalValue)).remove(l.size() - 1);
                            ((List<Integer>) m.get(Constans.pTPD)).remove(((List<Integer>) m.get(Constans.pTPD)).size() - 1);
                        }
                }
                if (mListSelected != null && !mListSelected.isEmpty()) {
                    for (Map<String, Object> m : mListSelected) {
                        List<Integer> l = (List<Integer>) m.get(Constans.pFinalValue);
                        if (l == null)
                            break;
                        while (l.size() >= maxSamples)
                            l.remove(l.size() - 1);
                        l = (List<Integer>) m.get(Constans.pTPD);
                        while (l.size() >= maxSamples)
                            l.remove(l.size() - 1);
                    }
                }

                // Memory values. Percentages are calculated in the ActivityMonitor class.
                if (firstRead && s.startsWith("MemTotal:")) {
                    memTotal = Integer.parseInt(s.split("[ ]+", 3)[1]);
                    firstRead = false;
                } else if (s.startsWith("MemFree:")) {
                    memFree.add(0, s.split("[ ]+", 3)[1]);
                } else if (s.startsWith("Cached:")) {
                    cached.add(0, s.split("[ ]+", 3)[1]);
                }

                s = reader.readLine();
            }
            reader.close();

            // http://stackoverflow.com/questions/3170691/how-to-get-current-memory-usage-in-android
            am.getMemoryInfo(mi);
            if (mi == null) { // Sometimes mi is null
                memUsed.add(0, String.valueOf(0));
                memAvailable.add(0, String.valueOf(0));
                threshold.add(0, String.valueOf(0));
            } else {
                memUsed.add(0, String.valueOf(memTotal - mi.availMem / 1024));
                memAvailable.add(0, String.valueOf(mi.availMem / 1024));
                threshold.add(0, String.valueOf(mi.threshold / 1024));
            }

            memoryAM.add(amMI[0].getTotalPrivateDirty());
//			Log.d("TotalPrivateDirty", String.valueOf(amMI[0].getTotalPrivateDirty()));
//			Log.d("TotalPrivateClean", String.valueOf(amMI[0].getTotalPrivateClean()));
//			Log.d("TotalPss", String.valueOf(amMI[0].getTotalPss()));
//			Log.d("TotalSharedDirty", String.valueOf(amMI[0].getTotalSharedDirty()));

//			CPU usage percents calculation. It is possible negative values or values higher than 100% may appear.
//			http://stackoverflow.com/questions/1420426
//			http://kernel.org/doc/Documentation/filesystems/proc.txt
            if (Build.VERSION.SDK_INT < 26) {
                reader = new BufferedReader(new FileReader("/proc/stat"));
                sa = reader.readLine().split("[ ]+", 9);
                work = Long.parseLong(sa[1]) + Long.parseLong(sa[2]) + Long.parseLong(sa[3]);
                total = work + Long.parseLong(sa[4]) + Long.parseLong(sa[5]) + Long.parseLong(sa[6]) + Long.parseLong(sa[7]);
                reader.close();
            }

            reader = new BufferedReader(new FileReader("/proc/" + pId + "/stat"));
            sa = reader.readLine().split("[ ]+", 18);
            workAM = Long.parseLong(sa[13]) + Long.parseLong(sa[14]) + Long.parseLong(sa[15]) + Long.parseLong(sa[16]);
            reader.close();

            if (mListSelected != null && !mListSelected.isEmpty()) {
                int[] arrayPIds = new int[mListSelected.size()];
                synchronized (mListSelected) {
                    int n = 0;
                    for (Map<String, Object> p : mListSelected) {
                        try {
                            if (p.get(Constans.pDead) == null) {
                                reader = new BufferedReader(new FileReader("/proc/" + p.get(Constans.pId) + "/stat"));
                                arrayPIds[n] = Integer.valueOf((String) p.get(Constans.pId));
                                ++n;
                                sa = reader.readLine().split("[ ]+", 18);
                                p.put(Constans.work, (float) Long.parseLong(sa[13]) + Long.parseLong(sa[14]) + Long.parseLong(sa[15]) + Long.parseLong(sa[16]));
                                reader.close();
                            }
                        } catch (FileNotFoundException e) {
                            p.put(Constans.pDead, Boolean.TRUE);
                            Intent intent = new Intent(Constans.actionDeadProcess);
                            intent.putExtra(Constans.process, (Serializable) p);
                            sendBroadcast(intent);
                        }
                    }
                }

                MemoryInfo[] mip = am.getProcessMemoryInfo(arrayPIds);
                int n = 0;
                for (Map<String, Object> entry : mListSelected) {
                    List<Integer> l = (List<Integer>) entry.get(Constans.pTPD);
                    if (l == null) {
                        l = new ArrayList<Integer>();
                        entry.put(Constans.pTPD, l);
                    }
                    if (entry.get(Constans.pDead) == null) {
//						if (mip[n].getTotalPrivateDirty() !=0
//								&& mip[n].getTotalPss() != 0
//								&& mip[n].getTotalSharedDirty() != 0) // To avoid dead processes
                        l.add(0, mip[n].getTotalPrivateDirty());
                        ++n;
                    } else l.add(0, 0);
                }
//				Log.d("MemoryInfo entries", String.valueOf(mip.length));
//				Log.d("List Selected entries", String.valueOf(mListSelected.size()));

//				Log.d("TotalSharedClean", String.valueOf(mi[0].getTotalSharedClean()));
//				Log.d("TotalSharedDirty", String.valueOf(mi[0].getTotalSharedDirty()));
//				Log.d("TotalPrivateClean", String.valueOf(mi[0].getTotalPrivateClean()));
//				Log.d("TotalPrivateDirty", String.valueOf(mi[0].getTotalPrivateDirty()));
//				Log.d("TotalPss", String.valueOf(mi[0].getTotalPss()));
//				Log.d("Pss", String.valueOf(Debug.getPss()));
//				Log.d("GlobalAllocSize", String.valueOf(Debug.getGlobalAllocSize()));
//				Log.d("NativeHeapSize", String.valueOf(Debug.getNativeHeapSize()/1024));
//				Log.d("NativeHeapAllocatedSize", String.valueOf(Debug.getNativeHeapAllocatedSize()/1024));
            }

            if (totalBefore != 0) {
                totalT = total - totalBefore;
                workT = work - workBefore;
                workAMT = workAM - workAMBefore;

                cpuTotal.add(0, restrictPercentage(workT * 100 / (float) totalT));
                cpuAM.add(0, restrictPercentage(workAMT * 100 / (float) totalT));

                if (mListSelected != null && !mListSelected.isEmpty()) {
                    int workPT = 0;
                    List<Float> l;

                    synchronized (mListSelected) {
                        for (Map<String, Object> p : mListSelected) {
                            if (p.get(Constans.workBefore) == null)
                                break;
                            l = (List<Float>) p.get(Constans.pFinalValue);
                            if (l == null) {
                                l = new ArrayList<Float>();
                                p.put(Constans.pFinalValue, l);
                            }
                            while (l.size() >= maxSamples)
                                l.remove(l.size() - 1);

                            workPT = (int) ((Float) p.get(Constans.work) - (Float) p.get(Constans.workBefore));
                            l.add(0, restrictPercentage(workPT * 100 / (float) totalT));
                        }
                    }
                }
            }

            totalBefore = total;
            workBefore = work;
            workAMBefore = workAM;

            if (mListSelected != null && !mListSelected.isEmpty())
                for (Map<String, Object> p : mListSelected)
                    p.put(Constans.workBefore, p.get(Constans.work));

            reader.close();

            if (recording)
                record();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private float restrictPercentage(float percentage) {
        if (percentage > 100) {
            return 100;
        } else if (percentage < 0) {
            return 0;
        } else {
            return percentage;
        }
    }


    @SuppressWarnings("unchecked")
    private void record() {
        if (mW == null) {

            mFile = new File(App.getApp().getRootDir(), new StringBuilder().append("Monitor").append("Record-").append(getDate()).append(".csv").toString());

            try {
                mW = new BufferedWriter(new FileWriter(mFile));
            } catch (IOException e) {
                notifyError(e);
                return;
            }
        }

        try {
            if (topRow) {
                StringBuilder sb = new StringBuilder()
                        .append(getString(R.string.app_name))
                        .append(" Record,Starting date and time:,")
                        .append(getDate())
                        .append(",Read interval (ms):,")
                        .append(intervalRead)
                        .append(",MemTotal (kB),")
                        .append(memTotal)
                        .append("\nTotal CPU usage (%),AnotherMonitor (Pid ").append(Process.myPid()).append(") CPU usage (%),AnotherMonitor Memory (kB)");
                if (mListSelected != null && !mListSelected.isEmpty())
                    for (Map<String, Object> p : mListSelected)
                        sb.append(",").append(p.get(Constans.pAppName)).append(" (Pid ").append(p.get(Constans.pId)).append(") CPU usage (%)")
                                .append(",").append(p.get(Constans.pAppName)).append(" Memory (kB)");

                sb.append(",,Memory used (kB),Memory available (MemFree+Cached) (kB),MemFree (kB),Cached (kB),Threshold (kB)");

                mW.write(sb.toString());
                mNM.notify(10, mNotificationRecord);
                topRow = false;
            }

            StringBuilder sb = new StringBuilder()
                    .append("\n").append(cpuTotal.get(0))
                    .append(",").append(cpuAM.get(0))
                    .append(",").append(memoryAM.get(0));
            if (mListSelected != null && !mListSelected.isEmpty())
                for (Map<String, Object> p : mListSelected) {
                    if (p.get(Constans.pDead) != null)
                        sb.append(",DEAD,DEAD");
                    else sb.append(",").append(((List<Integer>) p.get(Constans.pFinalValue)).get(0))
                            .append(",").append(((List<Integer>) p.get(Constans.pTPD)).get(0));
                }
            sb.append(",")
                    .append(",").append(memUsed.get(0))
                    .append(",").append(memAvailable.get(0))
                    .append(",").append(memFree.get(0))
                    .append(",").append(cached.get(0))
                    .append(",").append(threshold.get(0));

            mW.write(sb.toString());
        } catch (IOException e) {
            notifyError(e);
        }
    }


    public void startRecord() {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, this.getString(R.string.w_main_storage_permission), Toast.LENGTH_SHORT).show();
            return;
        }
        recording = true;
        sendBroadcast(new Intent(Constans.actionSetIconRecord));
    }

    public void stopRecord() {
        recording = false;
        sendBroadcast(new Intent(Constans.actionSetIconRecord));
        try {
            mW.flush();
            mW.close();
            mW = null;

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).setData(Uri.fromFile(mFile)));

            String s = new StringBuilder().append("Monitor").append("Record-").append(getDate()).append(".csv ").append(getString(R.string.notify_toast_saved))
                    .append(" " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/Monitor").toString();
            Logger.d(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        topRow = true;
        mNM.notify(10, mNotificationRead);
    }

    public boolean isRecording() {
        return recording;
    }


    public void notifyError(final IOException e) {
        e.printStackTrace();
        if (mW != null)
            stopRecord();
        else {
            recording = false;
            sendBroadcast(new Intent(Constans.actionSetIconRecord));

            // http://stackoverflow.com/questions/3875184/cant-create-handler-inside-thread-that-has-not-called-looper-prepare
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ServiceReader.this, getString(R.string.notify_toast_error_2) + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            mNM.notify(10, mNotificationRead);
        }
    }


    private String getDate() {
        Calendar c = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        return new StringBuilder()
                .append(df.format(c.get(Calendar.YEAR))).append("-")
                .append(df.format(c.get(Calendar.MONTH) + 1)).append("-")
                .append(df.format(c.get(Calendar.DATE))).append("-")
                .append(df.format(c.get(Calendar.HOUR_OF_DAY))).append("-")
                .append(df.format(c.get(Calendar.MINUTE))).append("-")
                .append(df.format(c.get(Calendar.SECOND))).toString();
    }


    public void setIntervals(int intervalRead, int intervalUpdate, int intervalWidth) {
        this.intervalRead = intervalRead;
        this.intervalUpdate = intervalUpdate;
        this.intervalWidth = intervalWidth;
    }


    public List<Map<String, Object>> getProcesses() {
        return mListSelected != null && !mListSelected.isEmpty() ? mListSelected : null;
    }

    public void addProcess(Map<String, Object> process) {
        // Integer	   Constans.pId
        // String	   Constans.pName
        // Integer	   Constans.work
        // Integer	   Constans.workBefore
        // List<Sring> Constans.finalValue
        if (mListSelected == null)
            mListSelected = Collections.synchronizedList(new ArrayList<Map<String, Object>>());
        mListSelected.add(process);
    }

    public void removeProcess(Map<String, Object> process) {
        synchronized (mListSelected) {
            Iterator<Map<String, Object>> i = mListSelected.iterator();
            while (i.hasNext())
                if (i.next().get(Constans.pId).equals(process.get(Constans.pId))) {
                    i.remove();
                    Logger.i(getString(R.string.w_processes_dead_notification), (String) process.get(Constans.pName));
                }
        }
    }


    public int getIntervalRead() {
        return intervalRead;
    }

    public int getIntervalUpdate() {
        return intervalUpdate;
    }

    public int getIntervalWidth() {
        return intervalWidth;
    }


    public List<Float> getCPUTotalP() {
        return cpuTotal;
    }

    public List<Float> getCPUAMP() {
        return cpuAM;
    }

    public List<Integer> getMemoryAM() {
        return memoryAM;
    }

    public int getMemTotal() {
        return memTotal;
    }

    public List<String> getMemUsed() {
        return memUsed;
    }

    public List<String> getMemAvailable() {
        return memAvailable;
    }

    public List<String> getMemFree() {
        return memFree;
    }

    public List<String> getCached() {
        return cached;
    }

    public List<String> getThreshold() {
        return threshold;
    }
}

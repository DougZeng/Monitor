package org.doug.monitor.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.print.PageRange;
import android.util.Log;

import com.bugsnag.android.Bugsnag;
import com.doug.monitor.AddressBookProtos;
import com.google.protobuf.InvalidProtocolBufferException;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.doug.monitor.BuildConfig;
import org.doug.monitor.base.util.BlockDetectByPrinter;
import org.doug.monitor.base.util.Cockroach;
import org.doug.monitor.base.util.SharedPreferencesUtils;

import java.io.File;

/**
 * Created by wesine on 2018/5/23.
 */

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    private static App app;

    private static Context context;


    private File dir;


    @Override
    public void onCreate() {
        super.onCreate();
        Bugsnag.init(this);
        app = this;
        context = this;
        SharedPreferencesUtils.clearSpfs(this);
        installCockroach();
        BlockDetectByPrinter.start();
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        AddressBookProtos.Person person = AddressBookProtos.Person.newBuilder()
                .setId(1)
                .setName("text")
                .setEmail("123")
                .build();
        AddressBookProtos.AddressBook book = AddressBookProtos.AddressBook.newBuilder()
                .addPerson(person)
                .build();

        Logger.d(person.toString());
        Logger.d(book.toString());

        try {
            AddressBookProtos.AddressBook addressBook = AddressBookProtos.AddressBook.parseFrom(book.toByteArray());
            Logger.d(addressBook.toString());
            AddressBookProtos.Person parseFrom = AddressBookProtos.Person.parseFrom(person.toByteArray());
            Logger.d(parseFrom.toString());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            Bugsnag.notify(e);
        }
        int spfs = (int) SharedPreferencesUtils.getFromSpfs(this, Constans.IS_FIRST_BOOT, Constans.DEFAULT_COUNT);
        if (spfs > 0) {
            spfs += 1;
            SharedPreferencesUtils.putToSpfs(this, Constans.IS_FIRST_BOOT, spfs);
            Logger.d("spfs  = %s", spfs);
        }

        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Monitor");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public File getRootDir() {
        return dir;
    }

    public void deleteFile(File dir) {
        File[] list = dir.listFiles(); //获取目标文件夹下所有的文件和文件夹数组
        for (File subFiles : list) {    //遍历
            if (subFiles.isFile()) {    //判断
                subFiles.delete(); //是文件就删除
            } else {
                deleteFile(subFiles); //是文件夹就遍历
            }
        }
//        dir.delete();
    }


    public static App getApp() {
        return app;
    }

    public void exit() {
        Cockroach.uninstall();
        android.os.Process.killProcess(android.os.Process.myPid());// kill this  test thread
    }


    public static Context getAppContext() {
        return context;
    }


    private void installCockroach() {
        Cockroach.install(new Cockroach.ExceptionHandler() {

            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e(TAG, "--->CockroachException:" + thread + "<---", throwable);
                        } catch (Throwable e) {
                            Log.e(TAG, "run: ", e);
                        }
                    }
                });
            }
        });
    }

}

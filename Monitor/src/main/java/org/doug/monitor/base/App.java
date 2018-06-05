package org.doug.monitor.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.print.PageRange;
import android.util.Log;

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


    private static File dir;


    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = this;
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

    public static File getRootDir() {
        return dir;
    }

    public static App getApp() {
        return app;
    }

    public static void exit() {
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

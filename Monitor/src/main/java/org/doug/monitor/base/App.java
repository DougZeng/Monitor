package org.doug.monitor.base;

import android.app.Application;

import com.doug.monitor.AddressBookProtos;
import com.google.protobuf.InvalidProtocolBufferException;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.doug.monitor.BuildConfig;

/**
 * Created by wesine on 2018/5/23.
 */

public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
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
    }

    public static App getApp() {
        return app;
    }

    public static void exit() {
        android.os.Process.killProcess(android.os.Process.myPid());// kill this  test thread
    }

}

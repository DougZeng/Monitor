package org.doug.monitor.bluetooth.exception.hanlder;

import android.content.Context;

import org.doug.monitor.util.Toaster;
import org.doug.monitor.bluetooth.exception.ConnectException;
import org.doug.monitor.bluetooth.exception.GattException;
import org.doug.monitor.bluetooth.exception.InitiatedException;
import org.doug.monitor.bluetooth.exception.OtherException;
import org.doug.monitor.bluetooth.exception.TimeoutException;

/**
 * Toast exception.
 */
public class DefaultBleExceptionHandler extends BleExceptionHandler {
    private Context context;

    public DefaultBleExceptionHandler(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    protected void onConnectException(ConnectException e) {
        Toaster.showToast(context, e.getDescription());
    }

    @Override
    protected void onGattException(GattException e) {
        Toaster.showToast(context, e.getDescription());
    }

    @Override
    protected void onTimeoutException(TimeoutException e) {
        Toaster.showToast(context, e.getDescription());
    }

    @Override
    protected void onInitiatedException(InitiatedException e) {
        Toaster.showToast(context, e.getDescription());
    }

    @Override
    protected void onOtherException(OtherException e) {
        Toaster.showToast(context, e.getDescription());
    }
}

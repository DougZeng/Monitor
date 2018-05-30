package org.doug.monitor.base.bluetooth.conn;

import android.bluetooth.BluetoothGattDescriptor;

/**
 * callback of {@link BluetoothGattDescriptor} operation.
 */
public abstract class BleDescriptorCallback extends BleCallback {
    public abstract void onSuccess(BluetoothGattDescriptor descriptor);
}
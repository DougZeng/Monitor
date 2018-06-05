package org.doug.monitor.base.util;

/**
 * Created by wesine on 2018/6/4.
 */

public final class QuitCockroachException extends RuntimeException {
    public QuitCockroachException(String message) {
        super(message);
    }
}

package org.os.netcore.init;

import org.os.netcore.net.exception.GlobalException;

public interface NetRequestListener<D> {
    void onRequestStart();

    void onRequestCompleted();

    void onRequestSucceeded(D data);

    void onRequestError(GlobalException exception);

}

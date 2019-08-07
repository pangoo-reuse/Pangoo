package org.os.netcore.net.exception;

public interface ErrorProcessor {
    GlobalException processor(Throwable throwable);
}

package org.os.netcore.net.exception;

public final class GlobalException {
    private String message;
    private Throwable error;
    public static final int DEFAULT_REQUEST_ERROR = -19701212;

    private int errorCode;

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getError() {
        return error;
    }

    public GlobalException(Throwable e) {
        this.error(e);

    }

    public void setException(Throwable e) {
        this.error(e);
    }


    private void error(Throwable e) {
        error = e.getCause();
        message = e.getMessage();
    }
}

package com.example.library.base;

/**
 * Created by bjhl on 16/6/24.
 *
 * 异常
 */
public class FException extends RuntimeException {
    private long errorCode;

    public FException(long errorCode) {
        this(errorCode, "");
    }

    public FException(long errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public void setErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }

    public long getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[errorCode : ");
        builder.append(errorCode)
                .append("]\t");
        builder.append("[msg: ");
        builder.append(getLocalizedMessage());
        builder.append("]");
        return builder.toString();
    }
}

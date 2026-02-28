package com.nexus.flex.common.exception;

import com.nexus.flex.common.IResultCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : 11's papa
 * @since : 2026/2/25, 星期三
 **/
public class BusinessException extends RuntimeException {
    protected IResultCode resultCode;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(IResultCode errorCode) {
        super(StringUtils.EMPTY);
        this.resultCode = errorCode;
    }

    public BusinessException(IResultCode errorCode, String message) {
        super(message);
        this.resultCode = errorCode;
    }

    public BusinessException(IResultCode errorCode, Throwable t) {
        super(t);
        this.resultCode = errorCode;
    }

    public BusinessException(String message, Throwable t) {
        super(message, t);
    }

    public IResultCode getErrorCode() {
        return resultCode;
    }

    public BusinessException(Throwable t) {
        super(t);
    }
}

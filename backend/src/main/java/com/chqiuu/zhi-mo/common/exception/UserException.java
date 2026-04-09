package com.chqiuu.zhi-mo.common.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final Integer code = 400;

    public UserException(String message) {
        super(message);
    }
}

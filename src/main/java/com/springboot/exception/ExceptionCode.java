package com.springboot.exception;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_NOT_FOUND(404, "Member not found"),
    MEMBER_EXISTS(409, "Member exists"),
    BOARD_NOT_FOUND(404, "Board not found"),
    BOARD_EXISTS(409, "Board exists"),
    LIKE_EXISTS(409, "Like exists"),
    UNAUTHORIZED_USER(401, "unauthorized user");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}

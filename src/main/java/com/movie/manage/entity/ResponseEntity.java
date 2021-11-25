package com.movie.manage.entity;

import lombok.Getter;

@Getter
public class ResponseEntity<T> {
    private final T obj;
    private final int status;
    private final String msg;

    private ResponseEntity(T obj, int status, String msg) {
        this.obj = obj;
        this.status = status;
        this.msg = msg;
    }

    public static <T> ResponseEntity<T> success(T obj) {
        return new ResponseEntity<>(obj, 0, "success");
    }

    public static <T> ResponseEntity<T> fail(int status, String msg) {
        return new ResponseEntity<>(null, status, msg);
    }
}

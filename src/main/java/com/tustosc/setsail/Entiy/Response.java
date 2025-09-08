package com.tustosc.setsail.Entiy;

import lombok.Data;

@Data
public class Response<T> {

    private Integer code;
    private String msg;
    private T data;

    public Response() {}

    public Response(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Response(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(T data) {
        this.code = 0;
        this.msg = "Succeed.";
        this.data = data;
    }
}

package lol.cicco.admin.common.model;

import lombok.Getter;

import java.util.Collection;

public class R {
    // 系统异常状态码
    public static final int ERROR = 500;
    // 提示状态码
    public static final int OTHER = 400;
    // 无权限
    public static final int NO_PERMISSION = 404;
    // 登录状态码
    public static final int LOGIN = 999;
    // 正常状态码
    public static final int OK = 200;

    @Getter
    private int code;
    @Getter
    private String message;
    @Getter
    private Object data;
    @Getter
    private Integer count;

    private R(int code, String message) {
        this(code, message, null);
    }

    private R(int code, String message, Object data) {
        this(code, message, data, null);
    }

    private R(int code, String message, Object data, Integer count) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.count = count;
    }

    public static R ok() {
        return new R(OK, "success");
    }

    public static R ok(Object data) {
        return new R(OK, "success", data);
    }


    public static R ok(Collection<?> data, int count) {
        return new R(OK, "success", data, count);
    }

    public static R other(String message) {
        return new R(OTHER, message);
    }

    public static R error() {
        return new R(ERROR, "系统异常");
    }

    public static R login() {
        return new R(LOGIN, "用户信息已失效,请重新登录!");
    }

    public static R noPermission(){
        return new R(NO_PERMISSION, "无权限");
    }

}

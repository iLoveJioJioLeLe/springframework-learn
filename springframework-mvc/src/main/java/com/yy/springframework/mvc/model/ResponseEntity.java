package com.yy.springframework.mvc.model;

/**
 * Created by 2019/7/21.
 */
public class ResponseEntity {

    private String code;
    private Object data;
    private String msg;

    public ResponseEntity(String code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static ResponseEntity success(Object data) {
        return new ResponseEntity("200", data, "处理成功");
    }

    public static ResponseEntity fail(String msg) {
        return new ResponseEntity("-1", null, msg);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

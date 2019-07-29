package com.imooc.user.response;

import java.io.Serializable;

public class Response  implements Serializable {
    public static final Response  USERNAME_PASSWORD_INVALID= new Response(1001,"user or password is error");
    private  String code;
    private  String message;

    public Response(String code,String message){
        this.code=code;
        this.message=message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

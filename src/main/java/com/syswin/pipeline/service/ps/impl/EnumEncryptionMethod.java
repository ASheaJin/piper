package com.syswin.pipeline.service.ps.impl;

public enum EnumEncryptionMethod {
    NO(0, "不加密"),RRSA(1,"RSA公钥加密(receiver)"),
    SRSA(2,"RSA公钥加密(sender)"),CBC(3,"对称秘钥AES"),
    RECC(4,"ECC公钥加密(receiver)"),
    SECC(5,"ECC公钥加密(sender)");

    public Integer code;
    public String msg;

    EnumEncryptionMethod(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

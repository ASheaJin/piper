package com.syswin.pipeline.service.ps.impl;

import lombok.Data;

/**
 * 请求pubKey的返回值
 * Created by 115477 on 2018/12/18.
 */
@Data
public class TemailPubKeyOut<T> {
    private int code;
    private T data;
}

@Data
class TemailPubKeyData {
    private String pubKey;
    private String temail;
    private String algorithm;
    private int type;
}
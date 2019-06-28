package com.syswin.pipeline.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by 115477 on 2019/4/3.
 */
public final class JacksonJsonUtil {
    private final static Logger logger = LoggerFactory.getLogger(JacksonJsonUtil.class);


    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> String toJson(T t) {
        try {
            return mapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> T fromJson(String text, Class<T> clazz) {
        try {
            return mapper.readValue(text, clazz);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    private static <T> T fromJson(String text, Type type) {
        throw new UnsupportedOperationException("fromJson");
    }

    public static <T> T fromJson(String text, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(text, typeReference);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static void main(String[] args) {
        InetAddress address = null;
        try {
            address = InetAddress.getByName("application.t.email");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("获取失败");
        }
        System.out.println(address.getHostAddress().toString());

        try {
            Socket s = new Socket("application.t.email",8686);

            //构建IO
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            //向服务器端发送一条消息
            bw.write("测试客户端和服务器通信，服务器接收到消息返回到客户端\n");
            bw.flush();

            //读取服务器返回的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String mess = br.readLine();
            System.out.println("服务器："+mess);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package com.syswin.pipeline.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;

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
}

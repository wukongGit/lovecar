package com.sunc.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SerializeUtil {

    private volatile static SerializeUtil instance;
    private ObjectMapper objectMapper;

    private SerializeUtil() {
        objectMapper = new ObjectMapper();
    }

    public static SerializeUtil getInstance() {
        if (instance == null) {
            synchronized (SerializeUtil.class) {
                if (instance == null) {
                    instance = new SerializeUtil();
                }
            }
        }
        return instance;
    }

    public String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T deserialize(String string, Class<T> tClass) {
        try {
            return objectMapper.readValue(string, tClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T deserialize(String string, TypeReference typeReference) {
        try {
            return objectMapper.readValue(string, typeReference);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

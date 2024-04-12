package com.boyouquan.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectUtil {

    private static final Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T jsonToObject(String json, Class<T> objectType) {
        try {
            return mapper.readValue(json, objectType);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
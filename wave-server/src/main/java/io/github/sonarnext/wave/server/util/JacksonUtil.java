package io.github.sonarnext.wave.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {

    private JacksonUtil() {}

    private static final ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getMapper() {
        return mapper;
    }
}

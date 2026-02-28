package com.nexus.flex.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import org.jspecify.annotations.Nullable;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.function.Function;

/**
 * @author : 11's papa
 * @since : 2026/2/25, 星期三
 **/
public class JSON {
    private static final JsonMapper objectMapper = JsonMapper.builder()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            // .enable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
            .changeDefaultVisibility(visibility
                    -> visibility.withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY))
            .build();

    public static @Nullable String toJSONString(Object value) {
        return objectMapper.writeValueAsString(value);
    }
    public static <T> Function<Object, T> objectToType(Class<T> clazz) {
        return o -> objectMapper.convertValue(o, clazz);
    }
}

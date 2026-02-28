package com.nexus.flex.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.core.StreamReadFeature;
import tools.jackson.core.json.JsonReadFeature;
import tools.jackson.databind.DeserializationFeature;

/**
 * @author : 11's papa
 * @since : 2026/2/25, 星期三
 **/
@Configuration(proxyBeanMethods = false)
public class JacksonConfig {
    @Bean
    public JsonMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.changeDefaultPropertyInclusion(
                    inclusion -> inclusion.withValueInclusion(JsonInclude.Include.NON_NULL)
            );
            // 自动检测所有类的全部属性
            builder.changeDefaultVisibility(
                    visibility -> visibility.withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY));
            builder.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
            builder.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            builder.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS);
            builder.enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION);
            builder.findAndAddModules();
            // builder.enable(MapperFeature.DEFAULT_VIEW_INCLUSION);
            // 如果一个对象中没有任何的属性，那么在序列化的时候就会报错
            // builder.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            // builder.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            // builder.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            // builder.addModule();
        };
    }
}

package com.nexus.flex.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;

/**
 * @author : 11's papa
 * @since : 2026/2/28, 星期六
 **/
public class CustomGrantedAuthorityDeserializer extends JsonDeserializer<SimpleGrantedAuthority> {

    @Override
    public SimpleGrantedAuthority deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.getCurrentToken();

        if (token == JsonToken.VALUE_STRING) {
            // 处理直接的字符串值
            String authority = p.getValueAsString();
            return new SimpleGrantedAuthority(authority);
        } else if (token == JsonToken.START_OBJECT) {
            // 处理对象格式，例如：{"role": "SOME_ROLE"}
            JsonNode node = ctxt.readTree(p);
            JsonNode roleNode = node.get("role");
            if (roleNode != null) {
                String authority = roleNode.asText();
                return new SimpleGrantedAuthority(authority);
            } else {
                // 如果没有role字段，尝试其他可能的字段名
                JsonNode authorityNode = node.get("authority");
                if (authorityNode != null) {
                    String authority = authorityNode.asText();
                    return new SimpleGrantedAuthority(authority);
                }
            }
        }

        // 默认返回空权限
        return new SimpleGrantedAuthority("");
    }
}

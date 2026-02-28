package com.nexus.flex.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author : 11's papa
 * @since : 2026/2/25, 星期三
 **/
@Component
public class Translator {
    private final MessageSource messageSource;

    public Translator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * 单Key翻译
     */
    public String get(String key) {
        return messageSource.getMessage(key, null, "Not Support Key: " + key, LocaleContextHolder.getLocale());
    }

    /**
     * 单Key翻译，并设置默认值
     */
    public String get(String key, String defaultMessage) {
        return messageSource.getMessage(key, null, defaultMessage, LocaleContextHolder.getLocale());
    }

    /**
     * 单Key翻译，并指定默认语言
     */
    public String get(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }

    /**
     * 带参数
     */
    public String getWithArgs(String key, Object... args) {
        return messageSource.getMessage(key, args, "Not Support Key: " + key, LocaleContextHolder.getLocale());
    }
}

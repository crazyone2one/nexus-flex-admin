package com.nexus.flex.common;

/**
 * @author : 11's papa
 * @since : 2026/2/25, 星期三
 **/
public interface IResultCode {
    /**
     * 返回状态码
     */
    int getCode();

    /**
     * 返回状态码信息
     */
    String getMessage();
}

package cn.jzyunqi.common.third.tencent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @date 2021/5/14.
 */
@Getter
@AllArgsConstructor
public enum Action {
    SendSms("sms", "sms.tencentcloudapi.com", null, "application/json; charset=utf-8", "2019-07-11");

    /**
     * 服务
     */
    private final String service;

    /**
     * 域名
     */
    private final String host;

    /**
     * 服务器地区，可以为空
     */
    private final String region;

    /**
     * action的内容
     */
    private final String contentType;

    /**
     * action的版本
     */
    private final String version;
}

package cn.jzyunqi.common.third.tencent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2021/5/14.
 */
@Getter
@Setter
public class SmsResultData implements Serializable {
    @Serial
    private static final long serialVersionUID = 137009828227821839L;

    /**
     * 唯一请求 ID, 来自TencentBaseResult的冗余字段
     */
    @JsonIgnore
    private String requestId;

    /**
     * 发送流水号
     */
    @JsonProperty("SerialNo")
    private String serialNo;

    /**
     * 手机号码
     */
    @JsonProperty("PhoneNumber")
    private String phoneNumber;

    /**
     * 计费条数
     */
    @JsonProperty("Fee")
    private String fee;

    /**
     * 用户Session内容
     */
    @JsonProperty("SessionContext")
    private String sessionContext;

    /**
     * 短信请求错误码
     */
    @JsonProperty("Code")
    private String code;

    /**
     * 短信请求错误码描述
     */
    @JsonProperty("Message")
    private String message;

    /**
     * 国家码或地区码
     */
    @JsonProperty("IsoCode")
    private String isoCode;
}

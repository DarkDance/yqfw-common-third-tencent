package cn.jzyunqi.common.third.tencent.qq.callback.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2025/12/12
 */
@Getter
@Setter
public class VerifyData extends BaseDispatchData {

    /**
     * 需要计算签名的字符串
     */
    @JsonProperty("plain_token")
    private String plainToken;

    /**
     * 计算签名使用时间戳
     */
    @JsonProperty("event_ts")
    private Long eventTimestamp;

    /**
     * 签名
     */
    @JsonProperty("signature")
    private String signature;
}

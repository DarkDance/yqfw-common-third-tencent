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

    @JsonProperty("plain_token")
    private String plainToken;

    @JsonProperty("event_ts")
    private Long eventTimestamp;

    @JsonProperty("signature")
    private String signature;
}

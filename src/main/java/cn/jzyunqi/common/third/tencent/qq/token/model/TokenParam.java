package cn.jzyunqi.common.third.tencent.qq.token.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2025/12/12
 */
@Getter
@Setter
public class TokenParam {
    /**
     * 应用id
     */
    @JsonProperty("appId")
    private String appId;

    /**
     * 应用密钥
     */
    @JsonProperty("clientSecret")
    private String clientSecret;
}

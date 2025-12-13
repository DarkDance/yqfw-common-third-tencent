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
public class ClientTokenData {
    /**
     * 访问令牌
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 有效期(秒)
     */
    @JsonProperty("expires_in")
    private Long expiresIn;
}

package cn.jzyunqi.common.third.tencent.qq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2025/12/12
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TencentQQAuth {

    /**
     * app id
     */
    private String appId;

    /**
     * app密码
     */
    private String clientSecret;
}

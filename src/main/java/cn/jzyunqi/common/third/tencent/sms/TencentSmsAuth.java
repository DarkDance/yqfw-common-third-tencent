package cn.jzyunqi.common.third.tencent.sms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2025/5/26
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TencentSmsAuth {

    /**
     * app id
     */
    private String secretId;

    /**
     * app密码
     */
    private String secretKey;

    /**
     * 在 短信控制台 添加应用后生成的实际 SdkAppId
     */
    private String smsSdkAppId;
}
